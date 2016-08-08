package org.lst.trading.lib.model;

public interface TradingStrategy<T> 
{
    default void onStart(TradingContext<T> context) {

    }

    default void onTick() {

    }

    default void onEnd() {

    }
}
