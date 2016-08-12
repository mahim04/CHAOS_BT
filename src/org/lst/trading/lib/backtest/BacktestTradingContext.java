package org.lst.trading.lib.backtest;

import static org.lst.trading.lib.util.Util.check;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.lst.trading.lib.model.ClosedOrder;
import org.lst.trading.lib.model.Order;
import org.lst.trading.lib.model.TradingContext;
import org.lst.trading.lib.series.BarSeries;
import org.lst.trading.lib.series.DoubleSeries;
import org.lst.trading.lib.series.MultipleDoubleSeries;

class BacktestTradingContext implements TradingContext<DoubleSeries> {
    Instant mInstant;
    List<Double> mPrices;
    List<String> mInstruments;
    DoubleSeries mPl = new DoubleSeries("pl");
    DoubleSeries mFundsHistory = new DoubleSeries("funds");
    MultipleDoubleSeries mHistory;
    double mInitialFunds;
    double mCommissions;

    int mOrderId = 1;

    List<SimpleOrder> mOrders = new ArrayList<>();

    double mClosedPl = 0;
    List<SimpleClosedOrder> mClosedOrders = new ArrayList<>();
    double mLeverage;

    @Override public Instant getTime() {
        return mInstant;
    }

    @Override public DoubleSeries getLastClosePrice(String instrument) 
    {
    	System.err.println("For now this is broken");
    	return null;
//        return mPrices.get(mInstruments.indexOf(instrument));
    }
    
    
    public double getCurrentPrice() 
    {
    	System.err.println("For now this is broken");
    	return -1.0;
//        return mPrices.get(mInstruments.indexOf(instrument));
    }
    
    

//    @Override public Stream<TimeSeries.Entry<Double>> getHistory(String instrument) {
//        int index = mInstruments.indexOf(instrument);
//        return mHistory.reversedStream().map(t -> new TimeSeries.Entry<>(t.getItem().get(index), t.getInstant()));
//    }

    @Override public Order order(String instrument, boolean buy, int amount) {
        check(amount > 0);

        double price = getCurrentPrice();
        SimpleOrder order = new SimpleOrder(mOrderId++, instrument, getTime(), price, amount * (buy ? 1 : -1));
        mOrders.add(order);

        mCommissions += calculateCommission(order);

        return order;
    }

    @Override public ClosedOrder close(Order order) {
        SimpleOrder simpleOrder = (SimpleOrder) order;
        mOrders.remove(simpleOrder);
        double price = getCurrentPrice();
        SimpleClosedOrder closedOrder = new SimpleClosedOrder(simpleOrder, price, getTime());
        mClosedOrders.add(closedOrder);
        mClosedPl += closedOrder.getPl();
        mCommissions += calculateCommission(order);

        return closedOrder;
    }

    @Override public double getPl() {
        return mClosedPl + mOrders.stream().mapToDouble(t -> t.calculatePl(getCurrentPrice())).sum() - mCommissions;
    }

    @Override public List<String> getInstruments() {
        return mInstruments;
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
	public String getInstrumentName() {
		System.err.println("Method is not implemented for BacktestTradingContext.");
		return null;
	}

	@Override
	public void setTime(Instant ins) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public DoubleSeries getCurrentEntry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCurrentEntry(DoubleSeries currentEntry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SimpleOrder> getOrderes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SimpleClosedOrder> getClosedOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getClosedPl() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DoubleSeries getPL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DoubleSeries getFundsHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getCommissions() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addtoPL(Double pl, Instant instant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addtoFundsHistory(Double funds, Instant instant) {
		// TODO Auto-generated method stub
		
	}

	public DoubleSeries getHistory(String instrument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BarSeries getHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DateTime getTradingStartDay() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

