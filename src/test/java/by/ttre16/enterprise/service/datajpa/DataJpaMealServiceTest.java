package by.ttre16.enterprise.service.datajpa;

import by.ttre16.enterprise.service.AbstractMealServiceTest;
import org.springframework.test.context.ActiveProfiles;

import static by.ttre16.enterprise.util.ProfileUtil.DATA_JPA;

@ActiveProfiles(DATA_JPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest { }
