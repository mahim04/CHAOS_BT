package com.mm.chaos.prob;

import java.text.DecimalFormat;
import java.util.List;

import com.mm.chaos.prob.csv.NiftyCSVImport;
import com.mm.chaos.prob.data.Frequency;
import com.mm.chaos.prob.data.NiftyData;

public class NiftyAnalysis 
{
	
	DecimalFormat df = new DecimalFormat("#.##");
	
	
	List<NiftyData> niftyArr = null;
	String analysisName = "";


	public NiftyAnalysis(String analysisName) {
		super();
		this.analysisName = analysisName;
	}

	public static void main(String[] args) 
	{
		int start = 2011;
		for(int i=0; i<6; i++)
		{
			String file = ""+(start+i);
			String fileName = "/home/mm/CHAOS_PROB/NSE_DATA/NIFTY/"+file+".csv";
			NiftyAnalysis na = new NiftyAnalysis("NIFTY_"+file);
			
			na.loadData(fileName);
			na.analyse();
		}
	}
	
	private void loadData(String fileName)
	{
		niftyArr = new NiftyCSVImport().readCsvFile(fileName);
	}
	
	private void analyse()
	{

		Frequency upFr = new Frequency("NIFTY", "UP");
		Frequency downFr = new Frequency("NIFTY", "DOWN");
		
		
		
		int skip=0;
		NiftyData last = null;
		
		for(NiftyData nd : niftyArr)
		{
			//skip first line
			if(skip++==0)
			{
				last = nd;
				continue;
			}
			
			//Up cases
			if(nd.getClose() > last.getClose())
			{
				//O0 cases
				if(nd.getOpen()< last.getOpen())
				{
					//H1 cases
					if(nd.getHigh() > last.getHigh())
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							upFr.incrO0H1L1();
							
						}
						else //L0 cases
						{
							upFr.incrO0H1L0();
						}
					}
					else //H0 cases
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							upFr.incrO0H0L1();
							
						}
						else //O0H1L0
						{
							upFr.incrO0H0L0();
						}
					}
				}
				else //O1 cases
				{
					//H1 cases
					if(nd.getHigh() > last.getHigh())
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							upFr.incrO1H1L1();
						}
						else //L0 cases
						{
							upFr.incrO1H1L0();
						}
					}
					else //H0 cases
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							upFr.incrO1H0L1();
							
						}
						else //O0H1L0
						{
							upFr.incrO1H0L0();
						}
					}
					
				}
			}
			else //Down cases : Down
			{
				//O0 cases
				if(nd.getOpen()< last.getOpen())
				{
					//H1 cases
					if(nd.getHigh() > last.getHigh())
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							downFr.incrO0H1L1();
							
						}
						else //L0 cases
						{
							downFr.incrO0H1L0();
						}
					}
					else //H0 cases
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							downFr.incrO0H0L1();
							
						}
						else //O0H1L0
						{
							downFr.incrO0H0L0();
						}
					}
				}
				else //O1 cases
				{
					//H1 cases
					if(nd.getHigh() > last.getHigh())
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							downFr.incrO1H1L1();
							
						}
						else //L0 cases
						{
							downFr.incrO1H1L0();
						}
					}
					else //H0 cases
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							downFr.incrO1H0L1();
							
						}
						else //O0H1L0
						{
							downFr.incrO1H0L0();
						}
					}
					
				}
				
			}
			//assigning last
			last = nd;
		}//End of for loop
		
		printResults(upFr, downFr, niftyArr.size());
	}

	
	private void printResults(Frequency upFr, Frequency downFr, Integer total)
	{
		System.out.println("------------------------------"+analysisName+"------------------------------");
		
		System.out.println("TOTAL COUNT : " + total);
		
		System.out.println("---------------UP---------------");
		
		System.out.println("CLOSE\t: TYPE\t\t: COUNT\t: PERCENTAGE");
		System.out.println("-----\t: ----\t\t: -----\t: ---------");
		System.out.println("UP\t: O0H0L0\t: " + upFr.getO0H0L0() + "\t: " + (calcPC(upFr.getO0H0L0(),total)));
		System.out.println("UP\t: O0H0L1\t: " + upFr.getO0H0L1() + "\t: " + (calcPC(upFr.getO0H0L1(),total)));
		System.out.println("UP\t: O0H1L0\t: " + upFr.getO0H1L0() + "\t: " + (calcPC(upFr.getO0H1L0(),total)));
		System.out.println("UP\t: O0H1L1\t: " + upFr.getO0H1L1() + "\t: " + (calcPC(upFr.getO0H1L1(),total)));
		
		System.out.println("UP\t: O1H0L0\t: " + upFr.getO1H0L0() + "\t: " + (calcPC(upFr.getO1H0L0(),total)));
		System.out.println("UP\t: O1H0L1\t: " + upFr.getO1H0L1() + "\t: " + (calcPC(upFr.getO1H0L1(),total)));
		System.out.println("UP\t: O1H1L0\t: " + upFr.getO1H1L0() + "\t: " + (calcPC(upFr.getO1H1L0(),total)));
		System.out.println("UP\t: O1H1L1\t: " + upFr.getO1H1L1() + "\t: " + (calcPC(upFr.getO1H1L1(),total)));
		
		System.out.println("---------------DOWN---------------");
		System.out.println("DOWN\t: O0H0L0\t: " + downFr.getO0H0L0() + "\t: " + (calcPC(downFr.getO0H0L0(),total)));
		System.out.println("DOWN\t: O0H0L1\t: " + downFr.getO0H0L1() + "\t: " + (calcPC(downFr.getO0H0L1(),total)));
		System.out.println("DOWN\t: O0H1L0\t: " + downFr.getO0H1L0() + "\t: " + (calcPC(downFr.getO0H1L0(),total)));
		System.out.println("DOWN\t: O0H1L1\t: " + downFr.getO0H1L1() + "\t: " + (calcPC(downFr.getO0H1L1(),total)));
		
		System.out.println("DOWN\t: O1H0L0\t: " + downFr.getO1H0L0() + "\t: " + (calcPC(downFr.getO1H0L0(),total)));
		System.out.println("DOWN\t: O1H0L1\t: " + downFr.getO1H0L1() + "\t: " + (calcPC(downFr.getO1H0L1(),total)));
		System.out.println("DOWN\t: O1H1L0\t: " + downFr.getO1H1L0() + "\t: " + (calcPC(downFr.getO1H1L0(),total)));
		System.out.println("DOWN\t: O1H1L1\t: " + downFr.getO1H1L1() + "\t: " + (calcPC(downFr.getO1H1L1(),total)));
		
		System.out.println("-----------------------------------------------------------------");
		
	}
	
	private String calcPC(Integer i, Integer total)
	{
		
		Double p = ((i*1.0)/total)*100.0;
		
		return df.format(p);
		
	}
	
	
	private void analyseWeekDay()
	{

		Frequency upFr = new Frequency("NIFTY", "UP");
		Frequency downFr = new Frequency("NIFTY", "DOWN");
		
		
		
		int skip=0;
		NiftyData last = null;
		
		for(NiftyData nd : niftyArr)
		{
			//skip first line
			if(skip++==0)
			{
				last = nd;
				continue;
			}
			
			//Up cases
			if(nd.getClose() > last.getClose())
			{
				//O0 cases
				if(nd.getOpen()< last.getOpen())
				{
					//H1 cases
					if(nd.getHigh() > last.getHigh())
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							upFr.incrO0H1L1();
							
						}
						else //L0 cases
						{
							upFr.incrO0H1L0();
						}
					}
					else //H0 cases
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							upFr.incrO0H0L1();
							
						}
						else //O0H1L0
						{
							upFr.incrO0H0L0();
						}
					}
				}
				else //O1 cases
				{
					//H1 cases
					if(nd.getHigh() > last.getHigh())
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							upFr.incrO1H1L1();
						}
						else //L0 cases
						{
							upFr.incrO1H1L0();
						}
					}
					else //H0 cases
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							upFr.incrO1H0L1();
							
						}
						else //O0H1L0
						{
							upFr.incrO1H0L0();
						}
					}
					
				}
			}
			else //Down cases : Down
			{
				//O0 cases
				if(nd.getOpen()< last.getOpen())
				{
					//H1 cases
					if(nd.getHigh() > last.getHigh())
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							downFr.incrO0H1L1();
							
						}
						else //L0 cases
						{
							downFr.incrO0H1L0();
						}
					}
					else //H0 cases
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							downFr.incrO0H0L1();
							
						}
						else //O0H1L0
						{
							downFr.incrO0H0L0();
						}
					}
				}
				else //O1 cases
				{
					//H1 cases
					if(nd.getHigh() > last.getHigh())
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							downFr.incrO1H1L1();
							
						}
						else //L0 cases
						{
							downFr.incrO1H1L0();
						}
					}
					else //H0 cases
					{
						//L1 cases
						if(nd.getLow() > last.getLow())
						{
							//O0H1L1
							downFr.incrO1H0L1();
							
						}
						else //O0H1L0
						{
							downFr.incrO1H0L0();
						}
					}
					
				}
				
			}
			//assigning last
			last = nd;
		}//End of for loop
		
		printResults(upFr, downFr, niftyArr.size());
	}

}
