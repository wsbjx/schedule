package org.polaris.schedule.meta;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.polaris.schedule.xml.annotation.XmlAttribute;
import org.polaris.schedule.xml.annotation.XmlList;

public class ScheduleConfig
{
	private static final String[] DATEFORMATES = new String[] { "yyyy-MM-dd" };
	private static Log log = LogFactory.getLog(ScheduleConfig.class);

	@XmlAttribute(name = "exceptdates")
	private String exceptDates;
	@XmlList(name = "staff", itemClass = Staff.class)
	private List<Staff> staffList;
	@XmlAttribute(name = "workingstaff")
	private int workingStaff;
	@XmlAttribute(name = "holidaystaff")
	private int holidayStaff;

	private Set<Long> exceptTimeSet;

	public Set<Long> getExceptTimes()
	{
		if (exceptTimeSet == null)
		{
			exceptTimeSet = new HashSet<Long>();
			for (String date : StringUtils.split(exceptDates, ','))
			{
				try
				{
					Date d = DateUtils.parseDate(date, DATEFORMATES);
					exceptTimeSet.add(d.getTime());
				}
				catch (ParseException e)
				{
					log.error("Date parsed failed! date:" + date, e);
				}
			}
		}
		return exceptTimeSet;
	}

	public List<Staff> getStaffList()
	{
		return staffList;
	}

	public int getWorkingStaff()
	{
		return workingStaff;
	}

	public int getHolidayStaff()
	{
		return holidayStaff;
	}

}
