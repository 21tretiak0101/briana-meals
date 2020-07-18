package by.ttre16.enterprise.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.jcache.JCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public CacheManager cacheManager() throws IOException {
        JCacheCacheManager jCacheCacheManager = new JCacheCacheManager();
        jCacheCacheManager.setCacheManager(cacheManagerFactory().getObject());
        return jCacheCacheManager;
    }

    @Bean
    public JCacheManagerFactoryBean cacheManagerFactory() throws IOException {
        JCacheManagerFactoryBean jCacheManagerFactoryBean =
                new JCacheManagerFactoryBean();
        jCacheManagerFactoryBean.setCacheManagerUri(
                new ClassPathResource("cache/ehcache.xml").getURI());
        return jCacheManagerFactoryBean;
    }
}
