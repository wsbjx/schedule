package org.polaris.schedule;

import org.polaris.schedule.meta.ScheduleResult;

/**
 * 排班服务
 * 
 * @author wang.sheng
 * 
 */
public interface ScheduleService
{
	ScheduleResult schedule(long startTime, long endTime);
}
