package samtomindustrys.stex2;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.google.gson.annotations.SerializedName;


public final class Order { 
  
  private final Side side;
  private final Stock symbol;
  private final int volume;

  @SerializedName("txid")
  private final String txId;
  private final String accountId;
  private final String brokerId;
  @SerializedName("sender_ts")
  private final Instant senderTimestamp;
  @SerializedName("received_ts")
  private final Instant receivedTimestamp;

  private final BigDecimal price;

  private static final DateTimeFormatter timeFormat = 
    DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSSS")
    .withLocale(Locale.UK)
    .withZone(ZoneId.systemDefault()); 
  //DateTimeFormatter.ofPattern("YYYY MM dd HH mm ss SSSS");

  private Order(Side side, Stock symbol, int volume, BigDecimal price, Instant senderTimestamp, String txId, String accountId, String brokerId, Instant receivedTimestamp) {
    this.side = side;
    this.symbol = symbol;
    this.volume = volume;
    this.price = price;
    this.senderTimestamp = senderTimestamp;

    this.txId = txId;
    this.accountId = accountId;
    this.brokerId = brokerId;

    this.receivedTimestamp = receivedTimestamp;
 }

  public Order(Side side, Stock symbol, int volume, BigDecimal price, Instant senderTimestamp, String txId, String accountId, String brokerId) {
    this(side, symbol, volume, price, senderTimestamp, txId, accountId, brokerId, Instant.now()); 
  }


  //TODO: what do we do with the receivedTimestamp. Is it duped or reset (currently reset)
  public Order withQuantity(int quan) {
    Order o = new Order(side, symbol, quan, price, senderTimestamp, txId, accountId, brokerId);
    return o;
  }

  public Side side() {
    return side;
  }

  public Stock stock() {
    return symbol;
  }

  public int quantity() {
    return volume;
  }

  public BigDecimal price() {
    return price;
  }

  public Instant timestamp() {
    return receivedTimestamp;
  }

  

  @Override
  public String toString() {
    return String.format("%s %s %d @ %s %s%n", timeFormat.format(senderTimestamp),
        side.name(), volume, price.toString(), symbol.ticker());
  }
}
