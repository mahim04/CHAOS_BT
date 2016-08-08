package com.mm.chaos.prob.ana.intraday.analyse;

import java.text.DecimalFormat;

import com.mm.chaos.prob.ana.intraday.data.MACDDTO;
import com.mm.chaos.prob.ana.intraday.data.NiftyIntraDayData;
import com.mm.chaos.prob.ana.intraday.data.StochasticDTO;
import com.mm.chaos.prob.ana.intraday.recommendation.Direction;
import com.mm.chaos.prob.ana.intraday.recommendation.Level;
import com.mm.chaos.prob.ana.intraday.recommendation.PositionStatus;
import com.mm.chaos.prob.ana.intraday.recommendation.Recommendation;
import com.tictactec.ta.lib.Core;

public class ANAPatternMACD_STO_Combined 
{
	
	private DecimalFormat df = new DecimalFormat("#.##");
	//TA LIB Handle
	private Core lib = new Core();
	
	//NIFTY IntraDay Data
	private NiftyIntraDayData nift = null;
	
	private MACDDTO macdDTO = null;
	private StochasticDTO stoDTO = null;
	private Recommendation recomm = new Recommendation();
	
	public ANAPatternMACD_STO_Combined(NiftyIntraDayData nift) {
		super();
		this.nift = nift;
		
		recomm.setEntryPoint(nift.getLastClose());
		
		ANAPatternMACD macd = new ANAPatternMACD(nift);
		macdDTO = macd.calculate();
		
		ANAPatternStochastic sto = new ANAPatternStochastic(nift);
		stoDTO = sto.calculate();
		
		System.out.println(macdDTO.printLastValues(3) + stoDTO.printLastValues(3));
	}
	
	public Recommendation calculateLongEntry()
	{
		//for long entry 
		if(macdDTO.getLongSignal() == PositionStatus.ENTER && stoDTO.isStocasticLessThan(80.0))
		{
			recomm.setDirection(Direction.UP);
			recomm.setLevel(Level.STRONG_BUY);
			recomm.setPositionStatus(PositionStatus.ENTER);
		}
		return recomm;
	}
	
	public Recommendation calculateLongExit()
	{
		//For exit from Long
		if(macdDTO.getLongExitSignal() == PositionStatus.EXIT 
				|| stoDTO.isStocasticLessThan(45.0) //Assuming stoch is going into over sold category
				)
		{
			recomm.setDirection(Direction.UP_BROKEN);
			recomm.setLevel(Level.STRONG_SELL);
			recomm.setPositionStatus(PositionStatus.EXIT);
		}
		return recomm;
	}
	

}
