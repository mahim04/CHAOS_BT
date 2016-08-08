package com.mm.chaos.prob.data;

import java.util.Date;

public class NiftyData 
{
	private Date date = new Date();
	private Double open = 0.0;
	private Double high = 0.0;
	private Double low = 0.0;
	private Double close = 0.0;
	private Integer sharesTraded = 0;
	private Double turnOver = 0.0;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Double getOpen() {
		return open;
	}
	public void setOpen(Double open) {
		this.open = open;
	}
	public Double getHigh() {
		return high;
	}
	public void setHigh(Double high) {
		this.high = high;
	}
	public Double getLow() {
		return low;
	}
	public void setLow(Double low) {
		this.low = low;
	}
	public Double getClose() {
		return close;
	}
	public void setClose(Double close) {
		this.close = close;
	}
	public Integer getSharesTraded() {
		return sharesTraded;
	}
	public void setSharesTraded(Integer sharesTraded) {
		this.sharesTraded = sharesTraded;
	}
	public Double getTurnOver() {
		return turnOver;
	}
	public void setTurnOver(Double turnOver) {
		this.turnOver = turnOver;
	}
	
	
	@Override
	public String toString() {
		return "NiftyData [date=" + date + ", open=" + open + ", high=" + high + ", low=" + low + ", close=" + close
				+ ", sharesTraded=" + sharesTraded + ", turnOver=" + turnOver + "]";
	}
	
	

}
