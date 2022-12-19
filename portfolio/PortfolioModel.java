package portfolio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Interface for the Model.
 * The Model contains all the functions that the user can access, as well as the
 * helper functions, that helps a user perform the features of the application.
 */
public interface PortfolioModel {

  /**
   * writeToPortfolio() function writes to the username and the type of the portfolio.
   *
   * @param username        input given by the user.
   * @param typeofPortfolio type of the stock portfolio.
   * @param newWrite        boolean value to for function overloading.
   */
  void writeToPortfolio(String username, String typeofPortfolio, boolean newWrite);

  /**
   * writeToInfPortfolio function writes to the username and type of portfolio.
   * This is for an Inflexible Portfolio.
   *
   * @param userName      input given by the user.
   * @param portfolioType type of the stock portfolio.
   */
  void writeToInfPortfolio(String userName, String portfolioType);

  /**
   * getStockValue() fetches the output stock for a particular stock/ticker symbol.
   *
   * @param stockSymbol the stock symbol or ticker symbol which is supposed to be given by the user.
   * @param noOfShares  no of shares the user wants to own.
   * @return returns "Found", if the stock symbol is found. If not found, returns "Invalid".
   * @throws IOException    thrown when there are failed I/O Operations.
   * @throws ParseException thrown when value is not parsed.
   */

  String getStockValue(String stockSymbol, float noOfShares) throws IOException, ParseException;

  /**
   * displayPortfolioNames() displays the names of the portfolios present in the given path.
   *
   * @param userName username given by the user.
   * @return returns the names of the portfolios present in the given path
   */

  String displayPortfolioNames(String userName);

  /**
   * checkPortfolio() checks if the portfolio is present in the path.
   *
   * @param userName      username inputted by the user.
   * @param portfolioType type of the portfolio the user wants to create.
   * @return returns portfolioExists which is a boolean value.
   */

  boolean checkPortfolio(String userName, String portfolioType);

  /**
   * The whole value of a portfolio is given based on the date entered by the user.
   *
   * @param foundStr    the strings found inside a portfolio.
   * @param noOfShare   no of shares the user wants to own.
   * @param totalAmount total amount = noOfShares * stockValue
   *                    (stockValue = closeValue of the day fetched from the api.)
   * @return returns the total value of the portfolio for a particular date.
   */

  float getPortfolioValue(String foundStr, String noOfShare, String totalAmount);

  /**
   * readPortfolio() reads the file line by line and returns the lines from the file.
   *
   * @param userName      name of the user given as an input by the user.
   * @param portfolioType type of the stock portfolio, a user will want to create.
   * @return returns the lines from the portfolio.
   */

  String readPortfolio(String userName, String portfolioType);

  /**
   * Returns the total value for the value of the portfolio, for flexible files.
   *
   * @param userGivenDate Date entered by the user to find the total value of the portfolio
   *                      on that date.
   * @param totFileStr    all the data present in the file.
   * @return returns the stringValue of the total amount of that day.
   * @throws IOException    thrown when there are failed I/O Operations.
   * @throws ParseException thrown when values are not parsed.
   */

  String processValueForFile(String userGivenDate, String totFileStr) throws IOException,
          ParseException;

  /**
   * Returns the total value for the value of the portfolio, for inflexible files.
   *
   * @param inpDate    date given by the user.
   * @param totFileStr all the data present in the file
   * @return returns the stringValue of the total amount of that day.
   * @throws IOException    thrown when there are failed I/O operations.
   * @throws ParseException thrown when values are not parsed.
   */
  String processValueForInflexibleFile(String inpDate, String totFileStr) throws IOException,
          ParseException;

  /**
   * The Function saves and retrieves portfolios.
   *
   * @param directory Directory of a particular file.
   * @return returns the lines from a portfolio.
   * @throws FileNotFoundException If the file is not found in the directory, a
   *                               FileNotFoundException is thrown.
   */

  String loadAndRetrievePortfolio(String directory) throws FileNotFoundException;

  /**
   * Creates an empty portfolio, in which stocks can be bought.
   *
   * @param userName      input given by the user.
   * @param portfolioType type of the portfolio the user wants to create.
   * @return returns Boolean value if portfolio is created.
   */
  boolean createPortfolio(String userName, String portfolioType);

  /**
   * This function returns the number of buys.
   *
   * @return returns the number of buys.
   */
  int getNoOfBuys();

  /**
   * This function returns the number of shares to sell.
   *
   * @return returns the number of shares to sell.
   */
  int getNoOfSharesToSell();

  /**
   * It returns the number of times, same stock that is bought in different days.
   * Gives the user the choice to select one of the date.
   *
   * @param stockSymbol      stockSymbol is the ticker symbol of the stock.
   * @param portfolioContent the content inside the portfolio.
   * @return returns the no of the time, same stock that is bought in different days.
   */
  String getBuysOfStock(String stockSymbol, String portfolioContent);

  /**
   * This function allows the user to buy stocks.
   *
   * @param portfolioChosen the portfolio which has been chosen in which the stock
   *                        will be added.
   * @param stockSymbol     it is the ticker symbol of a stock.
   * @param noOfShares      no of shares the user wants to buy.
   * @param inpDate         input date in which the user wants to buy the stock.
   * @param userName        username is a user input given by the user.
   * @param portfolioType   it is the type of the portfolio given by the user.
   * @param commissionFee   commissionFee is taken as an input in percentage format.
   * @return it returns the string to be written inside the portfolio.
   */
  String buyStock(String portfolioChosen, String stockSymbol, float noOfShares,
                  String inpDate, String userName, String portfolioType, float commissionFee);

  /**
   * Shows the performance of the portfolio.
   *
   * @param startDate Start date from which the user wants the performance of the portfolio.
   * @param endDate   End date till which the user wants the performance of the portfolio.
   * @return returns the performance graph.
   * @throws ParseException throws an exception which the date cannot be parsed.
   * @throws IOException    throws an IO exception when there are input, output errors.
   */
  String showPerformance(String startDate, String endDate) throws ParseException, IOException;

  /**
   * Creates a list of the contents inside the portfolio, to sort them date wise.
   *
   * @param portfolioContent Contents inside the portfolio.
   */
  void createListOfPortfolio(String portfolioContent);

  /**
   * This function enables the user to sell a stock.
   *
   * @param userName         input given by the user.
   * @param portfolioType    type of portfolio the user wants to create.
   * @param portfolioContent contents inside the portfolio.
   * @param noOfShares       no of shares the user wants to sell.
   * @param inpDate          the date in which the user wants to sell.
   * @param stockSymbol      ticker symbol of the stock which the user wants to sell.
   * @param choiceDate       if a stock is bought in 2 or more different dates,
   *                         the user can choose a date from the given 2 to sell one of
   *                         the stock which is bought.
   * @param commissionFee    commissionFee is taken as an input in percentage format.
   * @return returns retStr, which tells that the stock is sold successfully or not.
   * @throws IOException    thrown when there are failed I/O Operations.
   * @throws ParseException throws an exception if the date cannot be parsed.
   */
  String sellStock(String userName, String portfolioType,
                   String portfolioContent, float noOfShares, String inpDate,
                   String stockSymbol, String choiceDate, float commissionFee)
          throws IOException, ParseException;

  /**
   * Calculates the total amount of money invested in the portfolio until the given date.
   *
   * @param costDate contains the date in which the user wants to calculate the cost basis.
   * @return returns the cost basis until the user wants until the entered date.
   * @throws ParseException throws an exception if the date cannot be parsed.
   */
  String calculateCostBasis(String costDate) throws ParseException;

  /**
   * Checks if the entered date is valid.
   *
   * @param inpDate date given as input.
   * @return returns if the date is valid or not.
   */
  boolean dateValidation(String inpDate);

  /**
   * Checks if the entered date is a future date.
   *
   * @param inpDate date given as input.
   * @return returns if the date is future or not
   * @throws IOException IO exception is thrown when there is an error with the Input ot Output.
   */
  boolean futureDateValidation(String inpDate) throws IOException;

  /**
   * Function to get the amount.
   *
   * @param totAmtInvested total amount invested in the portfolio.
   * @param weightage      weightage for a specific stock symbol.
   * @return returns the amount.
   */
  Float getAmount(float totAmtInvested, float weightage);

  /**
   * Function to get stock value of a particular stock.
   *
   * @param stockSymbol   stock symbol of a particular company.
   * @param amount        amount of the stock.
   * @param commissionFee commission fee the user wants to process the transaction in.
   * @param toGet         to get the boolean value.
   * @return returns the stock value.
   * @throws IOException    exception thrown when there is an invalid Input or Output.
   * @throws ParseException exception thrown when a value cannot be parsed.
   */
  String getStockValueForAvgPortfolio(String stockSymbol, float amount, float commissionFee,
                                      boolean toGet) throws IOException, ParseException;

  /**
   * Function to get the last date of the portfolio.
   *
   * @param portfolioContent contents inside the portfolio.
   * @return returns the last date of portfolio.
   */
  String getLasteDateOfPortfolio(String portfolioContent);

  /**
   * Function to get the initial purchase date.
   *
   * @param displayPortfolio contents inside the portfolio.
   * @return returns the initial purchase date.
   */
  String getInitialPurchaseDate(String displayPortfolio);

  /**
   * Function to get the unique stock names.
   *
   * @param portfolioContent contents inside the portfolio.
   * @return returns the list of unique stock names.
   */
  Set<String> getUniqueStockNames(String portfolioContent);

  /**
   * Function to check between dates.
   *
   * @param dateToCheck date which is supposed to be checked.
   * @param startDate   start date the user gives.
   * @param endDate     end date the user gives.
   * @return returns a boolean value.
   */
  boolean checkBetween(Date dateToCheck, Date startDate, Date endDate);

  /**
   * Creates a date range wise list.
   *
   * @param startDate start date of the range.
   * @param endDate   end date of the range.
   * @param nTotal    total no of dates.
   * @return returns the date range list.
   * @throws ParseException Exception is passed when a value cannot be parsed.
   */
  List<String> createDateRangeList(String startDate, String endDate, int nTotal)
          throws ParseException;

  /**
   * Checks if the property files exists.
   *
   * @param userName      username entered by the portfolio.
   * @param portfolioType portfolio type the user wants to create.
   * @return returns boolean value, true or false.
   * @throws IOException exception is thrown when an invalid input or output is entered.
   */
  boolean isPropertyFileExists(String userName, String portfolioType) throws IOException;

  /**
   * Dollar cost Averaging feature in a portfolio.
   *
   * @param entirePortfolio        displays the content inside a portfolio.
   * @param userName               username entered by the user.
   * @param portfolioType          type of portfolio the user wants to create.
   * @param startDate              start date of the portfolio.
   * @param endDate                end date of the portfolio.
   * @param pairStockWithWeightage pairs stock with the weightage given by the user.
   * @param commissionFees         commission fees through which the user wants
   *                               the transaction to happen.
   * @param totAmtInvested         total amount invested by the user.
   * @param frequency              frequency in which the user wants to invest.
   * @return returns true or false, depending on the success or failure of transaction.
   * @throws IOException    thrown when there is an invalid Input or Output entered.
   * @throws ParseException exception thrown when a value cannot be parsed.
   */
  String dollarCostAveraging(String entirePortfolio, String userName, String portfolioType,
                             String startDate, String endDate, String pairStockWithWeightage,
                             Float commissionFees, Float totAmtInvested, int frequency)
          throws IOException, ParseException;

  /**
   * Difference in value of stock given.
   *
   * @param date       date in which the value is bought.
   * @param stockName  name of the stock which has been purchased.
   * @param noOfShares no of shares the stock is bought on.
   * @param toBuy      boolean value.
   * @return returns a string which contains the value.
   * @throws IOException    Input/Output exception passed when there is an invalid input.
   * @throws ParseException exception passed when a value cannot be parsed.
   */
  String differenceInValueOfStock(String date, String stockName, String noOfShares,
                                  boolean toBuy) throws IOException, ParseException;

  /**
   * checks if the username exists or not.
   *
   * @param userName username the user enters.
   * @return returns true or false.
   */
  boolean checkUserName(String userName);

  /**
   * Investment strategy on an existing portfolio.
   *
   * @param entirePortfolio         the content inside the entire portfolio.
   * @param userName                username entered by the user.
   * @param portfolioType           portfolio type the user wants to create.
   * @param inpDate                 date given by the user to invest on.
   * @param pairStocksWithWeightage stocks and weightages the user gives.
   * @param commissionFees          commission fees through which the user wants
   *                                the transaction to happen.
   * @param investment              Amount which the user wants to invest.
   * @return returns the string of investment strategy.
   * @throws ParseException exception is thrown when a value cannot be parsed.
   * @throws IOException    exception is thrown when an invalid input or output date is given.
   */
  String investmentStrategyOnExistingPortfolios(String entirePortfolio, String userName,
                                                String portfolioType, String inpDate,
                                                String pairStocksWithWeightage,
                                                float commissionFees, float investment)
          throws ParseException, IOException;

  /**
   * Function to read the entire property file.
   *
   * @param username      username the user enters.
   * @param portfolioType portfoliotype the user wants to create.
   * @return returns the value inside the property file.
   * @throws IOException IOException is thrown when an invalid input or output value is given.
   */
  String readEntirePropertyFile(String username, String portfolioType) throws IOException;

  /**
   * Function that gets the list of results for the GUI.
   *
   * @return returns the list of results for the GUI
   */
  List<String> getresultListforGUI();
}