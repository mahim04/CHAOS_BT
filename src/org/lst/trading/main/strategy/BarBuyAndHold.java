package org.lst.trading.main.strategy;

import java.util.HashMap;
import java.util.Map;

import org.lst.trading.lib.model.Bar;
import org.lst.trading.lib.model.Order;
import org.lst.trading.lib.model.TradingContext;
import org.lst.trading.lib.model.TradingStrategy;

public class BarBuyAndHold<T> implements TradingStrategy<T> {
    Map<String, Order> mOrders;
    TradingContext<T> mContext;

    @Override public void onStart(TradingContext<T> context) {
        mContext = context;
    }

    @Override public void onTick() {
        if (mOrders == null) 
        {
            mOrders = new HashMap<>();
//            mContext.getInstrument().stream().forEach(instrument -> mOrders.put(instrument, mContext.order(instrument, true, 1)));
           mOrders.put(mContext.getInstrumentName(), mContext.order(mContext.getInstrumentName(), true, 1));
        }
    }
}
