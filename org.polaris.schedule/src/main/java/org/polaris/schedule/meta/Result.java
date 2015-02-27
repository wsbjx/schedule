package org.polaris.schedule.meta;

import java.util.Date;

import org.polaris.schedule.xml.annotation.XmlAttribute;

public class Result
{
	@XmlAttribute
	private Date date;
	@XmlAttribute(name = "morning-staffs")
	private String[] morningStaffs;
	@XmlAttribute(name = "night-staffs")
	private String[] nightStaffs;
	@XmlAttribute(name = "workingday")
	private boolean workingDay;

	public void setDate(Date date)
	{
		this.date = date;
	}

	public void setMorningStaffs(String[] morningStaffs)
	{
		this.morningStaffs = morningStaffs;
	}

	public void setNightStaffs(String[] nightStaffs)
	{
		this.nightStaffs = nightStaffs;
	}

	public void setWorkingDay(boolean workingDay)
	{
		this.workingDay = workingDay;
	}

	public Date getDate()
	{
		return date;
	}

	public String[] getMorningStaffs()
	{
		return morningStaffs;
	}

	public String[] getNightStaffs()
	{
		return nightStaffs;
	}

	public boolean isWorkingDay()
	{
		return workingDay;
	}

}
