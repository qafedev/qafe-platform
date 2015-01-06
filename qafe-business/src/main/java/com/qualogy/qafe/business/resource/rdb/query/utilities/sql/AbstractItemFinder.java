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
package com.qualogy.qafe.business.resource.rdb.query.utilities.sql;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.Statement;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


public abstract class AbstractItemFinder {
    protected List<String> tables = new ArrayList<String>();
    protected List columns = new ArrayList();
    protected Statement statement;

    protected List getColumns(){
       return columns;
    }

    protected void visitColumns(PlainSelect plainSelect){
        columns = plainSelect.getSelectItems();
    }

    public void setTables(List<String> tables){
        this.tables = tables;        
    }

    protected void addTable(String tableName){
        tables.add(tableName);        
    }
    
}
