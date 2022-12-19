package portfolio;

import java.io.IOException;

/**
 * Interface for the View.
 * The View is responsible for printing all the input prompts and the output after processing,
 * has a function that does the same.
 */
public interface PortfolioView {
  /**
   * It is a printer function.
   *
   * @param s parameter which is going to perform the System.out.print() function.
   * @throws IOException IOException is thrown when there are input output errors.
   */
  void printer(String s) throws IOException;

}
