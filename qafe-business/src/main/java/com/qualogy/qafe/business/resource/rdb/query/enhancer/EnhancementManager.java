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
package com.qualogy.qafe.business.resource.rdb.query.enhancer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.qualogy.qafe.bind.resource.query.Batch;
import com.qualogy.qafe.bind.resource.query.Query;
import com.qualogy.qafe.bind.resource.query.QueryContainer;
import com.qualogy.qafe.business.resource.rdb.RDBDatasource;

public class EnhancementManager {
	public final static Logger logger = Logger
			.getLogger(EnhancementManager.class.getName());

	private static Query enhance(Query query, QueryContainer container,
			DatabaseMetaData md) {
		if (StringUtils.isNotBlank(query.getRef())) {
			Query reference = container.get(query.getRef());

			if (reference == null) {
				throw new EnhancementFailedException(
						"Referencing to non-existing query, ref="
								+ query.getRef());
			}

			query.setReference(reference);
		} else {
			Enhancer enhancer = EnhancerFactory.create(query);

			if (enhancer != null) {
				query = enhancer.enhance(query, md);
				logger.info("Enhanced query with id [" + query.getId() + "]");
			}
		}

		return query;
	}

	public static QueryContainer enhance(QueryContainer container,
			RDBDatasource dsResource) {
		if ((dsResource == null) || (dsResource.getDataSource() == null)) {
			throw new IllegalArgumentException(
					"Properties not read correctly or properties are incorrect, loading datasource failed");
		}

		DataSource dataSource = dsResource.getDataSource();

		Connection con = null;

		try {
			con = dataSource.getConnection();

			DatabaseMetaData md = con.getMetaData();

			for (Iterator<Query> iter = container.values().iterator(); iter
					.hasNext();) {
				Query query = (Query) iter.next();

				if (query instanceof Batch) {
					for (Iterator<Query> iterator = ((Batch) query)
							.getQueries().iterator(); iterator.hasNext();) {
						Query batchQuery = (Query) iterator.next();
						batchQuery = enhance(batchQuery, container, md);
					}
				} else {
					query = enhance(query, container, md);
				}

				container.update(query);
			}
		} catch (SQLException e) {
			String error = e.getMessage() + "[ on resource id: "
					+ dsResource.getResourceId() + "]";
			throw new EnhancementFailedException(error);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					String error = e.getMessage() + "[ on resource id: "
							+ dsResource.getResourceId() + "]";
					throw new EnhancementFailedException(error);
				}
			}
		}

		return container;
	}

	public static Query enhance(Query query, RDBDatasource dsResource) {
		QueryContainer container = new QueryContainer();

		return enhance(container, dsResource).get(query.getId());
	}
}
