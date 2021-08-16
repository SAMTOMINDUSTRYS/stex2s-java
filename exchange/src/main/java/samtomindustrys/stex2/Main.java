package samtomindustrys.stex2;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class Main {
  static Stock sti = new Stock("Sam and Tom Industrys", "STI");
  static Stock birm = new Stock("Birmingham University", "BRUM");
  static Stock aber = new Stock("Aberystwyth University", "AB3R");
  static Market market = new Market(new FiFoMatcher(), new InMemoryMarketRepository());

  public static void main(String... args) {

    JsonSerializer<Stock> stockSerializer = new JsonSerializer<Stock>() {
      @Override
      public JsonElement serialize(Stock src, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(src.ticker());
      }
    };


    JsonSerializer<Instant> instantSerializer = new JsonSerializer<Instant>() {
      @Override
      public JsonElement serialize(Instant src, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(src.toEpochMilli());
      }
    };

    JsonDeserializer<Order> orderDeserializer = new JsonDeserializer<Order> () {
      public Order deserialize(JsonElement json, Type type, JsonDeserializationContext context)
          throws JsonParseException {
          JsonObject jsonObject = json.getAsJsonObject();

          Side side = (jsonObject.get("side").getAsString().equals(Side.BUY.name()) ? Side.BUY : Side.SELL);
          Stock stock = new Stock("", jsonObject.get("symbol").getAsString()); 
          int quantity = jsonObject.get("volume").getAsInt();
          BigDecimal price = new BigDecimal(jsonObject.get("price").getAsString());
          String txId = jsonObject.get("txid").getAsString();
          String customerId = jsonObject.get("account_id").getAsString();
          String brokerId = jsonObject.get("broker_id").getAsString();

          Instant senderTimestamp = Instant.ofEpochMilli(jsonObject.get("sender_ts").getAsLong());

          Order o = new Order(side, stock, quantity, price, senderTimestamp,
              txId, customerId, brokerId);
          return o;
      }
    };



    GsonBuilder builder = new GsonBuilder()
      .setPrettyPrinting()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

    builder.registerTypeAdapter(Order.class, orderDeserializer);
    builder.registerTypeAdapter(Stock.class, stockSerializer);
    builder.registerTypeAdapter(Instant.class, instantSerializer);
    Gson gson = builder.create();

    initialiseSomeOrders(market);
    System.out.println(market);
    Order buySTi = new Order(Side.BUY, sti, 521, new BigDecimal("200.00"), Instant.now(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());

    newOrder(gson, gson.toJson(buySTi));
    System.out.println("============\n"+market);



    Order tooBig =  new Order(Side.BUY, sti, 400, new BigDecimal("199"), Instant.now(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());


    newOrder(gson, gson.toJson(tooBig));
    System.out.println("============\n"+market);

    Order filler =  new Order(Side.SELL, sti, 1000, new BigDecimal("199"), Instant.now(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());


    newOrder(gson, gson.toJson(filler));
    System.out.println("============\n"+market);

    //TODO: the sorting was not working when generating big decimals using a float? Why is this.?
    Random rand = new Random();
    int i = 0;
    List<Order> orders = new ArrayList<>(20_000);
    for (i = 0; i < 20_000; i++) {
      Stock stk = (rand.nextBoolean()?sti:aber);
      Side s = (rand.nextBoolean()?Side.BUY:Side.SELL);
      Order o = new Order(s, stk, 1+rand.nextInt(1_000), new BigDecimal(rand.nextInt(10000)), Instant.now(), "", "","");
      orders.add(o);
      //System.out.println("============\n"+market);
    }
    long start = System.currentTimeMillis();
    for (Order o : orders) {
      market.accept(o);
    }
    long end = System.currentTimeMillis();
    System.out.println("Time for "+i+"orders = "+(end - start)+ "ms");
    System.out.println("order book size: " + market.size(sti));
    
    //System.out.println("============\n"+market);
  }

  private static String newOrder(Gson gson, String request) { 
    
    Order o = gson.fromJson(request, Order.class);
    market.accept(o);
    return "";
  }
  private static void initialiseSomeOrders(Market market) {

    Order sellSti = new Order(Side.SELL, sti, 10, new BigDecimal("100"),Instant.now(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {}
    Order sellSti2 = new Order(Side.SELL, sti, 400, new BigDecimal("100"),Instant.now(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());

    Order sellSti3 = new Order(Side.SELL, sti, 100, new BigDecimal("200"),Instant.now(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());

    Order sellBrum = new Order(Side.SELL, sti, 10, new BigDecimal("10"), Instant.now(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());

    //    Order sellAber = new Order(Side.SELL, sti, 10, new BigDecimal(""));


    market.accept(sellSti);
    market.accept(sellSti2);
    market.accept(sellSti3);
    market.accept(sellBrum);

  }


}
