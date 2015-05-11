package com.qualogy.qafe.business.resource.rdb.query.call;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.springframework.jdbc.support.nativejdbc.CommonsDbcpNativeJdbcExtractor;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.jdbc.support.nativejdbc.WebLogicNativeJdbcExtractor;

public class NativeConnectionHandler {
    
    private static final Logger LOG = Logger.getLogger(BaseCall.class.getName());

    public static Connection getNativeConnection(Connection conn) throws SQLException {
        Connection nativeConnection = conn;
        NativeJdbcExtractor extractor = findNativeConnectionExtractor();
        LOG.info("Natice Connection Extractor :" + extractor.getClass());
        if (extractor != null) {
            nativeConnection = extractor.getNativeConnection(conn);
        }
        LOG.info("Natice Connection  :" + nativeConnection.getClass());

        return nativeConnection;
    }

    private static NativeJdbcExtractor findNativeConnectionExtractor() {
        NativeJdbcExtractor extractor = null;
        try {
            extractor = new WebLogicNativeJdbcExtractor();
        } catch (Exception e) {
        }

        if (extractor != null) {
            return extractor;
        }
        try {
            extractor = new CommonsDbcpNativeJdbcExtractor();
        } catch (Exception e) {
        }
        return extractor;
    }
}
