package org.polaris.schedule.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.polaris.schedule.RenderService;
import org.polaris.schedule.ScheduleItem;
import org.springframework.stereotype.Service;

/**
 * 文本渲染器
 * 
 * @author wang.sheng
 * 
 */
@Service
public class TextRenderService implements RenderService
{
	Log log = LogFactory.getLog(getClass());

	private static final String[] WEEK_DAYS = new String[] { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	@Override
	public void render(ScheduleItem[] scheduleItems)
	{
		if (scheduleItems == null || scheduleItems.length < 1)
		{
			throw new IllegalArgumentException("scheduleItems cannot empty!");
		}
		StringBuffer buffer = new StringBuffer();
		for (String day : WEEK_DAYS)
		{
			buffer.append("\t\t").append(day);
		}
		buffer.append("\n-----------------------------------------------------------------------------------------------------------------------\n");
		Calendar calendar = Calendar.getInstance();
		List<String> dayList = new ArrayList<String>();
		List<String> morningList = new ArrayList<String>();
		List<String> nightList = new ArrayList<String>();
		boolean first = true;
		for (ScheduleItem scheduleItem : scheduleItems)
		{
			calendar.setTimeInMillis(scheduleItem.getTime());
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
			int monthOfYear = calendar.get(Calendar.MONTH) + 1;
			if (first)
			{
				first = false;
				// 第一周, 需要补充前面的空缺
				for (int i = 0; i < dayOfWeek; i++)
				{
					dayList.add("");
					morningList.add("");
					nightList.add("");
				}
			}
			if (dayOfWeek == 0 && !dayList.isEmpty())
			{
				Week week = new Week(dayList.toArray(new String[0]), morningList.toArray(new String[0]),
						nightList.toArray(new String[0]));
				printWeek(week, buffer);
				dayList.clear();
				morningList.clear();
				nightList.clear();
			}
			if (dayOfMonth == 1)
			{
				dayList.add(monthOfYear + "月" + dayOfMonth + "日");
			}
			else
			{
				dayList.add(dayOfMonth + "");
			}
			morningList.add(StringUtils.join(scheduleItem.getMorningStaffSet().toArray(), ','));
			nightList.add(StringUtils.join(scheduleItem.getNightStaffSet().toArray(), ','));
		}
		if (!dayList.isEmpty())
		{
			Week week = new Week(dayList.toArray(new String[0]), morningList.toArray(new String[0]),
					nightList.toArray(new String[0]));
			printWeek(week, buffer);
			dayList.clear();
			morningList.clear();
			nightList.clear();
		}
		writeToFile(buffer.toString());
	}

	private void printWeek(Week week, StringBuffer buffer)
	{
		int length = week.getDays().length;
		buffer.append("\n日期|\t\t");
		for (int i = 0; i < length; i++)
		{
			buffer.append(week.getDays()[i]).append("\t\t");
		}
		buffer.append("\n早班|\t\t");
		for (int i = 0; i < length; i++)
		{
			buffer.append(week.getMornings()[i]).append("\t\t");
		}
		buffer.append("\n晚班|\t\t");
		for (int i = 0; i < length; i++)
		{
			buffer.append(week.getNights()[i]).append("\t\t");
		}
		buffer.append("\n-----------------------------------------------------------------------------------------------------------------------\n");
	}

	private void writeToFile(String content)
	{
		File file = new File("排班表-" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".txt");
		try
		{
			FileUtils.write(file, content);
		}
		catch (IOException e)
		{
			log.error("writeToFile failed!", e);
		}
	}

	static class Week
	{
		private String[] days;
		private String[] mornings;
		private String[] nights;

		public Week(String[] days, String[] mornings, String[] nights)
		{
			this.days = days;
			this.mornings = mornings;
			this.nights = nights;
		}

		public String[] getDays()
		{
			return days;
		}

		public String[] getMornings()
		{
			return mornings;
		}

		public String[] getNights()
		{
			return nights;
		}

	}

}
