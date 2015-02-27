package org.polaris.schedule.meta;

import java.util.Date;
import java.util.List;

import org.polaris.schedule.xml.annotation.XmlAttribute;
import org.polaris.schedule.xml.annotation.XmlList;

public class ScheduleResult
{
	@XmlAttribute(name = "startdate")
	private Date startDate;
	@XmlAttribute(name = "enddate")
	private Date endDate;
	@XmlList(name = "result", itemClass = Result.class)
	private List<Result> resultList;

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public List<Result> getResultList()
	{
		return resultList;
	}

	public void setResultList(List<Result> resultList)
	{
		this.resultList = resultList;
	}

}
