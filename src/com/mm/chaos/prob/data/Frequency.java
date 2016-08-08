package com.mm.chaos.prob.data;

public class Frequency 
{
	String name = "";
	String type = "";

	
	
	public Frequency(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	Integer cnt=0;
	
	//First combo with open < prOpen
	Integer O0H0L0 = 0, O0H0L1=0, O0H1L0=0, O0H1L1=0;

	//Second combo with open > prOpen
	Integer O1H0L0 = 0, O1H0L1=0, O1H1L0=0, O1H1L1=0;
	
	
	public void incrO0H0L0()
	{
		cnt++;
		O0H0L0++;
	}
	
	public void incrO0H0L1()
	{
		cnt++;
		O0H0L1++;
	}
	
	public void incrO0H1L0()
	{
		cnt++;
		O0H1L0++;
	}
	
	public void incrO0H1L1()
	{
		cnt++;
		O0H1L1++;
	}
	
	public void incrO1H0L0()
	{
		cnt++;
		O1H0L0++;
	}
	
	public void incrO1H0L1()
	{
		cnt++;
		O1H0L1++;
	}
	
	public void incrO1H1L0()
	{
		cnt++;
		O1H1L0++;
	}

	public void incrO1H1L1()
	{
		cnt++;
		O1H1L1++;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Integer getCnt() {
		return cnt;
	}

	public Integer getO0H0L0() {
		return O0H0L0;
	}

	public Integer getO0H0L1() {
		return O0H0L1;
	}

	public Integer getO0H1L0() {
		return O0H1L0;
	}

	public Integer getO0H1L1() {
		return O0H1L1;
	}

	public Integer getO1H0L0() {
		return O1H0L0;
	}

	public Integer getO1H0L1() {
		return O1H0L1;
	}

	public Integer getO1H1L0() {
		return O1H1L0;
	}

	public Integer getO1H1L1() {
		return O1H1L1;
	}
	

	
}
