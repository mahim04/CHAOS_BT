package com.mm.chaos.prob.csv;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.mm.chaos.prob.data.NiftyData;

public class NiftyCSVImport 
{
	
	//CSV file header
    private static final String [] FILE_HEADER_MAPPING = {"Date","Open","High","Low","Close","Shares Traded","Turnover (Rs. Cr)"};
	
	//Student attributes
	private static final String DATE = "Date";
	private static final String OPEN = "Open";
	private static final String HIGH = "High";
	private static final String LOW  = "Low"; 
	private static final String CLOSE = "Close";
	private static final String SHARES_TRADED = "Shares Traded";
	private static final String TURNOVER = "Turnover (Rs. Cr)";
	
	
	public static void main(String[] args) {
		
		List<NiftyData> arr = new NiftyCSVImport().readCsvFile("/home/mm/CHAOS_PROB/NSE_DATA/NIFTY/2011.csv");
		
		System.out.println(arr.get(0));

	}
	
	
	public List<NiftyData> readCsvFile(String fileName) 
	{
		
    	//Create a new list of student to be filled by CSV file data 
    	List<NiftyData> niftyArr = new ArrayList<NiftyData>();

		FileReader fileReader = null;
		
		CSVParser csvFileParser = null;
		
		//Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
     
        try {
            
            //initialize FileReader object
            fileReader = new FileReader(fileName);
            
            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
            
            //Get a list of CSV file records
            List csvRecords = csvFileParser.getRecords(); 
            
            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
            	CSVRecord record = (CSVRecord) csvRecords.get(i);
            	NiftyData nd = new NiftyData();
            	
            	nd.setDate(new SimpleDateFormat("dd-MMM-yyyy").parse(record.get(DATE).trim()));
            	nd.setOpen(Double.parseDouble(record.get(OPEN).trim()));
            	nd.setHigh(Double.parseDouble(record.get(HIGH).trim()));
            	nd.setLow(Double.parseDouble(record.get(LOW).trim()));
            	nd.setClose(Double.parseDouble(record.get(CLOSE).trim()));
            	nd.setSharesTraded(Integer.parseInt(record.get(SHARES_TRADED).trim()));
            	nd.setTurnOver(Double.parseDouble(record.get(TURNOVER).trim()));
            	
            	niftyArr.add(nd);
			}
            
            /*//Print the new student list
            for (Student student : students) {
				System.out.println(student.toString());
			}*/
        } 
        catch (Exception e) {
        	System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvFileParser.close();
            } catch (IOException e) {
            	System.out.println("Error while closing fileReader/csvFileParser !!!");
                e.printStackTrace();
            }
        }
        
        return niftyArr;

	}

}
