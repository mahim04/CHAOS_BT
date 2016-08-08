package org.lst.trading.main.strategy;

import java.util.ArrayList;

import org.lst.trading.lib.model.Order;
import org.lst.trading.lib.model.TradingContext;
import org.lst.trading.lib.model.TradingStrategy;

import com.mm.chaos.prob.ana.intraday.analyse.bar.ANAPatternSMACalc;
import com.mm.chaos.prob.ana.intraday.data.SMADTO;

public class BarSMATradingStrategy<T> implements TradingStrategy<T> 
{
    ArrayList<Order> openOrderArr = new ArrayList<>();
    TradingContext<T> mContext;

    private Boolean openPositionIndicator = false;
    
    @Override public void onStart(TradingContext<T> context) 
    {
        mContext = context;
    }

    @Override public void onTick() 
    {

//     mOrders.put(mContext.getInstrumentName(), mContext.order(mContext.getInstrumentName(), true, 1));
    	SMADTO smadto = new ANAPatternSMACalc(mContext.getHistory()).calculate();
    	
    	int beginShort = smadto.getOutBegIdxSMAShort().value;
    	int endShort = smadto.getOutBegIdxSMAShort().value;
    	
    	int beginLong = smadto.getOutBegIdxSMALong().value;
    	int endLong = smadto.getOutNBElementSMALong().value;
    	
    	//first check if we have enough elements.
    	if(!openPositionIndicator)
    	{
	    	if(endLong  - beginLong > 5)
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
	    	}
    	}
    	else
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
