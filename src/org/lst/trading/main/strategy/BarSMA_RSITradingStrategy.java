package org.lst.trading.main.strategy;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import org.lst.trading.lib.model.Order;
import org.lst.trading.lib.model.TradingContext;
import org.lst.trading.lib.model.TradingStrategy;

import com.mm.chaos.prob.ana.intraday.analyse.bar.ANAPatternSMA_RSI_Calc;
import com.mm.chaos.prob.ana.intraday.data.SMADTO;
import com.mm.chaos.prob.utils.CHAOS_Utils;

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
    	//Also ensure we are within trading window of 9:45 AM to 3:15 PM.
    	if(
    			mContext.getHistory().getLast().getStart().isAfter(mContext.getTradingStartDay().toDate().toInstant())
    			&&
    			mContext.getHistory().getLast().getStart().isBefore(getClosingEODInstant(mContext.getHistory().getLast().getStart(), "15:20:00"))
    	  )
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
	    		
	    		//Check 1 : If we are near EOD then close this position.
	    		if(mContext.getHistory().getLast().getStart().isAfter(getClosingEODInstant(mContext.getHistory().getLast().getStart(), "15:15:00")))
	    		{
	    			System.out.println("EOD is approaching. Closing the orders now");
	    			closeOrders();
	    		}
	    		
	    		//Check 2 : We are keeping a stop loss of 5 points.
	    		for(Order o: openOrderArr)
	    		{
		    			if((mContext.getHistory().getLast().getClose() - o.getOpenPrice())< -5)
		    			{
		    				System.out.println("Loss limit was breached. Closing order.");
		    				closeOrders();
		    				break;
		    			}
		    	}
	    		
	    		
	    		if(smadto.getSmaShort()[endShort -1] - smadto.getSmaLong()[endLong -1] <0)
	    		{
	    			System.out.println("SMA trend has brokern. Closing order");
	    			closeOrders();
	    		}
    		
	    	}
    	}
    }
    
    private Instant getClosingEODInstant(Instant currentIns, String eodTime)
    {
    	Date curDT = new Date(currentIns.toEpochMilli());
    	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    	
    	String dt = df.format(curDT);
    	
    	
    	SimpleDateFormat dfNew = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    	try 
    	{
    		Date newDT = dfNew.parse(dt +" "+ eodTime);
    		System.out.println("EOD Date: " + CHAOS_Utils.printDate(newDT.toInstant()));
			return newDT.toInstant();
		} catch (ParseException e) 
    	{
			e.printStackTrace();
		}
    	return null;
    }
    
    private void closeOrders()
    {
    	System.err.println("CLosing Order........");
		openPositionIndicator = Boolean.FALSE;
		openOrderArr.stream().forEach(o -> {System.out.println(o); mContext.close(o);});
		openOrderArr = new ArrayList<>();
    }
    
}






