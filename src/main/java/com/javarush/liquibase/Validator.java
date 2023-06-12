package com.javarush.liquibase;

import com.javarush.liquibase.dataSource.ConnectionData;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;


import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;


public class Validator {
    public void liquibaseConnector() throws Exception{

        Map<String, Object> config = new HashMap<>();

        Connection connection = ConnectionData.getConnection();

        try (connection) {
            Scope.child(config, () -> {

                Database database = DatabaseFactory
                        .getInstance()
                        .findCorrectDatabaseImplementation(new JdbcConnection(connection));

                Liquibase liquibase = new liquibase
                        .Liquibase("changelog.xml", new ClassLoaderResourceAccessor(), database);

                liquibase.update(new Contexts(), new LabelExpression());
            });
        }


    }
}
