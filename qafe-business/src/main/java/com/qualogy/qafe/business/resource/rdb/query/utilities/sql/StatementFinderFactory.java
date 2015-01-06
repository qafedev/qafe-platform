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

import java.io.StringReader;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;


public class StatementFinderFactory {

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws JSQLParserException
	 */
    public static StatementFinder getStatementFinder(String sql) {

        CCJSqlParserManager pm = new CCJSqlParserManager();
        net.sf.jsqlparser.statement.Statement statement = null;
        StatementFinder statementFinder = null;
        try {
            statement = pm.parse(new StringReader(sql));
            if (statement instanceof Select) {
                Select selectStatement = (Select) statement;
                statementFinder = new SelectItemFinder(selectStatement);
            } else if (statement instanceof Insert) {
                Insert insertStatement = (Insert) statement;
                statementFinder = new InsertItemFinder(insertStatement);
            }
            else if (statement instanceof Delete) {
                Delete deleteStatement = (Delete) statement;
                statementFinder = new DeleteItemFinder(deleteStatement);
            } else if (statement instanceof Update) {
                Update updateStatement = (Update) statement;
                statementFinder = new UpdateItemFinder(updateStatement);
            }
        } catch (JSQLParserException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return statementFinder;
    }

}
