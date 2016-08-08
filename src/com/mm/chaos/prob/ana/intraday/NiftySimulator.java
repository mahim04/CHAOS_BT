package com.mm.chaos.prob.ana.intraday;

import java.io.IOException;
import java.io.StringReader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mm.chaos.prob.ana.intraday.analyse.ANAPatternEMA;
import com.mm.chaos.prob.ana.intraday.analyse.ANAPatternMACD;
import com.mm.chaos.prob.ana.intraday.analyse.ANAPatternMACD_STO_Combined;
import com.mm.chaos.prob.ana.intraday.analyse.ANAPatternStochastic;
import com.mm.chaos.prob.ana.intraday.data.MACDDTO;
import com.mm.chaos.prob.ana.intraday.data.NiftyIntraDayData;
import com.mm.chaos.prob.ana.intraday.data.StochasticDTO;
import com.mm.chaos.prob.ana.intraday.executions.NiftyExecutionManager;
import com.mm.chaos.prob.ana.intraday.recommendation.Direction;
import com.mm.chaos.prob.ana.intraday.recommendation.Level;
import com.mm.chaos.prob.ana.intraday.recommendation.PositionStatus;
import com.mm.chaos.prob.ana.intraday.recommendation.Recommendation;
import com.mm.chaos.prob.ana.intraday.simulator.FeedSimulator;

public class NiftySimulator 
{
	private String file = "/home/mm/CHAOS_PROB/DATA/NIFTY_SAMPLE2";
	
	private FeedSimulator fs = null;
	private NiftyExecutionManager ne = new NiftyExecutionManager();
	private DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MMM-yyyy HH:mm:ss");
	private DateTimeFormatter fmt_time = DateTimeFormat.forPattern("HHmm");
	private LoadIntraDayData ld = new LoadIntraDayData();
	private NiftyExecutionManager nem = new NiftyExecutionManager();
	
	
	public NiftySimulator() throws IOException {
		super();
		fs = new FeedSimulator(file);
	}



	public static void main(String[] args) throws Exception 
	{
		NiftySimulator ns = new NiftySimulator();
		
		ns.runSimulation();

	}
	
	private void runSimulation() throws Exception
	{
		
		
		while(fs.hasMoreData())
		{
			StringBuilder sb = fs.getNext();
			
			
			NiftyIntraDayData nift = ld.readCsvFile(new StringReader(sb.toString()), fs.getInterval(), fs.getBaseDate());
			
			System.out.print(fmt.print(nift.getLastDateTime())+"::NIFTY{"+nift.getLastClose()+"}");
			
			//We will not trade before 9:40 AM. so no point calculating values.
			Integer time = getTimeValue(nift.getLastDateTime());
			if(time < 942)
			{
				System.out.println(" Outside Trading Window");
				continue;
			}
			
			if(time > 1515)
			{
				System.out.println(" Outside Trading Window");
				nem.runCloseOfDay();
				continue;
			}			
			
			ANAPatternMACD_STO_Combined anaMACD_STO_Combined = new ANAPatternMACD_STO_Combined(nift);
			
			Recommendation recomm = null;
					
			if(nem.getPositionStatus() == PositionStatus.CALL)
			{
				recomm = anaMACD_STO_Combined.calculateLongExit();
				System.out.println("calculateLongExit ::" + recomm);
				
				if(recomm.getPositionStatus() == PositionStatus.EXIT)
				{
					nem.exitCallPosition(recomm);
				}
					
			}
			else
			{
				recomm = anaMACD_STO_Combined.calculateLongEntry();
				if(recomm.getDirection() == Direction.UP && recomm.getLevel() == Level.STRONG_BUY)
				{
					nem.enterCallPosition(recomm);
				}
			}
		}
		
//		All position played for. Lets close anything if still open.
		nem.runCloseOfDay();
		nem.printEndOfDayStatus();
		
	}

	//Will test of we are within 09:45 to 15:15 PM
	private Integer getTimeValue(DateTime d)
	{
		return Integer.parseInt(fmt_time.print(d)); 
	}
}
