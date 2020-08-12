package by.ttre16.enterprise;

import by.ttre16.enterprise.service.util.TimingRules;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import static by.ttre16.enterprise.util.profile.ProfileUtil.TEST;

@RunWith(SpringRunner.class)
@Sql(value = "classpath:populate.sql",
    config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
@ActiveProfiles(TEST)
public abstract class AbstractTest {
    @Rule
    public Stopwatch stopwatch = TimingRules.STOPWATCH;
    @ClassRule
    public static ExternalResource summary = TimingRules.SUMMARY;
}
