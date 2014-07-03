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
package com.qualogy.qafe.bind.core.statement;

import com.qualogy.qafe.bind.commons.type.Parameter;

/**
 * holder class for 'if' or 'switch-case' statements
 * @author 
 *
 */
public class SelectionStatement extends ControlStatement{
	/**
	 * Holder for selection statement properties. A selection statement
	 * can be evaluated by its expression and the right statementresult
	 * can be selected from its results list
	 */
	private static final long serialVersionUID = -5688877613457495925L;

	/**
	 * 
	 */
	protected Parameter expression;

	public Parameter getExpression() {
		return expression;
	}

	public void setExpression(Parameter expression) {
		this.expression = expression;
	}
}
