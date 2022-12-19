package portfolio;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

/**
 * Interface Features is the Controller interface that is implemented by class ControllerGUI.
 * This is responsible for mediating between the View and Model for tasks, executing the
 * Graphical User Interface.
 */
public interface Features {

  /**
   * Feature used to create a portfolio. It takes in username and portfoliotype as arguments.
   *
   * @param username       - username given by the user.
   * @param portfolioTypes - type of the portfolio the user wants to create.
   */
  void createPortfolio(String username, String portfolioTypes);

  /**
   * Feature that allows the user to buy a stock in a portfolio.
   *
   * @param username       - username given by the user.
   * @param portfolioTypes - type of portfolio the user wants to create.
   * @param stock          - the user should enter the stock symbol of the company they want
   *                       to purchase.
   * @param date           - the date in which the user wants to purchase the stock.
   * @param commission     - commission fee in which user wants the transaction to happen.
   * @param shares         - no of shares the user wants to buy of the particular company.
   * @throws IOException - when there is a wrong Input/Output, IOException is thrown.
   */
  void buyAStock(String username, String portfolioTypes, String stock, String date,
                 float commission, float shares) throws IOException;

  /**
   * Feature that allows the user to sell a stock in a portfolio.
   *
   * @param username       - username given by the user.
   * @param portfolioTypes - type of portfolio the user wants to create.
   * @param shares         - number of shares the user wants to sell.
   * @param date           - date in which the user wants to sell a stock.
   * @param pricePerShare  - price per share taken from the api.
   * @param stock          - stock symbol of the company the user wants to purchase the shares.
   * @param choiceDate     - (If any.) For example,If the user has bought the same stock in
   *                       2 different days,they are expected to give the choice of the date they
   *                       want to pick to sell their stock.
   * @param commission     - commission fee for the particular transaction the user wishes to give.
   * @throws IOException    - throws an error when there is an invalid Input or Output.
   * @throws ParseException - when the value does not parse correctly, parse exception is thrown.
   */

  void sellAStock(String username, String portfolioTypes, float shares,
                  String date, float pricePerShare, String stock,
                  String choiceDate, float commission) throws IOException, ParseException;

  /**
   * Feature to determine the value of the portfolio.
   *
   * @param date           the date for which the user wants to determine the value
   *                       of the portfolio.
   * @param username       - entered by the user.
   * @param portfolioTypes - the type of the portfolio the user wants to create.
   * @throws IOException    - throws an exception where there is aan Input/Output error.
   * @throws ParseException - throws an exception when a value cannot be passed.
   */
  void calculateValue(String date, String username, String portfolioTypes)
          throws IOException, ParseException;

  /**
   * Feature to get the cost basis till the date given by the user.
   *
   * @param username       - username entered by the user.
   * @param portfolioTypes - portfolio type the user wants to create.
   * @param date           - date till which the user wants to calculate the cost basis.
   * @throws ParseException - error thrown when a value cannot be parsed.
   */

  void costBasisCalculation(String username, String portfolioTypes, String date)
          throws ParseException;

  /**
   * Dialog box called inside calculate the value of portfolio function.
   *
   * @param valDialog Message passed to the dialog box.
   */
  void valueDialog(String valDialog);

  /**
   * Information Dialog box, called when an information needs to be shown.
   *
   * @param infoDialog Message passed to the dialog box.
   */
  void informationDialog(String infoDialog);

  /**
   * Feature which displays the portfolio content.
   *
   * @param username       - username given by the user.
   * @param portfolioTypes - portfoliotype the user wants to create.
   * @return returns the portfolio display string.
   * @throws IOException - thrown when there is an input/output exception.
   */

  String displayPortfolio(String username, String portfolioTypes) throws IOException;

  /**
   * Dialog box for portfolio display.
   *
   * @param dispDialog Message passed to the dialog box.
   */
  void portfolioDisplayDialog(String dispDialog);

  /**
   * Feature to calculate the dollar cost average.
   *
   * @param username               - username given by the user.
   * @param portfolioTypes         - portfoliotype the user wants to create.
   * @param dtPortfolioLastDate    - End date of the scheduled transaction.
   * @param dtPortfolioStartDate   - Start date of the scheduled transaction.
   * @param commissionFees         - commission fee in which the user wants to
   *                               perform the transaction.
   * @param totAmtInvested         - total amount the user wants to invest in the portfolio.
   * @param frequency              - no of days/ how frequent the user wants to schedule the
   *                               transaction in.
   * @param pairStockWithWeightage - stock with weightage entered by the user.
   * @return returns true or false.
   * @throws IOException    - exception is thrown when a wrong input or output is given.
   * @throws ParseException - exception is thrown when a value cannot be parsed.
   */
  String dollarCostAveragingCalculation(String username, String portfolioTypes,
                                        String dtPortfolioLastDate, String dtPortfolioStartDate,
                                        float commissionFees, float totAmtInvested,
                                        int frequency, String pairStockWithWeightage)
          throws IOException, ParseException;

  /**
   * Gets the username.
   *
   * @return - Returns the username.
   */
  String getUserName();

  /**
   * Checks if the portfolio exists or not.
   *
   * @param username      username the user enters.
   * @param portfolioType type of the portfolio the user wants to create.
   * @return - returns a boolean value if the portfolio exists or not.
   */
  boolean portfolioExists(String username, String portfolioType);

  /**
   * Function to set username.
   *
   * @param userName - userName contains the username entered in textfield.
   */
  void setUserName(String userName);

  /**
   * List of portfolio present in the path.
   *
   * @param username - username the user enters.
   * @return - returns the array of portfolio.
   */
  String[] portfolioList(String username);

  /**
   * Label Status of the stock and weightage entered by the user.
   *
   * @param username       - username the user enters.
   * @param portfolioTypes - portfoliotype the user wants to create.
   * @param stock          - ticker symbol of the stock.
   * @return returns the stockplusweightage string.
   */

  String setLabelStatus(String username, String portfolioTypes, String stock);

  /**
   * Displays the unique stock names in the portfolio.
   *
   * @param portfolioContent contents inside the portfolio.
   * @return returns the list of unique stock names.
   */

  Set<String> displayUniqueStockNames(String portfolioContent);

  /**
   * Function to check if the property file exists or not.
   *
   * @param username       username the user enters.
   * @param portfolioTypes portfoliotype the user wants to create.
   * @return returns a boolean value, true if the property file exists and vice versa.
   * @throws IOException throws an IOException when an invalid input/output is given.
   */
  boolean propertyFileExist(String username, String portfolioTypes) throws IOException;

  /**
   * Feature to implement investment strategy on a given portfolio.
   *
   * @param entirePortfolio         - display portfolio contents.
   * @param userName                - username the user enters.
   * @param portfolioType           - portfolio type the user wants to create.
   * @param inpDate                 - date in which the user wants to invest on.
   * @param pairStocksWithWeightage - stocks with weightage given by the user.
   * @param commissionFees          - commission fee the user wants to process the transaction with.
   * @param investment              - investment amount the user wants to give to
   *                                invest in the portfolio.
   * @throws ParseException - parse exception is thrown when a value cannot be parsed.
   * @throws IOException    - throws an IOException when an invalid input/output is given.
   */

  void investmentStrategyExisting(String entirePortfolio, String userName,
                                  String portfolioType, String inpDate,
                                  String pairStocksWithWeightage,
                                  float commissionFees, float investment)
          throws ParseException, IOException;

  /**
   * Property file values are loaded and shown in this function.
   *
   * @param username       username entered by the user.
   * @param portfolioTypes portfoliotype the user wants to create.
   * @return returns the string of values inside the property file.
   * @throws IOException throws an IOException when an invalid input/output is given.
   */
  String loadPropertyFileValues(String username, String portfolioTypes) throws IOException;

  /**
   * Displays the values inside the portfolio.
   *
   * @param username       username entered by the user.
   * @param portfolioTypes portfoliotype the user wants to create.
   * @return returns the display portfolio value.
   */

  String displayPortfolioTwo(String username, String portfolioTypes);

  /**
   * Feature to create a performance graph.
   *
   * @param username       username entered by the user.
   * @param portfolioTypes portfoliotype the user wants to create.
   * @param startDate      start date of the performance graph.
   * @param endDate        end date of the performance graph.
   * @return returns the performance graph values.
   * @throws ParseException parse exception is thrown when a value cannot be parsed.
   * @throws IOException    IO exception is thrown when the passed I/O value is invalid.
   */
  List<String> performanceGraph(String username, String portfolioTypes, String startDate,
                                String endDate) throws ParseException, IOException;


}
