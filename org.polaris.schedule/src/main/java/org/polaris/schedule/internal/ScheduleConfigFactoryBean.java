package org.polaris.schedule.internal;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.polaris.schedule.meta.ScheduleConfig;
import org.polaris.schedule.xml.XmlParseService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * Schedule配置的工厂Bean
 * 
 * @author wang.sheng
 * 
 */
public class ScheduleConfigFactoryBean implements FactoryBean<ScheduleConfig>
{
	Log log = LogFactory.getLog(getClass());

	private XmlParseService xmlParseService;
	private Resource resource;

	/**
	 * 使用Spring的依赖注入
	 * 
	 * @param xmlParseService
	 */
	public void setXmlParseService(XmlParseService xmlParseService)
	{
		this.xmlParseService = xmlParseService;
	}

	/**
	 * 使用Spring的依赖注入
	 * 
	 * @param resource
	 */
	public void setResource(Resource resource)
	{
		this.resource = resource;
	}

	@Override
	public ScheduleConfig getObject() throws Exception
	{
		InputStream is = null;
		try
		{
			is = resource.getInputStream();
			return xmlParseService.parseObject(ScheduleConfig.class, is);
		}
		catch (IOException e)
		{
			log.error("ScheduleConfig created failed!", e);
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
		return null;
	}

	@Override
	public Class<?> getObjectType()
	{
		return ScheduleConfig.class;
	}

	@Override
	public boolean isSingleton()
	{
		return true;
	}

}
