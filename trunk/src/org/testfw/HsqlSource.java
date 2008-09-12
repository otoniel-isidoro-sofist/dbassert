/**
 * @(#) FakeSource.java;
 * <p/>
 * Created on May 27, 2008
 * AUTHOR    ** Danil Glinenko
 * EMAIL     ** dglinenko@gmail.com
 * <p/>
 *
 * Copyright (C) 2008 Danil Glinenko. All rights reserved.
 */

package org.testfw;


import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.StringTokenizer;

public class HsqlSource extends DbSource {
    public HsqlSource(final String sourceName, final SourceSet sourceSet, final Class invoker) {
        super(sourceName, sourceSet, invoker);
    }


    public void loadSchemaFile(final String schemaFileName) {
        try {
            final String sqlSchema = loadSQLFromFile(schemaFileName);
            applySql(sqlSchema);
        }
        catch (SQLException e) {
            throw new RuntimeException("Unable to apply schema file: " + schemaFileName + " check your SQL schema file.", e);
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to load schema file: " + schemaFileName, e);
        }
    }

    void setInvoker(final Class invokerClass) {
        this.invokerClass = invokerClass;
    }

    private String loadSQLFromFile(final String fileName) throws IOException {
        //load file and return its contents as String
        final InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        if (file != null) {
            final byte[] b = new byte[file.available()];
            file.read(b);
            file.close();
            return new String(b);
        } else {
            throw new RuntimeException("File: " + fileName + " cannot be found or loaded:");
        }
    }


    /**
     * Runs SQL statements on a given sql connection object.
     *
     * @param sql        SQL statements to run.
     * @throws SQLException if unable to run SQL statements.
     */
    private void applySql(final String sql) throws SQLException {
        final StringTokenizer st = new StringTokenizer(sql, ";");
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if (!token.equals("")) {
                this.runSqlStatement(token += ";", false);
            }
        }
    }

    //**** Only for jUnit testing purpose
    public class TestDoor {
        public String $loadSqlFromFile(final String fileName) throws IOException {
            return loadSQLFromFile(fileName);
        }
    }
}