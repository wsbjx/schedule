package org.polaris.schedule.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.polaris.schedule.meta.ScheduleConfig;
import org.polaris.schedule.meta.Staff;

/**
 * 负载均衡容器
 * 
 * @author wang.sheng
 * 
 */
class BalanceContainer
{
	private static Log log = LogFactory.getLog(BalanceContainer.class);
	private final List<Balance> balanceList = new ArrayList<Balance>();

	public void reset(ScheduleConfig scheduleConfig)
	{
		if (!balanceList.isEmpty())
		{
			balanceList.clear();
		}
		for (Staff staff : scheduleConfig.getStaffList())
		{
			Balance balance = new Balance();
			balance.setStaff(staff);
			balance.setBusy(0);
			balanceList.add(balance);
		}
	}

	public void reset()
	{
		for (Balance balance : balanceList)
		{
			balance.setBusy(0);
		}
	}

	public List<Balance> getBalanceList()
	{
		return balanceList;
	}

	public Balance getBalance(Staff staff)
	{
		for (Balance balance : balanceList)
		{
			if (balance.getStaff() == staff)
			{
				return balance;
			}
		}
		throw new RuntimeException("Staff:" + staff.getName() + " not found!");
	}

	/**
	 * 根据负载均衡需要,随机选择一个Balance对象
	 * 
	 * @param staffSet
	 * @param holiday
	 * @param morning
	 * @return
	 */
	public Balance randomSelect(Set<Staff> staffSet, boolean holiday, boolean morning)
	{
		if (staffSet == null || staffSet.isEmpty())
		{
			throw new IllegalArgumentException("StaffSet cannot empty!");
		}
		int min = Integer.MAX_VALUE;
		List<Balance> list = new ArrayList<Balance>();
		for (Staff staff : staffSet)
		{
			Balance balance = this.getBalance(staff);
			int busy = balance.getBusy();
			if (busy < min)
			{
				// 发现最小值
				min = busy;
				list.clear();
				list.add(balance);
			}
			else if (busy == min)
			{
				list.add(balance);
			}
		}
		int radom = (int) (Math.random() * 100);
		int index = radom % list.size();
		Balance balance = list.get(index);// 随机获取集合中的对象
		log.info("------------------------------------");
		for (Balance b : list)
		{
			log.info("minList: " + b);
		}
		log.info("randomSelect: " + staffSet + ", select result: " + balance.getStaff() + " holiday:" + holiday
				+ ", morning:" + morning);
		log.info("------------------------------------");
		return balance;
	}
}