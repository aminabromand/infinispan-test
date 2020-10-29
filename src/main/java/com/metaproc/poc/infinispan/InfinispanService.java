package com.metaproc.poc.infinispan;

import com.metaproc.poc.model.AuthorHibernate;
import com.metaproc.poc.model.BookHibernate;
import com.metaproc.poc.utils.ResourceHandler;
import java.io.IOException;
import java.util.Collections;
import org.infinispan.Cache;
import org.infinispan.commons.api.CacheContainerAdmin;
import org.infinispan.commons.configuration.ClassWhiteList;
import org.infinispan.commons.marshall.JavaSerializationMarshaller;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.persistence.jdbc.configuration.JdbcStringBasedStoreConfigurationBuilder;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.GenericTransactionManagerLookup;
import org.infinispan.util.concurrent.IsolationLevel;

public class InfinispanService {

  ResourceHandler resourceHandler;

  EmbeddedCacheManager manager;
  Cache<Integer, BookHibernate> bookCache;
  Cache<String, String> stringCache;

  boolean transportConfigActive = true;
  boolean serializationConfigActive = true;
  boolean globalStateConfigActive = true;

  boolean clusteringCacheConfigActive = true;
  boolean indexingCacheConfigActive = false;
  boolean persistenceCacheConfigActive = false;
  boolean simpleTransactionCacheConfigActive = false;
  boolean transactionCacheConfigActive = false;
  boolean batchingCacheConfigActive = false;
  boolean isolationCacheConfigActive = false;

  int id;
  String stringId;
  String persistenceLocation = "";

  public InfinispanService(int id) {
    this.id = id;
    this.stringId = Integer.toString(id);
    this.resourceHandler = new ResourceHandler();
  }

  public InfinispanService(int id, int persistenceLocation) {
    this(id);
    this.persistenceLocation = Integer.toString(persistenceLocation);;
  }

  public void activateManager() throws IOException {

    GlobalConfigurationBuilder builder = GlobalConfigurationBuilder.defaultClusteredBuilder();

    //@formatter:off
    if(transportConfigActive) {
      builder
          .transport()
            .defaultTransport()
            .clusterName("clusterName")
            .distributedSyncTimeout(50000)
            .addProperty("configurationFile", resourceHandler.getPathToJGroupsConfig(stringId));
    }

    if(serializationConfigActive) {
      builder
          .serialization()
            .marshaller(new JavaSerializationMarshaller(new ClassWhiteList(Collections.singletonList("com.metaproc.eagent.*"))));
    }

    if(globalStateConfigActive) {
      builder
          .globalState()
            .enable()
            .persistentLocation(resourceHandler.getPersistenceLocation(stringId + persistenceLocation));
    }
    //@formatter:on

    manager = new DefaultCacheManager(builder.build());
  }

  public void activateStringCache() {
    stringCache = activateCache("string");
  }

  public void activateBookCache() {
    bookCache = activateCache("book");
  }

  private <K, V>Cache<K, V> activateCache(String cacheName) {
    ConfigurationBuilder configBuilder = new ConfigurationBuilder();

    //@formatter:off
    if(clusteringCacheConfigActive){
      configBuilder
          .clustering()
            .cacheMode(CacheMode.DIST_SYNC);
    }

    if(indexingCacheConfigActive) {
      configBuilder
          .indexing()
            .addIndexedEntity(BookHibernate.class)
            .addIndexedEntity(AuthorHibernate.class);
    }

    if(persistenceCacheConfigActive) {
      configBuilder
          .persistence()
            .addStore(JdbcStringBasedStoreConfigurationBuilder.class)
              .fetchPersistentState(true)
              .purgeOnStartup(false)
              .segmented(true)
            .table()
              .tableNamePrefix("cache_store")
              .idColumnName("ID_COLUMN").idColumnType("VARCHAR")
              .dataColumnName("DATA_COLUMN").dataColumnType("VARCHAR")
              .timestampColumnName("TIMESTAMP_COLUMN").timestampColumnType("BIGINT")
              .segmentColumnName("SEGMENT_COLUMN").segmentColumnType("INT")
            .connectionPool()
              .driverClass("org.h2.Driver")
              .connectionUrl(getJdbcConnectionUrl("string"))
              .username("sa")
              .password("!123456 sa");
    }

    if(simpleTransactionCacheConfigActive) {
      configBuilder
          .transaction()
            .transactionMode(TransactionMode.TRANSACTIONAL)
            .transactionManagerLookup(new GenericTransactionManagerLookup());
    }

    if(batchingCacheConfigActive) {
      configBuilder
          .invocationBatching()
          .enable();
    }

    if(isolationCacheConfigActive) {
      configBuilder
          .locking()
          .isolationLevel(IsolationLevel.READ_COMMITTED);
    }

    if(transactionCacheConfigActive) {
      configBuilder
          .transaction()
            .lockingMode(LockingMode.OPTIMISTIC)
              .autoCommit(true)
              .completedTxTimeout(60000)
            .transactionMode(TransactionMode.TRANSACTIONAL)
              .useSynchronization(false)
              .notifications(true)
              .reaperWakeUpInterval(30000)
              .cacheStopTimeout(30000)
              .transactionManagerLookup(new GenericTransactionManagerLookup())
            .recovery()
              .enabled(false)
              .recoveryInfoCacheName("__recoveryInfoCacheName__");
    }
    //@formatter:on

    return manager.administration().withFlags(CacheContainerAdmin.AdminFlag.VOLATILE).getOrCreateCache(cacheName, configBuilder.build());
  }

  private String getJdbcConnectionUrl(String cacheName) {
    StringBuilder sb = new StringBuilder();
    sb.append("jdbc:h2:")
        .append(resourceHandler.getCachePersistenceLocation(stringId, cacheName))
        .append(";CIPHER=AES")
        .append(";DB_CLOSE_DELAY=0")
        .append(";DB_CLOSE_ON_EXIT=TRUE");
    return sb.toString();
  }

  public void deactivate() throws InterruptedException {

    if(bookCache != null) {
      bookCache.stop();
      bookCache.shutdown();
      Thread.sleep(3000);
    }

    if(stringCache != null) {
      stringCache.stop();
      stringCache.shutdown();
      Thread.sleep(3000);
    }

    manager.stop();
  }

  public Cache<Integer, BookHibernate> getBookCache() {
    return bookCache;
  }

  public Cache<String, String> getStringCache() {
    return stringCache;
  }

  public void setTransportConfigActive(boolean transportConfigActive) {
    this.transportConfigActive = transportConfigActive;
  }

  public void setSerializationConfigActive(boolean serializationConfigActive) {
    this.serializationConfigActive = serializationConfigActive;
  }

  public void setGlobalStateConfigActive(boolean globalStateConfigActive) {
    this.globalStateConfigActive = globalStateConfigActive;
  }

  public void setClusteringCacheConfigActive(boolean clusteringCacheConfigActive) {
    this.clusteringCacheConfigActive = clusteringCacheConfigActive;
  }

  public void setIndexingCacheConfigActive(boolean indexingCacheConfigActive) {
    this.indexingCacheConfigActive = indexingCacheConfigActive;
  }

  public void setPersistenceCacheConfigActive(boolean persistenceCacheConfigActive) {
    this.persistenceCacheConfigActive = persistenceCacheConfigActive;
  }

  public void setSimpleTransactionCacheConfigActive(boolean simpleTransactionCacheConfigActive) {
    this.simpleTransactionCacheConfigActive = simpleTransactionCacheConfigActive;
  }

  public void setTransactionCacheConfigActive(boolean transactionCacheConfigActive) {
    this.transactionCacheConfigActive = transactionCacheConfigActive;
  }

  public void setBatchingCacheConfigActive(boolean batchingCacheConfigActive) {
    this.batchingCacheConfigActive = batchingCacheConfigActive;
  }

  public void setIsolationCacheConfigActive(boolean isolationCacheConfigActive) {
    this.isolationCacheConfigActive = isolationCacheConfigActive;
  }
}
