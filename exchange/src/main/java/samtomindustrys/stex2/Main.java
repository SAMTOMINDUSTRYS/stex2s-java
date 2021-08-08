package samtomindustrys.stex2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
  static Stock sti = new Stock("Sam and Tom Industrys", "STI");
  static Stock birm = new Stock("Birmingham University", "BRUM");
  static Stock aber = new Stock("Aberystwyth University", "AB3R");

  public static void main(String... args) {
    Market market = new Market(new FiFoMatcher(), new InMemoryMarketRepository());

    initialiseSomeOrders(market);
    System.out.println(market);

    Order buySTi = new Order(Side.BUY, sti, 521, new BigDecimal("200"));

    market.accept(buySTi);

    System.out.println(market);
    Order tooBig =  new Order(Side.BUY, sti, 400, new BigDecimal("199"));
    market.accept(tooBig);
    System.out.println(market);

    Order filler =  new Order(Side.SELL, sti, 1000, new BigDecimal("199"));
    market.accept(filler);
    System.out.println(market);
  }


  private static void initialiseSomeOrders(Market market) {

    Order sellSti = new Order(Side.SELL, sti, 10, new BigDecimal("100"));
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {}
    Order sellSti2 = new Order(Side.SELL, sti, 400, new BigDecimal("100"));
    Order sellSti3 = new Order(Side.SELL, sti, 100, new BigDecimal("200"));
    Order sellBrum = new Order(Side.SELL, sti, 10, new BigDecimal("10"));
    //    Order sellAber = new Order(Side.SELL, sti, 10, new BigDecimal(""));


    market.accept(sellSti);
    market.accept(sellSti2);
    market.accept(sellSti3);
    market.accept(sellBrum);

  }


}
