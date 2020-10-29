package com.metaproc.poc.infinispan;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import org.apache.commons.io.FileUtils;
import org.infinispan.Cache;
import org.junit.After;
import org.junit.Test;

public class InfinispanServiceStringCacheTest {

  @After
  public void removeTempFiles() throws IOException {
    FileUtils.deleteDirectory(new File(Paths.get(System.getProperty("user.dir"), "target", "nodes").toString()));
  }

  @Test
  public void testStringCache() throws InterruptedException, IOException {

    InfinispanServiceFactory.transportConfigActive = true;
    InfinispanServiceFactory.serializationConfigActive = true;
    InfinispanServiceFactory.globalStateConfigActive = true;

    InfinispanServiceFactory.clusteringCacheConfigActive = true;
    InfinispanServiceFactory.indexingCacheConfigActive = false;
    InfinispanServiceFactory.persistenceCacheConfigActive = false;
    InfinispanServiceFactory.simpleTransactionCacheConfigActive = false;
    InfinispanServiceFactory.transactionCacheConfigActive = false;
    InfinispanServiceFactory.batchingCacheConfigActive = false;
    InfinispanServiceFactory.isolationCacheConfigActive = false;

    InfinispanService infinispanService = InfinispanServiceFactory.build(1);
    Thread.sleep(3000);
    InfinispanService infinispanService2 = InfinispanServiceFactory.build(2);


    Cache<String, String> stringCache = infinispanService.getStringCache();
    queryStringCache(stringCache);
    Cache<String, String> stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    testPuttingWith2Caches(stringCache, stringCache2);

    infinispanService.deactivate();
    infinispanService2.deactivate();

  }

  @Test
  public void testStringCacheWithRestart() throws InterruptedException, IOException {

    InfinispanServiceFactory.transportConfigActive = true;
    InfinispanServiceFactory.serializationConfigActive = true;
    InfinispanServiceFactory.globalStateConfigActive = true;

    InfinispanServiceFactory.clusteringCacheConfigActive = true;
    InfinispanServiceFactory.indexingCacheConfigActive = false;
    InfinispanServiceFactory.persistenceCacheConfigActive = false;
    InfinispanServiceFactory.simpleTransactionCacheConfigActive = false;
    InfinispanServiceFactory.transactionCacheConfigActive = false;
    InfinispanServiceFactory.batchingCacheConfigActive = false;
    InfinispanServiceFactory.isolationCacheConfigActive = false;


    InfinispanService infinispanService = InfinispanServiceFactory.build(1);
    Thread.sleep(3000);
    InfinispanService infinispanService2 = InfinispanServiceFactory.build(2);


    Cache<String, String> stringCache = infinispanService.getStringCache();
    queryStringCache(stringCache);
    Cache<String, String> stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    testPuttingWith2Caches(stringCache, stringCache2);

    infinispanService.deactivate();
    infinispanService2.deactivate();

    Thread.sleep(3000);

    infinispanService.activateManager();
    Thread.sleep(3000);
    infinispanService.activateStringCache();

    infinispanService2.activateManager();
    Thread.sleep(3000);
    infinispanService2.activateStringCache();

    stringCache = infinispanService.getStringCache();
    queryStringCache(stringCache);
    stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    testPuttingWith2Caches(stringCache, stringCache2);

    infinispanService.deactivate();
    infinispanService2.deactivate();

    Thread.sleep(3000);
  }

  @Test
  public void testStringCacheWithRestartOnly2nd_containsNoData() throws InterruptedException, IOException {

    InfinispanServiceFactory.transportConfigActive = true;
    InfinispanServiceFactory.serializationConfigActive = true;
    InfinispanServiceFactory.globalStateConfigActive = true;

    InfinispanServiceFactory.clusteringCacheConfigActive = true;
    InfinispanServiceFactory.indexingCacheConfigActive = false;
    InfinispanServiceFactory.persistenceCacheConfigActive = false;
    InfinispanServiceFactory.simpleTransactionCacheConfigActive = false;
    InfinispanServiceFactory.transactionCacheConfigActive = false;
    InfinispanServiceFactory.batchingCacheConfigActive = false;
    InfinispanServiceFactory.isolationCacheConfigActive = false;


    InfinispanService infinispanService = InfinispanServiceFactory.build(1);
    Thread.sleep(3000);
    InfinispanService infinispanService2 = InfinispanServiceFactory.build(2);


    Cache<String, String> stringCache = infinispanService.getStringCache();
    queryStringCache(stringCache);
    Cache<String, String> stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    testPuttingWith2Caches(stringCache, stringCache2);

    infinispanService.deactivate();
    infinispanService2.deactivate();

    Thread.sleep(3000);

    infinispanService2.activateManager();
    Thread.sleep(3000);
    infinispanService2.activateStringCache();

    stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    infinispanService2.deactivate();

    Thread.sleep(3000);
  }

  @Test
  public void testStringCacheWithFullSetting() throws InterruptedException, IOException {

    InfinispanServiceFactory.transportConfigActive = true;
    InfinispanServiceFactory.serializationConfigActive = true;
    InfinispanServiceFactory.globalStateConfigActive = true;

    InfinispanServiceFactory.clusteringCacheConfigActive = true;
    InfinispanServiceFactory.indexingCacheConfigActive = false;
    InfinispanServiceFactory.persistenceCacheConfigActive = true;
    InfinispanServiceFactory.simpleTransactionCacheConfigActive = true;
    InfinispanServiceFactory.transactionCacheConfigActive = true;
    InfinispanServiceFactory.batchingCacheConfigActive = true;
    InfinispanServiceFactory.isolationCacheConfigActive = true;


    InfinispanService infinispanService = InfinispanServiceFactory.build(1);
    Thread.sleep(3000);
    InfinispanService infinispanService2 = InfinispanServiceFactory.build(2);


    Cache<String, String> stringCache = infinispanService.getStringCache();
    queryStringCache(stringCache);
    Cache<String, String> stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    testPuttingWith2Caches(stringCache, stringCache2);

    infinispanService.deactivate();
    infinispanService2.deactivate();

  }

  @Test
  public void testStringCacheWithFullSettingAndRestart()
      throws InterruptedException, IOException, HeuristicRollbackException, HeuristicMixedException, NotSupportedException, RollbackException, SystemException {

    InfinispanServiceFactory.transportConfigActive = true;
    InfinispanServiceFactory.serializationConfigActive = true;
    InfinispanServiceFactory.globalStateConfigActive = true;

    InfinispanServiceFactory.clusteringCacheConfigActive = true;
    InfinispanServiceFactory.indexingCacheConfigActive = false;
    InfinispanServiceFactory.persistenceCacheConfigActive = true;
    InfinispanServiceFactory.simpleTransactionCacheConfigActive = true;
    InfinispanServiceFactory.transactionCacheConfigActive = true;
    InfinispanServiceFactory.batchingCacheConfigActive = true;
    InfinispanServiceFactory.isolationCacheConfigActive = true;

    InfinispanService infinispanService = InfinispanServiceFactory.build(1);
    Thread.sleep(3000);
    InfinispanService infinispanService2 = InfinispanServiceFactory.build(2);


    Cache<String, String> stringCache = infinispanService.getStringCache();
    queryStringCache(stringCache);
    Cache<String, String> stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    testPuttingWith2Caches(stringCache, stringCache2);

    infinispanService.deactivate();
    infinispanService2.deactivate();

    Thread.sleep(3000);

    infinispanService.activateManager();
    Thread.sleep(3000);
    infinispanService.activateStringCache();

    infinispanService2.activateManager();
    Thread.sleep(3000);
    infinispanService2.activateStringCache();

    stringCache = infinispanService.getStringCache();
    queryStringCache(stringCache);
    stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    testPuttingWith2Caches(stringCache, stringCache2);

    testTransaction(stringCache);
    testBatch(stringCache);

    infinispanService.deactivate();
    infinispanService2.deactivate();

    Thread.sleep(3000);
  }

  @Test
  public void testStringCacheWithFullSettingAndRestartOnly2nd_containsNoData()
      throws InterruptedException, IOException {

    InfinispanServiceFactory.transportConfigActive = true;
    InfinispanServiceFactory.serializationConfigActive = true;
    InfinispanServiceFactory.globalStateConfigActive = true;

    InfinispanServiceFactory.clusteringCacheConfigActive = true;
    InfinispanServiceFactory.indexingCacheConfigActive = false;
    InfinispanServiceFactory.persistenceCacheConfigActive = true;
    InfinispanServiceFactory.simpleTransactionCacheConfigActive = true;
    InfinispanServiceFactory.transactionCacheConfigActive = true;
    InfinispanServiceFactory.batchingCacheConfigActive = true;
    InfinispanServiceFactory.isolationCacheConfigActive = true;


    InfinispanService infinispanService = InfinispanServiceFactory.build(1);
    Thread.sleep(3000);
    InfinispanService infinispanService2 = InfinispanServiceFactory.build(2);


    Cache<String, String> stringCache = infinispanService.getStringCache();
    queryStringCache(stringCache);
    Cache<String, String> stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    testPuttingWith2Caches(stringCache, stringCache2);

    infinispanService.deactivate();
    Thread.sleep(3000);
    infinispanService2.deactivate();

    Thread.sleep(3000);

    infinispanService2.activateManager();
    Thread.sleep(3000);
    infinispanService2.activateStringCache();

    stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    infinispanService2.deactivate();

    Thread.sleep(3000);
  }

  @Test
  public void testStringCacheWithSimpleTransactionAndRestartOnly1st() throws InterruptedException, IOException {

    InfinispanServiceFactory.transportConfigActive = true;
    InfinispanServiceFactory.serializationConfigActive = true;
    InfinispanServiceFactory.globalStateConfigActive = true;

    InfinispanServiceFactory.clusteringCacheConfigActive = true;
    InfinispanServiceFactory.indexingCacheConfigActive = false;
    InfinispanServiceFactory.persistenceCacheConfigActive = true;
    InfinispanServiceFactory.simpleTransactionCacheConfigActive = true;
    InfinispanServiceFactory.transactionCacheConfigActive = false;
    InfinispanServiceFactory.batchingCacheConfigActive = false;
    InfinispanServiceFactory.isolationCacheConfigActive = false;


    InfinispanService infinispanService = InfinispanServiceFactory.build(1);
    Thread.sleep(3000);
    InfinispanService infinispanService2 = InfinispanServiceFactory.build(1, 2);


    Cache<String, String> stringCache = infinispanService.getStringCache();
    queryStringCache(stringCache);
    Cache<String, String> stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    testPuttingWith2Caches(stringCache, stringCache2);

    infinispanService.deactivate();
    infinispanService2.deactivate();

    Thread.sleep(3000);

    infinispanService.activateManager();
    Thread.sleep(3000);
    infinispanService.activateStringCache();

    stringCache = infinispanService.getStringCache();
    stringCache.put("id1", "updated value1");
    queryStringCache(stringCache);

//    Throws AssertionError on another Thread, so it cannot be caught
//    try {
//      infinispanService2.activateManager();
//    } catch (AssertionError | Exception e) {
//      System.out.println(e.getMessage());
//    }
//    Thread.sleep(3000);
//    infinispanService2.activateStringCache();
//
//    stringCache2 = infinispanService2.getStringCache();
//    queryStringCache(stringCache2);
//
//    infinispanService2.deactivate();

    infinispanService.deactivate();

    Thread.sleep(3000);
  }

  @Test
  public void testStringCacheWithSimpleTransactionAndRestartOnly2nd_containsAllData() throws InterruptedException, IOException {

    InfinispanServiceFactory.transportConfigActive = true;
    InfinispanServiceFactory.serializationConfigActive = true;
    InfinispanServiceFactory.globalStateConfigActive = true;

    InfinispanServiceFactory.clusteringCacheConfigActive = true;
    InfinispanServiceFactory.indexingCacheConfigActive = false;
    InfinispanServiceFactory.persistenceCacheConfigActive = true;
    InfinispanServiceFactory.simpleTransactionCacheConfigActive = true;
    InfinispanServiceFactory.transactionCacheConfigActive = false;
    InfinispanServiceFactory.batchingCacheConfigActive = false;
    InfinispanServiceFactory.isolationCacheConfigActive = false;


    InfinispanService infinispanService = InfinispanServiceFactory.build(1);
    Thread.sleep(3000);
    InfinispanService infinispanService2 = InfinispanServiceFactory.build(1, 2);


    Cache<String, String> stringCache = infinispanService.getStringCache();
    queryStringCache(stringCache);
    Cache<String, String> stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    testPuttingWith2Caches(stringCache, stringCache2);

    infinispanService.deactivate();
    infinispanService2.deactivate();

    Thread.sleep(3000);

    infinispanService2.activateManager();
    Thread.sleep(3000);
    infinispanService2.activateStringCache();

    stringCache2 = infinispanService2.getStringCache();
    stringCache2.put("id1", "updated value1");
    queryStringCache(stringCache2);

    infinispanService2.deactivate();

    Thread.sleep(3000);
  }

  @Test
  public void testLongString() throws InterruptedException, IOException {

    InfinispanServiceFactory.transportConfigActive = true;
    InfinispanServiceFactory.serializationConfigActive = true;
    InfinispanServiceFactory.globalStateConfigActive = true;

    InfinispanServiceFactory.clusteringCacheConfigActive = true;
    InfinispanServiceFactory.indexingCacheConfigActive = false;
    InfinispanServiceFactory.persistenceCacheConfigActive = false;
    InfinispanServiceFactory.simpleTransactionCacheConfigActive = false;
    InfinispanServiceFactory.transactionCacheConfigActive = false;
    InfinispanServiceFactory.batchingCacheConfigActive = false;
    InfinispanServiceFactory.isolationCacheConfigActive = false;

    InfinispanService infinispanService = InfinispanServiceFactory.build(1);
    Thread.sleep(3000);
    InfinispanService infinispanService2 = InfinispanServiceFactory.build(2);


    Cache<String, String> stringCache = infinispanService.getStringCache();
    queryStringCache(stringCache);
    Cache<String, String> stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    String longKey = "";
    for(int i = 0; i<10000; i++) {
      longKey = longKey + "1234567890";
    }
    String longValue = "";
    for(int i = 0; i<10000; i++) {
      longValue = longValue + "1234567890";
    }

    System.out.println("key length: " + longKey.length());
    System.out.println("value length: " + longValue.length());
    System.out.println("key(" + longKey.length() + "): " + longKey);
    System.out.println("value(" + longValue.length() + "): " + longValue);

    stringCache.put(longKey, longValue);

    queryStringCache(stringCache);
    queryStringCache(stringCache2);

    infinispanService.deactivate();
    infinispanService2.deactivate();

  }

  @Test
  public void testLongStringWithFullSettings() throws InterruptedException, IOException {

    InfinispanServiceFactory.transportConfigActive = true;
    InfinispanServiceFactory.serializationConfigActive = true;
    InfinispanServiceFactory.globalStateConfigActive = true;

    InfinispanServiceFactory.clusteringCacheConfigActive = true;
    InfinispanServiceFactory.indexingCacheConfigActive = false;
    InfinispanServiceFactory.persistenceCacheConfigActive = true;
    InfinispanServiceFactory.simpleTransactionCacheConfigActive = true;
    InfinispanServiceFactory.transactionCacheConfigActive = true;
    InfinispanServiceFactory.batchingCacheConfigActive = true;
    InfinispanServiceFactory.isolationCacheConfigActive = true;

    InfinispanService infinispanService = InfinispanServiceFactory.build(1);
    Thread.sleep(3000);
    InfinispanService infinispanService2 = InfinispanServiceFactory.build(2);


    Cache<String, String> stringCache = infinispanService.getStringCache();
    queryStringCache(stringCache);
    Cache<String, String> stringCache2 = infinispanService2.getStringCache();
    queryStringCache(stringCache2);

    String longKey = "";
    for(int i = 0; i<100000; i++) {
      longKey = longKey + "1234567890";
    }
    String longValue = "";
    for(int i = 0; i<100000; i++) {
      longValue = longValue + "1234567890";
    }

    System.out.println("key length: " + longKey.length());
    System.out.println("value length: " + longValue.length());
    System.out.println("key(" + longKey.length() + "): " + longKey);
    System.out.println("value(" + longValue.length() + "): " + longValue);

    stringCache.put(longKey, longValue);

    queryStringCache(stringCache);
    queryStringCache(stringCache2);

    infinispanService.deactivate();
    infinispanService2.deactivate();

  }

  public static void testPuttingWith2Caches(Cache<String, String> stringCache, Cache<String, String> stringCache2)
      throws InterruptedException {

    System.out.println("####### putting cache1");
    Thread.sleep(500);
    stringCache.put("id1", "value1");
    Thread.sleep(500);
    stringCache.put("id2", "value2");
    Thread.sleep(500);
    stringCache.put("id3", "value3");
    System.out.println("####### putting done");

    System.out.println("####### query cache2");
    queryStringCache(stringCache2);

    System.out.println("####### putting cache2");
    Thread.sleep(500);
    stringCache2.put("id4", "value4");
    Thread.sleep(500);
    stringCache2.put("id1", "updated value1");
    System.out.println("####### putting done");

    System.out.println("####### query cache1");
    queryStringCache(stringCache);
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
}