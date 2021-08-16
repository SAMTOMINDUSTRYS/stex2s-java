package samtomindustrys.stex2;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

//TODO: Use not a priority queue as we cannot iterate it in order.
public class OrderBook {
  private Set<Order> buys;
  private Set<Order> sells;

  private static final Comparator<Order> buyOrder;
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


  public OrderBook() {
    buys = new TreeSet<>(buyOrder);
    sells = new TreeSet<>(sellOrder);
  }

  private Set<Order> getSet(Order o) {
    if (o.side().equals(Side.SELL)) {
      return sells;
    }
    return buys;
  }

  public Set<Order> getSells() {
    return sells;
  }

  public Set<Order> getBuys() {
    return buys;
  }

  public void delete(Order o) {
    getSet(o).remove(o);
  }

  public void add(Order o) {
    getSet(o).add(o);
  }


}
