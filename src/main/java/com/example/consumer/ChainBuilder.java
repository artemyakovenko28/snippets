package com.example.consumer;

public abstract class ChainBuilder<ChainResultClass> {

    private ChainResultClass builder;

    public ChainBuilder(ChainResultClass builder) {
        this.builder = builder;
    }
}
