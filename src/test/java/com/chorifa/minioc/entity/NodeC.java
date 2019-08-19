package com.chorifa.minioc.entity;

import javax.inject.Named;

@Named("nodeC")
public class NodeC implements Node {

    private int ver = 12;

    @Override
    public int getVersion() {
        return ver;
    }

}
