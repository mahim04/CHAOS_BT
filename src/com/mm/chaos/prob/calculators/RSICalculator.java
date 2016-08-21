package com.mm.chaos.prob.calculators;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Stack;

public class RSICalculator 
{
	 private int periodLength;
	    private final Stack<Averages> avgList;
	    private final ArrayList<Double> pricesArr;
	    private final ArrayList<Double> rsi;

	    public RSICalculator(int periodLength, ArrayList<Double> pricesArr) 
	    {
	        super();
	        this.periodLength = periodLength;
	        this.avgList = new Stack<Averages>();
	        this.pricesArr = pricesArr;
	        this.rsi = new ArrayList<>();
	    }

	    
	    public ArrayList<Double> calculateRSI()
	    {
	    	for(int i=periodLength; i<pricesArr.size();i++)
	    	{
	    		rsi.add(calculate(new ArrayList<Double>(pricesArr.subList(0, i))));
	    	}
	    	return rsi;
	    }
	    
	    private double calculate(ArrayList<Double> prices) {
	        double value = 0;
	        int pricesSize = prices.size();
	        int lastPrice = pricesSize - 1;
	        int firstPrice = lastPrice - periodLength + 1;

	        double gains = 0;
	        double losses = 0;
	        double avgUp = 0;
	        double avgDown = 0;

	        double delta = prices.get(lastPrice)
	                - prices.get(lastPrice - 1);
	        gains = Math.max(0, delta);
	        losses = Math.max(0, -delta);

	        if (avgList.isEmpty()) {
	            for (int bar = firstPrice + 1; bar <= lastPrice; bar++) {
	                double change = prices.get(bar)
	                        - prices.get(bar - 1);
	                gains += Math.max(0, change);
	                losses += Math.max(0, -change);
	            }
	            avgUp = gains / periodLength;
	            avgDown = losses / periodLength;
	            avgList.push(new Averages(avgUp, avgDown));

	        } else {

	            Averages avg = avgList.pop();
	            avgUp = avg.getAvgUp();
	            avgDown = avg.getAvgDown();
	            avgUp = ((avgUp * (periodLength - 1)) + gains) / (periodLength);
	            avgDown = ((avgDown * (periodLength - 1)) + losses)
	                    / (periodLength);
	            avgList.add(new Averages(avgUp, avgDown));
	        }
	        value = 100 - (100 / (1 + (avgUp / avgDown)));

	        return Math.round(value);
	    }

	    private class Averages {

	        private final double avgUp;
	        private final double avgDown;

	        public Averages(double up, double down) {
	            this.avgDown = down;
	            this.avgUp = up;
	        }

	        public double getAvgUp() {
	            return avgUp;
	        }

	        public double getAvgDown() {
	            return avgDown;
	        }
	        
	        @Override public String toString()
	        {
	        	return "Averages[avgUP:" + avgUp + ", avgDown:" + avgDown + "]";
	        }
	        
	    }

	    public int getPeriodLength() {
	        return periodLength;
	    }

}
