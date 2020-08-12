package by.ttre16.enterprise.service;

import by.ttre16.enterprise.AbstractTest;
import by.ttre16.enterprise.configuration.root.RootContextConfiguration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {RootContextConfiguration.class})
public abstract class AbstractServiceTest extends AbstractTest { }
