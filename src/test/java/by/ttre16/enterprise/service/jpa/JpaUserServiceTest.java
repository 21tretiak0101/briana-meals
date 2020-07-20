package by.ttre16.enterprise.service.jpa;

import by.ttre16.enterprise.service.AbstractUserServiceTest;
import org.springframework.test.context.ActiveProfiles;

import static by.ttre16.enterprise.util.ProfileUtil.JPA;

@ActiveProfiles(JPA)
public class JpaUserServiceTest extends AbstractUserServiceTest { }
