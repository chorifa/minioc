package com.chorifa.minioc;

import com.chorifa.minioc.context.ApplicationContext;
import com.chorifa.minioc.context.DefaultApplicationContext;
import com.chorifa.minioc.entity.NodeA;
import com.chorifa.minioc.entity.NodeB;
import org.junit.Test;

import com.chorifa.minioc.aop.matcher.AdviserMatcher;

public class AopTest {

    @Test
    public void matchTest(){
        String s = "doWhatEverYouWant";
        String p = "*Eve*ant*";
        System.out.println(AdviserMatcher.isMatch(s,p));
    }

    @Test
    public void testAop(){
        ApplicationContext applicationContext = new DefaultApplicationContext(this.getClass().getPackageName());
        NodeA nodeA = applicationContext.getBean(NodeA.class);
        NodeB nodeB = applicationContext.getBean(NodeB.class);
        NodeA nodeA1 = applicationContext.getBean(NodeA.class);
        System.out.println(nodeA);
        System.out.println();
        System.out.println(nodeB);
        System.out.println();
        System.out.println(nodeA == nodeA1);
    }

}
