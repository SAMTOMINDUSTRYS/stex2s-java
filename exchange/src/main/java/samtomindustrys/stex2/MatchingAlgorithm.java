package samtomindustrys.stex2;

import java.util.List;

public interface MatchingAlgorithm{
  List<Trade> fulfill(OrderBook book, Order o);
}
