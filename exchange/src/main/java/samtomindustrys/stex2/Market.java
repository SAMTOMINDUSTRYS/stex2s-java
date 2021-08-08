package samtomindustrys.stex2;

import java.util.List;

public class Market {

  private final MatchingAlgorithm algo;
  private final MarketRepository repo;

  public Market(MatchingAlgorithm algo, MarketRepository repo) {
    this.algo = algo;
    this.repo = repo;
  }

  /**
   * Accept an order for this market. Sell orders are just listed,
   * Buy orders are fulfilled via the chosen fulfillment  */
  public void accept(Order o) {

    //Add the order to the market
    repo.addOrder(o);
    
    //Get the buys/sells for this stock
    List<Order> buys = repo.getBuys(o.stock());
    List<Order> sells = repo.getSells(o.stock());

    List<Trade> result = null;

    // Run the matching algorithm
    while((result = algo.fulfill(buys, sells,o)) != null) {
      
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
      buys = repo.getBuys(o.stock());
      sells = repo.getSells(o.stock());

    }
  } 

  @Override
  public String toString() {
    return repo.toString();
  }
}
