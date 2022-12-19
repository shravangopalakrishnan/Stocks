package portfolio;

import java.io.IOException;
import java.text.ParseException;

/**
 * Interface for the Controller.
 * It contains the Main Menu to perform features of the application.
 * The Controller contains the objects of both the Model and the View.
 * It has the scanner to get input from the user, and passes the input to the Model functions.
 * The controller is responsible for fetching commands from the View and sending
 * it to the functions in Model, and returning the output to the View.
 * Controller also handles the exceptions and validation of all input.
 */

public interface PortfolioController {
  /**
   * getInput() contains the Menu of the whole application.
   *
   * @throws IOException    thrown when there are failed I/O Operations.
   * @throws ParseException throws an error when a value cannot be parsed.
   */
  void getInput() throws IOException, ParseException;

  /**
   * getStockMenu() contains the menu for adding more stocks to the portfolio
   * or to save the portfolio.
   *
   * @return returns boolean false.
   * @throws IOException IOException is thrown for input output errors.
   */
  boolean getStockMenu() throws IOException;

  /**
   * Displays the contents of a portfolio.
   *
   * @return returns the contents of the portfolio.
   * @throws IOException    thrown when there are failed I/O Operations.
   * @throws ParseException throws an exception when cannot be parsed.
   */
  String displayContentsOfPortfolio() throws IOException, ParseException;

}
