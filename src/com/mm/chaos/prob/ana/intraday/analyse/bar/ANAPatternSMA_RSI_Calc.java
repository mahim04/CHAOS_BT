package com.mm.chaos.prob.ana.intraday.analyse.bar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.lst.trading.lib.model.Bar;
import org.lst.trading.lib.series.BarSeries;

import com.mm.chaos.prob.ana.intraday.data.SMADTO;
import com.mm.chaos.prob.calculators.RSICalculator;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;



//This is a combination of SMA and RSI.
//RSI is used for second level of verification
public class ANAPatternSMA_RSI_Calc 
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
    
    private MInteger outBegIdxRSI = new MInteger(); 
    private MInteger outNBElementRSI = new MInteger();
    
	public ANAPatternSMA_RSI_Calc(BarSeries data) {
		super();
		this.data = data;
	}

	
	public SMADTO calculate()
	{
		System.err.println("MM-- hack");
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
        
        //TA LIB RSI Calculator is not matching with Google ones.
        //therefore we have written our own calculator.
        Integer periodLength = 2;
        ArrayList<Double> rsiInput = new ArrayList<>();
        for(double d : close)
        {
        	rsiInput.add(d);
        }
        RSICalculator rsiCalc = new RSICalculator(periodLength, rsiInput); 
        
        ArrayList<Double> rsi  = rsiCalc.calculateRSI();
        		
        
        
        for(int i=0; i<rsi.size();i++)
        {
        	Bar bar = data.get(periodLength + i).getItem();
        	System.out.println("RSI[" + bar + "]RSI:" + rsi.get(i));
        }
        
        
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
