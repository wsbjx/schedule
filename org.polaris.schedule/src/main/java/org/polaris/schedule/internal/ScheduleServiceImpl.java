package org.polaris.schedule.internal;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.polaris.schedule.ScheduleService;
import org.polaris.schedule.meta.ScheduleConfig;
import org.polaris.schedule.meta.ScheduleResult;
import org.polaris.schedule.meta.Staff;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements ScheduleService
{
	Log log = LogFactory.getLog(getClass());

	@Resource
	private ScheduleConfig scheduleConfig;

	@Override
	public ScheduleResult schedule(long startTime, long endTime)
	{
		// 创建上下文
		ScheduleContext context = new ScheduleContext(startTime, endTime);
		context.getBalanceContainer().reset(scheduleConfig);
		context.getHistoryContainer().clear();
		Calendar calendar = context.getCalendar();
		while (context.hasNextDay())
		{
			boolean holiday = this.isHoliday(calendar);
			ScheduleItem todayScheduleItem = new ScheduleItem(calendar.getTimeInMillis(), holiday);
			if (holiday)
			{
				// 节假日
				allocateHolidayMorning(context, todayScheduleItem);
				allocateHolidayNight(context, todayScheduleItem);
			}
			else
			{
				// 工作日
				allocateWorkingMorning(context, todayScheduleItem);
				allocateWorkingNight(context, todayScheduleItem);
			}
			context.nextDay();// 分析下一天
			context.getHistoryContainer().add(todayScheduleItem);
		}
		ScheduleItem[] scheduleItems = context.getHistoryContainer().getScheduleItems();
		for (ScheduleItem scheduleItem : scheduleItems)
		{
			log.info(scheduleItem);
		}
		return null;
	}

	/**
	 * 分配工作日早班
	 * 
	 * @param context
	 * @param todayScheduleItem
	 */
	private void allocateWorkingMorning(ScheduleContext context, ScheduleItem todayScheduleItem)
	{
		ScheduleItem lastestItem = context.getHistoryContainer().getLatestItem();
		// 工作日早班
		Set<Staff> tempStaffSet = new HashSet<Staff>();// 一个临时的,用于分配待选的容器
		for (int i = 0; i < scheduleConfig.getWorkingStaff(); i++)
		{
			if (tempStaffSet.isEmpty())
			{
				for (Staff staff : scheduleConfig.getStaffList())
				{
					if (staff.isWorkingMorning() && !todayScheduleItem.containsMorning(staff))
					{
						// 职员工作日早班使能,并且昨天没有上晚班,添加到候选容器
						if (lastestItem == null || !lastestItem.containsNight(staff))
						{
							// 昨天上的不是夜班
							tempStaffSet.add(staff);
						}
					}
				}
			}
			if (tempStaffSet.isEmpty())
			{
				// 仍然为空,则无法进行选取
				throw new RuntimeException("cannot find real Staff!");
			}
			Balance balance = context.getBalanceContainer().randomSelect(tempStaffSet, false, true);
			todayScheduleItem.addMorning(balance.getStaff());
			balance.increase();
			tempStaffSet.remove(balance.getStaff());// 从候补队列中删除
		}
	}

	/**
	 * 分配工作日晚班
	 * 
	 * @param context
	 * @param todayScheduleItem
	 */
	private void allocateWorkingNight(ScheduleContext context, ScheduleItem todayScheduleItem)
	{
		// 工作日晚班
		Set<Staff> tempStaffSet = new HashSet<Staff>();// 一个临时的,用于分配待选的容器
		for (int i = 0; i < scheduleConfig.getWorkingStaff(); i++)
		{
			if (tempStaffSet.isEmpty())
			{
				for (Staff staff : scheduleConfig.getStaffList())
				{
					if (staff.isWorkingNight() && !todayScheduleItem.contains(staff))
					{
						// 职员工作日晚班使能,并且今天没有上早班,添加到候选容器
						tempStaffSet.add(staff);
					}
				}
			}
			if (tempStaffSet.isEmpty())
			{
				// 仍然为空,则无法进行选取
				throw new RuntimeException("cannot find real Staff!");
			}
			Balance balance = context.getBalanceContainer().randomSelect(tempStaffSet, false, false);
			todayScheduleItem.addNight(balance.getStaff());
			balance.increase();
			tempStaffSet.remove(balance.getStaff());// 从候补队列中删除
		}
	}

	/**
	 * 分配节假日早班
	 * 
	 * @param context
	 * @param todayScheduleItem
	 */
	private void allocateHolidayMorning(ScheduleContext context, ScheduleItem todayScheduleItem)
	{
		ScheduleItem[] items = context.getHistoryContainer().getLatestItems(false, 5);
		ScheduleItem lastestItem = context.getHistoryContainer().getLatestItem();
		// 节假日早班
		Set<Staff> tempStaffSet = new HashSet<Staff>();// 一个临时的,用于分配待选的容器
		for (ScheduleItem item : items)
		{
			for (Staff staff : item.getMorningStaffSet())
			{
				if (staff.isHolidayMorning())
				{
					// 职员节假日早班使能,并且昨天没有上晚班,添加到候选容器
					if (lastestItem == null || !lastestItem.containsNight(staff))
					{
						// 昨天上的不是夜班
						tempStaffSet.add(staff);
					}
				}
			}
		}
		for (int i = 0; i < scheduleConfig.getHolidayStaff(); i++)
		{
			if (tempStaffSet.isEmpty())
			{
				// 没有符合条件的,则遍历全部职员
				for (Staff staff : scheduleConfig.getStaffList())
				{
					if (staff.isHolidayMorning() && !todayScheduleItem.containsMorning(staff))
					{
						// 职员节假日早班使能,并且昨天没有上晚班,添加到候选容器
						if (lastestItem == null || !lastestItem.containsNight(staff))
						{
							// 昨天上的不是夜班
							tempStaffSet.add(staff);
						}
					}
				}
			}
			if (tempStaffSet.isEmpty())
			{
				// 仍然为空,则无法进行选取
				throw new RuntimeException("cannot find real Staff!");
			}

			Balance balance = context.getBalanceContainer().randomSelect(tempStaffSet, true, true);
			todayScheduleItem.addMorning(balance.getStaff());
			balance.increase();
			tempStaffSet.remove(balance.getStaff());// 从候补队列中删除
		}
	}

	/**
	 * 分配节假日晚班
	 * 
	 * @param context
	 * @param todayScheduleItem
	 */
	private void allocateHolidayNight(ScheduleContext context, ScheduleItem todayScheduleItem)
	{
		ScheduleItem[] items = context.getHistoryContainer().getLatestItems(false, 5);
		// 节假日晚班
		Set<Staff> tempStaffSet = new HashSet<Staff>();// 一个临时的,用于分配待选的容器
		for (ScheduleItem item : items)
		{
			for (Staff staff : item.getNightStaffSet())
			{
				if (staff.isHolidayNight())
				{
					// 职员节假日晚班使能,并且今天没有上早班,添加到候选容器
					if (!todayScheduleItem.containsMorning(staff))
					{
						tempStaffSet.add(staff);
					}
				}
			}
		}
		for (int i = 0; i < scheduleConfig.getHolidayStaff(); i++)
		{
			if (tempStaffSet.isEmpty())
			{
				// 没有符合条件的,则遍历全部职员
				for (Staff staff : scheduleConfig.getStaffList())
				{
					if (staff.isHolidayNight() && !todayScheduleItem.contains(staff))
					{
						// 职员节假日晚班使能,并且今天没有上早班,添加到候选容器
						tempStaffSet.add(staff);
					}
				}
			}
			if (tempStaffSet.isEmpty())
			{
				// 仍然为空,则无法进行选取
				throw new RuntimeException("cannot find real Staff!");
			}

			Balance balance = context.getBalanceContainer().randomSelect(tempStaffSet, true, false);
			todayScheduleItem.addNight(balance.getStaff());
			balance.increase();
			tempStaffSet.remove(balance.getStaff());// 从候补队列中删除
		}
	}

	/**
	 * 判断是否假期
	 * 
	 * @param calendar
	 * @return
	 */
	private boolean isHoliday(Calendar calendar)
	{
		int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		boolean isWeekend = (week == 0 || week == 6);// 周六或者周日
		Long time = calendar.getTimeInMillis();
		if (scheduleConfig.getExceptTimes().contains(time))
		{
			// 需要做例外处理
			return !isWeekend;
		}
		return isWeekend;
	}

}
