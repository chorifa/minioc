# MEMO

## 未完成事项
- 只提供CGlib作为AOP中的代理，只支持给予特定接口的AOP配置，每种AOP范围可以允许多次。  
- IOC只能解字段循环依赖，构造函数循环依赖本身不可解，字段构造函数混合循环依赖随机解(依照初始化顺序)。   

## 细节

### IOC
- IOC主要分为两大部分，先扫描所有class以及annotation，构造出BeanDefinition;再根据BeanDefinition初始化实例。
- SpringIOC中主要采用递归初始化，初始化包括了:生成实例、注入字段以及后处理(在这里进行AOP代理)，使用3级缓存模型解决字段循环依赖。  
- miniIOC中每个BeanDefinition有一个状态字段(Prototype下为ThreadLocal类型)，用来表征所处的状态，如果在递归中发现某个BeanDefinition正在创建实例中则出现了构造器循环依赖。  
- 字段循环依赖使用提前曝光来规避。  

### AOP
- miniIOC中AOP代理处在创建实例和注入字段之间，为了处理字段循环依赖下的AOP代理。Spring的AOP代理也作为PostProcessor的一种在最后initializeBean的时候进行(猜测如果存在循环依赖，则也会在创建实例后进行额外的AOP处理)。   
- Spring使用责任链模式进行新增方法(Netty也使用了)。早期版本的miniIOC为了简单直接将方法顺序在子类的覆盖方法体中调用。当前版本也使用了责任链模式，允许同个AOP范围多次使用。这里的代理类是个壳其方法只会运行新增的advice方法，原方法仍旧给原始对象运行。  
- 所有的Advice在一开始遍历类的时候解析为Interceptor(生成BeanDefinition的时候)，放在整个list中。在生成Bean实例的时候，在list中拿到符合这个Bean的Interceptor，然后创建proxy。

#### 责任链
优化后的责任链本质上就是两个函数的互相递归调用。分成两个函数是为了使得掌管整体运行flow的部分 与 每个处理机实际添加的invoke部分分开，更灵活。当前版本的责任链实际就是MethodInvocation的flow()方法和MethodInterceptor的invoke()互相调用。flow()方法每次选择一个Interceptor执行其invoke方法，选完了就执行target原对象的被代理方法。invoke()方法则在合适的位置调用flow()方法，让责任链传播下去。   

责任链中的顺序: before->after->after return/throw->around   

实际递归中的顺序: before->around->after return/throw->after   

