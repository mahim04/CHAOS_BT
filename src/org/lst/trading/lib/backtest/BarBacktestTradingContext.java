package org.lst.trading.lib.backtest;

import static org.lst.trading.lib.util.Util.check;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.lst.trading.lib.model.Bar;
import org.lst.trading.lib.model.ClosedOrder;
import org.lst.trading.lib.model.Order;
import org.lst.trading.lib.model.TradingContext;
import org.lst.trading.lib.series.BarSeries;
import org.lst.trading.lib.series.DoubleSeries;
import org.lst.trading.lib.series.TimeSeries.Entry;

class BarBacktestTradingContext implements TradingContext<Bar> 
{
	
	
    private Instant mInstant;
    private BarSeries mPrices;
    private String mInstrument;
    private DoubleSeries mPl = new DoubleSeries("pl");
    private DoubleSeries mFundsHistory = new DoubleSeries("funds");
    private double mInitialFunds=0;
    private double mCommissions=0.05;
    
    
    
    
    
    public BarBacktestTradingContext(String mInstrument, double mInitialFunds, double mCommissions, BarSeries mPrices, double mLeverage) 
    {
		super();
		this.mInstrument = mInstrument;
		this.mInitialFunds = mInitialFunds;
		this.mCommissions = mCommissions;
		this.mPrices = mPrices;
		this.mLeverage = mLeverage;
	}

	//This is for current value of Bar we are backtesting for
    private Bar currentEntry = null;
    private BarSeries mHistoryBars = new BarSeries(mInstrument);
    
    private int mOrderId = 1;

    private List<SimpleOrder> mOrders = new ArrayList<>();

    private double mClosedPl = 0;
    private List<SimpleClosedOrder> mClosedOrders = new ArrayList<>();
    private double mLeverage = 1;

    @Override public Instant getTime() {
        return mInstant;
    }
    
    @Override public void setTime(Instant ins) {
        this.mInstant = ins;
    }

    @Override public Bar getLastClosePrice(String instrument) 
    {
//    	System.err.println("mPrices.getLast()>> " + mPrices.getLast());
        return mPrices.getLast();
    }
    

    @Override public Order order(String instrument, boolean buy, int amount) {
        check(amount > 0);

        double price = getCurrentEntry().getClose();
        SimpleOrder order = new SimpleOrder(mOrderId++, instrument, getTime(), price, amount * (buy ? 1 : -1));
        mOrders.add(order);

        mCommissions += calculateCommission(order);

        return order;
    }

    @Override public ClosedOrder close(Order order) {
        SimpleOrder simpleOrder = (SimpleOrder) order;
        mOrders.remove(simpleOrder);
        Bar closeBar = getCurrentEntry();
        System.out.println("Close Order using "+ closeBar);
        SimpleClosedOrder closedOrder = new SimpleClosedOrder(simpleOrder, closeBar.getClose(), closeBar.getStart());
        mClosedOrders.add(closedOrder);
        mClosedPl += closedOrder.getPl();
        mCommissions += calculateCommission(order);

        return closedOrder;
    }

    @Override public double getPl() 
    {
//    	System.out.println("mClosedPl :" +mClosedPl + ", mOrders:" + mOrders.size() + ", getCurrentEntry():"+ getCurrentEntry() + ", commission:" + mCommissions);
        return mClosedPl + mOrders.stream().mapToDouble(t -> t.calculatePl(getCurrentEntry().getClose())).sum() - mCommissions;
    }

    @Override public String getInstrumentName() {
        return mInstrument;
    }

    @Override public double getAvailableFunds() {
        return getNetValue() - mOrders.stream().mapToDouble(t -> Math.abs(t.getAmount()) * t.getOpenPrice() / mLeverage).sum();
    }

    @Override public double getInitialFunds() {
        return mInitialFunds;
    }

    @Override public double getNetValue() {
        return mInitialFunds + getPl();
    }

    @Override public double getLeverage() {
        return mLeverage;
    }

    double calculateCommission(Order order) {
        return 1 + Math.abs(order.getAmount()) * 0.005;
    }

	@Override
	public List<String> getInstruments() 
	{
		System.err.println("Method getInstruments is not implemented in BarBacktesting.");
		return null;
	}

	@Override
	public Bar getCurrentEntry() {
		return currentEntry;
	}
	
	

	public void setCurrentEntry(Bar currentEntry) 
	{
		this.currentEntry = currentEntry;
		mHistoryBars.add(new Entry<Bar>(currentEntry, currentEntry.getStart()));
	}
	

	@Override
	public List<SimpleOrder> getOrderes() {
		return mOrders;
	}

	@Override
	public List<SimpleClosedOrder> getClosedOrders() {
		return mClosedOrders;
	}

	@Override
	public double getClosedPl() {
		return mClosedPl;
	}

	@Override
	public DoubleSeries getPL() {
		return mPl;
	}

	@Override
	public DoubleSeries getFundsHistory() {
		return mFundsHistory;
	}

	@Override
	public double getCommissions() {
		return mCommissions;
	}

	@Override
	public void addtoPL(Double pl, Instant instant) {
		mPl.add(new Entry<Double>(pl, instant));
	}

	@Override
	public void addtoFundsHistory(Double funds, Instant instant) {
		mFundsHistory.add(new Entry<Double>(funds, instant));
	}

	@Override
	public BarSeries getHistory() {
		return mHistoryBars;
	}
}

