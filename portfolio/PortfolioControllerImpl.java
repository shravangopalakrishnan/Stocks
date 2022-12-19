package portfolio;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * This is a class implementation for PortfolioController interface.The controller is responsible
 * for giving tasks to the Model and mediating between the Model and View.
 */
public class PortfolioControllerImpl extends ApiFetchClass implements PortfolioController {

  private PortfolioModel pfModel;
  private PortfolioView pfView;
  String userName;
  String retStr = "";

  String strToCheckStockName = "";
  String portfolioType;
  String stockSymbol;
  String inpDate;
  float commissionFees;
  float noOfShares;
  float weightage;
  float totAmtInvested;

  int frequency;
  int noOfDays;
  final Readable in;
  boolean bProfileBreak = false;

  /**
   * It is the constructor of the portfolio controller class.
   *
   * @param pfModel object of model interface.
   * @param pfView  object of view interface.
   * @param in      for input.
   */

  public PortfolioControllerImpl(PortfolioModel pfModel, PortfolioView pfView, Readable in) {
    this.pfModel = pfModel;
    this.pfView = pfView;
    this.in = in;
  }

  @Override
  public void getInput() throws IOException, ParseException, InputMismatchException {

    pfView.printer("\n--- CHOOSE ANY OF THE MENU OPTIONS FROM BELOW --- \n"
            + "1. Create a Portfolio \n"
            + "2. Buy Stock \n"
            + "3. Sell Stock \n"
            + "4. Strategize a Portfolio \n"
            + "5. Portfolio Composition \n"
            + "6. Value of Portfolio on a certain date. \n"
            + "7. Cost Basis of a Portfolio \n"
            + "8. Performance of a Portfolio \n"
            + "9. Save / Load and Retrieve Portfolio \n"
            + "10. Exit \n");
    Scanner scanner = new Scanner(in);
    boolean bpfExists = false;
    int choice = scanner.nextInt();
    switch (choice) {

      case 1:
        pfView.printer("\nWhat type of Portfolio would you like to create? \n"
                + "1. Flexible Portfolio \n"
                + "2. Inflexible Portfolio \n");
        Scanner sc = new Scanner(in);
        int option = sc.nextInt();
        switch (option) {

          case 1:
            Scanner myObj = new Scanner(in);
            creationOfFlexiblePortfolio(myObj, false);
            getInput();
            break;
          case 2:
            Scanner obj = new Scanner(in);
            creationOfInflexiblePortfolio(obj, false);
            getInput();
            break;
          case 3:
            break;
          default:
        }
        break;
      case 2:
        Scanner buyObj = new Scanner(in);
        buyStockIntoPortfolio(buyObj);
        break;
      case 3:
        Scanner selectObj = new Scanner(in);
        sellStockFromPortfolio(selectObj);
        break;
      case 4:
        pfView.printer("\nWhat would you like to do ? \n"
                + "1. Deploy Investment Strategy to an existing portfolio \n"
                + "2. Deploy Dollar-Cost Averaging Strategy to a New portfolio \n"
                + "3. Deploy Dollar-Cost Averaging Strategy to an existing portfolio \n");
        Scanner object = new Scanner(in);
        int inp = object.nextInt();
        switch (inp) {
          case 1:
            Scanner objInv = new Scanner(in);
            investingStrategyOnExistingPortfolio(objInv);
            getInput();
            break;
          case 2:
            Scanner object1 = new Scanner(in);
            dollarCostAvgOnNewPortfolio(object1);
            getInput();
            break;
          case 3:
            Scanner object2 = new Scanner(in);
            dollarCostAvgOnExstPortfolio(object2);
            break;
          default:
        }
        break;

      case 5:
        displayContentsOfPortfolio();
        getInput();
        break;
      case 6:
        pfView.printer("\nWhich type of Portfolio would you like to check the Value of? \n"
                + "1. Flexible Portfolio \n"
                + "2. Inflexible Portfolio \n");
        Scanner scanning = new Scanner(in);
        int opt = scanning.nextInt();
        switch (opt) {
          case 1:
            Scanner scan1 = new Scanner(in);
            valueOfFlexiblePortfolio(scan1);
            getInput();
            break;
          case 2:
            Scanner scan = new Scanner(in);
            valueOfInflexiblePortfolio(scan);
            break;
          case 3:
            break;
          default:
        }
        break;
      case 7:
        Scanner obj1 = new Scanner(in);
        costBasisOfPortfolio(obj1);
        break;
      case 8:
        Scanner obj2 = new Scanner(in);
        performanceOfPortfolio(obj2);
        break;
      case 9:
        Scanner scanObj = new Scanner(in);
        persistPortfolio(scanObj);
        break;
      case 10:
        break;
      default:
    }
  }

  @Override
  public boolean getStockMenu() throws IOException {
    pfView.printer("1. Add more Stocks to this Portfolio \n"
            + "2. Save Portfolio \n");
    Scanner scanner = new Scanner(in);
    int choice = scanner.nextInt();
    switch (choice) {
      case 1:
        Scanner obj = new Scanner(in);
        addStocksToInfPortfolio(obj);
        break;
      case 2:
        pfModel.writeToInfPortfolio(userName, portfolioType);
        pfView.printer("Portfolio created successfully! \n");
        bProfileBreak = true;
        break;
      case 3:
        break;
      default:
    }
    return false;
  }

  @Override
  public String displayContentsOfPortfolio() throws IOException, ParseException {
    Scanner scan = new Scanner(in);
    try {
      pfView.printer("Enter username: \n");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    userName = scan.nextLine();
    String portfolioNames = pfModel.displayPortfolioNames(userName);
    if (portfolioNames.equals("")) {
      pfView.printer("Username does not exist.\n");
      getInput();
    }
    pfView.printer("Please select one of the following portfolio "
            + "[Enter only the type, example: If 'user.IT.txt', enter IT] \n" + portfolioNames);
    portfolioType = scan.nextLine();
    String displayPortfolio = pfModel.readPortfolio(userName, portfolioType);
    if (displayPortfolio.equals("")) {
      pfView.printer("Portfolio type does not exist! \n");
      getInput();
    }
    pfView.printer(displayPortfolio);
    return displayPortfolio;
  }

  /**
   * This method takes in the content of a portfolio and returns the unique stock names
   * that are contained in it.
   * @param portfolioContent content inside portfolio.
   * @return returns set string.
   */
  public Set<String> displayUniqueStockNames(String portfolioContent) {
    Set<String> dispStockNames;

    dispStockNames = pfModel.getUniqueStockNames(portfolioContent);
    return dispStockNames;

  }

  private void addStocksToInfPortfolio(Scanner obj) throws IOException {
    pfView.printer("Enter Stock / Ticker Symbol: \n");

    stockSymbol = obj.nextLine();
    if (strToCheckStockName.contains(stockSymbol)) {
      pfView.printer("You have already bought this stock.. please try another \n");
      getStockMenu();
      if (bProfileBreak) {
        return;
      }
    }
    strToCheckStockName += stockSymbol + ",";

    pfView.printer("Enter Number of Shares you wish to buy: \n");
    try {
      noOfShares = obj.nextInt();
      if (noOfShares <= 0) {
        pfView.printer("Invalid Input \n");
        getStockMenu();
      }
    } catch (java.util.InputMismatchException jui) {

      pfView.printer("Invalid Input \n");
      getStockMenu();
    }
    try {
      retStr = pfModel.getStockValue(stockSymbol, noOfShares);
    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }
    if (!retStr.equals("Found")) {
      pfView.printer(retStr);
    }
    bProfileBreak = false;
    boolean kk = getStockMenu();
    if (bProfileBreak) {
      return;
    }
  }

  private void creationOfFlexiblePortfolio(Scanner myObj, Boolean bpfExists)
          throws IOException, ParseException {
    String retStr = "";
    pfView.printer("Enter username: \n");
    userName = myObj.nextLine();
    pfView.printer("Enter Portfolio type (such as IT, Health, FMCG, etc.) \n");
    portfolioType = myObj.nextLine();
    bpfExists = pfModel.checkPortfolio(userName, portfolioType);
    if (bpfExists) {
      pfView.printer("Portfolio Already Exists!! Please create different Portfolio.. \n");
      getInput();
    } else {
      boolean bCreated = pfModel.createPortfolio(userName, portfolioType);
      pfView.printer("Your Portfolio created Successfully! You may start buying stocks!");
    }
  }

  private void creationOfInflexiblePortfolio(Scanner obj, Boolean bpfExists)
          throws IOException, ParseException {
    String returnStr = "";
    pfView.printer("Enter username: \n");
    userName = obj.nextLine();
    pfView.printer("Enter Portfolio type (such as IT, Health, FMCG, etc.) \n");
    portfolioType = obj.nextLine();
    bpfExists = pfModel.checkPortfolio(userName, portfolioType);
    if (bpfExists) {
      pfView.printer("Portfolio Already Exists!! Please create different Portfolio.. \n");
      getInput();
    }
    pfView.printer("Enter Stock / Ticker Symbol: \n");
    stockSymbol = obj.nextLine();
    strToCheckStockName += stockSymbol + ",";

    pfView.printer("Enter Number of Shares you wish to buy: \n");
    try {
      noOfShares = obj.nextFloat();
    } catch (java.util.InputMismatchException jui) {
      pfView.printer("Invalid Input \n");
      getInput();
    }
    try {
      returnStr = pfModel.getStockValue(stockSymbol, noOfShares);

    } catch (Exception illegal) {
      pfView.printer(illegal.getMessage());
    }
    if (!returnStr.equals("Found")) {
      pfView.printer(returnStr);
    }
    boolean bk = getStockMenu();
  }

  private void processBuyStockIntoPortfolio(Scanner pBuyObj, String portfolioChosen)
          throws IOException, ParseException {
    pfView.printer("\nEnter Stock / Ticker Symbol to buy into this portfolio:  \n");
    stockSymbol = pBuyObj.nextLine();
    strToCheckStockName += stockSymbol + ",";
    pfView.printer("Enter the Date you would like to buy this Stock: [yyyy-mm-dd] \n");
    inpDate = pBuyObj.nextLine();
    if (!pfModel.dateValidation(inpDate)) {
      pfView.printer("invalid date format! please enter as yyyy-mm-dd");
      getInput();
    }
    if (pfModel.futureDateValidation(inpDate)) {
      pfView.printer("Its a future date, cant buy stocks in future dates!\n");
      getInput();
    }
    pfView.printer("Enter the Commission fee for this transaction: [0-3%] \n");
    commissionFees = pBuyObj.nextFloat();
    if (commissionFees < 0 || commissionFees > 3) {
      pfView.printer("Commission fee should be within 0% to 3% of transaction amount. "
              + "Please enter valid fee! \n");
      return;
    }
    pfView.printer("Enter Number of Shares you wish to buy: \n");
    try {
      noOfShares = pBuyObj.nextInt();
      if (noOfShares <= 0) {
        pfView.printer("Invalid Input \n");
        getInput();
      }
    } catch (java.util.InputMismatchException jui) {
      pfView.printer("Invalid Input \n");
      getInput();
    }
    try {
      String returnString = pfModel.buyStock(portfolioChosen, stockSymbol, noOfShares,
              inpDate, userName, portfolioType, commissionFees);
      pfView.printer(returnString + "\n");
    } catch (Exception illegal) {
      pfView.printer("The Stock Symbol does not exist!\n");
      getInput();
    }
    bProfileBreak = false;
    boolean kk = userMenu();
    if (bProfileBreak) {
      return;
    }
  }

  private void buyStockIntoPortfolio(Scanner buyObj) throws IOException, ParseException {
    String portfolioChosen = displayContentsOfPortfolio();
    if (portfolioChosen.contains("Commission Fee") || portfolioChosen.contains("Commission")) {
      processBuyStockIntoPortfolio(buyObj, portfolioChosen);
      getInput();
    } else {
      pfView.printer("Cannot buy stocks in this portfolio! Try a different portfolio");
      return;
    }

  }

  private void sellStockFromPortfolio(Scanner selectObj) throws IOException, ParseException {
    String portfolioContent = displayContentsOfPortfolio();

    if (portfolioContent.contains("Commission Fee")
            || portfolioContent.contains("Commission")) {
      pfView.printer("Enter the Stock Symbol you would like to sell: \n");
      stockSymbol = selectObj.nextLine();
      String retString = pfModel.getBuysOfStock(stockSymbol, portfolioContent);
      int noOfBuys = pfModel.getNoOfBuys();
      String choiceDate = "";

      if (noOfBuys == 0) {
        pfView.printer("Sorry! The stock given not in your portfolio!");
        getInput();
      }
      if (noOfBuys > 1) {
        pfView.printer("You have purchased this Stock " + noOfBuys + " times \n");
        pfView.printer(retString);
        pfView.printer("Which among the Dates / Purchases from above would you"
                + " like to sell this Stock from?: [yyyy-mm-dd] \n");
        choiceDate = selectObj.nextLine();

      }
      pfView.printer("\nEnter the Date you would like to sell this Stock: [yyyy-mm-dd] \n");
      inpDate = selectObj.nextLine();

      if (!pfModel.dateValidation(inpDate)) {
        pfView.printer("invalid date format! please enter as [yyyy-mm-dd]\n");
        getInput();
      }
      if (pfModel.futureDateValidation(inpDate)) {
        pfView.printer("Its a future date, cant sell stocks in future dates!\n");
        getInput();
      }
      if (noOfBuys == 1) {
        choiceDate = "0000-00-00";
      }
      int noOfSharesToSell = pfModel.getNoOfSharesToSell();
      if (noOfSharesToSell == 0) {
        pfView.printer("You have already sold all the shares of this Stock\n");
        getInput();
      }
      pfView.printer("How many Shares of this Stock would you like to sell?\n");

      try {
        noOfShares = selectObj.nextInt();
        if (noOfShares <= 0) {
          pfView.printer("Invalid Input \n");
          getInput();
        }
        if (noOfShares > noOfSharesToSell) {
          pfView.printer("You have given more than the number of stocks available to sell!\n");
          getInput();
        }
      } catch (java.util.InputMismatchException jui) {
        pfView.printer("Invalid Input \n");
        getInput();
      }
      pfView.printer("Enter the Commission fee for this transaction [0-3%] \n");
      commissionFees = selectObj.nextFloat();
      if (commissionFees < 0 || commissionFees > 3) {
        pfView.printer("Commission fee should be within 0% to 3% of transaction amount. "
                + "Please enter valid fee! \n");
        return;
      }
      try {
        String retStr = pfModel.sellStock(userName, portfolioType, portfolioContent,
                noOfShares, inpDate, stockSymbol, choiceDate, commissionFees);
        pfView.printer(retStr + "\n");
      } catch (Exception illegal) {

        pfView.printer("The Stock Symbol does not exist!\n");
      }
      getInput();
    } else {
      pfView.printer("Cannot sell stocks from this portfolio! Try a different portfolio\n");
      return;
    }
  }

  private void valueOfFlexiblePortfolio(Scanner scan1) throws IOException, ParseException {
    String portfolioDisplay = displayContentsOfPortfolio();
    pfModel.createListOfPortfolio(portfolioDisplay);
    pfView.printer("Enter the date to compare [yyyy-mm-dd]: \n");
    inpDate = scan1.nextLine();
    if (!pfModel.dateValidation(inpDate)) {
      pfView.printer("invalid date format! please enter as yyyy-mm-dd\n");
      getInput();
    }
    if (pfModel.futureDateValidation(inpDate)) {
      pfView.printer("Its a future date! Cant find value of portfolio for future date!");
      getInput();
    }
    if (portfolioDisplay.contains("Commission Fee")) {
      String value = pfModel.processValueForFile(inpDate, portfolioDisplay);
      pfView.printer("Total amount of portfolio on: " + inpDate + " is: $" + value + "\n");
      getInput();
    } else {
      pfView.printer("Inflexible Portfolio! "
              + "Cannot take the value of portfolio in this case.\n");
      return;
    }
  }

  private void valueOfInflexiblePortfolio(Scanner scan) throws IOException, ParseException {
    String displayPortfolio = displayContentsOfPortfolio();
    pfView.printer("Enter the date to compare(yyyy-mm-dd): \n");
    String inpDate = scan.nextLine();
    if (displayPortfolio.contains("Commission Fee")) {
      pfView.printer("You are trying to get the value of a flexible portfolio."
              + " Please try again!\n");
      getInput();
    }
    if (!pfModel.dateValidation(inpDate)) {
      pfView.printer("invalid date format! please enter as [yyyy-mm-dd]\n");
      getInput();
    }
    if (pfModel.futureDateValidation(inpDate)) {
      pfView.printer("Its a future date! Cant find value of portfolio for future date!\n");
      getInput();
    }
    String value1 = pfModel.processValueForInflexibleFile(inpDate, displayPortfolio);
    pfView.printer("Total amount of portfolio on: " + inpDate + " is: $" + value1 + "\n");
    getInput();
  }

  private void costBasisOfPortfolio(Scanner obj1) throws IOException, ParseException {
    String portFolioContent = displayContentsOfPortfolio();
    pfModel.createListOfPortfolio(portFolioContent);
    pfView.printer("Enter the date until which, you want to calculate "
            + "the cost basis [yyyy-mm-dd]:\n");
    String costDate = obj1.nextLine();
    if (!pfModel.dateValidation(costDate)) {
      pfView.printer("invalid date format! please enter as [yyyy-mm-dd]\n");
      getInput();
    }
    String result = pfModel.calculateCostBasis(costDate);
    pfView.printer(result);
    getInput();
  }

  private void performanceOfPortfolio(Scanner obj2) throws IOException, ParseException {
    String contentsOfSelectedPortfolio = displayContentsOfPortfolio();
    pfModel.createListOfPortfolio(contentsOfSelectedPortfolio);
    pfView.printer("Enter the start date [yyyy-mm-dd] for the performance graph:\n");
    String startDate = obj2.nextLine();
    if (!pfModel.dateValidation(startDate)) {
      pfView.printer("invalid date format! please enter as [yyyy-mm-dd]\n");
      getInput();
    }
    pfView.printer("Enter the end date [yyyy-mm-dd] for the performance graph:\n");
    String endDate = obj2.nextLine();
    if (!pfModel.dateValidation(endDate)) {
      pfView.printer("invalid date format! please enter as [yyyy-mm-dd]\n");
      getInput();
    }
    if (startDate.equals(endDate)) {
      pfView.printer("The given dates do not have an interval!"
              + " Please enter valid dates for interval.");
      getInput();
    }
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date1 = simpleDateFormat.parse(startDate);
    Date date2 = simpleDateFormat.parse(endDate);
    if ((date1.compareTo(date2) > 0)) {
      pfView.printer("The given Start Date comes after the given End Date!"
              + " Please enter a valid interval.");
      getInput();
    }

    String chart = pfModel.showPerformance(startDate, endDate);
    pfView.printer(chart);
    getInput();
  }

  private void persistPortfolio(Scanner scanObj) throws IOException, ParseException {

    pfView.printer("Enter File Name to Save / Load and Retrieve: [with .txt extension] \n");
    String directory = scanObj.nextLine();
    String fileContent = pfModel.loadAndRetrievePortfolio(directory);
    pfView.printer(fileContent);
    getInput();
  }


  private void processingDollarCostAvg(Scanner objInp, String contentsOfSelectedPortfolio)
          throws IOException, ParseException {
    String strProcessed = "";
    String[] separatePortfolio = contentsOfSelectedPortfolio.split("\r\n|\r|\n");
    if (separatePortfolio.length == 1) {
      pfView.printer("Portfolio contains no Stock! Please buy stocks and try this feature");
      getInput();
    }
    Set<String> stockNamesToDisplay = displayUniqueStockNames(contentsOfSelectedPortfolio);
    int nOOfUniqueStocks = stockNamesToDisplay.size();
    String strPortfolioLastDate = pfModel.getLasteDateOfPortfolio(contentsOfSelectedPortfolio);

    Date dtPortfolioLastDate;

    boolean bportExists = pfModel.isPropertyFileExists(userName, portfolioType);
    if (bportExists) {
      strProcessed = pfModel.dollarCostAveraging(contentsOfSelectedPortfolio, userName,
              portfolioType, "", "", "", commissionFees,
              totAmtInvested, frequency);

    }

    if (strProcessed.equals("true")) {
      pfView.printer("This Portfolio is processed already for Dollar Cost Averaging\n");
      getInput();
    }
    if (strProcessed.contains("This Portfolio is processed already for Today")) {
      pfView.printer(strProcessed);
      getInput();
    }
    String startDate = "";
    pfView.printer("\nEnter the start date [yyyy-mm-dd] of Strategy Period :\n");
    try {
      startDate = objInp.nextLine();
    } catch (java.util.InputMismatchException jui) {
      pfView.printer("Exception... \n");
      getInput();
    }
    if (!pfModel.dateValidation(startDate)) {
      pfView.printer("invalid date format! please enter as [yyyy-mm-dd]\n");
      getInput();
    }
    pfView.printer("\nEnter the end date [yyyy-mm-dd] of Strategy Period:"
            + " [if no end date, please enter 9999-12-31]\n");
    String endDate = objInp.nextLine();
    if (!pfModel.dateValidation(endDate)) {
      pfView.printer("Invalid date format! please enter as [yyyy-mm-dd]\n");
      getInput();
    }
    if (startDate.equals(endDate)) {
      pfView.printer("\nThe given dates do not have an interval!"
              + " Please enter valid dates for interval.");
      getInput();
    }
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date1 = simpleDateFormat.parse(startDate);
    Date date2 = simpleDateFormat.parse(endDate);
    noOfDays = (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
    dtPortfolioLastDate = simpleDateFormat.parse(strPortfolioLastDate);
    if (date1.before(dtPortfolioLastDate)) {
      pfView.printer("\n The Start Date given is before the last bought date of the "
              + "portfolio stock. Please enter valid Date after " + strPortfolioLastDate);
      getInput();
    }
    if ((date1.compareTo(date2) > 0)) {
      pfView.printer("\nThe given Start Date comes after the given End Date!"
              + " Please enter a valid interval.");
      getInput();
    }

    pfView.printer("\nEnter the Total Amount you want to invest in this portfolio: ");
    try {
      totAmtInvested = objInp.nextFloat();
    } catch (java.util.InputMismatchException jui) {
      pfView.printer("Invalid Input \n");
      getInput();
    }

    pfView.printer("\nHow frequent do you want to invest this amount in this Portfolio?"
            + " [Please enter the number of days]\n");
    try {
      frequency = objInp.nextInt();
    } catch (java.util.InputMismatchException jui) {
      pfView.printer("Invalid Input \n");
      getInput();
    }

    if (frequency > noOfDays) {
      pfView.printer("The frequency of days you have given is exceeding the time range! "
              + "Please enter valid frequency");
      getInput();
    }

    pfView.printer("\nEnter the weightage you would want to give to the stocks "

            + stockNamesToDisplay + " as Below\n");
    Iterator newData = stockNamesToDisplay.iterator();
    String pairStockWithWeightage = "";
    int nPercent = 0;
    int nthisStock = 0;
    String stockName = "";
    for (int i = 0; i < nOOfUniqueStocks; i++) {
      stockName = newData.next().toString();
      pfView.printer("\n For Stock->" + stockName + "--> ");
      nthisStock = objInp.nextInt();
      nPercent += nthisStock;
      pairStockWithWeightage += stockName + "," + nthisStock + "\r\n";
    }
    if (nPercent != 100) {
      pfView.printer("Sorry! The Total Percentage should be 100%. "
              + "Your total percentage input-->" + nPercent);
      getInput();
    }
    pfView.printer("The Stockname and the Weightage = \n" + pairStockWithWeightage);


    pfView.printer("\nEnter the Commission fee for this transaction [0-3%] \n");
    commissionFees = objInp.nextFloat();

    strProcessed = pfModel.dollarCostAveraging(contentsOfSelectedPortfolio, userName, portfolioType,
            startDate, endDate, pairStockWithWeightage, commissionFees, totAmtInvested, frequency);
    if (strProcessed.equals("true")) {
      pfView.printer("Dollar Cost Averaging Done Successfully!");
      getInput();
    } else {
      pfView.printer(strProcessed);
      getInput();
    }
  }

  private boolean userMenu() throws IOException, ParseException {
    pfView.printer("1. Add more Stocks to this Portfolio \n"
            + "2. Save this Portfolio \n");
    Scanner scanner = new Scanner(in);
    int choice = scanner.nextInt();
    switch (choice) {
      case 1:
        Scanner obj = new Scanner(in);
        String contentsOfPortfolio = pfModel.readPortfolio(userName, portfolioType);
        processBuyStockIntoPortfolio(obj, contentsOfPortfolio);
        //userMenu();
        break;
      case 2:
        pfModel.writeToPortfolio(userName, portfolioType, false);
        pfView.printer("Portfolio saved successfully! \n");
        bProfileBreak = false;
        break;
      case 3:
        break;
      default:
    }
    return false;
  }

  private void dollarCostAvgOnNewPortfolio(Scanner object1) throws IOException, ParseException {

    creationOfFlexiblePortfolio(object1, false);
    String contentsOfPortfolio = pfModel.readPortfolio(userName, portfolioType);
    processBuyStockIntoPortfolio(object1, contentsOfPortfolio);
    object1.nextLine();
    contentsOfPortfolio = pfModel.readPortfolio(userName, portfolioType);
    processingDollarCostAvg(object1, contentsOfPortfolio);

  }


  private void dollarCostAvgOnExstPortfolio(Scanner object2) throws IOException, ParseException {
    String returnStr = "";
    String contentsOfSelectedPortfolio = displayContentsOfPortfolio();
    processingDollarCostAvg(object2, contentsOfSelectedPortfolio);
  }

  private void investingStrategyOnExistingPortfolio(Scanner objInv)
          throws IOException, ParseException {
    String contentsOfSelectedPortfolio = displayContentsOfPortfolio();
    String strProcessed = "";
    String[] separatePortfolio = contentsOfSelectedPortfolio.split("\r\n|\r|\n");
    if (separatePortfolio.length == 1) {
      pfView.printer("Portfolio contains no Stock! Please buy stocks and try this feature");
      getInput();
    }
    Set<String> stockNamesToDisplay = displayUniqueStockNames(contentsOfSelectedPortfolio);
    int nOOfUniqueStocks = stockNamesToDisplay.size();
    String strPortfolioLastDate = pfModel.getLasteDateOfPortfolio(contentsOfSelectedPortfolio);


    pfView.printer("\nEnter the Total Amount you want to invest in this portfolio: ");
    try {
      totAmtInvested = objInv.nextFloat();
    } catch (java.util.InputMismatchException jui) {
      pfView.printer("Invalid Input \n");
      getInput();
    }
    objInv.nextLine();
    pfView.printer("\nWhen do you want to invest this amount in the portfolio?");
    String inpDate = objInv.nextLine();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date inputDate = simpleDateFormat.parse(inpDate);
    Date dtPortfolioLastDate = simpleDateFormat.parse(strPortfolioLastDate);


    if (!pfModel.dateValidation(inpDate)) {
      pfView.printer("Invalid date format! please enter as [yyyy-mm-dd]\n");
      getInput();
    }
    if (inputDate.before(dtPortfolioLastDate)) {
      pfView.printer("\n The Date given is before the last bought date of the portfolio stock. "
              + "Please enter valid Date after " + strPortfolioLastDate);
      getInput();
    }


    pfView.printer("\nEnter the weightage you would want to give to the stocks "
            + stockNamesToDisplay + " as below\n");
    Iterator newData = stockNamesToDisplay.iterator();
    String pairStockWithWeightage = "";
    int nPercent = 0;
    int nthisStock = 0;
    String stockName = "";
    for (int i = 0; i < nOOfUniqueStocks; i++) {
      stockName = newData.next().toString();
      pfView.printer("\n For Stock->" + stockName + "--> ");
      nthisStock = objInv.nextInt();
      nPercent += nthisStock;
      pairStockWithWeightage += stockName + "," + nthisStock + "\r\n";
    }
    if (nPercent != 100) {
      pfView.printer("Sorry! The Total Percentage should be 100%. "
              + "Your total percentage input-->" + nPercent);
      getInput();
    }
    pfView.printer("The Stockname and the Weightage = \n" + pairStockWithWeightage);


    pfView.printer("\nEnter the Commission fee for this transaction [0-3%] \n");
    commissionFees = objInv.nextFloat();
    if (commissionFees < 0 || commissionFees > 3) {
      pfView.printer("Commission fee should be within 0% to 3% of transaction amount. "
              + "Please enter valid fee! \n");
      return;
    }

    strProcessed = pfModel.investmentStrategyOnExistingPortfolios(contentsOfSelectedPortfolio,
            userName, portfolioType, inpDate, pairStockWithWeightage,
            commissionFees, totAmtInvested);
    if (strProcessed.equals("true")) {
      pfView.printer("Investment Strategy applied to portfolio successfully!");
      getInput();


    }

  }


}