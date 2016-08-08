package com.mm.chaos.prob.ana.intraday;

import java.io.StringReader;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.mm.chaos.prob.ana.intraday.analyse.ANAPatternEMA;
import com.mm.chaos.prob.ana.intraday.data.NiftyIntraDayData;

public class ANA_MAIN 
{

	
	//NIFTY IntraDay Data
	NiftyIntraDayData nift = null;
	
	
	

	public ANA_MAIN() {
		super();
		System.out.println("calling setup");
		
		setup();
		
	}

	public static void main(String[] args) 
	{
		ANA_MAIN ana = new ANA_MAIN();
		
		ana.runEMAPattern();

	}
	
	private void setup()
	{
		getIntraDayFeed();
		
	}

	
	private void runEMAPattern()
	{
		ANAPatternEMA ema = new ANAPatternEMA(nift);
		ema.runPattern();
		
	}
	
	private void getIntraDayFeed()
	{
		//For now, let us hard code for argument sake.
		StringBuilder sb = new StringBuilder("DATE,CLOSE,HIGH,LOW,OPEN,VOLUME"
				+ "\n"
				+ "a1467171900,8172.65,8173.1,8172.65,8173.1,0"
				+ "\n"
				+ "1,8167.9,8175.8,8167.9,8172.9,0"
				+ "\n"
				+ "2,8165.45,8168.5,8164.55,8168.35,0"
				+ "\n"
				+ "3,8171.5,8172.4,8166.05,8166.05,0"
				+ "\n"
				+ "4,8170.8,8172.05,8170.7,8171.4,0"
				+ "\n"
				+ "5,8166.2,8171.25,8166.2,8170.9,0"
				+ "\n"
				+ "6,8167.9,8168.45,8166.45,8166.5,0"
				+ "\n"
				+ "7,8168.4,8169,8167.25,8167.8,0"
				+ "\n"
				+ "8,8165.5,8168.85,8164.9,8168.55,0"
				+ "\n"
				+ "9,8165,8165.55,8163.65,8165.55,0"
				+ "\n"
				+ "10,8167.1,8167.35,8165.05,8165.35,0"
				+ "\n"
				+ "11,8166.1,8167.5,8165.4,8167.15,0"
				+ "\n"
				+ "12,8168.65,8168.9,8166.4,8166.45,0"
				+ "\n"
				+ "13,8167.05,8169.5,8166.8,8168.65,0"
				+ "\n"
				+ "14,8163.95,8167.2,8163.95,8167.2,0"
				+ "\n"
				+ "15,8163.5,8163.75,8162.4,8163.45,0"
				+ "\n"
				+ "16,8165.35,8166.1,8163.5,8163.5,0"
				+ "\n"
				+ "17,8165.5,8165.9,8164.4,8165.4,0"
				+ "\n"
				+ "18,8165.15,8165.9,8164.75,8165.5,0"
				+ "\n"
				+ "19,8164.75,8165.25,8162.85,8165.2,0"
				+ "\n"
				+ "20,8167.45,8167.8,8164.55,8165.1,0"
				+ "\n"
				+ "21,8167.35,8168.25,8167.25,8167.55,0"
				+ "\n"
				+ "22,8168.3,8168.35,8167.1,8167.1,0"
				+ "\n"
				+ "23,8172.9,8173.2,8168.15,8168.15,0"
				+ "\n"
				+ "24,8173.3,8174.7,8172.65,8173.1,0"
				+ "\n"
				+ "25,8172.1,8173.7,8171.4,8173.55,0"
				+ "\n"
				+ "26,8175.35,8175.9,8172.3,8172.3,0"
				+ "\n"
				+ "27,8174.9,8175.85,8174.65,8175.3,0"
				+ "\n"
				+ "28,8175.75,8175.85,8174.55,8175.15,0"
				+ "\n"
				+ "29,8175.1,8176.15,8174.6,8175.85,0"
				+ "\n"
				+ "30,8172.95,8175.65,8172.95,8175.2,0"
				+ "\n"
				+ "31,8173.65,8174.2,8172.4,8172.8,0"
				+ "\n"
				+ "32,8173.9,8174.9,8173.65,8173.85,0"
				+ "\n"
				+ "33,8174.5,8174.8,8173.75,8174.2,0"
				+ "\n"
				+ "34,8173.6,8174.75,8172.9,8174.55,0"
				+ "\n"
				+ "35,8172,8173.7,8171.6,8173.7,0"
				+ "\n"
				+ "36,8171.9,8172.55,8171.65,8172.15,0"
				+ "\n"
				+ "37,8172.25,8172.85,8171.9,8172.15,0"
				+ "\n"
				+ "38,8171.35,8172.4,8170.75,8172.15,0"
				+ "\n"
				+ "39,8174.5,8174.55,8171.4,8171.5,0"
				+ "\n"
				+ "40,8175.7,8175.7,8174.25,8174.25,0"
				+ "\n"
				+ "41,8175.85,8176.55,8175.45,8175.8,0"
				+ "\n"
				+ "42,8175.1,8176.95,8175.1,8175.5,0"
				+ "\n"
				+ "43,8175.95,8176,8174.35,8174.75,0"
				+ "\n"
				+ "44,8177.45,8177.45,8175.7,8175.8,0"
				+ "\n"
				+ "45,8176.45,8177.5,8175.85,8177.5,0"
				+ "\n"
				+ "46,8177.15,8177.15,8175.6,8176.55,0"
				+ "\n"
				+ "47,8178.15,8178.9,8177.15,8177.25,0"
				+ "\n"
				+ "48,8177.7,8178.85,8177.1,8178.35,0"
				+ "\n"
				+ "49,8180.5,8180.85,8179.05,8179.45,0"
				+ "\n"
				+ "50,8181.2,8181.2,8179.7,8180.4,0"
				+ "\n"
				+ "51,8182,8183.15,8181.05,8181.1,0"
				+ "\n"
				+ "52,8181.15,8182.9,8180.75,8181.95,0"
				+ "\n"
				+ "53,8180.3,8181.55,8180.1,8181,0"
				+ "\n"
				+ "54,8177.5,8180.35,8177.5,8180.35,0"
				+ "\n"
				+ "55,8177.65,8178.5,8176.9,8177.5,0"
				+ "\n"
				+ "56,8177.2,8178,8176.7,8177.75,0"
				+ "\n"
				+ "57,8178.25,8178.6,8176.95,8177.1,0"
				+ "\n"
				+ "58,8179.05,8179.85,8177.95,8178.15,0"
				+ "\n"
				+ "59,8179.4,8180.35,8178.75,8179.3,0"
				+ "\n"
				+ "60,8179.25,8180.1,8179.25,8179.25,0"
				+ "\n");

		LoadIntraDayData ld = new LoadIntraDayData();
		DateTime bd = ld.parseUnixDate("a1467171900");
		System.out.println("This is base date : " + bd.toDateTime(DateTimeZone.forID("Asia/Kolkata")));
		
		nift = ld.readCsvFile(new StringReader(sb.toString()), 60, bd);
		
		System.out.println(nift);
	}
	
	
	

}
