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
package com.qualogy.qafe.business.resource.rdb.query.utilities.sql;

import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.expression.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class SelectItemFinder  extends AbstractItemFinder
        implements StatementFinder, SelectVisitor, FromItemVisitor, ExpressionVisitor, ItemsListVisitor
         {
    private Select select;

    public SelectItemFinder(Select select){
        this.select = select;
        setTableList();
    }

    public List getTableList() {
		return tables;
	}

    public void setTableList(){
        select.getSelectBody().accept(this);
    }
    public List getColumns(){
       return columns;
    }

    public void visitColumns(PlainSelect plainSelect){
        columns = plainSelect.getSelectItems();
    }

    public void visit(PlainSelect plainSelect) {
		plainSelect.getFromItem().accept(this);

		if (plainSelect.getJoins() != null) {
			for (Iterator joinsIt = plainSelect.getJoins().iterator(); joinsIt.hasNext();) {
				Join join = (Join) joinsIt.next();
				join.getRightItem().accept(this);
			}
		}
		if (plainSelect.getWhere() != null)
			plainSelect.getWhere().accept(this);

       visitColumns(plainSelect);
    }

	public void visit(Union union) {
		for (Iterator iter = union.getPlainSelects().iterator(); iter.hasNext();) {
			PlainSelect plainSelect = (PlainSelect) iter.next();
			visit(plainSelect);
		}
	}

	public void visit(Table tableName) {
		String tableWholeName = tableName.getWholeTableName();
		tables.add(tableWholeName);
	}

	public void visit(SubSelect subSelect) {
		subSelect.getSelectBody().accept(this);
	}

	public void visit(Between between) {
		between.getLeftExpression().accept(this);
		between.getBetweenExpressionStart().accept(this);
		between.getBetweenExpressionEnd().accept(this);
	}

     public void visit(InExpression inExpression) {
         inExpression.getLeftExpression().accept(this);
         inExpression.getItemsList().accept(this);
     }

     public void visit(InverseExpression inverseExpression) {
         inverseExpression.getExpression().accept(this);
     }

     public void visit(ExistsExpression existsExpression) {
		existsExpression.getRightExpression().accept(this);
	}

    public void visit(Parenthesis parenthesis) {
		parenthesis.getExpression().accept(this);
	}
	public void visitBinaryExpression(BinaryExpression binaryExpression) {
		binaryExpression.getLeftExpression().accept(this);
		binaryExpression.getRightExpression().accept(this);
	}

	public void visit(ExpressionList expressionList) {
		for (Iterator iter = expressionList.getExpressions().iterator(); iter.hasNext();) {
			Expression expression = (Expression) iter.next();
			expression.accept(this);
		}
	}

	public void visit(AllComparisonExpression allComparisonExpression) {
		allComparisonExpression.GetSubSelect().getSelectBody().accept(this);
	}

	public void visit(AnyComparisonExpression anyComparisonExpression) {
		anyComparisonExpression.GetSubSelect().getSelectBody().accept(this);
	}

	public void visit(SubJoin subjoin) {
		subjoin.getLeft().accept(this);
		subjoin.getJoin().getRightItem().accept(this);
	}

     public void visit(DateValue dateValue) {
     }

     public void visit(TimestampValue timestampValue) {
     }

     public void visit(TimeValue timeValue) {
     }

     public void visit(CaseExpression caseExpression) {
     }

     public void visit(WhenClause whenClause) {
     }

     public void visit(Addition addition) {
          visitBinaryExpression(addition);
     }

     public void visit(AndExpression andExpression) {
         visitBinaryExpression(andExpression);
     }

     public void visit(Column tableColumn) {
     }

     public void visit(Division division) {
         visitBinaryExpression(division);
     }

     public void visit(DoubleValue doubleValue) {
     }

     public void visit(EqualsTo equalsTo) {
         visitBinaryExpression(equalsTo);
     }

     public void visit(Function function) {
     }

     public void visit(GreaterThan greaterThan) {
         visitBinaryExpression(greaterThan);
     }

     public void visit(GreaterThanEquals greaterThanEquals) {
         visitBinaryExpression(greaterThanEquals);
     }

     public void visit(IsNullExpression isNullExpression) {
     }

     public void visit(JdbcParameter jdbcParameter) {
     }

     public void visit(LikeExpression likeExpression) {
         visitBinaryExpression(likeExpression);
     }

     public void visit(LongValue longValue) {
     }

     public void visit(MinorThan minorThan) {
         visitBinaryExpression(minorThan);
     }

     public void visit(MinorThanEquals minorThanEquals) {
         visitBinaryExpression(minorThanEquals);
     }

     public void visit(Multiplication multiplication) {
         visitBinaryExpression(multiplication);
     }

     public void visit(NotEqualsTo notEqualsTo) {
         visitBinaryExpression(notEqualsTo);
     }

     public void visit(NullValue nullValue) {
     }

     public void visit(OrExpression orExpression) {
         visitBinaryExpression(orExpression);
     }

      public void visit(StringValue stringValue) {
      }

      public void visit(Subtraction subtraction) {
          visitBinaryExpression(subtraction);
      }
             

}