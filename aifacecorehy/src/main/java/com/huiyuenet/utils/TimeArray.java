package com.huiyuenet.utils;

public class TimeArray {
	private long[] arrTime = new long[50];
	
	class Index
	{
		private int index = 0; 

		public int phyIndex(int idx)
		{
			return idx % arrTime.length;
		}
		public int inc()
		{
			return phyIndex(index++);
		}
		public int count()
		{
			return index;
		}
	}
	private Index index = new Index();
	
	public int count()
	{
		return index.count();
	}
	
	public void newTime()
	{
		arrTime[index.inc()] = System.currentTimeMillis();
	}
	
	// 计算速率
	public double rate()
	{
		int indexBegin = index.count() - arrTime.length;
		if (indexBegin < 0)
			indexBegin = 0;
			
		if (index.count() < 2)
			return 0;
		else
			//return 10+ (1000.0 * (index.count() - indexBegin) / (arrTime[index.phyIndex(index.count()-1)] - arrTime[index.phyIndex(indexBegin)]));
		return (1000.0 * (index.count() - indexBegin) / (arrTime[index.phyIndex(index.count()-1)] - arrTime[index.phyIndex(indexBegin)]));
	}
	
	@Override
	public String toString() {
		return String.format("[%d] %.1f帧/秒",count(), rate());
	}
}
