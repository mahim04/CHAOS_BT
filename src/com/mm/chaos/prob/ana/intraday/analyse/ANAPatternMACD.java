package com.mm.chaos.prob.ana.intraday.analyse;

import java.text.DecimalFormat;

import com.mm.chaos.prob.ana.intraday.data.MACDDTO;
import com.mm.chaos.prob.ana.intraday.data.NiftyIntraDayData;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class ANAPatternMACD 
{
	
	DecimalFormat df = new DecimalFormat("#.##");
	//TA LIB Handle
	private Core lib = new Core();
	
	//NIFTY IntraDay Data
	NiftyIntraDayData nift = null;
	
    private RetCode retCode = null;
    private int lookback = 0;

    private MInteger outBegIdx = new MInteger(); 
    private MInteger outNbElement = new MInteger();
    
	public ANAPatternMACD(NiftyIntraDayData nift) {
		super();
		this.nift = nift;
	}

	
	public MACDDTO calculate()
	{
		double[] close = nift.getClosePrices();
//		System.out.println("Input close size " + close.length);
    	double macd[]   = new double[close.length];
    	double signal[] = new double[close.length];
    	double hist[]   = new double[close.length];
   	    lookback = lib.macdLookback(15,26,9);
        retCode = lib.macd(0,close.length-1,close,15,26,9,outBegIdx,outNbElement,macd,signal,hist);
        
//        System.out.println("This is retCode : " + retCode);
        
        
        return new MACDDTO(outBegIdx, outNbElement, macd, signal, hist);
	}
	

}
