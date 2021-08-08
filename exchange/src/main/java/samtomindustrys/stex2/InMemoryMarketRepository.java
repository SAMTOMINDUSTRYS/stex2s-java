package samtomindustrys.stex2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryMarketRepository implements MarketRepository {

  private final Map<Stock, OrderBook> market= new HashMap<>();

  @Override
  public List<Order> getSells(Stock stock) {
    OrderBook ob = market.getOrDefault(stock, null);

    if (ob == null) {
      return null;
    }
    return new ArrayList<Order>(ob.sells); 
  }

  @Override
  public List<Order> getBuys(Stock stock) {
    OrderBook ob = market.getOrDefault(stock, null);

    if (ob == null) {
      return null;
    }
    return new ArrayList<Order>(ob.buys);
  }

  @Override
  public void amendOrder(Order old, Order newValues) {
    OrderBook ob = market.getOrDefault(old.stock(), null);

    if (ob != null && old.stock().equals(newValues.stock())) {
      ob.delete(old);
      ob.add(newValues);
    }
  }

  @Override
  public void addOrder(Order o) {
    OrderBook ob = market.getOrDefault(o.stock(), null);

    if (ob == null) {
      ob = new OrderBook();
      market.put(o.stock(), ob);
    }
    ob.add(o);
  }

  @Override
  public void deleteOrder(Order o) {
    OrderBook ob = market.getOrDefault(o.stock(), null);

    if (ob != null) {
      ob.delete(o);
    }
  }

  @Override
  public List<Order> getOrdersByStock(Stock stock) {
    OrderBook orderBook = market.getOrDefault(stock, null);
    if (orderBook == null) {
      return null;
    }
    return Stream.concat(orderBook.buys.stream(), orderBook.sells.stream())
      .collect(Collectors.toList());
  }

  @Override
  public List<Order> getAllOrders() {
    return market.values()
      .stream()
      .flatMap(o -> Stream.concat(o.buys.stream(), o.sells.stream()))
      .collect(Collectors.toList());
  }

  private class OrderBook {
    private Set<Order> buys;
    private Set<Order> sells;

    public OrderBook() {
      buys = new HashSet<>();
      sells = new HashSet<>();
    }

    private Set<Order> getSet(Order o) {
      if (o.side().equals(Side.SELL)) {
        return sells;
      }
      return buys;
    }

    public void delete(Order o) {
      getSet(o).remove(o);
    }

    public void add(Order o) {
      getSet(o).add(o);
    }
  }

  @Override
  public String toString() {
    final Comparator<Order> buyOrder = (o1, o2) -> {
      int price = o2.price().compareTo(o1.price());
      if (price == 0) {
        return o1.timestamp().compareTo(o2.timestamp());
      }
      return price;
    };

    final Comparator<Order> sellOrder = (o1, o2) -> {
      int price = o1.price().compareTo(o2.price());
      if (price == 0) {
        return o1.timestamp().compareTo(o2.timestamp());
      }
      return price;
    };

    StringBuilder stringBuilder = new StringBuilder();

    market.keySet()
      .stream()
      .map(key -> {
        stringBuilder.append("["+key.ticker()+"]");
        return market.get(key);
      })
    .flatMap(ob -> Stream.concat(ob.buys.stream().sorted(buyOrder),ob.sells.stream().sorted(sellOrder)))
    .forEach(order -> stringBuilder.append("\t"+order));

    return stringBuilder.toString();
  }

}
