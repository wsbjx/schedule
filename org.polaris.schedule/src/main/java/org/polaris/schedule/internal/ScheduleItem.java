package org.polaris.schedule.internal;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.time.DateFormatUtils;
import org.polaris.schedule.meta.Staff;

/**
 * 班次单元
 * 
 * @author wang.sheng
 * 
 */
class ScheduleItem
{
	private long time;
	private Set<Staff> morningSet = new HashSet<Staff>();
	private Set<Staff> nightSet = new HashSet<Staff>();
	private boolean holiday;

	public ScheduleItem(long time, boolean holiday)
	{
		this.time = time;
		this.holiday = holiday;
	}

	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n--------------- Date: ").append(DateFormatUtils.format(new Date(time), "yyyy-MM-dd"))
				.append(" Holiday: ").append(holiday).append(" ---------------\n");
		buffer.append("Morning: ").append(morningSet).append("\n");
		buffer.append("Night: ").append(nightSet).append("\n");
		buffer.append("---------------------------------------------------------------");
		return buffer.toString();
	}

	public Set<Staff> getMorningStaffSet()
	{
		return morningSet;
	}

	public Set<Staff> getNightStaffSet()
	{
		return nightSet;
	}

	public boolean isHoliday()
	{
		return holiday;
	}

	public long getTime()
	{
		return time;
	}

	public void addMorning(Staff staff)
	{
		morningSet.add(staff);
	}

	public void addNight(Staff staff)
	{
		nightSet.add(staff);
	}

	public boolean contains(Staff staff)
	{
		return morningSet.contains(staff) || nightSet.contains(staff);
	}

	public boolean containsMorning(Staff staff)
	{
		return morningSet.contains(staff);
	}

	public boolean containsNight(Staff staff)
	{
		return nightSet.contains(staff);
	}
}
