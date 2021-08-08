package samtomindustrys.stex2;

import java.util.List;

public interface MatchingAlgorithm{
  List<Trade> fulfill(List<Order> buys, List<Order> sells, Order o);
}
