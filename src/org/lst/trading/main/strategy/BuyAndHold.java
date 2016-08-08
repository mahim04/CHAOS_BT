package org.lst.trading.main.strategy;

import java.util.HashMap;
import java.util.Map;

import org.lst.trading.lib.model.Order;
import org.lst.trading.lib.model.TradingContext;
import org.lst.trading.lib.model.TradingStrategy;
import org.lst.trading.lib.series.DoubleSeries;

public class BuyAndHold implements TradingStrategy<DoubleSeries> {
    Map<String, Order> mOrders;
    TradingContext<DoubleSeries> mContext;

    @Override public void onStart(TradingContext<DoubleSeries> context) {
        mContext = context;
    }

    @Override public void onTick() {
        if (mOrders == null) {
            mOrders = new HashMap<>();
            mContext.getInstruments().stream().forEach(instrument -> mOrders.put(instrument, mContext.order(instrument, true, 1)));
        }
    }
}
