package org.polaris.schedule.meta;

import org.polaris.schedule.xml.annotation.XmlAttribute;

public class Staff
{
	@XmlAttribute
	private String name;
	@XmlAttribute(name = "working-morning")
	private boolean workingMorning;
	@XmlAttribute(name = "working-night")
	private boolean workingNight;
	@XmlAttribute(name = "holiday-morning")
	private boolean holidayMorning;
	@XmlAttribute(name = "holiday-night")
	private boolean holidayNight;

	@Override
	public String toString()
	{
		return name;
	}

	public String getName()
	{
		return name;
	}

	public boolean isWorkingMorning()
	{
		return workingMorning;
	}

	public boolean isWorkingNight()
	{
		return workingNight;
	}

	public boolean isHolidayMorning()
	{
		return holidayMorning;
	}

	public boolean isHolidayNight()
	{
		return holidayNight;
	}

}
