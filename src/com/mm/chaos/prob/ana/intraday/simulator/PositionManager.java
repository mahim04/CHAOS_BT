package com.mm.chaos.prob.ana.intraday.simulator;

import com.mm.chaos.prob.ana.intraday.recommendation.PositionStatus;

public class PositionManager
{
	
//	private static final Logger logger = Logger.getLogger(PositionManager.class);
	
	private Double value = 0.0;
	private PositionStatus pos = PositionStatus.NONE;
	private Double initialAmount = 10000.0;
	private Double breakeven = 3.0;
	private Double balance = initialAmount;
	private Double invested = 0.0;
	private Double entryBuffer = 1.0;
	private Double lastActivityPrice = 0.0;
	
	StringBuilder activityLedger = new StringBuilder();
	
	public void addPosition(Double entryPoint, PositionStatus pos) throws Exception
	{
		lastActivityPrice = entryPoint;
		entryPoint = entryPoint + entryBuffer;
		if(balance < entryPoint || this.pos!=PositionStatus.NONE)
			throw new Exception("Not enough Balance in account");
		
		this.pos = pos;
		if(pos == PositionStatus.CALL)
		{
			invested = entryPoint;
		}
		else
		{
			invested = entryPoint;
		}
		balance = balance - invested;		
		
		System.out.println("Entering Position : " + pos + ", NIFTY@" + entryPoint +", Invested:"+invested+", Balance:" + balance);
		activityLedger.append("Entering Position : " + pos + ", NIFTY@" + entryPoint + ", Invested:"+invested+", Balance:" + balance).append("\n");
	}
	
	
	public void closePosition(Double exitPoint)
	{
		lastActivityPrice=  exitPoint;
		Double pnl = 0.0;
		if(this.pos == PositionStatus.CALL)
		{
			pnl = (exitPoint - invested) - (entryBuffer + breakeven);
			//reducing cost from pnl
			
			balance += invested + pnl;
		}
		
		if(this.pos == PositionStatus.PUT)
		{
			
			pnl = (invested - exitPoint)- (entryBuffer + breakeven); //Note equation is reversed so still reducing - (entryBuffer + breakeven)
			balance += invested + pnl;
		}
		
		this.pos = PositionStatus.NONE;
		System.out.println("Exit Position : " + pos + ", NIFTY@" + exitPoint + ", Balance:" + balance + ", PnL: " + pnl);
		activityLedger.append("Exit Position : " + pos + ", NIFTY@" + exitPoint + ", Balance:" + balance + ", PnL: " + pnl).append("\n");
	}
	
	public Double getNetPnL()
	{
		return balance - initialAmount;
	}
	
	public PositionStatus getPosition()
	{
		return pos;
	}
	
	public void printLedger()
	{
		System.out.println("-------------ACTIVITY REPORT --------------");
		System.out.println(activityLedger);
		System.out.println("-------------------------------------------");
		
	}
	
	public void closeOfDay(Double endOfDayPrice)
	{
		lastActivityPrice = endOfDayPrice;
		Double pnl=0.0;
		if(this.pos == PositionStatus.CALL)
		{
			pnl = (endOfDayPrice - invested) - (entryBuffer + breakeven);
			balance += invested + pnl;
		}
		
		if(this.pos == PositionStatus.PUT)
		{
			pnl = (invested - endOfDayPrice) - (entryBuffer + breakeven);
			balance += invested + pnl;
		}
		
		this.pos = PositionStatus.NONE;
	}
	
	public void printEndOfDayStatus()
	{
		System.out.println("End of Day Exit Position : " + pos + ", NIFTY@" + lastActivityPrice + ", Balance:" + balance + ", NetPnL: " + (balance - initialAmount));
	}
}
