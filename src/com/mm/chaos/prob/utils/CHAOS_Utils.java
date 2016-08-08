package com.mm.chaos.prob.utils;

import java.time.Instant;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CHAOS_Utils 
{
	private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MMM-yyyy HH:mm:ss z");

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static String printDate(Instant instant)
	{
		return fmt.print(new DateTime(instant.toEpochMilli()));
		
	}

}
