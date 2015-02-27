package org.polaris.schedule.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史分配容器
 * 
 * @author wang.sheng
 * 
 */
class HistoryContainer
{
	private final List<ScheduleItem> itemList = new ArrayList<ScheduleItem>();

	public void add(ScheduleItem item)
	{
		itemList.add(item);
	}

	public void clear()
	{
		itemList.clear();
	}

	public ScheduleItem[] getScheduleItems()
	{
		return itemList.toArray(new ScheduleItem[0]);
	}

	/**
	 * 获取上一天的分配单元
	 * 
	 * @return
	 */
	public ScheduleItem getLatestItem()
	{
		if (!itemList.isEmpty())
		{
			return itemList.get(itemList.size() - 1);
		}
		return null;
	}

	/**
	 * 获取最近几天的分配单元
	 * 
	 * @param holiday
	 * @param limit
	 * @return
	 */
	public ScheduleItem[] getLatestItems(boolean holiday, int limit)
	{
		List<ScheduleItem> list = new ArrayList<ScheduleItem>();
		int count = 0;
		for (int i = itemList.size() - 1; i >= 0; i--)
		{
			if (count > limit)
			{
				break;
			}
			ScheduleItem item = itemList.get(i);
			if (item.isHoliday() == holiday)
			{
				count++;
				list.add(item);
			}
		}
		return list.toArray(new ScheduleItem[0]);
	}

}
