package com.metaproc.poc.launcher;

import com.metaproc.poc.infinispan.InfinispanService;
import com.metaproc.poc.model.BookHibernate;
import java.util.Random;
import org.infinispan.Cache;

public class Launcher {

  public static void main(String[] args) throws InterruptedException {

    InfinispanService infinispanService = new InfinispanService();
    infinispanService.activate();

    Cache<Integer, BookHibernate> cache = infinispanService.getCache();

    Thread.sleep(3000);
    putTestData(cache);
    Thread.sleep(3000);
    putTestData(cache);
    Thread.sleep(3000);
    putTestData(cache);
    Thread.sleep(3000);
    queryTestDataStream(cache);
    Thread.sleep(3000);






    infinispanService.deactivate();

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
