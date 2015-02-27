package org.polaris.schedule.internal;

import org.polaris.schedule.meta.Staff;

/**
 * 负载均衡
 * 
 * @author wang.sheng
 * 
 */
class Balance
{
	private Staff staff;
	private int busy = 0;

	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("Balance: [" + staff + "] " + busy);
		return buffer.toString();
	}

	public int getBusy()
	{
		return busy;
	}

	public void setBusy(int busy)
	{
		this.busy = busy;
	}

	public void increase()
	{
		busy++;
	}
	
	public Staff getStaff()
	{
		return staff;
	}

	public void setStaff(Staff staff)
	{
		this.staff = staff;
	}

}
