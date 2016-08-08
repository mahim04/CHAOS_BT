package org.lst.trading.main.strategy;

import java.util.HashMap;
import java.util.Map;

import org.lst.trading.lib.model.Order;
import org.lst.trading.lib.model.TradingContext;
import org.lst.trading.lib.model.TradingStrategy;

import com.mm.chaos.prob.ana.intraday.analyse.bar.ANAPatternMACDCalc;
import com.tictactec.ta.lib.Core;

public class BarMACDTradingStrategy<T> implements TradingStrategy<T> {
    Map<String, Order> mOrders = new HashMap<>();;
    TradingContext<T> mContext;

    @Override public void onStart(TradingContext<T> context) 
    {
        mContext = context;
    }

    @Override public void onTick() 
    {

//     mOrders.put(mContext.getInstrumentName(), mContext.order(mContext.getInstrumentName(), true, 1));
    	new ANAPatternMACDCalc(mContext.getHistory()).calculate();
    }
}
