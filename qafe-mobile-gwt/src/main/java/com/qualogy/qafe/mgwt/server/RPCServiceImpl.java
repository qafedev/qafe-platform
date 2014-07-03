/**
 * Copyright 2008-2014 Qualogy Solutions B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qualogy.qafe.mgwt.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.StopWatch;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationIdentifier;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.core.QafeApplicationContext;
import com.qualogy.qafe.core.application.ApplicationCluster;
import com.qualogy.qafe.core.application.UserInfo;
import com.qualogy.qafe.core.datastore.ApplicationLocalStore;
import com.qualogy.qafe.core.i18n.MessagesHandler;
import com.qualogy.qafe.core.security.SecurityInfo;
import com.qualogy.qafe.mgwt.client.exception.GWTServiceException;
import com.qualogy.qafe.mgwt.client.service.RPCService;
import com.qualogy.qafe.mgwt.client.vo.data.EventDataGVO;
import com.qualogy.qafe.mgwt.client.vo.data.GDataObject;
import com.qualogy.qafe.mgwt.client.vo.data.InitState;
import com.qualogy.qafe.mgwt.client.vo.functions.DataContainerGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.UIGVO;
import com.qualogy.qafe.mgwt.client.vo.ui.UIVOCluster;
import com.qualogy.qafe.mgwt.server.event.assembler.SetValueBuiltInRenderer;
import com.qualogy.qafe.mgwt.server.helper.DatagridExportHelper;
import com.qualogy.qafe.mgwt.server.processor.EventProcessor;
import com.qualogy.qafe.mgwt.server.processor.impl.EventProcessorImpl;
import com.qualogy.qafe.mgwt.server.service.UIService;
import com.qualogy.qafe.mgwt.server.service.impl.UIServiceImpl;
import com.qualogy.qafe.presentation.io.ApplicationClusterUtil;
import com.qualogy.qafe.util.ExceptionHelper;
import com.qualogy.qafe.web.ContextLoader;
import com.qualogy.qafe.web.ContextLoaderHelper;
import com.qualogy.qafe.web.UploadService;
import com.qualogy.qafe.web.css.util.CssProvider;
import com.qualogy.qafe.web.util.DatagridStorageHelper;
import com.qualogy.qafe.web.util.MessageUtil;
import com.qualogy.qafe.web.util.ServletUtilities;
import com.qualogy.qafe.web.util.SessionContainer;
import com.qualogy.qafe.web.util.UserAgentUtil;

/**
 * @author rjankie
 * 
 */
public class RPCServiceImpl extends RemoteServiceServlet implements RPCService {
	public final static Logger logger = Logger.getLogger(RPCServiceImpl.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 3988869926326800260L;

	private UIService service = new UIServiceImpl();

	private EventProcessor eventProcessor = new EventProcessorImpl();
	/*
	 * protected MaintenanceService getMaintenanceService(){
	 * WebApplicationContext wac =
	 * WebApplicationContextUtils.getWebApplicationContext(
	 * getServletContext()); MaintenanceService maintenanceService =
	 * (MaintenanceService)wac.getBean("maintenanceService"); return
	 * maintenanceService; }
	 */

	/**
	 * Write the serialized response out to stdout. This is a very unusual thing
	 * to do, but it allows us to create a static file version of the response
	 * without deploying a servlet.
	 */
	protected void onAfterResponseSerialized(String serializedResponse) {
		logger.fine(serializedResponse);
	}

	protected GWTServiceException handleException(Throwable e) {
		GWTServiceException gwtE = null;
		if (e != null) {
			Throwable throwable = ExceptionUtils.getRootCause(e);
			if (throwable == null) { // fallback if no rootCause could be found!
				throwable = e;
			}
			gwtE = new GWTServiceException(e.getMessage(), throwable);
			String detailedMessage = ExceptionHelper.printStackTrace(e);
			gwtE.setDetailedMessage(detailedMessage);
			// try {
			// notify("qafe.support@qualogy.com", "Exception occurred -" +
			// sdf.format(new Date()), detailedMessage);
			// } catch (GWTServiceException e1) {
			// String dm = ExceptionHelper.printStackTrace(e);
			// gwtE = new GWTServiceException("Notify email failed", dm, e1);
			//
			// }	
		} else { // fallback if exception somehow is null
			gwtE = new GWTServiceException("Unexpected error occured -" + MessageUtil.SIMPLE_DATE_FORMAT.format(new Date()), (Throwable) null);
			gwtE.setDetailedMessage("Unexpected error occured");
		}
		return gwtE;
	}

	public UIVOCluster reload(Map<String,String> parameters) throws GWTServiceException {
		UIVOCluster cluster = null;
		try {
			ContextLoader contextLoader = (ContextLoader) getServletContext().getAttribute("contextloader");
			contextLoader.init(getServletContext());
			MessagesHandler.clear();
			// ApplicationContextLoader.reload();
			String css = CssProvider.getInstance().generateTypedCSS("gwt", null);
			service.clear();
			cluster = getUISFromApplicationContext(parameters);
			cluster.setCss(css);
		} catch (Exception e) {
			throw handleException(e);
		}
		return cluster;
	}
	
	public UIVOCluster activate(Map<String,String> parameters) throws GWTServiceException {
		return getUISFromApplicationContext(parameters);
	}

	public UIVOCluster getUISFromApplicationContext(Map<String,String> parameters) throws GWTServiceException {
		if (getServletContext().getAttribute("init_failure") != null) {
			Throwable throwable = (Throwable) getServletContext().getAttribute("init_failure");
			getServletContext().removeAttribute("init_failure");
			getServletContext().removeAttribute("init_failure_message");
			throw handleException(throwable);
		} else {
			try {
				UIVOCluster cluster = null;

				Iterator<ApplicationIdentifier> contextItr = ApplicationCluster.getInstance().keysIterator();
				String appId = null;
				String windowSession = null;
				cluster = new UIVOCluster();
				cluster.setExternalProperties(ApplicationClusterUtil.getExternalProperties());
				cluster.setDebugMode(ContextLoaderHelper.isDebugMode());
				cluster.setShowLog(ContextLoaderHelper.showLog());
				cluster.setGlobalDateFormat(ApplicationClusterUtil.getGlobalDateFormat());
				cluster.setReloadable(ContextLoaderHelper.isReloadable(getThreadLocalRequest()));
				cluster.setUseDockMode(ContextLoaderHelper.isDockMode());

				
				cluster.setSystemMenuApplication(service.getSystemApplication(ApplicationCluster.getInstance().getSystemApplicationContext(), appId, windowSession, getLocale(),parameters));
				List<UIGVO> gvoList = new ArrayList<UIGVO>();

				StopWatch stopWatch = new StopWatch();

				while (contextItr.hasNext()) {
					ApplicationIdentifier key = contextItr.next();
					if (!ApplicationClusterUtil.isSystemApplication(key)) {
						stopWatch.start();

						UIGVO uigvo = null;
						ApplicationContext context = ApplicationCluster.getInstance().get(key);
						
						if(context.getLoadException() == null) {
							try {
								uigvo = service.getUI(context.getId(), appId, windowSession, getLocale(),parameters);
							} catch(Exception e) {
								String errorMessage = e != null ? "(" + e.getMessage() + ")" : "";
								String message = key.toString() + " caused exception : " + errorMessage;
								cluster.getMessages().add(message);
							}
							
						}						

						if (uigvo != null) {
							uigvo.setAppId(context.getId().toString());
							uigvo.setDebug(Boolean.valueOf(context.getConfigurationItem(/*
																						 * Configuration.
																						 * DEVELOPMENT_MODE
																						 */"")));

							uigvo.setTitle(context.getName());
							stopWatch.stop();
							uigvo.setTime(Long.valueOf(stopWatch.getTime()));
							logger.info("Application -" + context.getName() + " is rendered in  " + stopWatch.getTime() + "ms");

							stopWatch.reset();
							gvoList.add(uigvo);
						} else {
							stopWatch.stop();
							stopWatch.reset();
							String errorMessage = context.getLoadException() != null ? "(" + context.getLoadException().getMessage() + ")" : "";
							String message = "Application with id " + key.toString() + " is not loaded! " + errorMessage;

							cluster.getMessages().add(message);
						}

						if (context.getWarningMessages() != null) {
							for (String strMsg : context.getWarningMessages()) {
								cluster.getMessages().add(strMsg);
							}
						}

					}
				}
				
				cluster.setVos(gvoList.toArray(new UIGVO[] {}));
				cluster = service.stripCluster(cluster);
				String css = CssProvider.getInstance().generateTypedCSS("gwt", null);
				cluster.setCss(css);
				return cluster;

			} catch (Exception e) {
				throw handleException(e);

			}
		}

	}

	/**
	 * Default showcase of components
	 */
	public UIGVO getUIFromApplicationContext() throws GWTServiceException {
		try {
			return null;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	public UIGVO getUIVO(String xmlUI) throws GWTServiceException {
		try {
			return service.getUIFromXML(xmlUI, null, null, getLocale());
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	public String getUI(String xmlUI) throws GWTServiceException {
		String url = null;
		if (service.isValidXML(xmlUI)) {
			logger.fine("XML Send by client : \n" + xmlUI);

			try {
				
				String urlBase = ApplicationCluster.getInstance().getConfigurationItem(Configuration.FLEX_DEMO_WAR_URL);
				if(urlBase == null || urlBase.length() == 0) {
					urlBase = getThreadLocalRequest().getScheme() + "://" + getThreadLocalRequest().getServerName() + ":" + getThreadLocalRequest().getServerPort() + "/qafe-web-flex";
				}
				
				String urlStore = urlBase + "/store";
				logger.fine("URL Store is =" + urlStore);
								
				OutputStreamWriter wr = null;
				BufferedReader rd = null;
				
				try {
					// Send data
					URL requestURL = new URL(urlStore);
					URLConnection conn = requestURL.openConnection();
				    conn.setDoOutput(true);
				    wr = new OutputStreamWriter(conn.getOutputStream());
				    String data = "xml" + "=" + xmlUI;
				    wr.write(data);
				    wr.flush();
					
				    // Get the response
				    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				    String line;
				    while ((line = rd.readLine()) != null) {
				    	url = urlBase + "/index.jsp?uuid=" + line;
						logger.fine(url);
				    }
				} finally {
					 wr.close();
					 rd.close();
				}
			} catch (Exception e) {
				throw handleException(e);
			}
		} else {
			try {
				service.getUIFromXML(xmlUI, null, null, getLocale());
			} catch (Exception e) {
				throw handleException(e);
			}
		}
		return url;
	}


	public String getXMLUIByUUID(String uuid) throws GWTServiceException {
		try {
			Map<String, String> map = (Map<String, String>) getServletContext().getAttribute("forms-tempmap");
			String qamlCode = map.get(uuid); 
			map.remove(uuid);
			return qamlCode;

		} catch (Exception e) {
			throw handleException(e);
		}
	}

	public GDataObject executeEvent(EventDataGVO eventData) throws GWTServiceException {
		StopWatch stopWatch = new StopWatch();
		try {

			stopWatch.start();
			
			if (StringUtils.isEmpty(eventData.getUserUID()) || (SecurityInfo.getInstance().getUserInfo(eventData.getUserUID()) == null)) {
				UserInfo userInfo = new UserInfo(eventData.getUserUID());
				eventData.setUserUID(userInfo.getUserUID());
				SecurityInfo.getInstance().addUserInfo(userInfo);
			}
			
			String appIdUUID = eventData.getUuid().substring(eventData.getUuid().lastIndexOf('|') + 1);// uuid.substring(uuid.lastIndexOf('|')+1);
			ApplicationIdentifier appId = service.getApplicationId(appIdUUID);
			GDataObject gDataObject = null;
			if (appId != null) {
				eventData.setContext(appId.toString());
				resolveRequest(eventData);
				gDataObject = eventProcessor.execute(eventData, appId, new SessionContainer(getLocale(),eventData.getParameters()));
			}
			stopWatch.stop();

			if (gDataObject != null) {
				gDataObject.setTime(Long.valueOf(stopWatch.getTime()));
			}
			return gDataObject;
		} catch (Exception e) {
			GWTServiceException gWTServiceException = handleException(e);
			gWTServiceException.setGDataObject(ExceptionProcessor.handle(eventData, e));
			stopWatch.stop();
			if (gWTServiceException.getGDataObject() != null) {
				gWTServiceException.getGDataObject().setTime(Long.valueOf(stopWatch.getTime()));
			}
			throw gWTServiceException;
		}
	}

	protected void resolveRequest(EventDataGVO eventData) {
		Map<String, Object> request = new HashMap<String, Object>();

		String serverProtocol = getThreadLocalRequest().getProtocol();
		if ((serverProtocol != null) && (serverProtocol.split("/").length > 0)) {
			serverProtocol = serverProtocol.split("/")[0];
		}
		String serverContextRoot = getThreadLocalRequest().getRequestURI();
		if ((serverContextRoot != null) && (serverContextRoot.split("/").length > 1)) {
			serverContextRoot = serverContextRoot.split("/")[1];
		}
		String serverName = getThreadLocalRequest().getServerName();
		String serverPort = String.valueOf(getThreadLocalRequest().getServerPort());
		String serverBaseURL = serverProtocol + "://" + serverName + ":" + serverPort + "/" + serverContextRoot;

		request.put(EventDataGVO.REQUEST_PROTOCOL, serverProtocol);
		request.put(EventDataGVO.REQUEST_SERVER_NAME, serverName);
		request.put(EventDataGVO.REQUEST_PORT, serverPort);
		request.put(EventDataGVO.REQUEST_CONTEXT_ROOT, serverContextRoot);
		request.put(EventDataGVO.REQUEST_BASE_URL, serverBaseURL);
		
		if(eventData.getLocationDataMap() != null) {
			request.put(EventDataGVO.REQUEST_GEO, eventData.getLocationDataMap());
		}
		
		if (getThreadLocalRequest().getCookies()!=null){
			Cookie[] cks = getThreadLocalRequest().getCookies();
			Map<String,String> cookies = new HashMap<String,String>();
			int length =cks.length;
			for (int i =0;i<length;i++){
				cookies.put(cks[i].getName(),cks[i].getValue());
			}
			request.put(EventDataGVO.REQUEST_COOKIES, cookies);
		}
		
		
		/* Improved Request object*/
		Map<String,String> requestData = new HashMap<String,String>();
		requestData.put(EventDataGVO.REQUEST_PROP_PROTOCOL, serverProtocol);
		requestData.put(EventDataGVO.REQUEST_PROP_SERVER_NAME, serverName);
		requestData.put(EventDataGVO.REQUEST_PROP_PORT, serverPort);
		requestData.put(EventDataGVO.REQUEST_PROP_CONTEXT_ROOT, serverContextRoot);
		requestData.put(EventDataGVO.REQUEST_PROP_BASE_URL, serverBaseURL);
		request.put(EventDataGVO.REQUEST, requestData);
		
		logger.info(eventData.getMouse().toString());
		eventData.setRequest(request);
	}

	public UIGVO getUIByUUID(String uuid) throws GWTServiceException {
		try {
			long millis = Calendar.getInstance().getTimeInMillis();
			UIGVO ui = null;
			// for the openwindow builtin functionality, this uuid needs to be
			// "clean"
			if (uuid.lastIndexOf('|') > 0) {
				String strippedUUID = uuid.substring(uuid.lastIndexOf('|') + 1);
				ui = service.getUIByUUID(strippedUUID);
			} else {
				ui = service.getUIByUUID(uuid);
			}
			long executionTime = Calendar.getInstance().getTimeInMillis() - millis;
			if (ui != null) {
				ui.setTime(Long.valueOf(executionTime));
			}
			return ui;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	public void removeUI(String uuid) throws GWTServiceException {
		try {

		} catch (Exception e) {
			throw handleException(e);
		}

	}

	public InitState getMDIState(Map<String,String> parameters) throws GWTServiceException {
		try {
			String mdiModeParamValue = parameters.get(Configuration.MDI_MODE);
			return new InitState(isMDIMode(mdiModeParamValue), ContextLoaderHelper.isDockMode());
		} catch (Exception e) {
			throw handleException(e);
		}
	}
	

	private boolean isMDIMode(String mdiModeParamValue) {
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		if (UserAgentUtil.isMobile(userAgent)) {
			return false;
		}
		// Check the dynamic parameter mode.mdi passed with url
		if(mdiModeParamValue != null) {
			if (mdiModeParamValue.equalsIgnoreCase("true")) {
				return true;
			} else if(mdiModeParamValue.equalsIgnoreCase("false")){
				return false;
			}
		}
		
		return ContextLoaderHelper.isMDIMode();
	}
	
	private Locale getLocale() {
		return getThreadLocalRequest().getLocale();
	}

	public List<DataContainerGVO> getDataForDatagridFromUpload(String uuid) throws GWTServiceException {
		List<DataContainerGVO> returnValue = null;
		Object o = ApplicationLocalStore.getInstance().retrieve(uuid, uuid);
		ApplicationLocalStore.getInstance().delete(uuid, uuid);
		if (o instanceof List<?>) {
			DataContainerGVO dcg = new SetValueBuiltInRenderer().createContainer(o);
			returnValue = dcg.getListofDC();

		}
		return returnValue;
	}

	public String prepareForExport(List<DataContainerGVO> list, String exportCode, String header, boolean generateColumnHeader) throws GWTServiceException {
		String uuid = DatagridStorageHelper.storeDataGridData(DatagridExportHelper.convert(list), exportCode, header, generateColumnHeader);
		return uuid;
	}

	public void notify(String subject, String message) throws GWTServiceException {
		MessageUtil.notifyMessage(subject, message, getThreadLocalRequest()/*, clientApplication*/);
	}
	
	public void removeWindowsEventData(String windowSession, String windowId) {
		String lookupKey = windowSession + ApplicationLocalStore.OBJECT_DELIMITER + windowId;
		ApplicationLocalStore.getInstance().deleteAll(lookupKey);
	}

	public void removeFileFromLocalStore(String appUUID, String windowId, String uploadUUID) throws GWTServiceException {
		UploadService uploadService = new UploadService();
		uploadService.removeUploadedFile(appUUID, windowId, uploadUUID);
	}
	
	public String generateTypedCSS(String rendererType, String applicationId) throws GWTServiceException {
		String css = CssProvider.getInstance().generateTypedCSS(rendererType, applicationId);
		return css;
	}

}
