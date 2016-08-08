package com.mm.chaos.prob.ana.intraday.executions;

import java.util.ArrayList;

import com.mm.chaos.prob.ana.intraday.recommendation.Direction;
import com.mm.chaos.prob.ana.intraday.recommendation.Level;
import com.mm.chaos.prob.ana.intraday.recommendation.PositionStatus;
import com.mm.chaos.prob.ana.intraday.recommendation.Recommendation;
import com.mm.chaos.prob.ana.intraday.simulator.PositionManager;

public class NiftyExecutionManager 
{

	ArrayList<Recommendation> recommArr = new ArrayList<>();
	PositionManager pm = new PositionManager();
	
	
	public void exitCallPosition(Recommendation recomm) throws Exception
	{
		recommArr.add(recomm);
		
		if(recomm == null)
		{
			System.err.println("Can not execute null recommendation");
			return;
		}
		
		pm.closePosition(recomm.getEntryPoint());
	}
	
	public void enterCallPosition(Recommendation recomm) throws Exception
	{
		recommArr.add(recomm);
		
		if(recomm == null)
		{
			System.err.println("Can not execute null recommendation");
			return;
		}
		
		pm.addPosition(recomm.getEntryPoint(), PositionStatus.CALL);
	}
	
	
	public void runCloseOfDay()
	{
		if(recommArr.size()>0)
		{
			pm.closeOfDay(recommArr.get(recommArr.size()-1).getEntryPoint());
		}
	}

	
	public void executeRecommendationMACD_STO_Combined(Recommendation recomm) throws Exception
	{
		if(recomm.getDirection() == Direction.UP && recomm.getLevel() == Level.STRONG_BUY && recomm.getPositionStatus() == PositionStatus.ENTER)
		{
			//Take positive position. For now assume we are 2 points more than last close
			pm.addPosition(recomm.getEntryPoint(), PositionStatus.CALL);
		}
		
		if(pm.getPosition() == PositionStatus.CALL)
		{
			if(recomm.getDirection() == Direction.UP_BROKEN && recomm.getLevel() == Level.STRONG_SELL && recomm.getPositionStatus() == PositionStatus.EXIT)
			{
				pm.closePosition(recomm.getEntryPoint());
			}
		}
		
	}
	
	public PositionStatus getPositionStatus()
	{
		return pm.getPosition();
	}
	
	public void printEndOfDayStatus()
	{
		pm.printEndOfDayStatus();
	}
}
