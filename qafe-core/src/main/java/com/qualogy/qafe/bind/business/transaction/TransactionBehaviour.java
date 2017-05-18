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
package com.qualogy.qafe.bind.business.transaction;

import java.io.Serializable;

import com.qualogy.qafe.bind.Validatable;
import com.qualogy.qafe.bind.ValidationException;
/**
 * Holder for transaction settings. 
 * @author 
 *
 */
public class TransactionBehaviour implements Serializable, Validatable{

	private static final long serialVersionUID = 3519369953608887272L;
	
	/**
	 * options for management setting
	 */
	public final static String MANAGEMENT_NO = "no";
	public final static String MANAGEMENT_LOCAL = "local";
	public final static String MANAGEMENT_GLOBAL = "global";
	
	/**
	 * Support a current transaction; create a new one if none exists.
	 * Analogous to the EJB transaction attribute of the same name.
	 * <p>This is typically the default setting of a transaction definition,
	 * and typically defines a transaction synchronization scope.
	 */
	public final static String PROPAGATION_REQUIRED = "required";

	/**
	 * Support a current transaction; execute non-transactionally if none exists.
	 * Analogous to the EJB transaction attribute of the same name.
	 * <p><b>NOTE:</b> For transaction managers with transaction synchronization,
	 * <code>PROPAGATION_SUPPORTS</code> is slightly different from no transaction
	 * at all, as it defines a transaction scope that synchronization might apply to.
	 * As a consequence, the same resources (a JDBC <code>Connection</code>, a
	 * Hibernate <code>Session</code>, etc) will be shared for the entire specified
	 * scope. Note that the exact behavior depends on the actual synchronization
	 * configuration of the transaction manager!
	 * <p>In general, use <code>PROPAGATION_SUPPORTS</code> with care! In particular, do
	 * not rely on <code>PROPAGATION_REQUIRED</code> or <code>PROPAGATION_REQUIRES_NEW</code>
	 * <i>within</i> a <code>PROPAGATION_SUPPORTS</code> scope (which may lead to
	 * synchronization conflicts at runtime). If such nesting is unavoidable, make sure
	 * to configure your transaction manager appropriately (typically switching to
	 * "synchronization on actual transaction").
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#setTransactionSynchronization
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#SYNCHRONIZATION_ON_ACTUAL_TRANSACTION
	 */
	public final static String PROPAGATION_SUPPORTS = "supported";

	/**
	 * Support a current transaction; throw an exception if no current transaction
	 * exists. Analogous to the EJB transaction attribute of the same name.
	 * <p>Note that transaction synchronization within a <code>PROPAGATION_MANDATORY</code>
	 * scope will always be driven by the surrounding transaction.
	 */
	public final static String PROPAGATION_MANDATORY = "mandatory";

	/**
	 * Create a new transaction, suspending the current transaction if one exists.
	 * Analogous to the EJB transaction attribute of the same name.
	 * <p><b>NOTE:</b> Actual transaction suspension will not work out-of-the-box
	 * on all transaction managers. This in particular applies to
	 * {@link org.springframework.transaction.jta.JtaTransactionManager},
	 * which requires the <code>javax.transaction.TransactionManager</code>
	 * to be made available it to it (which is server-specific in standard J2EE).
	 * <p>A <code>PROPAGATION_REQUIRES_NEW</code> scope always defines its own
	 * transaction synchronizations. Existing synchronizations will be suspended
	 * and resumed appropriately.
	 * @see org.springframework.transaction.jta.JtaTransactionManager#setTransactionManager
	 */
	public final static String PROPAGATION_REQUIRES_NEW = "requires_new";

	/**
	 * Do not support a current transaction; rather always execute non-transactionally.
	 * Analogous to the EJB transaction attribute of the same name.
	 * <p><b>NOTE:</b> Actual transaction suspension will not work out-of-the-box
	 * on all transaction managers. This in particular applies to
	 * {@link org.springframework.transaction.jta.JtaTransactionManager},
	 * which requires the <code>javax.transaction.TransactionManager</code>
	 * to be made available it to it (which is server-specific in standard J2EE).
	 * <p>Note that transaction synchronization is <i>not</i> available within a
	 * <code>PROPAGATION_NOT_SUPPORTED</code> scope. Existing synchronizations
	 * will be suspended and resumed appropriately.
	 * @see org.springframework.transaction.jta.JtaTransactionManager#setTransactionManager
	 */
	public final static String PROPAGATION_NOT_SUPPORTED = "not_supported";

	/**
	 * Do not support a current transaction; throw an exception if a current transaction
	 * exists. Analogous to the EJB transaction attribute of the same name.
	 * <p>Note that transaction synchronization is <i>not</i> available within a
	 * <code>PROPAGATION_NEVER</code> scope.
	 */
	public final static String PROPAGATION_NEVER = "never";

	/**
	 * Execute within a nested transaction if a current transaction exists,
	 * behave like {@link #PROPAGATION_REQUIRED} else. There is no analogous
	 * feature in EJB.
	 * <p><b>NOTE:</b> Actual creation of a nested transaction will only work on specific
	 * transaction managers. Out of the box, this only applies to the JDBC
	 * {@link org.springframework.jdbc.datasource.DataSourceTransactionManager}
	 * when working on a JDBC 3.0 driver. Some JTA providers might support
	 * nested transactions as well.
	 * @see org.springframework.jdbc.datasource.DataSourceTransactionManager
	 */
	public final static String PROPAGATION_NESTED = "nested";


	/**
	 * Use the default isolation level of the underlying datastore.
	 * All other levels correspond to the JDBC isolation levels.
	 */
	public final static String ISOLATION_DEFAULT = "default";
	
	/**
	 * Indicates that dirty reads, non-repeatable reads and phantom reads
	 * can occur.
	 * <p>This level allows a row changed by one transaction to be read by
	 * another transaction before any changes in that row have been committed
	 * (a "dirty read"). If any of the changes are rolled back, the second
	 * transaction will have retrieved an invalid row.
	 */
	public final static String ISOLATION_READ_UNCOMMITTED = "read_uncommited";

	/**
	 * Indicates that dirty reads are prevented; non-repeatable reads and
	 * phantom reads can occur.
	 * <p>This level only prohibits a transaction from reading a row
	 * with uncommitted changes in it.
	 */
	public final static String ISOLATION_READ_COMMITTED = "read_commited";

	/**
	 * Indicates that dirty reads and non-repeatable reads are prevented;
	 * phantom reads can occur.
	 * <p>This level prohibits a transaction from reading a row with
	 * uncommitted changes in it, and it also prohibits the situation
	 * where one transaction reads a row, a second transaction alters
	 * the row, and the first transaction rereads the row, getting
	 * different values the second time (a "non-repeatable read").
	 */
	public final static String ISOLATION_REPEATABLE_READ = "repeatable_read";

	/**
	 * Indicates that dirty reads, non-repeatable reads and phantom reads
	 * are prevented.
	 * <p>This level includes the prohibitions in
	 * {@link #ISOLATION_REPEATABLE_READ} and further prohibits the
	 * situation where one transaction reads all rows that satisfy a
	 * <code>WHERE</code> condition, a second transaction inserts a
	 * row that satisfies that <code>WHERE</code> condition, and the
	 * first transaction rereads for the same condition, retrieving
	 * the additional "phantom" row in the second read.
	 */
	public final static String ISOLATION_SERIALIZABLE = "serializable";
	
	/**
	 * options for management setting
	 */
	public final static String[] MANAGEMENT_OPTIONS = {
		MANAGEMENT_NO, 
		MANAGEMENT_LOCAL, 
		MANAGEMENT_GLOBAL
	};
	
	/**
	 * options for isolation setting
	 */
	public final static String[] ISOLATION_OPTIONS = {
		ISOLATION_DEFAULT, 
		ISOLATION_READ_COMMITTED, 
		ISOLATION_READ_UNCOMMITTED, 
		ISOLATION_REPEATABLE_READ, 
		ISOLATION_SERIALIZABLE
	};
	
	/**
	 * options for propagation setting
	 */
	public final static String[] PROPAGATION_OPTIONS = {
		PROPAGATION_MANDATORY,
		PROPAGATION_NESTED,
		PROPAGATION_NEVER,
		PROPAGATION_NOT_SUPPORTED,
		PROPAGATION_REQUIRED,
		PROPAGATION_REQUIRES_NEW,
		PROPAGATION_SUPPORTS
	};
	
	/**
	 * default settings
	 */
	public final static String DEFAULT_ISOLATION_SETTING = ISOLATION_DEFAULT;
	public final static String DEFAULT_MANAGEMENT_SETTING = MANAGEMENT_GLOBAL;
	public final static String DEFAULT_PROPAGATION_SETTING = PROPAGATION_REQUIRED;
	/**
	 * Use the default timeout of the underlying transaction system,
	 * or none if timeouts are not supported. 
	 */
	public final static int DEFAULT_TIMEOUT = -1;
	
	/**
	 * Holder for management setting.
	 * Global transactions are managed by the application server, using JTA. 
	 * Local transactions are resource-specific: for example, a transaction associated with a JDBC connection. 
	 * This choice had profound implications. 
	 * Global transactions provide the ability to work with multiple transactional resources. 
	 * (It's worth noting that most applications use a single transaction resource) 
	 * With local transactions, the application server is not involved in transaction management, 
	 * and cannot help ensure correctness across multiple resources. 
	 */
	protected String management;
	
	/**
	 * Holder for propagation setting.
	 * Normally all code executed within a transaction scope will run in that transaction. 
	 * However, there are several options specifying behavior if a transactional method 
	 * is executed when a transaction context already exists: 
	 * For example, simply running in the existing transaction (the most common case); 
	 * or suspending the existing transaction and creating a new transaction
	 */
	protected String propagation;
	
	/**
	 * holder for isolation setting.
	 * isolation: The degree of isolation this transaction has from the work of other transactions. 
	 * For example, can this transaction see uncommitted writes from other transactions?
	 */
	protected String isolation;
	
	/**
	 * Holder for timeout setting.
	 * The degree of isolation this transaction has from the work of other transactions. 
	 * For example, can this transaction see uncommitted writes from other transactions?
	 */
	protected int timeout = -1;

	public TransactionBehaviour() {
		super();
	}
	public TransactionBehaviour(String management, String propagation, String isolation, int timeout) {
		this.management = management;
		this.propagation = propagation;
		this.isolation = isolation;
		this.timeout = timeout;
	}
	/**
	 * Default instance of TransactionBehaviour with all
	 * properties set to its defaults, being
	 * MANAGEMENT_GLOBAL, PROPAGATION_REQUIRED, ISOLATION_DEFAULT, TIMEOUT_DEFAULT
	 */
	public static final TransactionBehaviour createDefault(){
		return new TransactionBehaviour(DEFAULT_MANAGEMENT_SETTING,DEFAULT_PROPAGATION_SETTING,DEFAULT_ISOLATION_SETTING,DEFAULT_TIMEOUT);
	}
	/**
	 * convenience method to check whether to use a local transaction
	 * according to the properties
	 * @return
	 */
	public boolean useLocal() {
		return MANAGEMENT_LOCAL.equals(management);
	}
	
	/**
	 * convenience method to check whether the transaction should behave
	 * managed or not
	 * according to the properties
	 * @return true if the transaction must be managed iow when managed!=MANAGEMENT_NO
	 */
	public boolean isManaged() {
		return !MANAGEMENT_NO.equals(management);
	}
	
	/**
	 * convenience method to check whether to use a global transaction
	 * according to the properties
	 * @return
	 */
	public boolean useGlobal() {
		return MANAGEMENT_GLOBAL.equals(management);
	}
	public String getIsolation() {
		return isolation;
	}
	public void setIsolation(String isolation) {
		this.isolation = isolation;
	}
	public String getManagement() {
		return management;
	}
	public void setManagement(String management) {
		this.management = management;
	}
	public String getPropagation() {
		return propagation;
	}
	public void setPropagation(String propagation) {
		this.propagation = propagation;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public void validate() throws ValidationException {
		boolean found = false;
		for (int i = 0; !found && i < MANAGEMENT_OPTIONS.length; i++) {
			found = (MANAGEMENT_OPTIONS[i].equals(this.management));
		}
		if(!found)
			throw new ValidationException("management option ["+management+"] is not applicable");
		for (int i = 0; !found && i < PROPAGATION_OPTIONS.length; i++) {
			found = (PROPAGATION_OPTIONS[i].equals(this.isolation));
		}
		if(!found)
			throw new ValidationException("propagation option ["+propagation+"] is not applicable");
		for (int i = 0; !found && i < ISOLATION_OPTIONS.length; i++) {
			found = (ISOLATION_OPTIONS[i].equals(this.isolation));
		}
		if(!found)
			throw new ValidationException("isolation option ["+isolation+"] is not applicable");
		
		if (timeout < DEFAULT_TIMEOUT) {
			throw new IllegalArgumentException("Timeout must be a positive integer or TIMEOUT_DEFAULT");
		}
	}
}
