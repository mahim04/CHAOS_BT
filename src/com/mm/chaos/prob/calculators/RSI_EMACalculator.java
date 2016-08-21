package com.mm.chaos.prob.calculators;

import java.util.ArrayList;

import org.lst.trading.lib.series.BarSeries;
import org.lst.trading.lib.util.chaos.CHAOSFinance;

import com.mm.chaos.prob.ana.intraday.data.RSIDTO;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class RSI_EMACalculator 
{
	 private int periodLength;
	 private BarSeries barSeries;
	 Core lib = new Core();
	 
	public RSI_EMACalculator(int periodLength, BarSeries barSeries) {
		super();
		this.periodLength = periodLength;
		this.barSeries = barSeries;
		
	}
	
	public static void main(String a[])
	{
        // download historical prices
        CHAOSFinance chaosFin = new CHAOSFinance();
        BarSeries barSeries = chaosFin.getHistoricalAdjustedPricesinBar("NIFTY").toBlocking().first();
        
        int i=0;
        ArrayList<RSIDTO> rsiDTOs = new RSI_EMACalculator(2, barSeries).calculate();
        for(RSIDTO r : rsiDTOs)
        {
        	System.out.println(r);
        	
        	if(i++>20) break;
        }
        
	}
	
	public ArrayList<RSIDTO> calculate()
	{
		ArrayList<RSIDTO> rsiArr = new ArrayList<>();
		double close[] = barSeries.getClosePrices();
		for(int i=periodLength + 1; i<close.length; i++)
		{
			ArrayList<Double> input = new ArrayList<>();
			for(int j=(i - (periodLength + 1)); j<(i); j++)
			{
				input.add(close[j]);
			}
			rsiArr.add(new RSIDTO(barSeries.get(i - 1).getInstant(),calculateRSI(input)));
		}
		return rsiArr;
	}
	
	private Double calculateRSI(ArrayList<Double> input)
	{
		//Step 1 sort out gains and losses from the series.
		double gains[] = new double[input.size()-1];
		double losses[] = new double[input.size()-1];
		int i=0;
		Boolean flag= Boolean.TRUE;
		Double preVal = 0.0;
		for(Double d: input)
		{
			if(flag)
			{
				preVal = d;
				flag= Boolean.FALSE;
				continue;
			}
			
			Double diff = d - preVal;
			
			if(diff > 0)//gains
			{
				gains[i++]= diff;
			}
			else
			{
				losses[i++] = Math.abs(diff);
			}
			preVal = d;
		}
		
		//calculate EMA
		Double avgGains  = calcEMA(gains);
		Double avgLosses = calcEMA(losses);
		
		if(avgGains<=0)
		{
			//means all losses so return 0;
			return 0.0;
		}
		if(avgLosses<=0)
		{
			//means all gains so return 100
			return 100.0;			
		}
		
		Double RS = avgGains / avgLosses;
		Double rsi = 100 - (100/(1 + RS));
		
		return rsi;
	}
	

	private Double calcEMA(double in[])
	{
		MInteger outBegIdx = new MInteger();
		MInteger outNBElement = new MInteger();
		double[] outReal = new double[in.length];
		
		RetCode ret = lib.ema(0, in.length-1, in, periodLength, outBegIdx, outNBElement, outReal);
		return outReal[outNBElement.value - 1];
	}
	 
}









