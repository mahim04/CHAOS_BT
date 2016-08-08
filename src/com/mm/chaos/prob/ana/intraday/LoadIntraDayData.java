package com.mm.chaos.prob.ana.intraday;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.mm.chaos.prob.ana.intraday.data.NiftyIntraDayData;

public class LoadIntraDayData 
{
	
	
	//CSV file header
    private static final String [] FILE_HEADER_MAPPING = {"DATE","CLOSE","HIGH","LOW","OPEN","VOLUME"};
	private final Map<DateTime, NiftyIntraDayData> timeSeries = new TreeMap<DateTime, NiftyIntraDayData>();
    
	//this is just for adding into timeseries map once day roll over has happened
	private NiftyIntraDayData previousNift = null;
	
	//Student attributes
	private static final String DATE = "DATE";
	private static final String OPEN = "OPEN";
	private static final String HIGH = "HIGH";
	private static final String LOW  = "LOW"; 
	private static final String CLOSE = "CLOSE";
	private static final String VOLUME = "VOLUME";
	

	public static void main(String[] args) 
	{
		StringBuilder sb = new StringBuilder("DATE,CLOSE,HIGH,LOW,OPEN,VOLUME"
				+ "\n"
				+ "a1467171900,8172.65,8173.1,8172.65,8173.1,0"
				+ "\n"
				+ "1,8167.9,8175.8,8167.9,8172.9,0"
				+ "\n"
				+ "2,8165.45,8168.5,8164.55,8168.35,0"
				+ "\n"
				+ "3,8171.5,8172.4,8166.05,8166.05,0"
				+ "\n");

		LoadIntraDayData ld = new LoadIntraDayData();
		DateTime bd = ld.parseUnixDate("a1467171900");
		System.out.println("This is base date : " + bd.toDateTime(DateTimeZone.forID("Asia/Kolkata")));
		
		NiftyIntraDayData nift = ld.readCsvFile(new StringReader(sb.toString()), 60, bd);
		
		System.out.println(nift);
		
	}
	
	public NiftyIntraDayData readCsvFile(Reader in, Integer interval, DateTime baseDate)
	{
		NiftyIntraDayData nift = new NiftyIntraDayData(interval, baseDate);
		
		CSVParser csvFileParser = null;
		
		//Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
        
        //initialize CSVParser object
        try {
			csvFileParser = new CSVParser(in, csvFileFormat);
		
        
	        //Get a list of CSV file records
	        List csvRecords = csvFileParser.getRecords();
	        
	        
	      //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) 
            {
            	CSVRecord record = (CSVRecord) csvRecords.get(i);
            	String dt = record.get(DATE);
            	
            	//This means a new day has started.
            	if(dt.startsWith("a"))
            	{
            		dt="0";
            		timeSeries.put(previousNift.getBaseDate(), previousNift);
            	}
            	
            	nift.addDataSet(Integer.parseInt(dt.trim()), 
            			Double.parseDouble(record.get(OPEN).trim()), 
            			Double.parseDouble(record.get(HIGH).trim()), 
            			Double.parseDouble(record.get(LOW).trim()), 
            			Double.parseDouble(record.get(CLOSE).trim()), 
            			Double.parseDouble(record.get(VOLUME).trim()));
            	
			}
        
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        previousNift = nift;
        return nift;		
	}
	
	
	public DateTime parseUnixDate(String d)
	{
		Long _startTS = ((long) Long.parseLong(d.substring(d.indexOf("a")+1, d.length())) * 1000L);
		DateTime _startDate = new DateTime( _startTS );
		return _startDate;
	}

}
