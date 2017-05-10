package project;

public class CPU 
{
	private int accum, pCounter, memBase;
	public int getAccum()
	{
		return accum;
	}
	public void setAccum(int a)
	{
		accum = a;
	}
	public int getPCounter()
	{
		return pCounter;
	}
	public void setpCounter(int p)
	{
		pCounter = p;
	}
	public int getMemBase()
	{
		return memBase;
	}
	public void setMemBase(int m)
	{
		memBase = m;
	}
	public void incrPC()
	{
		pCounter++;
	}

}
