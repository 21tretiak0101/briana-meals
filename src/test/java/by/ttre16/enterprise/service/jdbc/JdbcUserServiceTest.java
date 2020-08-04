package by.ttre16.enterprise.service.jdbc;

import by.ttre16.enterprise.service.AbstractUserServiceTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import static by.ttre16.enterprise.util.ProfileUtil.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest { }
