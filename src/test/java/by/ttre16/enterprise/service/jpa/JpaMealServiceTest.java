package by.ttre16.enterprise.service.jpa;

import by.ttre16.enterprise.service.AbstractMealServiceTest;
import org.springframework.test.context.ActiveProfiles;

import static by.ttre16.enterprise.util.profile.ProfileUtil.JPA;

@ActiveProfiles(JPA)
public class JpaMealServiceTest extends AbstractMealServiceTest { }
