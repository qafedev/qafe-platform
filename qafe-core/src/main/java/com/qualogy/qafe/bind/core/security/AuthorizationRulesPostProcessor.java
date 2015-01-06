/**
 * Copyright 2008-2015 Qualogy Solutions B.V.
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
package com.qualogy.qafe.bind.core.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.qualogy.qafe.bind.business.action.BusinessAction;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.ApplicationStack;
import com.qualogy.qafe.bind.core.pattern.ActionIdPattern;
import com.qualogy.qafe.bind.core.pattern.ApplicationIdPattern;
import com.qualogy.qafe.bind.core.pattern.ComponentIdPattern;
import com.qualogy.qafe.bind.core.pattern.IdPattern;
import com.qualogy.qafe.bind.presentation.component.Component;
import com.qualogy.qafe.bind.presentation.component.Window;
import com.qualogy.qafe.bind.util.InterfaceScanner;

/**
 * @author 
 *
 */
public class AuthorizationRulesPostProcessor {
	
	//if no pattern supplied
	private Map<String, Window> windowCache = new HashMap<String, Window>(); 

	//als geen window id
	private List<AuthorizationControlled> componentCache = new ArrayList<AuthorizationControlled>();
	
	//als windowid en pattern
	private Map<String, List<AuthorizationControlled>> windowComponentCache = new HashMap<String, List<AuthorizationControlled>>();     
	
	//als actionid
	private List<AuthorizationControlled> actionCache = new ArrayList<AuthorizationControlled>();

	//als appid
	private List<AuthorizationControlled> applicationCache = new ArrayList<AuthorizationControlled>();

	
	/**
	 * @param context
	 */
	private void postProcess(ApplicationContext context){
		if(context.getSecuritySettings()!=null){
			initCache(context);
		
		
			List<AuthorizationRule> rules = context.getSecuritySettings().getAuthorizationRules();
			if(rules!=null){
				for (Iterator<AuthorizationRule> iter = rules.listIterator(); iter.hasNext();) {
					applyRuleOnContext((AuthorizationRule) iter.next());
				}
			}
		}
	}
	/**
	 * method is not static because of dynamic cache
	 * @param stack
	 */
	public void postProcess(ApplicationStack stack){
		if(stack.getSecuritySettings()!=null){
			initCache(stack);
			
			List<AuthorizationRule> rules = stack.getSecuritySettings().getAuthorizationRules();
			if(rules!=null){
				for (Iterator<AuthorizationRule> iter = rules.listIterator(); iter.hasNext();) {
					applyRuleOnStack((AuthorizationRule) iter.next());
				}
			}
			for (Iterator<ApplicationContext> iter = stack.getApplicationsIterator(); iter.hasNext();) {
				postProcess((ApplicationContext) iter.next());
				
			}
		}
	}
	/**
	 * init cache for stack processing
	 */
	@SuppressWarnings("unchecked")
	private void initCache(ApplicationStack stack){
		for (Iterator iter = stack.getApplicationsIterator(); iter.hasNext();) {
			applicationCache.add((ApplicationContext) iter.next());
		}
	}
	/**
	 * init cache for context processing
	 */
	@SuppressWarnings("unchecked")
	private void initCache(ApplicationContext context){
		List<BusinessAction> businessActions = InterfaceScanner.scan(context, BusinessAction.class);
		for (Iterator iter = businessActions.iterator(); iter.hasNext();) {
			BusinessAction action = (BusinessAction) iter.next();
			actionCache.add(action);
		}
		
		if (context.getApplicationMapping() != null) {
			List<Window> windows = context.getApplicationMapping().getPresentationTier().getView().getWindows();
			for (Iterator iter = windows.iterator(); iter.hasNext();) {
				Window window = (Window) iter.next();
				windowCache.put(window.getId(), window);
				componentCache = InterfaceScanner.scan(context, Component.class, new Class[]{Window.class});
				windowComponentCache.put(window.getId(), componentCache);
			}
		}
	}
	
	private void applyRuleOnContext(AuthorizationRule rule) {
		
		List <IdPattern> baPatterns = rule.getFilteredAndPrioritizedPatterns(ActionIdPattern.class);
		for (Iterator<IdPattern> iter = baPatterns.iterator(); iter.hasNext();) {
			ActionIdPattern pattern = (ActionIdPattern) iter.next();
			doPatternMatchAndApplyRule(pattern, rule, actionCache);
		}
		
		List <IdPattern> componentPatterns = rule.getFilteredAndPrioritizedPatterns(ComponentIdPattern.class);
		for (Iterator<IdPattern> iter = componentPatterns.iterator(); iter.hasNext();) {
			ComponentIdPattern pattern = (ComponentIdPattern) iter.next();
			
			if(pattern.isEmpty() && pattern.getWindowId()!=null){
				Window window = windowCache.get(pattern.getWindowId());
				List<AuthorizationControlled> tmp = new ArrayList<AuthorizationControlled>();
				tmp.add(window);
				doPatternMatchAndApplyRule(pattern, rule, tmp);
			}else if(!pattern.isEmpty() && pattern.getWindowId()!=null){
				doPatternMatchAndApplyRule(pattern, rule, windowComponentCache.get(pattern.getWindowId()));
			}else if(!pattern.isEmpty()){
				doPatternMatchAndApplyRule(pattern, rule, componentCache);
			}
		}
	}
	
	private void applyRuleOnStack(AuthorizationRule rule) {
		List <IdPattern> patterns = rule.getIdPatterns();
		for (Iterator<IdPattern> iter = patterns.iterator(); iter.hasNext();) {
			ApplicationIdPattern pattern = (ApplicationIdPattern) iter.next();
			doPatternMatchAndApplyRule(pattern, rule, applicationCache);
		}
	}
	
	private void doPatternMatchAndApplyRule(IdPattern pattern, AuthorizationRule rule, List<AuthorizationControlled> controlledObjects){
		for (Iterator<AuthorizationControlled> iterator = controlledObjects.iterator(); iterator.hasNext();) {
			AuthorizationControlled controlled = (AuthorizationControlled)iterator.next();
			if(controlled.getControllingAuthorizationRule()==null && pattern.matches(controlled.getId().toString())){
				controlled.setControllingAuthorizationRule(rule);
			}
		}
	}
}
