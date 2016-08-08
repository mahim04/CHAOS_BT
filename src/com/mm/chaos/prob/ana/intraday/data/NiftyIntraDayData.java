package com.mm.chaos.prob.ana.intraday.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

public class NiftyIntraDayData 
{
	
	org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MMM-yyyy HH:mm:ss z");
	
	//Always assumed in seconds
	private Integer interval=0;
	private DateTime baseDate = new DateTime();
	
	private List<Integer> stepArr = new ArrayList<Integer>();
	private List<DateTime> dateArr = new ArrayList<DateTime>();
	private List<Double> openArr = new ArrayList<Double>();
	private List<Double> highArr = new ArrayList<Double>();
	private List<Double> lowArr = new ArrayList<Double>();
	private List<Double> closeArr = new ArrayList<Double>();
	private List<Double> volumeArr = new ArrayList<Double>();
	
	
	
	
	
	public NiftyIntraDayData(Integer interval, DateTime baseDate) {
		super();
		this.interval = interval;
		this.baseDate = baseDate;
	}

	public Integer getInterval() {
		return interval;
	}
	
	public DateTime getBaseDate() {
		return baseDate;
	}
	
	public void addDataSet(Integer interval_inc, Double open, Double high, Double low, Double close, Double volume)
	{
		
		stepArr.add(interval_inc);
		DateTime dt = baseDate.plusSeconds(interval * interval_inc);
		dateArr.add(dt);
		openArr.add(open);
		highArr.add(high);
		lowArr.add(low);
		closeArr.add(close);
		volumeArr.add(volume);
		
	}

	@Override
	public String toString() {
		return "NiftyIntraDayData [interval=" + interval + ", baseDate=" + fmt.print(baseDate.toDateTime(DateTimeZone.forID("Asia/Kolkata"))) + ","
				+ "openArr: " + openArr.size() + ","
				+ "highArr: " + highArr.size() + ","
				+ "lowArr " + lowArr.size() + ","
				+ "closeArr: " + closeArr.size() + ","
				+ "volumeArr: " + volumeArr.size()				
				+ "]";
	}
	
	
	public double[] getOpenPrices()
	{
		return ArrayUtils.toPrimitive(openArr.toArray(new Double[openArr.size()]));
	}
	
	public double[] getClosePrices()
	{
		return ArrayUtils.toPrimitive(closeArr.toArray(new Double[closeArr.size()]));
	}
	
	public double[] getLowPrices()
	{
		return ArrayUtils.toPrimitive(lowArr.toArray(new Double[lowArr.size()]));
	}
	
	public double[] getHighPrices()
	{
		return ArrayUtils.toPrimitive(highArr.toArray(new Double[highArr.size()]));
	}
	
	public DateTime getDateTimeAtPosition(int pos)
	{
		return dateArr.get(pos);
	}
	
	public Double getLastClose()
	{
		return closeArr.get(closeArr.size()-1);
	}
	
	public Integer getLastStep()
	{
		return stepArr.get(stepArr.size()-1);
	}
	
	//True for Last Close > previous last close
	public Boolean getLastCloseTrend()
	{
		return closeArr.get(closeArr.size()-1) > closeArr.get(closeArr.size()-2);
	}
	
	public DateTime getLastDateTime()
	{
		return dateArr.get(dateArr.size() - 1);
	}
	
	
}
