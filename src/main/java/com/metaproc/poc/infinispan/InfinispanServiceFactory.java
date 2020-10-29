package com.metaproc.poc.infinispan;

import java.io.IOException;

public class InfinispanServiceFactory {

  public static boolean transportConfigActive = true;
  public static boolean serializationConfigActive = true;
  public static boolean globalStateConfigActive = true;

  public static boolean clusteringCacheConfigActive = true;
  public static boolean indexingCacheConfigActive = false;
  public static boolean persistenceCacheConfigActive = false;
  public static boolean simpleTransactionCacheConfigActive = false;
  public static boolean transactionCacheConfigActive = false;
  public static boolean batchingCacheConfigActive = false;
  public static boolean isolationCacheConfigActive = false;

  public static InfinispanService build(int id) throws IOException {
    InfinispanService infinispanService = new InfinispanService(id);
    return setup(infinispanService);
  }

  public static InfinispanService build(int id, int persistenceLocation) throws IOException {
    InfinispanService infinispanService = new InfinispanService(id, persistenceLocation);
    return setup(infinispanService);
  }

  private static InfinispanService setup(InfinispanService infinispanService) throws IOException {
    infinispanService.setSerializationConfigActive(serializationConfigActive);
    infinispanService.setTransportConfigActive(transportConfigActive);
    infinispanService.setGlobalStateConfigActive(globalStateConfigActive);

    infinispanService.setClusteringCacheConfigActive(clusteringCacheConfigActive);
    infinispanService.setIndexingCacheConfigActive(indexingCacheConfigActive);
    infinispanService.setPersistenceCacheConfigActive(persistenceCacheConfigActive);
    infinispanService.setSimpleTransactionCacheConfigActive(simpleTransactionCacheConfigActive);
    infinispanService.setTransactionCacheConfigActive(transactionCacheConfigActive);
    infinispanService.setBatchingCacheConfigActive(batchingCacheConfigActive);
    infinispanService.setIsolationCacheConfigActive(isolationCacheConfigActive);

    infinispanService.activateManager();
    infinispanService.activateStringCache();

    return infinispanService;
  }

}
