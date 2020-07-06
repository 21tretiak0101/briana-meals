package by.ttre16.enterprise.service;

import by.ttre16.enterprise.configuration.ApplicationConfiguration;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@Sql(value = "classpath:populate.sql",
    config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.DEFAULT))
public abstract class AbstractServiceTest { }
