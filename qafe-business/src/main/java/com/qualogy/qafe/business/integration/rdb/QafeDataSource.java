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
package com.qualogy.qafe.business.integration.rdb;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.qualogy.qafe.business.resource.rdb.DataSourceConnectionFactory;
import com.qualogy.qafe.business.resource.rdb.RDBDatasource;
import com.qualogy.qafe.core.datastore.DataIdentifier;
import com.qualogy.qafe.util.ExceptionHelper;

/**
 *
 */
public final class QafeDataSource implements DataSource {
    
    private static final Logger LOG = Logger.getLogger(QafeDataSource.class.getName());
    
	private final DataSource dataSource;
	private final boolean isProxyConnection;
	private final DataIdentifier dataId;

	public QafeDataSource(final DataSource ds, final boolean isProxyConnection,
			final DataIdentifier dataId) {
		this.dataSource = ds;
		this.isProxyConnection = isProxyConnection;
		this.dataId = dataId;
	}
	
	public QafeDataSource(final RDBDatasource rdbDatasource, final DataIdentifier dataId) {
		this(rdbDatasource.getDataSource(), rdbDatasource.getResource().isProxyConnection(), dataId);
		//rdbDatasource.setDataSource(this);
	}
	/**
	 * {@inheritDoc}
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = dataSource.getConnection();

		if (isProxyConnection) {
			conn = DataSourceConnectionFactory.getProxyConnection(dataSource,
					dataId);
		}

		return conn;
	}

	/**
	 * {@inheritDoc}
	 */
	public Connection getConnection(final String username, final String password)
			throws SQLException {
		Connection conn = dataSource.getConnection(username, password);

		if (isProxyConnection) {
			conn = DataSourceConnectionFactory.getProxyConnection(dataSource,
					dataId);
		}

		return conn;
	}

	/**
	 * {@inheritDoc}
	 */
	public PrintWriter getLogWriter() throws SQLException {
		return dataSource.getLogWriter();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLogWriter(final PrintWriter out) throws SQLException {
		dataSource.setLogWriter(out);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLoginTimeout(final int seconds) throws SQLException {
		dataSource.setLoginTimeout(seconds);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getLoginTimeout() throws SQLException {
		return dataSource.getLoginTimeout();
	}

	public <T> T unwrap(final Class<T> iface) throws SQLException {
		Method m =null;
		try {
			m = dataSource.getClass().getMethod("unwrap", Class.class);
			if (m != null) {
				return (T) m.invoke(dataSource, iface);

			} else {
				return null;
			}
		} catch (final SecurityException e) {
			LOG.log(Level.SEVERE,ExceptionHelper.printStackTrace(e));
		} catch (final NoSuchMethodException e) {
			LOG.log(Level.SEVERE,ExceptionHelper.printStackTrace(e));
		} catch (final IllegalArgumentException e) {
			LOG.log(Level.SEVERE,ExceptionHelper.printStackTrace(e));
		} catch (final IllegalAccessException e) {
			LOG.log(Level.SEVERE,ExceptionHelper.printStackTrace(e));
		} catch (final InvocationTargetException e) {
			LOG.log(Level.SEVERE,ExceptionHelper.printStackTrace(e));
		}
		return null;

	}

	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		Method m =null;
		try {
			m = dataSource.getClass().getMethod("isWrapperFor", Class.class);
			if (m != null) {
				return (Boolean) m.invoke(dataSource, iface);

			} else {
				return false;
			}
		} catch (final SecurityException e) {
			LOG.log(Level.SEVERE,ExceptionHelper.printStackTrace(e));
		} catch (final NoSuchMethodException e) {
			LOG.log(Level.SEVERE,ExceptionHelper.printStackTrace(e));
		} catch (final IllegalArgumentException e) {
			LOG.log(Level.SEVERE,ExceptionHelper.printStackTrace(e));
		} catch (final IllegalAccessException e) {
			LOG.log(Level.SEVERE,ExceptionHelper.printStackTrace(e));
		} catch (final InvocationTargetException e) {
			LOG.log(Level.SEVERE,ExceptionHelper.printStackTrace(e));
		}
		return false;		
	}
	
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
	    return null;
	}
}
