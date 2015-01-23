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
package com.qafe.testcase;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Services {
    
    public List<Map<String, Object>> getGridData() {
        String columnId = "ID";
        String columnName = "NAME";
        String row1Name = "Panda organen cake";    
        String row2Name = "rotte appel benzine";
        String row3Name = "Zwavelzuur taart";
        String row4Name = "Antrax melk";
        
        List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();
        
        Map<String, Object> row1 = new HashMap<String, Object>();
        row1.put(columnId, 1);
        row1.put(columnName, row1Name);
        
        Map<String, Object> row2 = new HashMap<String, Object>();
        row2.put(columnId, 2);
        row2.put(columnName, row2Name);
        
        Map<String, Object> row3 = new HashMap<String, Object>();
        row3.put(columnId, 3);
        row3.put(columnName, row3Name);
        
        Map<String, Object> row4 = new HashMap<String, Object>();
        row4.put(columnId, 4);
        row4.put(columnName, row4Name);
        
        tableData.add(row1);
        tableData.add(row2);
        tableData.add(row3);
        tableData.add(row4);
        
        return tableData;
    }
}