package samtomindustrys.stex2;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Order { 
  private final Side side;
  private final Stock stock;
  private final int quantity;
  private final Instant timestamp;
  private final BigDecimal price;

  private static final DateTimeFormatter timeFormat = 
    DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSSS")
    .withLocale(Locale.UK)
    .withZone(ZoneId.systemDefault()); 
  //DateTimeFormatter.ofPattern("YYYY MM dd HH mm ss SSSS");

  public Order(Side side, Stock stock, int quantity, BigDecimal price) {
    this(side, stock, quantity, price,Instant.now());
  }
  
  private Order(Side side, Stock stock, int quantity, BigDecimal price, Instant timestamp) {
    this.side = side;
    this.stock = stock;
    this.quantity = quantity;
    this.price = price;
    this.timestamp = timestamp;
  }


  public Order withQuantity(int quan) {
    Order o = new Order(side, stock, quan, price, timestamp);
    return o;
  }

  public Side side() {
    return side;
  }

  public Stock stock() {
    return stock;
  }

  public int quantity() {
    return quantity;
  }

  public BigDecimal price() {
    return price;
  }

  public Instant timestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return String.format("%s %s %d @ %s %s%n", timeFormat.format(timestamp),
        side.name(), quantity, price.toString(), stock.ticker());
  }
}
