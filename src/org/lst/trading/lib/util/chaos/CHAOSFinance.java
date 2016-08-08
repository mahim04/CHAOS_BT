package org.lst.trading.lib.util.chaos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.lst.trading.lib.model.Bar;
import org.lst.trading.lib.series.BarSeries;
import org.lst.trading.lib.series.DoubleSeries;
import org.lst.trading.lib.util.HistoricalPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mm.chaos.prob.ana.intraday.data.NiftyIntraDayData;
import com.mm.chaos.prob.utils.CHAOS_Utils;

import rx.Observable;

public class CHAOSFinance implements HistoricalPriceService 
{
	private final String baseDir = "/home/mm/CHAOS_PROB/NSE_DATA/";
	private static final Logger log = LoggerFactory.getLogger(CHAOSFinance.class);
	private final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MMM-yyyy HH:mm:ss Z");
	
	Integer interval=120; //120 seconds	
	//CSV file header
    private static final String [] FILE_HEADER_MAPPING = {"DATE","CLOSE","HIGH","LOW","OPEN","VOLUME"};

    //Feed attributes
  	private static final String DATE = "DATE";
  	private static final String OPEN = "OPEN";
  	private static final String HIGH = "HIGH";
  	private static final String LOW  = "LOW"; 
  	private static final String CLOSE = "CLOSE";
  	private static final String VOLUME = "VOLUME";

    @Override public Observable<DoubleSeries> getHistoricalAdjustedPrices(String symbol) {
        return null;
    }
    
    public static void main(String a[])
    {
    	Observable<BarSeries> obs = new CHAOSFinance().getHistoricalAdjustedPricesinBar("NIFTY");
    	System.out.println(obs.toBlocking().first().getLast());
    }
    
	public BarSeries readCsvFile(Reader in, String symbol)
	{
		BarSeries bs = new BarSeries(symbol);
		
		CSVParser csvFileParser = null;
		
		//Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
        
        //initialize CSVParser object
        try 
        {
			csvFileParser = new CSVParser(in, csvFileFormat);
		
        
	        //Get a list of CSV file records
	        List csvRecords = csvFileParser.getRecords();
	        
	        
	        DateTime dt = null;
	        DateTime baseDate = null;
	      //Read the CSV file records starting from the second record to skip the header
            for (int i = 0; i < csvRecords.size(); i++) 
            {
            	CSVRecord record = (CSVRecord) csvRecords.get(i);
            	String d = record.get(DATE);
            	
            	//This means a new day has started.
            	if(d.startsWith("a"))
            	{
            		baseDate= parseUnixDate(d);
            		d="0";
            	}
            		
            	dt = baseDate.plusSeconds(interval * Integer.parseInt(d));
            	
//            	log.debug("Parsing for date:" + fmt.print(dt) + "::CLOSE=" + record.get(CLOSE).trim());
            	final Instant ins = dt.toDate().toInstant();
            	
            	
            	
//            	nift.addDataSet(Integer.parseInt(dt.trim()), 
//    			Double.parseDouble(record.get(OPEN).trim()), 
//    			Double.parseDouble(record.get(HIGH).trim()), 
//    			Double.parseDouble(record.get(LOW).trim()), 
//    			Double.parseDouble(record.get(CLOSE).trim()), 
//    			Double.parseDouble(record.get(VOLUME).trim()));
            	
            	Bar b = new Bar() {
                    private double mOpen = Double.parseDouble(record.get(OPEN).trim());
                    private double mHigh = Double.parseDouble(record.get(HIGH).trim());
                    private double mLow = Double.parseDouble(record.get(LOW).trim());
                    private double mClose = Double.parseDouble(record.get(CLOSE).trim());
                    private long mVolume = Long.parseLong(record.get(VOLUME).trim());
                    private Instant instant = ins;

                    @Override public double getOpen() {
                        return mOpen;
                    }

                    @Override public double getHigh() {
                        return mHigh;
                    }

                    @Override public double getLow() {
                        return mLow;
                    }

                    @Override public double getClose() {
                        return mClose;
                    }

                    @Override public long getVolume() {
                        return mVolume;
                    }

                    @Override public Instant getStart() {
                        return instant;
                    }

                    @Override public Duration getDuration() {
                        return null;
                    }

                    @Override public double getWAP() {
                        return 0;
                    }
                    
                    @Override public String toString() 
                    {
                        return "BAR["+ CHAOS_Utils.printDate(getStart()) +"]{OPEN: " + getOpen() +
                        		", HIGH: " + getHigh() + 
                        		", LOW: " + getLow() + 
                        		", CLOSE: " + getClose() + "}" 
                        		;
                    }
                };
            	
                bs.add(b, ins);            	
			}
        
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return bs;		
	}
	
	
	public DateTime parseUnixDate(String d)
	{
		Long _startTS = ((long) Long.parseLong(d.substring(d.indexOf("a")+1, d.length())) * 1000L);
		DateTime _startDate = new DateTime( _startTS );
		return _startDate;
	}

	@Override
	public Observable<BarSeries> getHistoricalAdjustedPricesinBar(String symbol) 
	{
		StringBuilder sb = loadFile(symbol);
		BarSeries bs = readCsvFile(new StringReader(sb.toString()), symbol);
		return Observable.just(bs);
	}

	
	private StringBuilder loadFile(String symbol)
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(baseDir+symbol+".csv"));
			//reading basic contents.
	//		EXCHANGE%3DNSE
			br.readLine();
			
	//		MARKET_OPEN_MINUTE=555
			br.readLine();
	//		MARKET_CLOSE_MINUTE=930
			br.readLine();
	//		INTERVAL=60
			String s = br.readLine();
			interval = Integer.parseInt(s.substring(s.indexOf("=")+1, s.length()).trim());
	//		COLUMNS=DATE,CLOSE,HIGH,LOW,OPEN,VOLUME
			br.readLine();
	//		DATA=
			br.readLine();
			
	//		TIMEZONE_OFFSET=330
			br.readLine();
			
			//Add header
//			sb.append("DATE,CLOSE,HIGH,LOW,OPEN,VOLUME\n");
			
			while((s=br.readLine())!=null)
			{
				sb.append(s).append("\n");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			if(br!=null)
			{
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return sb;
	}
}
