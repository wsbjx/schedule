package org.polaris.schedule;

/**
 * 排班服务
 * 
 * @author wang.sheng
 * 
 */
public interface ScheduleService
{
	ScheduleItem[] schedule(long startTime, long endTime);
}
