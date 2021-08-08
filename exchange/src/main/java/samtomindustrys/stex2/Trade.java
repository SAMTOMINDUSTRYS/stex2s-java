package samtomindustrys.stex2;

import java.time.Instant;
import java.util.Objects;

public class Trade {
  private final Order buy;
  private final Order sell;
  private final Instant timestamp;
  private final int volume;

  public Trade(Order buy, Order sell, int volume){
    this.buy = Objects.requireNonNull(buy, "Buy order cannot be null");
    this.sell = Objects.requireNonNull(sell, "Sell order cannot be null");
    this.volume = volume;
    timestamp = Instant.now();
  }

  public boolean completesSell() {
    return (sell.quantity() == volume);
  }

  public Order remainingSellOrder() {
    if (completesSell()) { return null; }

    return sell.withQuantity(sell.quantity() - volume);
  }

  public int volume() {
    return volume;
  }

  public Order sell() {
    return sell;
  }

  public Order buy() {
    return buy;
  }

  @Override
  public String toString() {
    if (sell == null) {
      return  String.format("%s buy for %d unfilfilled", timestamp.toString(), volume);
    }
    return String.format("%s buy for %d, %d fulfilled by sell for %d",
        timestamp.toString(), buy.quantity(), volume, sell.quantity());
  }

}
