package org.lst.trading.lib.util;

import java.io.Reader;

import org.lst.trading.lib.series.BarSeries;
import org.lst.trading.lib.series.DoubleSeries;

import rx.Observable;

public interface HistoricalPriceService {
    Observable<DoubleSeries> getHistoricalAdjustedPrices(String symbol);
    Observable<BarSeries> getHistoricalAdjustedPricesinBar(String symbol);
}
