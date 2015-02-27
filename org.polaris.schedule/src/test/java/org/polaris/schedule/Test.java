package org.polaris.schedule;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test
{
	@SuppressWarnings("resource")
	public static void main(String[] args)
	{
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:conf/spring/*.xml");
		ScheduleService scheduleService = applicationContext.getBean(ScheduleService.class);
		long startTime = System.currentTimeMillis();
		long endTime = startTime + 61 * 24 * 60 * 60 * 1000L;
		ScheduleItem[] scheduleItems = scheduleService.schedule(startTime, endTime);
		RenderService renderService = applicationContext.getBean(RenderService.class);
		renderService.render(scheduleItems);
	}
}
