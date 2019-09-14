package com.example.consumer;

public class MainBuilder<ChainResultClass> extends ChainBuilder<ChainResultClass> {

    public MainBuilder(ChainResultClass builder) {
        super(builder);
    }

    public MainBuilder<ChainResultClass> withKey(String key) {
        return this;
    }
}
