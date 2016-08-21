package org.lst.trading.main.strategy;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.lst.trading.lib.model.Order;
import org.lst.trading.lib.model.TradingContext;
import org.lst.trading.lib.model.TradingStrategy;

import com.mm.chaos.prob.ana.intraday.analyse.bar.ANAPatternSMA_RSI_Calc;
import com.mm.chaos.prob.ana.intraday.data.SMADTO;

public class BarSMA_RSITradingStrategy<T> implements TradingStrategy<T> 
{
    ArrayList<Order> openOrderArr = new ArrayList<>();
    TradingContext<T> mContext;
    DecimalFormat df = new DecimalFormat("#.##");

    private Boolean openPositionIndicator = false;
    
    @Override public void onStart(TradingContext<T> context) 
    {
        mContext = context;
    }

    @Override public void onTick() 
    {

//     mOrders.put(mContext.getInstrumentName(), mContext.order(mContext.getInstrumentName(), true, 1));
    	SMADTO smadto = new ANAPatternSMA_RSI_Calc(mContext.getHistory()).calculate();
    	
    	int beginShort = smadto.getOutBegIdxSMAShort().value;
    	int endShort = smadto.getOutNBElementSMAShort().value;
    	
    	int beginLong = smadto.getOutBegIdxSMALong().value;
    	int endLong = smadto.getOutNBElementSMALong().value;
    	
    	
    	
    	//Trading start day is required because this will ensure we have a consistent output compared to google api which uses 
    	//last day carry forward data.
    	if(mContext.getHistory().getLast().getStart().isAfter(mContext.getTradingStartDay().toDate().toInstant()))
    	{
    		
	    		    	
	    	System.out.println
			( mContext.getHistory().getLast() + 
					
					"\n smadto.getSmaShort()[endShort -4]:" + df.format(smadto.getSmaShort()[endShort -4]) + ", "
					+ "smadto.getSmaLong()[endLong -4]:" + df.format(smadto.getSmaLong()[endLong -4]) +", \n" + 
					
					"smadto.getSmaShort()[endShort -3]:" + df.format(smadto.getSmaShort()[endShort -3]) + ", "
					+ "smadto.getSmaLong()[endLong -3]:" + df.format(smadto.getSmaLong()[endLong -3]) +", \n" + 
	
						"smadto.getSmaShort()[endShort -2]:" + df.format(smadto.getSmaShort()[endShort -2]) + ", "
						+ "smadto.getSmaLong()[endLong -2]:" + df.format(smadto.getSmaLong()[endLong -2]) +", \n" + 
	
					"smadto.getSmaShort()[endShort -1]:" + df.format(smadto.getSmaShort()[endShort -1]) + ", "
						+ "smadto.getSmaLong()[endLong -1]:" + df.format(smadto.getSmaLong()[endLong -1]) +""
//						 + "smadto.getSmaShort()[endShort -0]:" + df.format(smadto.getSmaShort()[endShort -0]) + ", "
//						+ "smadto.getSmaLong()[endLong -0]:" + df.format(smadto.getSmaLong()[endLong -0]) +""
					);
    		
	    	//first check if we have enough elements.
	    	if(!openPositionIndicator)
	    	{
	    	
	    		if(
	    				(smadto.getSmaShort()[endShort -4] - smadto.getSmaLong()[endLong -4]<0 ) &&
	    				(smadto.getSmaShort()[endShort -3] - smadto.getSmaLong()[endLong -3]<0 ) &&
	    				(smadto.getSmaShort()[endShort -2] - smadto.getSmaLong()[endLong -2] > 0 ) &&
	    				(smadto.getSmaShort()[endShort -1] - smadto.getSmaLong()[endLong -1] > 0 )
	    		  )
	    		{
	    			System.err.println("Opening Order........");
	    			//Time to open a position
	    			openPositionIndicator = Boolean.TRUE;
	    			openOrderArr.add(mContext.order(mContext.getInstrumentName(), true, 1));
	    		}
	    	}else
	    	{
	    		//Moment we go back down we should exit.
	    		//lets assume we are not working on noise clearing.
	    		if(smadto.getSmaShort()[endShort -1] - smadto.getSmaLong()[endLong -1] <0)
	    		{
	    			System.err.println("CLosing Order........");
	    			openPositionIndicator = Boolean.FALSE;
	    			openOrderArr.stream().forEach(o -> {System.out.println(o); mContext.close(o);});
	    			openOrderArr = new ArrayList<>();
	    		}
    		
	    	}
    	}
    }
    
}
