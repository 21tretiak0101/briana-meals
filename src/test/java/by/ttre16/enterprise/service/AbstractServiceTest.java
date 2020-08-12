package by.ttre16.enterprise.service;

import by.ttre16.enterprise.AbstractTest;
import by.ttre16.enterprise.configuration.root.RootContextConfiguration;
import by.ttre16.enterprise.service.util.TimingRules;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static by.ttre16.enterprise.util.profile.ProfileUtil.*;

@ContextConfiguration(classes = {RootContextConfiguration.class})
@ActiveProfiles(TEST)
public abstract class AbstractServiceTest extends AbstractTest {
    @Rule
    public Stopwatch stopwatch = TimingRules.STOPWATCH;
    @ClassRule
    public static ExternalResource summary = TimingRules.SUMMARY;
}
