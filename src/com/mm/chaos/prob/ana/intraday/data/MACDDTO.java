package com.mm.chaos.prob.ana.intraday.data;

import java.text.DecimalFormat;
import java.util.Arrays;

import com.mm.chaos.prob.ana.intraday.recommendation.Direction;
import com.mm.chaos.prob.ana.intraday.recommendation.PositionStatus;
import com.tictactec.ta.lib.MInteger;

public class MACDDTO 
{
	DecimalFormat df = new DecimalFormat("#.##");
	private MInteger outBegIdx = new MInteger(); 
    private MInteger outNBElement = new MInteger();

    private double macd[]   = null;
	private double signal[] = null;
	private double hist[]   = null;
	
	
	public MACDDTO(MInteger outBegIdx, MInteger outNBElement, double[] macd, double[] signal, double[] hist) {
		super();
		this.outBegIdx = outBegIdx;
		this.outNBElement = outNBElement;
		this.macd = macd;
		this.signal = signal;
		this.hist = hist;
	}
	
	
	
	public PositionStatus getLongSignal()
	{
		//Checking direction is up
		int i=3;
		if(		outNBElement.value > i && 
				hist[outNBElement.value - i--] < hist[outNBElement.value - i] &&
				hist[outNBElement.value - i--] < hist[outNBElement.value - i] && 
				hist[outNBElement.value - i--] < hist[outNBElement.value - i] &&
				hist[outNBElement.value - 1] > 0.1
		  )
		{
			System.out.println("****************MACD SIGNAL ENTRY****************");
			return PositionStatus.ENTER;
		}
		else return PositionStatus.NONE;
	}
	
	public PositionStatus getLongExitSignal()
	{
		//We will exit call if MACD goes down consecutive 5 times or less than 0.1
		int i=3;
		if(		outNBElement.value > i && (
//				!( //Entry condition is not valid anymore.
//					hist[outNBElement.value - i--] < hist[outNBElement.value - i] &&
//					hist[outNBElement.value - i--] < hist[outNBElement.value - i] &&
//					hist[outNBElement.value - i--] < hist[outNBElement.value - i] &&
//					hist[outNBElement.value - i--] < hist[outNBElement.value - i]
//				) 
//				|| 
				hist[outNBElement.value - 1] <= 0.1 //Hard exit condition 
				)
		  )
		{
			System.out.println("RECOMM:: MACD exit....");
			return PositionStatus.EXIT;
		}
		else return PositionStatus.HOLD;
	}
	
	
	
	
	public MInteger getOutBegIdx() {
		return outBegIdx;
	}
	public MInteger getOutNbElement() {
		return outNBElement;
	}
	public double[] getMacd() {
		return macd;
	}
	public double[] getSignal() {
		return signal;
	}
	public double[] getHist() {
		return hist;
	}


	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("macd:" + macd.length + ", signal:" + signal.length + ", hist:" + hist.length).append("\n");
        for(int i=outBegIdx.value; i<outNBElement.value; i++)
        {
        	sb.append("macd:" + macd[i] + ", signal:" + signal[i] + ", hist:" + hist[i]).append("\n");
        }
        
        
        sb.append("MACDDTO [outBegIdx=" + outBegIdx + ", outNbElement=" + outNBElement + ", macd=" + Arrays.toString(macd)
				+ ", signal=" + Arrays.toString(signal) + ", hist=" + Arrays.toString(hist) + "]").append("\n");
        return sb.toString();
	}
	
	
	public String printLastValues(Integer i)
	{
		if(outNBElement.value <= i)
		{
			return "[MACD::Hist:NONE]";
		}
		
		StringBuilder sb = new StringBuilder("[MACD::Hist: ");
		for(int k=i; k>0; k--)
		{
			sb.append("["+(outNBElement.value - k)+"]" + df.format(hist[(outNBElement.value - k)]));
			sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}
	
	
}
