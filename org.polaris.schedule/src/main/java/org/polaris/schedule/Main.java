package org.polaris.schedule;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main
{
	private static final String[] DATE_FORMATES = new String[] { "yyyy-MM-dd" };

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception
	{
		long startTime = System.currentTimeMillis();
		long endTime = startTime + 32 * 24 * 60 * 60 * 1000L;
		if (args != null && args.length > 0)
		{
			if (args.length == 1)
			{
				Date date = DateUtils.parseDate(args[0], DATE_FORMATES);
				endTime = date.getTime();
			}
			else if (args.length == 2)
			{
				Date date = DateUtils.parseDate(args[0], DATE_FORMATES);
				startTime = date.getTime();
				date = DateUtils.parseDate(args[1], DATE_FORMATES);
				endTime = date.getTime();
			}
			else
			{
				throw new IllegalArgumentException("input params failed!");
			}
		}
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:conf/spring/*.xml");
		ScheduleService scheduleService = applicationContext.getBean(ScheduleService.class);
		ScheduleItem[] scheduleItems = scheduleService.schedule(startTime, endTime);
		RenderService renderService = applicationContext.getBean(RenderService.class);
		renderService.render(scheduleItems);
	}
}