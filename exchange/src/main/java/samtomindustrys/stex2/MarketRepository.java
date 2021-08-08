package samtomindustrys.stex2;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface MarketRepository {

  List<Order> getOrdersByStock(Stock stock);

  List<Order> getAllOrders();

  default List<Order> getAllMatchingOrders(Predicate<Order> predicate) {
    return getAllOrders().stream()
      .filter(o -> predicate.test(o))
      .collect(Collectors.toList());
  }

  default List<Order> getOrdersByStock(Stock stock, Side side) {
    return getOrdersByStock(stock)
      .stream()
      .filter(order -> order.side().equals(side))
      .collect(Collectors.toList());
  }

  List<Order> getBuys(Stock stock);

  List<Order> getSells(Stock stock);

  void addOrder(Order o);

  void deleteOrder(Order o);

  void amendOrder(Order old, Order newValues);
}
