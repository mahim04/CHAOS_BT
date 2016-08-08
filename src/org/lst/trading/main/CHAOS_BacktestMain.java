package org.lst.trading.main;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;

import org.lst.trading.lib.backtest.BarBacktest;
import org.lst.trading.lib.model.Bar;
import org.lst.trading.lib.model.ClosedOrder;
import org.lst.trading.lib.model.TradingStrategy;
import org.lst.trading.lib.series.BarSeries;
import org.lst.trading.lib.series.MultipleDoubleSeries;
import org.lst.trading.lib.util.Util;
import org.lst.trading.lib.util.chaos.CHAOSFinance;
import org.lst.trading.main.strategy.*;

import com.mm.chaos.prob.utils.CHAOS_Utils;

public class CHAOS_BacktestMain {
    public static void main(String[] args) throws URISyntaxException, IOException {
        // initialize the trading strategy
//        TradingStrategy strategy = new CointegrationTradingStrategy(x, y);
        TradingStrategy<Bar> strategy = new BarSMATradingStrategy<Bar>();

        // download historical prices
        CHAOSFinance chaosFin = new CHAOSFinance();
        BarSeries barSeries = chaosFin.getHistoricalAdjustedPricesinBar("NIFTY").toBlocking().first();

        
        // initialize the backtesting engine
        int deposit = 100000;
        BarBacktest barBackTest = new BarBacktest(deposit, barSeries);
        barBackTest.setLeverage(1);

        // do the backtest
        BarBacktest.Result result = barBackTest.run(strategy);

        // show results
        StringBuilder orders = new StringBuilder();
        orders.append("id,amount,side,instrument,from,to,open,close,pl\n");
        for (ClosedOrder order : result.getOrders()) {
            orders.append(format(Locale.US, "%d,%d,%s,%s,%s,%s,%f,%f,%f\n", order.getId(), Math.abs(order.getAmount()), order.isLong() ? "Buy" : "Sell", 
            				order.getInstrument(), CHAOS_Utils.printDate(order.getOpenInstant()), CHAOS_Utils.printDate(order.getCloseInstant()), 
            				order.getOpenPrice(), order.getClosePrice(), order.getPl()));
        }
        System.out.print(orders);

        int days = barSeries.size();

        System.out.println();
        System.out.println("Backtest result of " + strategy.getClass() + ": " + strategy);
        System.out.println("Prices: " + barSeries);
        System.out.println(format(Locale.US, "Simulated %d days, Initial deposit %d, Leverage %f", days, deposit, barBackTest.getLeverage()));
        System.out.println(format(Locale.US, "Commissions = %f", result.getCommissions()));
        System.out.println(format(Locale.US, "P/L = %.2f, Final value = %.2f, Result = %.2f%%, Annualized = %.2f%%, Sharpe (rf=0%%) = %.2f", result.getPl(), result.getFinalValue(), result.getReturn() * 100, result.getReturn() / (days / 251.) * 100, result.getSharpe()));

        System.out.println("Orders: " + Util.writeStringToTempFile(orders.toString()));
        System.out.println("Statistics: " + Util.writeCsv(new MultipleDoubleSeries(result.getPlHistory(), result.getMarginHistory())));
    }
}
