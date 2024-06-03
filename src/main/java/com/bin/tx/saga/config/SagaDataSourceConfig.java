package com.bin.tx.saga.config;

import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import javax.sql.DataSource;

public class SagaDataSourceConfig {





    public static void initDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(SagaConfig.getProp("datasource.saga.url"));
        ds.setDriverClassName(SagaConfig.getProp("datasource.saga.driverClassName"));
        ds.setUsername(SagaConfig.getProp("datasource.saga.username"));
        ds.setPassword(SagaConfig.getProp("datasource.saga.password"));
        String minIdle = SagaConfig.getProp("datasource.saga.minIdle");
        ds.setMinimumIdle(Integer.parseInt(minIdle));
        String maxPoolSize = SagaConfig.getProp("datasource.saga.maxPoolSize");
        ds.setMaximumPoolSize(Integer.parseInt(maxPoolSize));


        Jdbi jdbi = createJdbi(ds);
        if (globalJdbi == null) {
            globalJdbi = jdbi;
        }
    }



    private static Jdbi createJdbi(DataSource dataSource) {

        Jdbi jdbi = Jdbi.create(dataSource);

        return jdbi;
    }



    private static Jdbi globalJdbi;



    public static Jdbi getJdbi() {
        return globalJdbi;
    }
}
