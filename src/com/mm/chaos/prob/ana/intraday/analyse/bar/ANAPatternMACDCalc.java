package com.mm.chaos.prob.ana.intraday.analyse.bar;

import java.text.DecimalFormat;

import org.lst.trading.lib.series.BarSeries;

import com.mm.chaos.prob.ana.intraday.data.MACDDTO;
import com.mm.chaos.prob.utils.CHAOS_Utils;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class ANAPatternMACDCalc 
{
	
	DecimalFormat df = new DecimalFormat("#.##");
	//TA LIB Handle
	private Core lib = new Core();
	
	//NIFTY IntraDay Data
	BarSeries data = null;
	
    private RetCode retCode = null;
    private int lookback = 0;

    private MInteger outBegIdx = new MInteger(); 
    private MInteger outNBElement = new MInteger();
    
	public ANAPatternMACDCalc(BarSeries data) {
		super();
		this.data = data;
	}

	
	public MACDDTO calculate()
	{
		double[] close = data.getClosePrices();
		
    	double macd[]   = new double[close.length];
    	double signal[] = new double[close.length];
    	double hist[]   = new double[close.length];
        retCode = lib.macd(0,close.length-1,close,15,26,9,outBegIdx,outNBElement,macd,signal,hist);
        
        System.out.println("outBegIdx : " + outBegIdx.value + ", outNbElement : " + outNBElement.value);
        
        for(int i=0; i<outNBElement.value; i++)
        {
        	System.out.println("MACD["+CHAOS_Utils.printDate(data.get(outBegIdx.value+i).getItem().getStart())
        			+"]: CLOSE=" + close[outBegIdx.value+i] + ", macd:" + macd[i] + ", signal:" + signal[i] + ", hist:" + hist[i]);
        }
        
        return null;
	}
	

}
