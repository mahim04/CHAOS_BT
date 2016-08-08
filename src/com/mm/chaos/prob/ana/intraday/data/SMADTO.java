package com.mm.chaos.prob.ana.intraday.data;

import java.text.DecimalFormat;
import java.util.Arrays;

import com.mm.chaos.prob.ana.intraday.recommendation.Direction;
import com.mm.chaos.prob.ana.intraday.recommendation.PositionStatus;
import com.tictactec.ta.lib.MInteger;

public class SMADTO 
{
	DecimalFormat df = new DecimalFormat("#.##");

    private MInteger outBegIdxSMAShort = new MInteger(); 
    private MInteger outNBElementSMAShort = new MInteger();
    
    private MInteger outBegIdxSMALong = new MInteger(); 
    private MInteger outNBElementSMALong = new MInteger();
    

    private double smaShort[]   = null;
	private double smaLong[] = null;
	
	
	public SMADTO(MInteger outBegIdxSMAShort, MInteger outNBElementSMAShort, MInteger outBegIdxSMALong,
			MInteger outNBElementSMALong, double[] smaShort, double[] smaLong) {
		super();
		this.outBegIdxSMAShort = outBegIdxSMAShort;
		this.outNBElementSMAShort = outNBElementSMAShort;
		this.outBegIdxSMALong = outBegIdxSMALong;
		this.outNBElementSMALong = outNBElementSMALong;
		this.smaShort = smaShort;
		this.smaLong = smaLong;
	}


	public MInteger getOutBegIdxSMAShort() {
		return outBegIdxSMAShort;
	}


	public MInteger getOutNBElementSMAShort() {
		return outNBElementSMAShort;
	}


	public MInteger getOutBegIdxSMALong() {
		return outBegIdxSMALong;
	}


	public MInteger getOutNBElementSMALong() {
		return outNBElementSMALong;
	}


	public double[] getSmaShort() {
		return smaShort;
	}


	public double[] getSmaLong() {
		return smaLong;
	}
	
	
	
	
	


	
}
