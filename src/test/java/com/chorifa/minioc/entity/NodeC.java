package com.chorifa.minioc.entity;

import com.chorifa.minioc.annotation.Bean;

import javax.inject.Named;
import javax.inject.Singleton;

@Named("nodeC")
public class NodeC implements Node {

    private int ver = 12;

    @Override
    public int getVersion() {
        return ver;
    }

    @Bean
    @Named("nodeD")
    @Singleton
    public Node getNodeD(){
        return new NodeD();
    }

}
