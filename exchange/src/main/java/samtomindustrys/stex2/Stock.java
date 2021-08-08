package samtomindustrys.stex2;

public final class Stock {
  private final String name;
  private final String ticker;

  public Stock(String name, String ticker) {
    this.name = name;
    this.ticker = ticker;
  }

  public String ticker() {
    return ticker;
  }

  public String name() {
    return name;
  }
}
