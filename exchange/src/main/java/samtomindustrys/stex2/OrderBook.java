package samtomindustrys.stex2;

import java.util.List;
import java.util.ArrayList;

public class OrderBook {
  private List<Order> buys;
  private List<Order> sells;

  public OrderBook() {
    buys = new ArrayList<>();
    sells = new ArrayList<>();
  }

  private List<Order> getSet(Order o) {
    if (o.side().equals(Side.SELL)) {
      return sells;
    }
    return buys;
  }

  public List<Order> getSells() {
    return sells;
  }

  public List<Order> getBuys() {
    return buys;
  }

  public void delete(Order o) {
    getSet(o).remove(o);
  }

  public void add(Order o) {
    getSet(o).add(o);
  }


}
