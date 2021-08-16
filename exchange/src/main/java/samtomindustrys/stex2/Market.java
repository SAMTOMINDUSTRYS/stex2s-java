package samtomindustrys.stex2;

import java.util.List;

public class Market {

  private final MatchingAlgorithm algo;
  private final MarketRepository repo;

  public Market(MatchingAlgorithm algo, MarketRepository repo) {
    this.algo = algo;
    this.repo = repo;
  }

  public List<Order> getOrdersByStock(Stock stock) {
    return repo.getOrdersByStock(stock);
  } 

  public int size(Stock stock) {
    return repo.orderBookForStock(stock).getBuys().size() + repo.orderBookForStock(stock).getSells().size() ;
  }

  /**
   * Accept an order for this market. Sell orders are just listed,
   * Buy orders are fulfilled via the chosen fulfillment  */
  public void accept(Order o) {

    List<Trade> result = null;
    
    OrderBook orderBook = repo.orderBookForStock(o.stock());

    orderBook.add(o);
    // Run the matching algorithm
    while((result = algo.fulfill(orderBook, o)) != null) {
      
      // Delete or amend sells 
      for (Trade trade : result) {
        if (trade.completesSell()) {
          repo.deleteOrder(trade.sell());
        } else {
          repo.amendOrder(trade.sell(), trade.remainingSellOrder());
        }
      }

      // Remove buy
      repo.deleteOrder(result.get(0).buy());
      //buys = repo.getBuys(o.stock());
      //sells = repo.getSells(o.stock());

    }
  }


  @Override
  public String toString() {

    return repo.toString();
  }
}
