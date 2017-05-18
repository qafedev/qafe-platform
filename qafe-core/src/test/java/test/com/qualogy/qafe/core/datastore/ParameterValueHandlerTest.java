/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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
package test.com.qualogy.qafe.core.datastore;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import junit.framework.TestCase;

import com.qualogy.qafe.bind.commons.type.In;
import com.qualogy.qafe.bind.commons.type.Parameter;
import com.qualogy.qafe.bind.commons.type.Reference;
import com.qualogy.qafe.bind.commons.type.TypeDefinition;
import com.qualogy.qafe.bind.commons.type.Value;
import com.qualogy.qafe.bind.core.application.ApplicationContext;
import com.qualogy.qafe.bind.core.application.builder.ApplicationContextBuilder;
import com.qualogy.qafe.bind.core.messages.Bundle;
import com.qualogy.qafe.bind.core.messages.Message;
import com.qualogy.qafe.bind.core.messages.Messages;
import com.qualogy.qafe.bind.core.messages.PlaceHolder;
import com.qualogy.qafe.bind.domain.ApplicationMapping;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.core.datastore.DataStore;
import com.qualogy.qafe.core.datastore.ParameterValueHandler;
import com.qualogy.qafe.core.script.ScriptProcessingFailedException;

public class ParameterValueHandlerTest extends TestCase{

	private ApplicationContext context = null;
	
	
	public final static String PLACEHOLDERNAME = "var1";
	public final static String STUPID_PLACEHOLDERNAME = "1";
	
	public void testMessages() throws Exception{
		DataIdentifier dataId = DataStore.register();
		final String MESSAGEKEY = "MESSAGEKEY";
		
		DataStore.store(dataId, DataStore.KEY_LOCALE);
		
		Messages messages = createMessages(MESSAGEKEY, "Displaying someplaceholder [" + PlaceHolder.PREFIX + PLACEHOLDERNAME + PlaceHolder.POSTFIX + "]");
		setMessages(context, messages);
		
		In in = new In(new Reference(MESSAGEKEY, Reference.SOURCE_MESSAGE_ID));
		in.add(new PlaceHolder(PLACEHOLDERNAME, new Reference(PLACEHOLDERNAME)));
		
		DataStore.store(dataId, PLACEHOLDERNAME);
		assertEquals("Displaying someplaceholder []", ParameterValueHandler.get(context, dataId, in));
		
		DataStore.store(dataId, PLACEHOLDERNAME, "ghfd");
		assertEquals("Displaying someplaceholder [ghfd]", ParameterValueHandler.get(context, dataId, in));
		
		DataStore.store(dataId, PLACEHOLDERNAME, "");
		assertEquals("Displaying someplaceholder []", ParameterValueHandler.get(context, dataId, in));
		DataStore.unregister(dataId);
	}

	public void testValue() throws Exception{
		DataIdentifier dataId = DataStore.register();
		In in = new In("somename", new Value("Displaying someplaceholder [" + PlaceHolder.PREFIX + PLACEHOLDERNAME + PlaceHolder.POSTFIX + "]"));
		in.add(new PlaceHolder(PLACEHOLDERNAME, new Reference(PLACEHOLDERNAME)));
		
		DataStore.store(dataId, PLACEHOLDERNAME);
		assertEquals("Displaying someplaceholder []", ParameterValueHandler.get(context, dataId, in));
		
		DataStore.store(dataId, PLACEHOLDERNAME, "ghfd");
		assertEquals("Displaying someplaceholder [ghfd]", ParameterValueHandler.get(context, dataId, in));
		
		DataStore.store(dataId, PLACEHOLDERNAME, "");
		assertEquals("Displaying someplaceholder []", ParameterValueHandler.get(context, dataId, in));
		DataStore.unregister(dataId);
	}
	
	public void testScriptHappyDay1() throws Exception{
		DataIdentifier dataId = DataStore.register();
		In in = new In();
		in.add(new PlaceHolder(PLACEHOLDERNAME, new Reference(PLACEHOLDERNAME)));
		
		in.setExpression("11+1");
		assertEquals("12", ParameterValueHandler.get(context, dataId, in));
		
		in.setExpression("11+1==12");
		assertEquals("1", ParameterValueHandler.get(context, dataId, in));
		
		in.setExpression("2*2");
		assertEquals("4", ParameterValueHandler.get(context, dataId, in));
		DataStore.unregister(dataId);
	}
	
	public void testScriptHappyDay2() throws Exception{
		DataIdentifier dataId = DataStore.register();
		DataStore.store(dataId, PLACEHOLDERNAME, new Integer(2));
		In in = new In();
		in.add(new PlaceHolder(PLACEHOLDERNAME, new Reference(PLACEHOLDERNAME)));
		
		in.setExpression("2/"+ PlaceHolder.PREFIX + PLACEHOLDERNAME + PlaceHolder.POSTFIX);
		assertEquals("1", ParameterValueHandler.get(context, dataId, in));
		in.setExpression("3%"+ PlaceHolder.PREFIX + PLACEHOLDERNAME + PlaceHolder.POSTFIX);
		assertEquals("1", ParameterValueHandler.get(context, dataId, in));
		DataStore.unregister(dataId);
	}
		
	public void testScriptHappyDay3() throws Exception{
		In in = new In();
		DataIdentifier dataId = DataStore.register();
		DataStore.store(dataId, STUPID_PLACEHOLDERNAME, new Integer(8));
		in.setExpression("((1+1)*4)/"+ PlaceHolder.PREFIX + STUPID_PLACEHOLDERNAME + PlaceHolder.POSTFIX);
		assertEquals("1", ParameterValueHandler.get(context, dataId, in));
		
		DataStore.clear(dataId);
		in.setExpression("((1+1)*4)/"+ PlaceHolder.PREFIX + STUPID_PLACEHOLDERNAME + PlaceHolder.POSTFIX);
		in.add(new PlaceHolder(STUPID_PLACEHOLDERNAME, new Value(new TypeDefinition("int"), "8")));
		assertEquals("1", ParameterValueHandler.get(context, dataId, in));
		
		DataStore.clear(dataId);
		DataStore.store(dataId, STUPID_PLACEHOLDERNAME, new BigDecimal(8));
		in.setExpression("(4)*"+ PlaceHolder.PREFIX + STUPID_PLACEHOLDERNAME + PlaceHolder.POSTFIX);
		in.add(new PlaceHolder(STUPID_PLACEHOLDERNAME, new Reference(STUPID_PLACEHOLDERNAME)));
		assertEquals("32", ParameterValueHandler.get(context, dataId, in));
		DataStore.unregister(dataId);
	}
	
	public void testScriptNotSoHappyDay() throws Exception{	
		In in = new In();
		DataIdentifier dataId = DataStore.register();
		
		in.setExpression("${1}+1");
		try{
			ParameterValueHandler.get(context, dataId, in);
			fail("expecting to fail since no placeholders supplied");
		}catch(ScriptProcessingFailedException e){}
		
		in.setExpression("2/0");
		try{
			ParameterValueHandler.get(context, dataId, in);
			fail("expecting to fail since dividing by 0");
			
		}catch(ScriptProcessingFailedException e){}
		DataStore.unregister(dataId);
	}
	
	public void testNonExplicitPlaceholders() throws Exception{	
		In in = new In();
		
		DataIdentifier dataId = DataStore.register();
		
		ParameterValueHandler.store(dataId, new Parameter(new Reference("ref1")), "ref1 value");
		ParameterValueHandler.store(dataId, new Parameter(new Reference("ref2")), "ref2 value");
		in.setValue(new Value("Dit is ref1 met daar achteraan ref2: ${ref1}${ref2}"));
		
		String value = (String)ParameterValueHandler.get(context, dataId, in);
		assertEquals("Dit is ref1 met daar achteraan ref2: ref1 valueref2 value", value);
		
		
		ParameterValueHandler.store(dataId, new Parameter(new Reference("ref1")), "ref1 value");
		ParameterValueHandler.store(dataId, new Parameter(new Reference("ref2")), "ref2 value");
		in.setValue(new Value("${ref1}${ref2} <- dit was ref1 met daar achteraan ref2 ${"));
		
		value = (String)ParameterValueHandler.get(context, dataId, in);
		assertEquals("ref1 valueref2 value <- dit was ref1 met daar achteraan ref2 ${", value);
		DataStore.unregister(dataId);
	}
	
	private void setMessages(ApplicationContext context, Messages messages) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException {
		Field field = ApplicationContext.class.getDeclaredField("messages");
		field.setAccessible(true);
		
		field.set(context, messages);
	}

	private Messages createMessages(String messagekey, String messagevalue) {
		
		Message message = new Message();
		message.put(messagevalue);
		message.setKey(messagekey);
		
		Bundle bundle = new Bundle();
		bundle.put(message);
		
		Messages messages = new Messages();
		messages.put(bundle);
		return messages;
	}

	private ApplicationContext createContext() throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		
		context = ApplicationContextBuilder.build(new ApplicationMapping());
		
		context.performPostProcessing();
		
		context.init();
		
		return context;
	}

	protected void setUp() throws Exception {
		super.setUp();
		context = createContext();
//		dataId = DataStore.register();
	}
	
}
