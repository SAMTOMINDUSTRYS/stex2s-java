package samtomindustrys.stex2;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Stock)) {
      return false;
    }
    Stock that = (Stock)o;
    if (this.ticker.equals(that.ticker)) {
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return ticker.hashCode();
  }
}
