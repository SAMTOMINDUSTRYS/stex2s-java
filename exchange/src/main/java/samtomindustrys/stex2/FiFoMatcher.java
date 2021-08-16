package samtomindustrys.stex2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public final class FiFoMatcher implements MatchingAlgorithm {

 /* private static final Comparator<Order> buyOrder;
  private static final Comparator<Order> sellOrder;

  static {
    buyOrder = (o1, o2) -> {
      int price = o2.price().compareTo(o1.price());
      if (price == 0) {
        return o1.timestamp().compareTo(o2.timestamp());
      }
      return price;
    };
    sellOrder = (o1, o2) -> {
      int price = o1.price().compareTo(o2.price());
      if (price == 0) {
        return o1.timestamp().compareTo(o2.timestamp());
      }
      return price;
    };
  }
*/

  @Override
  public List<Trade> fulfill(OrderBook book, Order o) {

    // TODO: Check the price of the new order and fail early if:
    // * Its a buy and the price is less than the current best buy
    // * Its a sell snd the p;rice is more than the current best sell
    Set<Order> buys = book.getBuys();
    Set<Order> sells = book.getSells();
    
    // if this is a sell but there are no buys or this is a buy and there are no 
    if ( ((buys == null || buys.isEmpty())  && o.side().equals(Side.SELL)) ||
        ((sells == null || sells.isEmpty()) && o.side().equals(Side.BUY))) {
      return null;
        }

    List<Trade> trades = new ArrayList<>();

    if (buys.isEmpty()) { return null; }
    Order buy = ((TreeSet<Order>)buys).first();

    int quantityNeeded = buy.quantity();

    for (Order sell: sells) {

      // If the sell is less than the buy
      if (buy.price().compareTo(sell.price()) >= 0) {

        if (quantityNeeded <= sell.quantity()) {
          //System.out.format("%d of %d partial fulfill: %s%n ", quantityNeeded, quantityNeeded, sell);
          trades.add(new Trade(buy, sell, quantityNeeded));
          quantityNeeded = 0;

        } else {
          //System.out.format("%d of %d fulfilled in by: %s%n ", sell.quantity(), quantityNeeded, sell);

          trades.add(new Trade(buy, sell, sell.quantity()));
          quantityNeeded -= sell.quantity();
        }
      } 
      if (quantityNeeded == 0) {
        //System.out.println("Buy filled");
        break;
      }
    }

    // if no matches were made return null
    if (quantityNeeded == 0) {
      return trades;
    }
    return null;
  } 
}
