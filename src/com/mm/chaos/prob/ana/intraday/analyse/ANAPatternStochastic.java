package com.mm.chaos.prob.ana.intraday.analyse;

import java.text.DecimalFormat;

import com.mm.chaos.prob.ana.intraday.data.NiftyIntraDayData;
import com.mm.chaos.prob.ana.intraday.data.StochasticDTO;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class ANAPatternStochastic 
{
	
	DecimalFormat df = new DecimalFormat("#.##");
	//TA LIB Handle
	private Core lib = new Core();
	
	//NIFTY IntraDay Data
	NiftyIntraDayData nift = null;
	
    private RetCode retCode = null;
    private int lookback = 0;

    private MInteger outBegIdx = new MInteger(); 
    private MInteger outNBElement = new MInteger();
    private double[] outSlowK = null;
    private double[] outSlowD = null;
    
	public ANAPatternStochastic(NiftyIntraDayData nift) {
		super();
		this.nift = nift;
	}

	
	public StochasticDTO calculate()
	{
		double[] close = nift.getClosePrices();
		double[] open = nift.getOpenPrices();
		double[] high = nift.getHighPrices();
		double[] low = nift.getLowPrices();
		
		outSlowK = new double[close.length];
		outSlowD = new double[close.length];
		
//		System.out.println("Input close size " + close.length);
    	
        retCode = lib.stoch(0, close.length - 1, high, low, close, 10, 10, MAType.Sma, 3, MAType.Sma, outBegIdx, outNBElement, outSlowK, outSlowD);
        
//        System.out.println("This is retCode : " + retCode);
        
        return new StochasticDTO(outBegIdx, outNBElement, outSlowK, outSlowD);
	}
	

}
