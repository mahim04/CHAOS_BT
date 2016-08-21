package com.mm.chaos.prob.ana.intraday.data;

import java.time.Instant;

import com.mm.chaos.prob.utils.CHAOS_Utils;

public class RSIDTO 
{
	Instant instant;
	
	Double rsi;
	
	

	public RSIDTO(Instant instant, Double rsi) {
		super();
		this.instant = instant;
		this.rsi = rsi;
	}

	
	public Instant getInstant() {
		return instant;
	}

	public Double getRsi() {
		return rsi;
	}


	@Override
	public String toString() 
	{
		return "RSIDTO [instant=" + CHAOS_Utils.printDate(instant) + ", rsi=" + rsi + "]";
	}
	
	

}
