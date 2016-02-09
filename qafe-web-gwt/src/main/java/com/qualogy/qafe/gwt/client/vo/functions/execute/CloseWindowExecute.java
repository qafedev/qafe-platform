/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
package com.qualogy.qafe.gwt.client.vo.functions.execute;

import com.qualogy.qafe.gwt.client.context.ClientApplicationContext;
import com.qualogy.qafe.gwt.client.factory.WindowFactory;
import com.qualogy.qafe.gwt.client.vo.functions.BuiltInFunctionGVO;
import com.qualogy.qafe.gwt.client.vo.functions.CloseWindowGVO;

@Deprecated
public class CloseWindowExecute implements ExecuteCommand {


	public void execute(BuiltInFunctionGVO builtInFunction) {
		if (builtInFunction instanceof CloseWindowGVO) {
			CloseWindowGVO closeWindow = (CloseWindowGVO)builtInFunction;
			if (closeWindow.getWindow()!=null && closeWindow.getWindow().length()!=0){
				if (ClientApplicationContext.getInstance().isMDI()){
					ClientApplicationContext.getInstance().removeWindow(closeWindow.getWindow(), closeWindow.getContext(), closeWindow.getUuid());
					
				} else {
					WindowFactory.clearWidgetFromMainPanel();
					//closeBrowser();
				}
				
			} 
		}
		FunctionsExecutor.setProcessedBuiltIn(true);
	}
	
    /** 
     * Close browser window. 
     */ 
    public static native void closeBrowser() 
    /*-{ 
        $wnd.close(); 
    }-*/; 

}
