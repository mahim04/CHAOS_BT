package com.mm.chaos.prob.ana.intraday.analyse.bar;

import java.text.DecimalFormat;
import java.util.logging.Logger;

import org.lst.trading.lib.series.BarSeries;

import com.mm.chaos.prob.ana.intraday.data.SMADTO;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class ANAPatternSMACalc 
{
	Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	DecimalFormat df = new DecimalFormat("#.##");
	//TA LIB Handle
	private Core lib = new Core();
	
	//NIFTY IntraDay Data
	BarSeries data = null;
	
    private RetCode retCode = null;
    private int lookback = 0;

    private MInteger outBegIdxSMAShort = new MInteger(); 
    private MInteger outNBElementSMAShort = new MInteger();
    
    private MInteger outBegIdxSMALong = new MInteger(); 
    private MInteger outNBElementSMALong = new MInteger();
    
	public ANAPatternSMACalc(BarSeries data) {
		super();
		this.data = data;
	}

	
	public SMADTO calculate()
	{
		double[] close = data.getClosePrices();
		
    	double smaShort[]   = new double[close.length];
    	double smaLong[] = new double[close.length];
    	
    	
    	//for smashort calculation
        retCode = lib.sma(0,close.length-1,close,10,outBegIdxSMAShort,outNBElementSMAShort,smaShort);
        
        if(outNBElementSMAShort.value > 0)
        	log.fine("SMA SHORT : " + smaShort[outNBElementSMAShort.value-1]);
        
    	//for SMA LONG calculation
        retCode = lib.sma(0,close.length-1,close,20,outBegIdxSMALong,outNBElementSMALong,smaLong);
        
        if(outNBElementSMALong.value > 0)
        	log.fine("SMA LONG : " + smaShort[outNBElementSMALong.value-1]);
        
        
//        System.out.println("outBegIdxSMAShort : " + outBegIdxSMAShort.value + ", outNBElementSMAShort : " + outNBElementSMAShort.value);
//        System.out.println("outBegIdxSMALong : " + outBegIdxSMALong.value + ", outNBElementSMALong : " + outNBElementSMALong.value);
//        
//        if(outNBElement.value > 4)
//        {
//	        for(int i=outNBElement.value-4; i<outNBElement.value; i++)
//	        {
//	        	System.out.println("SMA["+CHAOS_Utils.printDate(data.get(outBegIdx.value+i).getItem().getStart())
//	        			+"]: CLOSE=" + close[outBegIdx.value+i] + ", smaShort:" + smaShort[i] + ", smaLong:" + smaLong[i]);
//	        }
//        }
//        
        return new SMADTO(outBegIdxSMAShort, outNBElementSMAShort, outBegIdxSMALong, outNBElementSMALong, smaShort, smaLong);
	}
}
