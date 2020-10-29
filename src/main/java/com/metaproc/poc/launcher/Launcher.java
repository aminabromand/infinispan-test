package com.metaproc.poc.launcher;

import com.metaproc.poc.infinispan.InfinispanService;
import com.metaproc.poc.model.BookHibernate;
import java.io.IOException;
import java.util.Random;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import org.infinispan.Cache;

public class Launcher {

  public static void main(String[] args)
      throws InterruptedException, IOException, HeuristicRollbackException, HeuristicMixedException, NotSupportedException, RollbackException, SystemException {

    boolean transportConfigActive = true;
    boolean serializationConfigActive = true;
    boolean globalStateConfigActive = true;

    boolean clusteringCacheConfigActive = true;
    boolean indexingCacheConfigActive = false;
    boolean persistenceCacheConfigActive = true;
    boolean transactionCacheConfigActive = true;

    InfinispanService infinispanService = new InfinispanService(1);

    infinispanService.setSerializationConfigActive(serializationConfigActive);
    infinispanService.setTransportConfigActive(transportConfigActive);
    infinispanService.setGlobalStateConfigActive(globalStateConfigActive);

    infinispanService.setClusteringCacheConfigActive(clusteringCacheConfigActive);
    infinispanService.setIndexingCacheConfigActive(indexingCacheConfigActive);
    infinispanService.setPersistenceCacheConfigActive(persistenceCacheConfigActive);
    infinispanService.setTransactionCacheConfigActive(transactionCacheConfigActive);

    infinispanService.activateManager();
    infinispanService.activateStringCache();

    Thread.sleep(3000);

    InfinispanService infinispanService2 = new InfinispanService(2);
//    InfinispanService infinispanService2 = new InfinispanService(1, 2);

    infinispanService2.setSerializationConfigActive(serializationConfigActive);
    infinispanService2.setTransportConfigActive(transportConfigActive);
    infinispanService2.setGlobalStateConfigActive(globalStateConfigActive);

    infinispanService2.setClusteringCacheConfigActive(clusteringCacheConfigActive);
    infinispanService2.setIndexingCacheConfigActive(indexingCacheConfigActive);
    infinispanService2.setPersistenceCacheConfigActive(persistenceCacheConfigActive);
    infinispanService2.setTransactionCacheConfigActive(transactionCacheConfigActive);

    infinispanService2.activateManager();
    infinispanService2.activateStringCache();



    Cache<String, String> stringCache = infinispanService.getStringCache();
    queryStringCache(stringCache);
    Cache<String, String> stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    System.out.println("####### putting");
    Thread.sleep(3000);
    stringCache.put("id1", "value1");
    Thread.sleep(3000);
    stringCache.put("id2", "value2");
    Thread.sleep(3000);
    stringCache.put("id3", "value3");
    System.out.println("####### putting done");

    queryStringCache(stringCache2);

    System.out.println("####### putting");
    Thread.sleep(3000);
    stringCache2.put("id4", "value4");
    Thread.sleep(3000);
    stringCache2.put("id1", "updated value1");
    System.out.println("####### putting done");

    queryStringCache(stringCache);

    System.out.println("####### tx");
    testTransaction(stringCache);
    System.out.println("####### tx done");

    queryStringCache(stringCache);

    System.out.println("####### batch");
    testBatch(stringCache);
    System.out.println("####### batch done");

    queryStringCache(stringCache);

    infinispanService.deactivate();
    infinispanService2.deactivate();

  }

  public static void queryStringCache(Cache<String, String> cache) {
    System.out.println("###");
    cache.entrySet().forEach(System.out::println);
    System.out.println("###");
  }

  public static void testTransaction(Cache<String, String> cache)
      throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
    cache.getAdvancedCache().getTransactionManager().begin();
    cache.remove("id2");
    cache.put("id2", "removed and added value2");
    cache.getAdvancedCache().getTransactionManager().commit();
  }

  public static void testBatch(Cache<String, String> cache) {
    cache.startBatch();
    cache.remove("id3");
    cache.put("id3", "removed and added value3");
    cache.endBatch(true);
  }



  public static void testBookData(InfinispanService infinispanService) throws InterruptedException {
    Cache<Integer, BookHibernate> cache = infinispanService.getBookCache();

    Thread.sleep(3000);
    putTestData(cache);
    Thread.sleep(3000);
    putTestData(cache);
    Thread.sleep(3000);
    putTestData(cache);
    Thread.sleep(3000);
    queryTestDataStream(cache);
    Thread.sleep(3000);
  }

  public static void putTestData(Cache<Integer, BookHibernate> cache) {

    int max = 1000;
    int min = 0;
    Random rn = new Random();
    int randNumber = rn.nextInt(max - min + 1) + min;

    System.out.println("###: " + randNumber);
    BookHibernate book1 = new BookHibernate();
    book1.setId(1);
    book1.setTitle("Titel: " + randNumber);
    book1.setDescription("Description: " + randNumber);
    cache.put(randNumber, book1);
    System.out.println("###");
  }

  public static void queryTestDataStream(Cache<Integer, BookHibernate> cache) {
    System.out.println("###");
    cache.entrySet().forEach(System.out::println);
    System.out.println("###");
  }

}
