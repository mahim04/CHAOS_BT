package org.lst.trading.lib.model;

import java.time.Instant;
import java.util.List;

import org.lst.trading.lib.backtest.SimpleClosedOrder;
import org.lst.trading.lib.backtest.SimpleOrder;
import org.lst.trading.lib.series.BarSeries;
import org.lst.trading.lib.series.DoubleSeries;

public interface TradingContext<T> {
    Instant getTime();
    
    void setTime(Instant ins);

    T getLastClosePrice(String instrument);

    T getCurrentEntry();
    
    void setCurrentEntry(T currentEntry);
    
    //Not used anywhere 
    public BarSeries getHistory();

    Order order(String instrument, boolean buy, int amount);

    ClosedOrder close(Order order);

    double getPl();

    List<String> getInstruments();
    
    String getInstrumentName();

    double getAvailableFunds();

    double getInitialFunds();

    double getNetValue();

    double getLeverage();
    
    List<SimpleOrder> getOrderes();
    
    List<SimpleClosedOrder> getClosedOrders();
    
    double getClosedPl();
    
    DoubleSeries getPL();
    
    DoubleSeries getFundsHistory();
    
    double getCommissions();
    
    void addtoPL(Double pl, Instant instant);
    
    void addtoFundsHistory(Double funds, Instant instant);
   
}
