package com.mm.chaos.prob.ana.intraday.analyse;

import java.text.DecimalFormat;

import com.mm.chaos.prob.ana.intraday.data.NiftyIntraDayData;
import com.mm.chaos.prob.ana.intraday.recommendation.Direction;
import com.mm.chaos.prob.ana.intraday.recommendation.Level;
import com.mm.chaos.prob.ana.intraday.recommendation.PositionStatus;
import com.mm.chaos.prob.ana.intraday.recommendation.Recommendation;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class ANAPatternEMA 
{
	DecimalFormat df = new DecimalFormat("#.##");
	//TA LIB Handle
	private Core lib = new Core();
	
	//NIFTY IntraDay Data
	NiftyIntraDayData nift = null;

	public ANAPatternEMA(NiftyIntraDayData nift) {
		super();
		this.nift = nift;
	}
	
	
	private double[] ema10 = null;
	private double[] ema20 = null;
	private double[] ema30 = null;
	private MInteger ema10last = new MInteger();
	private MInteger ema20last = new MInteger();
	private MInteger ema30last = new MInteger();

	
	private void calculate()
	{
		double openEma[] = nift.getClosePrices();
		
		MInteger begin = new MInteger();
		ema10 = new double[openEma.length];
		ema20 = new double[openEma.length];
		ema30 = new double[openEma.length];
		
        
//		lib.emaLookback(optInTimePeriod)
		//EMA 10
		RetCode rc = lib.ema(0, openEma.length -1, openEma, 10, begin, ema10last, ema10);
//		System.out.println("EMA:RC:: " + rc + ": END: " +end.value);
		
		//EMA 20
		rc = lib.ema(0, openEma.length -1, openEma, 20, begin, ema20last, ema20);
//		System.out.println("EMA:RC:: " + rc + ": END: " +end.value); 
		
		//EMA 30
		rc = lib.ema(0, openEma.length -1, openEma, 30, begin, ema30last, ema30);
//		System.out.println("EMA:RC:: " + rc + ": END: " +end.value);
	
		
		
//		int k=1;
//		for(double d : ema30)
//		{
//			System.out.println("EMA30:"+ k++ +" :: " + d);
//		}
		
		System.out.println("EMA VALUES:::: EMA10:" + df.format(ema10[ema10last.value -1]) + 
				", EMA20: "+ df.format(ema20[ema20last.value - 1]) + 
				", EMA30: "+ df.format(ema30[ema30last.value  - 1]));
		
	}
	
	
	public Recommendation runPattern()
	{
		calculate();
		return makeRecommendation();
	}
	
	private Recommendation makeRecommendation()
	{
		Recommendation recomm = new Recommendation();
		recomm.setEntryPoint(nift.getLastClose());
		
		System.out.println(", EMA30: "+ df.format(ema30[ema30last.value-1]) + ", EMA30[last-1]" + df.format(ema30[ema30last.value-2]) );
		
		//For recommendation:
		//For BUY side
		//Step 1 : check last trend for UP Trend
		if(nift.getLastCloseTrend())
		{
			if(ema30[ema30last.value-1] > ema30[ema30last.value-2] && ema20[ema20last.value-1] > ema20[ema20last.value-2] && ema10[ema10last.value-1] > ema10[ema10last.value-2] )
			{
				recomm.setDirection(Direction.UP);
				
				if(ema10[ema10last.value-1] > ema20[ema20last.value-1] && ema20[ema20last.value-1] > ema30[ema30last.value-1])
				{
					if(ema10[ema10last.value-1] > ema20[ema20last.value-1]*1.005)
					{
						recomm.setLevel(Level.STRONG_BUY);
						recomm.setPositionStatus(PositionStatus.ENTER);
					}
					else
					{
						recomm.setLevel(Level.BUY);
						recomm.setPositionStatus(PositionStatus.HOLD);
					}
					
				}else //direction got some what violated
				{
					recomm.setLevel(Level.SELL);
					recomm.setPositionStatus(PositionStatus.HOLD);
				}
			}else //direction got violated
			{
				recomm.setDirection(Direction.UP_BROKEN);
				recomm.setLevel(Level.SELL);
				recomm.setPositionStatus(PositionStatus.EXIT);
			}	
		}else
		{
		
			//For SELL side
			if(ema30[ema30last.value-1] < ema30[ema30last.value-2] && ema20[ema20last.value-1] < ema20[ema20last.value-2] && ema10[ema10last.value-1] < ema10[ema10last.value-2] )
			{
				recomm.setDirection(Direction.DOWN);
				
				if(ema10[ema10last.value-1] < ema20[ema20last.value-1] && ema20[ema20last.value-1] < ema30[ema30last.value-1])
				{
					if(ema10[ema10last.value-1] < ema20[ema20last.value-1]*1.005)
					{
						recomm.setLevel(Level.STRONG_SELL);
						recomm.setPositionStatus(PositionStatus.ENTER);
					}
					else
					{
						recomm.setLevel(Level.SELL);
						recomm.setPositionStatus(PositionStatus.HOLD);
					}
					
				}else //direction got some what violated
				{
					recomm.setLevel(Level.SELL);
					recomm.setPositionStatus(PositionStatus.HOLD);
				}
			}else //direction got violated
			{
				recomm.setDirection(Direction.DOWN_BROKEN);
				recomm.setLevel(Level.BUY);
				recomm.setPositionStatus(PositionStatus.EXIT);
			}		
		}
	
		return recomm;
	}

}
