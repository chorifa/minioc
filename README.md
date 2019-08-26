minioc: a mini IOC container with the AOP function
===
A concise IOC container based on DI, that satisfy the most of JSR330, along with the AOP.
- jdk8+

Features
--------------------------------------------------
- Support @Inject @Named @Singleton in JSR330, and additional @Bean annotation.   
- Provide AOP functions based on interface (implement Advice interface or extend DefaultAdvice, with @Aspect and @Pointcut annotations, to specify how to AOP).   

#### Note:
- Prototype is the  default strategy in IOC, using @Singleton on class to specify a whole lifetime Singleton instance.   
- Exploit CGlib in AOP module, the default non-args constructor is necessary in current version, while it will not influence DI.   
