package com.mm.chaos.prob.ana.intraday.simulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class FeedSimulator 
{

	DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MMM-yyyy HH:mm:ss");
	private String file = "";
	private BufferedReader br = null;
	//This will keep the file contents in memory.
	private StringBuilder sb = new StringBuilder();
	private Integer interval = 0;
	private DateTime baseDate = new DateTime();

	public FeedSimulator(String file) throws IOException {
		super();
		this.file = file;
		loadFile();
	}


	
	public StringBuilder getNext() throws IOException
	{
		String s = br.readLine();
		if(s!=null && s.length()>0)
		{
			//This means a new day has started. So let us keep old date in record and open a new data set.
			if(s.startsWith("a"))
			{
				//This means a new day has started.
				baseDate = parseUnixDate(s.substring(0, s.indexOf(",")));
				System.out.println("Parsed new Base date: " + fmt.print(baseDate));
				sb = new StringBuilder();
				sb.append("DATE,CLOSE,HIGH,LOW,OPEN,VOLUME\n");
			}
			
			sb.append(s).append("\n");
		}
		return sb;
	}



	private void loadFile() throws IOException
	{
		br = new BufferedReader(new FileReader(file));
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
		
		//First line with UNIX Timestamp
		s = br.readLine();
		baseDate = parseUnixDate(s.substring(0, s.indexOf(",")));
		

		sb.append("DATE,CLOSE,HIGH,LOW,OPEN,VOLUME\n");

		//Always we are assuming to read more than 20 lines.
		/*		for(int i=0; i<30; i++)
		{
			sb.append(br.readLine()).append("\n");
		}
		 */
	}
	
	
	public DateTime parseUnixDate(String d)
	{
		Long _startTS = ((long) Long.parseLong(d.substring(d.indexOf("a")+1, d.length())) * 1000L);
		DateTime _startDate = new DateTime( _startTS );
		return _startDate;
	}

	public Integer getInterval() {
		return interval;
	}

	public DateTime getBaseDate() {
		return baseDate;
	}
	
	
	public Boolean hasMoreData() throws IOException
	{
		return (br.ready());
	}

}
