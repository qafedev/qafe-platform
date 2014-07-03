package com.qualogy.qafe.business.resource.rdb.query;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.Configuration;
import com.qualogy.qafe.bind.integration.service.Method;
import com.qualogy.qafe.bind.integration.service.Service;
import com.qualogy.qafe.bind.io.FileLocation;
import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.bind.resource.DatasourceBindResource;
import com.qualogy.qafe.bind.resource.query.Query;
import com.qualogy.qafe.bind.resource.query.QueryContainer;
import com.qualogy.qafe.bind.resource.query.SQLQuery;
import com.qualogy.qafe.business.integration.filter.page.Page;
import com.qualogy.qafe.business.integration.rdb.RDBServiceProcessor;
import com.qualogy.qafe.business.resource.rdb.DriverManagerDataSource;
import com.qualogy.qafe.business.resource.rdb.JNDIDatasource;
import com.qualogy.qafe.business.resource.rdb.RDBDatasource;
import com.qualogy.qafe.business.resource.rdb.query.enhancer.EnhancementManager;

public aspect StatementCompiler {

	private final static Log logger = LogFactory.getLog(StatementCompiler.class);
	
	pointcut initDatasource(final ApplicationContext context):
		execution(* DriverManagerDataSource.init(ApplicationContext)) && args(context)|| execution(* JNDIDatasource.init(ApplicationContext)) && args(context);
	
	/**
	 * method initialzes this resource by reading the statement config files
	 * and binding them via a reader to a statements conatiner based upon
	 * the given properties.
	 */
	after(final ApplicationContext context) returning() : initDatasource(context){
		
		if(context==null)
			throw new IllegalArgumentException("cannot initialize without context");

		DatasourceBindResource resource = ((RDBDatasource)thisJoinPoint.getTarget()).getResource();
		
		List<FileLocation> fileLocationList = new ArrayList<FileLocation>();
		FileLocation stmtFileNameTemp = resource.getStatementsFileUrl();
		if(stmtFileNameTemp != null){
			if(stmtFileNameTemp.getLocation().contains(",")){
				String[] stmtFiles = resource.getStatementsFileUrl().getLocation().split(",");
				for(String stmtFile: stmtFiles){
					String root = stmtFileNameTemp.getRoot();
					FileLocation fileLocation = null;
					if(root == null && stmtFile.startsWith(FileLocation.SCHEME_FILE)) {
						fileLocation = new FileLocation(stmtFile);
					} 
					else {
						root = context.getRoot();
						fileLocation = new FileLocation(StringUtils.trim(root), StringUtils.trim(stmtFile));
					}
					fileLocationList.add(fileLocation);
				}
			} else {
				fileLocationList.add(resource.getStatementsFileUrl());
			}
		}
		
		QueryContainer container = null;
		boolean validating = Boolean.valueOf(context.getConfigurationItem(Configuration.QAFE_XML_VALIDATION)).booleanValue();
		
		for(FileLocation stmtFileName : fileLocationList) {
			if(stmtFileName==null){
				throw new IllegalArgumentException("DBSTATEMENTS_FILE_URL property not set");
			}
			
			URI uri = null;
			String stmtFileLocation = stmtFileName.getLocation();
			if( (stmtFileLocation.startsWith(FileLocation.SCHEME_HTTP + FileLocation.COMMON_SCHEME_DELIM)) || (stmtFileLocation.startsWith(FileLocation.SCHEME_FILE)) ) {
				uri = stmtFileName.toURI();
			} else {
				logger.debug(" Statement [" + stmtFileName + "] file could not be found. Now iterating the filelocations in the applicationmapping" );
				List<FileLocation> list = context.getMappingFileLocations();
				if(list != null) {
					for (FileLocation fileLocation : list) {
						logger.debug("Trying Statement [" + fileLocation.getLocation()+ File.separator + stmtFileName + "] file could not be found. Now iterating the filelocations in the applicationmapping" );
						String baseLocation = "";
						if(FilenameUtils.indexOfExtension(fileLocation.getLocation()) == -1) {
							baseLocation = fileLocation.getLocation();
						} else {
							baseLocation= FilenameUtils.getPath(fileLocation.getLocation());
						}
						String newpath = context.getRoot() + baseLocation + File.separator + stmtFileName.getLocation();
						FileLocation fileLoc = new FileLocation(context.getRoot(), baseLocation + File.separator + stmtFileName.getLocation());
						uri = fileLoc.toURI();	
						if(uri != null){
							break;
						}
					}
				} else {	
					uri = stmtFileName.toURI();
				}
			}
			
//			if (uri==null){
//				String uriString = context.getOriginAppConfigFileLocation().toString() + stmtFileName;
//				System.err.println(uriString);
//				uri = new FileLocation(uriString).toURI();
//			}
					
			logger.debug("loading from [" + ((uri!=null) ? uri.toString() : "<empty URI>") + "]");
			
			if (uri == null) {
				throw new IllegalArgumentException("DBSTATEMENTS_FILE_URL statements not found " + stmtFileName.getLocation());
			}
			if (container == null) {
				container = (QueryContainer)new Reader(QueryContainer.class, validating).read(uri);
			} else {
				QueryContainer containerTemp = (QueryContainer)new Reader(QueryContainer.class, validating).read(uri);
				Collection<Query> queries = containerTemp.values();
				for(Query query: queries) {
					container.put(query);
				}
			}
		}
		
		container = EnhancementManager.enhance(container, (RDBDatasource)thisJoinPoint.getThis());
		((RDBDatasource)thisJoinPoint.getThis()).setQueryContainer(container);
		fileLocationList.clear();
		
		((RDBDatasource)thisJoinPoint.getThis()).validate();
	}
	
	after(final String queryName) returning (Query query) : 
		call(Query RDBDatasource.get(String)) && args(queryName) && 
		withincode(* RDBServiceProcessor.execute(ApplicationContext, Service, Method, Map, Page)){
		
		if(query instanceof SQLQuery && query.containsErrors()){
	    	query = EnhancementManager.enhance(query, (RDBDatasource)thisJoinPoint.getTarget());
	    	((RDBDatasource)thisJoinPoint.getTarget()).put(query);//eventhough not necessary since enhancement is by reference
	    }
	}
}
