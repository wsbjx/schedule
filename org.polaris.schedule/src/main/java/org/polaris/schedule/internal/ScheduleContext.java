package org.polaris.schedule.internal;

import java.util.Calendar;

/**
 * 工作分配上下文
 * 
 * @author wang.sheng
 * 
 */
class ScheduleContext
{
	private static final long DAY_TIME = 24 * 60 * 60 * 1000L;
	
	private final Calendar calendar = Calendar.getInstance();
	private final HistoryContainer historyContainer = new HistoryContainer();
	private final BalanceContainer balanceContainer = new BalanceContainer();
	
	private long endTime;

	public ScheduleContext(long startTime, long endTime)
	{
		if (endTime <= startTime)
		{
			throw new IllegalArgumentException("endTime must after startTime!");
		}
		calendar.setTimeInMillis(startTime);
		this.endTime = endTime;
	}
	
	public BalanceContainer getBalanceContainer()
	{
		return balanceContainer;
	}

	public HistoryContainer getHistoryContainer()
	{
		return historyContainer;
	}

	public Calendar getCalendar()
	{
		return calendar;
	}

	public int getWeek()
	{
		return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 存在下一天
	 * 
	 * @return
	 */
	public boolean hasNextDay()
	{
		return calendar.getTimeInMillis() < endTime;
	}

	/**
	 * 日历进入下一天
	 * 
	 * @param calendar
	 */
	public void nextDay()
	{
		long time = calendar.getTimeInMillis();
		time += DAY_TIME;
		calendar.setTimeInMillis(time);
	}

}
