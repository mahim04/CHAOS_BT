package com.mm.chaos.prob.ana.intraday.data;

import java.text.DecimalFormat;
import java.util.Arrays;

import com.tictactec.ta.lib.MInteger;

public class StochasticDTO 
{
	DecimalFormat df = new DecimalFormat("#.##");	
	private MInteger outBegIdx = new MInteger(); 
    private MInteger outNBElement = new MInteger();
    private double[] outSlowK = null;
    private double[] outSlowD = null;
	
    public StochasticDTO(MInteger outBegIdx, MInteger outNBElement, double[] outSlowK, double[] outSlowD) {
		super();
		this.outBegIdx = outBegIdx;
		this.outNBElement = outNBElement;
		this.outSlowK = outSlowK;
		this.outSlowD = outSlowD;
	}
	
	public MInteger getOutBegIdx() {
		return outBegIdx;
	}
	public MInteger getOutNBElement() {
		return outNBElement;
	}
	public double[] getOutSlowK() {
		return outSlowK;
	}
	public double[] getOutSlowD() {
		return outSlowD;
	}

	
	public Boolean isStocasticBetween(Double high, Double low)
	{
		return (high > outSlowK[outNBElement.value - 1] && outSlowK[outNBElement.value - 1] > low);
	}
	
	public Boolean isStocasticGreaterThan(Double val)
	{
		return (outSlowK[outNBElement.value - 1] > val);
	}
	
	public Boolean isStocasticLessThan(Double val)
	{
		return (outSlowK[outNBElement.value - 1] < val);
	}
	
	@Override
	public String toString() 
	{
        StringBuilder sb = new StringBuilder();
        sb.append("outSlowK:" + outSlowK.length + ", outSlowD:" + outSlowD.length).append("\n");
        for(int i=outBegIdx.value; i<outNBElement.value; i++)
        {
        	sb.append("["+i+"] outSlowK:" + outSlowK[i] + ", outSlowD:" + outSlowD[i]).append("\n");
        }
        
		sb.append("StochasticDTO [outBegIdx=" + outBegIdx + ", outNBElement=" + outNBElement + ", outSlowK="
				+ Arrays.toString(outSlowK) + ", outSlowD=" + Arrays.toString(outSlowD) + "]");
		return sb.toString();
	}
    

	public String printLastValues(Integer i)
	{
		if(outNBElement.value <= i)
		{
			return "[STOCH:: NONE]";
		}
		
		StringBuilder sb = new StringBuilder("STOCH:: ");
		for(int k=i+1; k>0; k--)
		{
			sb.append("["+(outNBElement.value - k)+"]outSlowK:" + df.format(outSlowK[(outNBElement.value - k)])+" outSlowD:" + df.format(outSlowD[(outNBElement.value - k)]));
			sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
		
	}
	
	
}
