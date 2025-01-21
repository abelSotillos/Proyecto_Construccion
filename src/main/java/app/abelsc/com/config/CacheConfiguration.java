package app.abelsc.com.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, app.abelsc.com.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, app.abelsc.com.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, app.abelsc.com.domain.Authority.class.getName());
            createCache(cm, app.abelsc.com.domain.Cliente.class.getName());
            createCache(cm, app.abelsc.com.domain.Empleado.class.getName());
            createCache(cm, app.abelsc.com.domain.EmpleadoObra.class.getName());
            createCache(cm, app.abelsc.com.domain.Empresa.class.getName());
            createCache(cm, app.abelsc.com.domain.Maquinaria.class.getName());
            createCache(cm, app.abelsc.com.domain.MaquinariaObra.class.getName());
            createCache(cm, app.abelsc.com.domain.Material.class.getName());
            createCache(cm, app.abelsc.com.domain.MaterialObra.class.getName());
            createCache(cm, app.abelsc.com.domain.Obra.class.getName());
            createCache(cm, app.abelsc.com.domain.PerfilUsuario.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
