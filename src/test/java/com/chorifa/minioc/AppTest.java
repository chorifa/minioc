package com.chorifa.minioc;

import static org.junit.Assert.assertTrue;

import com.chorifa.minioc.context.ApplicationContext;
import com.chorifa.minioc.context.DefaultApplicationContext;
import com.chorifa.minioc.entity.Node;
import com.chorifa.minioc.entity.NodeA;
import com.chorifa.minioc.entity.NodeB;
import com.chorifa.minioc.entity.NodeC;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testSingletonIOC(){
        ApplicationContext applicationContext = new DefaultApplicationContext(this.getClass().getPackageName());
        NodeA nodeA = applicationContext.getBean(NodeA.class);
        NodeB nodeB = applicationContext.getBean(NodeB.class);
        Node node = applicationContext.getBean("nodeB", Node.class);
        System.out.println("nodeA == node : "+(nodeA == node));
        System.out.println("nodeB == node : "+(nodeB == node));
        System.out.println(nodeA);
        System.out.println(nodeB);
        System.out.println(node);
    }

    @Test
    public void testIOCPrototype(){
        ApplicationContext applicationContext = new DefaultApplicationContext(this.getClass().getPackageName());
        Node node1 = applicationContext.getBean(NodeC.class);
        Node node2 = applicationContext.getBean(NodeC.class);
        System.out.println(node1 == node2);
    }

}
