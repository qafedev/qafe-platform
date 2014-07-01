package com.qualogy.qafe.business.integration.filter.page;

import java.util.Map;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.integration.service.Method;
import com.qualogy.qafe.bind.integration.service.Service;
import com.qualogy.qafe.business.integration.Processor;

/**
 * Aspect to implement paging after result has been returned by the processor.
 */
public aspect PagePostProcessor {

	pointcut execExecute(ApplicationContext context, Service service, Method method, Map paramsIn, Page page):
		execution(Object Processor.execute(ApplicationContext, Service, Method, Map, Page))
	    && args(context, service, method, paramsIn, page);

	Object around(final ApplicationContext context, final Service service, final Method method, final Map paramsIn, final Page page) : execExecute(context, service, method, paramsIn, page) {
		Object retValue = proceed(context, service, method, paramsIn, page);
		return ListDataExtractor.extractData(retValue, page);
	}
}
