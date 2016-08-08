package org.lst.trading.lib.series;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.lst.trading.lib.model.Bar;

public class BarSeries extends TimeSeries<Bar> {
    String mName;

    BarSeries(List<Entry<Bar>> data, String name) {
        super(data);
        mName = name;
    }

    public BarSeries(String name) {
        super();
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public BarSeries merge(BarSeries other, MergeFunction<Bar, Bar> f) {
        return new BarSeries(BarSeries.merge(this, other, f).mData, mName);
    }

    public BarSeries mapToBar(Function<Bar, Bar> f) {
        return new BarSeries(map(f).mData, mName);
    }


    public Bar getLast() {
        return getData().get(size() - 1).getItem();
    }

    public BarSeries tail(int n) {
        return new BarSeries(getData().subList(size() - n, size()), getName());
    }

//    public double[] toArray() {
//        return stream().map(Entry::getItem).toArray();
//    }

    @Override public BarSeries toAscending() {
        return new BarSeries(super.toAscending().mData, getName());
    }

    @Override public BarSeries toDescending() {
        return new BarSeries(super.toDescending().mData, getName());
    }

    @Override public BarSeries lag(int k) {
        return new BarSeries(super.lag(k).mData, getName());
    }

    @Override public String toString() {
        return mData.isEmpty() ? "DoubleSeries{empty}" :
            "DoubleSeries{" +
                "mName=" + mName +
                ", from=" + mData.get(0).getInstant() +
                ", to=" + mData.get(mData.size() - 1).getInstant() +
                ", size=" + mData.size() +
                '}';
    }
    
    
    public double[] getClosePrices()
    {
    	ArrayList<Double> closeArr = new ArrayList<>();
    	this.stream().forEach(bar -> closeArr.add(bar.getItem().getClose()));
    	return closeArr.stream().mapToDouble(Double::doubleValue).toArray(); 
    }
    
    public double[] getOpenPrices()
    {
    	ArrayList<Double> openArr = new ArrayList<>();
    	this.stream().forEach(bar -> openArr.add(bar.getItem().getOpen()));
    	return openArr.stream().mapToDouble(Double::doubleValue).toArray(); 
    }
    
    public double[] getHighPrices()
    {
    	ArrayList<Double> highArr = new ArrayList<>();
    	this.stream().forEach(bar -> highArr.add(bar.getItem().getHigh()));
    	return highArr.stream().mapToDouble(Double::doubleValue).toArray(); 
    }
    
    public double[] getLowPrices()
    {
    	ArrayList<Double> lowArr = new ArrayList<>();
    	this.stream().forEach(bar -> lowArr.add(bar.getItem().getLow()));
    	return lowArr.stream().mapToDouble(Double::doubleValue).toArray(); 
    }
    
    
}
