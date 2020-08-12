package by.ttre16.enterprise;

import org.junit.runner.RunWith;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Sql(value = "classpath:populate.sql",
    config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
public abstract class AbstractTest { }
