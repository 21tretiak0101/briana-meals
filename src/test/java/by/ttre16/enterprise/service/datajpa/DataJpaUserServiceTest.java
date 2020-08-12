package by.ttre16.enterprise.service.datajpa;

import by.ttre16.enterprise.service.AbstractUserServiceTest;
import org.springframework.test.context.ActiveProfiles;

import static by.ttre16.enterprise.util.profile.ProfileUtil.DATA_JPA;

@ActiveProfiles(DATA_JPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest { }
