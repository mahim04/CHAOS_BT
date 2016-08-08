package org.lst.trading.lib.backtest;

import static org.lst.trading.lib.util.Util.check;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.lst.trading.lib.model.Bar;
import org.lst.trading.lib.model.ClosedOrder;
import org.lst.trading.lib.model.TradingStrategy;
import org.lst.trading.lib.series.BarSeries;
import org.lst.trading.lib.series.DoubleSeries;
import org.lst.trading.lib.series.TimeSeries;
import org.lst.trading.lib.util.Statistics;

public class BarBacktest {

    public static class Result {
        DoubleSeries mPlHistory;
        DoubleSeries mMarginHistory;
        double mPl;
        List<ClosedOrder> mOrders;
        double mInitialFund;
        double mFinalValue;
        double mCommissions;

        public Result(double pl, DoubleSeries plHistory, DoubleSeries marginHistory, List<ClosedOrder> orders, double initialFund, double finalValue, double commisions) {
            mPl = pl;
            mPlHistory = plHistory;
            mMarginHistory = marginHistory;
            mOrders = orders;
            mInitialFund = initialFund;
            mFinalValue = finalValue;
            mCommissions = commisions;
        }

        public DoubleSeries getMarginHistory() {
            return mMarginHistory;
        }

        public double getInitialFund() {
            return mInitialFund;
        }

        public DoubleSeries getAccountValueHistory() {
            return getPlHistory().plus(mInitialFund);
        }

        public double getFinalValue() {
            return mFinalValue;
        }

        public double getReturn() {
            return mFinalValue / mInitialFund - 1;
        }

        public double getAnnualizedReturn() {
            return getReturn() * 250 / getDaysCount();
        }

        public double getSharpe() {
            return Statistics.sharpe(Statistics.returns(getAccountValueHistory().toArray()));
        }

        public double getMaxDrawdown() {
            return Statistics.drawdown(getAccountValueHistory().toArray())[0];
        }

        public double getMaxDrawdownPercent() {
            return Statistics.drawdown(getAccountValueHistory().toArray())[1];
        }

        public int getDaysCount() {
            return mPlHistory.size();
        }

        public DoubleSeries getPlHistory() {
            return mPlHistory;
        }

        public double getPl() {
            return mPl;
        }

        public double getCommissions() {
            return mCommissions;
        }

        public List<ClosedOrder> getOrders() {
            return mOrders;
        }
    }

    BarSeries mPriceSeries;
    double mDeposit = 0;
    double mLeverage = 1;
    double mCommission= 0.5;

    TradingStrategy<Bar> mStrategy;
    BarBacktestTradingContext mContext;

    Iterator<TimeSeries.Entry<Bar>> mPriceIterator;
    Result mResult;

    public BarBacktest(double deposit, BarSeries priceSeries) {
        check(priceSeries.isAscending());
        this.mDeposit = deposit;
        this.mPriceSeries = priceSeries;
    }

    public void setLeverage(double leverage) {
        mLeverage = leverage;
    }

    public double getLeverage() {
        return mLeverage;
    }

    public Result run(TradingStrategy<Bar> strategy) {
        initialize(strategy);
        while (nextStep()) ;
        return mResult;
    }

    public void initialize(TradingStrategy<Bar> strategy) {
        mStrategy = strategy;
        mContext = new BarBacktestTradingContext(mPriceSeries.getName(), mDeposit, mCommission, mPriceSeries, mLeverage);

//        mContext.mInstrument = mPriceSeries.getName();
//        mContext.mInitialFunds = mDeposit;
//        mContext.mLeverage = mLeverage;
        
        
        strategy.onStart(mContext);
        mPriceIterator = mPriceSeries.iterator();
        nextStep();
    }

    public boolean nextStep() 
    {
    	
        if (!mPriceIterator.hasNext()) 
        {
        	System.err.println("Exit due end of series....");
            finish();
            return false;
        }

        TimeSeries.Entry<Bar> entry = mPriceIterator.next();
        mContext.setCurrentEntry(entry.getItem());
        
        mContext.setTime(entry.getInstant());
        mContext.addtoPL(mContext.getPl(), entry.getInstant());
        mContext.addtoFundsHistory(mContext.getAvailableFunds(), entry.getInstant());
        if (mContext.getAvailableFunds() < 0) 
        {
        	System.err.println("Exit due to funds shortage.....");
            finish();
            return false;
        }

        mStrategy.onTick();

        //No need to add to history 
//        mContext.mHistory.add(entry);

        return true;
    }

    public Result getResult() {
        return mResult;
    }

    private void finish() {
        for (SimpleOrder order : new ArrayList<>(mContext.getOrderes())) {
            mContext.close(order);
        }

        mStrategy.onEnd();

        List<ClosedOrder> orders = Collections.unmodifiableList(mContext.getClosedOrders());
        mResult = new Result(mContext.getClosedPl(), mContext.getPL(), mContext.getFundsHistory(), orders, mDeposit, mDeposit + mContext.getClosedPl(), mContext.getCommissions());
    }
}
