package portfolio;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A public class written to fetch values from the AlphaVantage API using a unique key.
 */
public class ApiFetchClass {
  /**
   * Fetches the stock value from the API using a unique API key.
   *
   * @param stockSymbol stockSymbol is passed inside as a parameter to get input.
   * @return returns the whole outputStock from the API.
   * @throws IOException thrown when there are failed I/O Operations.
   */
  public String getStock(String stockSymbol) throws IOException {
    String apiKey = "ZKMMTO1ATDBLXH2K";
    URL url = null;
    String outputStock;
    try {
      url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + stockSymbol + "&apikey=" + apiKey + "&datatype=csv");

    } catch (MalformedURLException e) {
      throw new RuntimeException("the API has either changed or "
              + "no longer works");
    }
    InputStream in = null;
    StringBuilder output = new StringBuilder();
    try {
      in = url.openStream();
      int b;
      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + stockSymbol);
    }
    if (output == null) {
      throw new IllegalArgumentException("No price data found for " + stockSymbol);
    }
    outputStock = output.toString();
    return outputStock;
  }

}




