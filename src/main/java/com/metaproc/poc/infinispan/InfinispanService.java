package com.metaproc.poc.infinispan;

import com.metaproc.poc.model.AuthorHibernate;
import com.metaproc.poc.model.BookHibernate;
import org.infinispan.Cache;
import org.infinispan.commons.api.CacheContainerAdmin;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class InfinispanService {

  EmbeddedCacheManager manager;
  Cache<Integer, BookHibernate> cache;

  public InfinispanService() {
  }

  public void activate() {

    GlobalConfigurationBuilder builder = GlobalConfigurationBuilder.defaultClusteredBuilder();
    builder.transport()
        .defaultTransport()
        .clusterName("clusterName")
        .distributedSyncTimeout(50000);
//        .addProperty("configurationFile", pathToJGroupsConfig);

    manager = new DefaultCacheManager(builder.build());

    ConfigurationBuilder configBuilder = new ConfigurationBuilder();
    configBuilder.clustering().cacheMode(CacheMode.DIST_SYNC);//.indexing().addIndexedEntity(BookHibernate.class).addIndexedEntity(AuthorHibernate.class);

    cache = manager.administration().withFlags(CacheContainerAdmin.AdminFlag.VOLATILE).getOrCreateCache("query", configBuilder.build());
  }

  public void deactivate() throws InterruptedException {

    cache.stop();
    cache.shutdown();
    Thread.sleep(3000);

    manager.stop();
  }

  public Cache<Integer, BookHibernate> getCache() {
    return cache;
  }

}
