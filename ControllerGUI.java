package portfolio;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * This is the class implementation of Controller Interface Features.
 * It is responsible for mediating between the PortfolioViewGUI and the four
 * Model class implementations that handle the operations and features.
 */
public class ControllerGUI implements Features {
  private PortfolioModel model;
  private PortfolioViewGUIInterface view;
  String displayPortfolio;
  String valueIs;
  String currUserName;

  /**
   * Controller GUI contains the model object.
   *
   * @param m object of the model interface.
   */
  public ControllerGUI(PortfolioModel m) {
    model = m;
  }

  public void setView(PortfolioViewGUIInterface v) {
    view = v;
    view.addFeatures(this);
  }


  @Override
  public String[] portfolioList(String username) {
    String portfolioNames = model.displayPortfolioNames(username);
    String[] fileName = portfolioNames.split(".txt");

    for (int i = 0; i < fileName.length; i++) {
      String myString = fileName[i];
      StringTokenizer tkm = new StringTokenizer(myString, ".");
      while (tkm.hasMoreTokens()) {
        fileName[i] = tkm.nextToken();
      }
    }
    return fileName;
  }

  @Override
  public void createPortfolio(String username, String portfolioTypes) {
    if (username.equals("") || portfolioTypes.equals("")) {
      view.invalidInput("Please enter your details!");
    } else {
      model.createPortfolio(username, portfolioTypes);
      //view.dialogBox();
      return;
    }
  }

  @Override
  public void buyAStock(String username, String portfolioTypes, String stock, String date,
                        float commission, float shares) throws IOException {
    String returnString = "";

    displayPortfolio = model.readPortfolio(username, portfolioTypes);
    if (!displayPortfolio.contains("Commission Fee")
            || !displayPortfolio.contains("Commission")) {
      view.invalidInput("This is an Inflexible portfolio."
              + " Application doesn't support this type of portfolio!");
      return;
    }
    if (commission < 0 || commission > 3) {
      view.invalidInput("Please enter commission fee percentage between 0 - 3% !");
      return;
    }
    if (shares < 0) {
      view.invalidInput("Please enter a valid share quantity");
      return;
    }
    if (!model.dateValidation(date)) {
      view.invalidInput("invalid date format! please enter as yyyy-mm-dd");
      return;
    }
    if (model.futureDateValidation(date)) {
      view.invalidInput("Its a future date, cant buy stocks in future dates!\n");
      return;
    } else {
      returnString = model.buyStock(displayPortfolio, stock, shares, date, username,
              portfolioTypes, commission);

      if (returnString.contains("Holiday")) {
        view.invalidInput("The Values not found for given Date! It is a Holiday");
        return;
      }
      if (returnString.contains("Invalid")) {
        view.invalidInput("The given Stock Symbol is Invalid! Please try a valid one.");
        return;
      }
      view.dialogStock();
      return;
    }

  }


  @Override
  public String setLabelStatus(String username, String portfolioTypes, String stock) {
    String retString = "";
    displayPortfolio = model.readPortfolio(username, portfolioTypes);
    retString = model.getBuysOfStock(stock, displayPortfolio);
    String exitRetString = "";
    int noOfBuys = model.getNoOfBuys();
    if (!displayPortfolio.contains("Commission Fee")
            || !displayPortfolio.contains("Commission")) {
      view.invalidInput("This is an Inflexible portfolio. Application doesn't "
              + "support this type of portfolio!");
      return exitRetString;
    } else {
      if (noOfBuys > 1) {
        view.getCostBasisDialog("You have purchased this Stock " + noOfBuys + " times \n");
      }
      if (noOfBuys == 0) {
        view.invalidInput("Sorry! The stock given not in your portfolio!");
      }
    }

    String[] splitString = retString.split("\r\n");
    String withBreak = "";
    for (int i = 0; i < splitString.length; i++) {
      withBreak += splitString[i] + "<BR>";
    }
    return withBreak;
  }


  @Override
  public void sellAStock(String username, String portfolioTypes, float shares,
                         String date, float pricePerShare, String stock, String choiceDate,
                         float commission) throws IOException, ParseException {

    String retString = model.getBuysOfStock(stock, displayPortfolio);

    System.out.println("Return Sting " + retString);

    if (commission < 0 || commission > 3) {
      view.invalidInput("Please enter commission fee percentage between 0 - 3% !");
      return;
    }
    if (shares < 0) {
      view.invalidInput("Please enter a valid share quantity");
      return;
    }
    if (!model.dateValidation(date)) {
      view.invalidInput("invalid date format! please enter as yyyy-mm-dd");
      return;
    }
    if (model.futureDateValidation(date)) {
      view.invalidInput("Its a future date, cant buy stocks in future dates!\n");
      return;
    }
    int noOfSharesToSell = model.getNoOfSharesToSell();

    if (noOfSharesToSell == 0) {
      view.invalidInput("You have already sold all the shares of this Stock\n");
      return;
    }

    if (shares > noOfSharesToSell) {
      view.invalidInput("You have given more than the number of stocks available to sell!\n");
      return;
    }

    String returnString = model.sellStock(username, portfolioTypes, displayPortfolio, shares, date,
            stock, choiceDate, commission);

    if (returnString.contains("Holiday")) {
      view.invalidInput("The Values not found for given Date! It is a Holiday");
      return;
    }
    if (returnString.contains("Invalid")) {
      view.invalidInput("The given Stock Symbol is Invalid! Please try a valid one.");
      return;
    }
    view.getCostBasisDialog("Stock Sold successfully!");
  }

  @Override

  public List<String> performanceGraph(String username, String portfolioTypes, String startDate,
                                       String endDate) throws ParseException, IOException {
    String contentsOfSelectedPortfolio = model.readPortfolio(username, portfolioTypes);
    view.getCostBasisDialog(contentsOfSelectedPortfolio);
    model.createListOfPortfolio(contentsOfSelectedPortfolio);
    String chart = model.showPerformance(startDate, endDate);
    view.getCostBasisDialog(chart);
    List<String> retList = model.getresultListforGUI();
    return retList;
  }


  @Override
  public void calculateValue(String username, String portfolioTypes, String date)
          throws IOException, ParseException {
    if (!model.dateValidation(date)) {
      view.invalidInput("invalid date format! please enter as yyyy-mm-dd");
      return;
    }
    if (model.futureDateValidation(date)) {
      view.invalidInput("Its a future date, cant buy stocks in future dates!\n");
      return;
    }
    displayPortfolio = model.readPortfolio(username, portfolioTypes);
    model.createListOfPortfolio(displayPortfolio);
    valueIs = model.processValueForFile(date, displayPortfolio);

    if (valueIs.contains("Holiday")) {
      view.invalidInput("The Values not found for given Date! It is a Holiday");
      return;
    }
    if (valueIs.contains("Invalid")) {
      view.invalidInput("The given Stock Symbol is Invalid! Please try a valid one.");
      return;
    }
    valueDialog(valueIs);

  }


  @Override
  public void costBasisCalculation(String username, String portfolioTypes, String date)
          throws ParseException {
    displayPortfolio = model.readPortfolio(username, portfolioTypes);
    model.createListOfPortfolio(displayPortfolio);
    String costBasisIs;
    costBasisIs = model.calculateCostBasis(date);
    informationDialog(costBasisIs);
  }

  @Override
  public Set<String> displayUniqueStockNames(String portfolioContent) {
    Set<String> dispStockNames;

    dispStockNames = model.getUniqueStockNames(portfolioContent);
    return dispStockNames;

  }

  @Override
  public String dollarCostAveragingCalculation(String username, String portfolioTypes,
                                               String dtPortfolioStartDate,
                                               String dtPortfolioLastDate, float commissionFees,
                                               float totAmtInvested,
                                               int frequency, String pairStockWithWeightage)
          throws IOException, ParseException {
    displayPortfolio = model.readPortfolio(username, portfolioTypes);
    if ((!model.dateValidation(dtPortfolioLastDate))
            || (!model.dateValidation(dtPortfolioStartDate))) {
      view.invalidInput("invalid date format! please enter as yyyy-mm-dd");
      return null;
    }
    if (model.futureDateValidation(dtPortfolioStartDate)) {
      view.invalidInput("Its a future date, cannot start a strategy in the future!!\n");
      return null;
    }
    if (commissionFees < 0 || commissionFees > 3) {
      view.invalidInput("Please enter commission fee percentage between 0 - 3% !");
      return null;
    }
    if (totAmtInvested <= 0) {
      view.invalidInput("Please enter valid Amount value to invest in the portfolio! ");
      return null;

    }
    if (frequency <= 0) {
      view.invalidInput("Please enter valid Frequency"
              + " [No. of days] to invest in the portfolio!");
      return null;

    }

    String retString = model.dollarCostAveraging(displayPortfolio, username,
            portfolioTypes, dtPortfolioStartDate,
            dtPortfolioLastDate, pairStockWithWeightage,
            commissionFees, totAmtInvested, frequency);
    return retString;


  }


  @Override
  public String displayPortfolio(String username, String portfolioTypes) throws IOException {
    displayPortfolio = model.readPortfolio(username, portfolioTypes);
    portfolioDisplayDialog(displayPortfolio);
    return displayPortfolio;
  }

  @Override
  public String displayPortfolioTwo(String username, String portfolioTypes) {
    String displayTwo = model.readPortfolio(username, portfolioTypes);
    return displayTwo;
  }

  @Override
  public void valueDialog(String valDialog) {
    view.getValueDialog(valDialog);

  }

  @Override
  public void informationDialog(String infoDialog) {
    view.getCostBasisDialog(infoDialog);

  }

  @Override
  public void portfolioDisplayDialog(String portfolioDispDialog) {
    view.getPortfolioDisplayDialog(portfolioDispDialog);
    return;
  }


  @Override
  public String loadPropertyFileValues(String userName, String portfolioTypes) throws IOException {

    return model.readEntirePropertyFile(userName, portfolioTypes);


  }

  @Override
  public boolean portfolioExists(String username, String portfolioType) {
    return model.checkPortfolio(username, portfolioType);
  }

  @Override
  public boolean propertyFileExist(String username, String portfolioTypes) throws IOException {
    return model.isPropertyFileExists(username, portfolioTypes);
  }

  @Override
  public void investmentStrategyExisting(String entirePortfolio, String userName,
                                         String portfolioType, String inpDate,
                                         String pairStocksWithWeightage, float commissionFees,
                                         float investment) throws ParseException, IOException {
    displayPortfolio = model.readPortfolio(userName, portfolioType);

    if (commissionFees < 0 || commissionFees > 3) {
      view.invalidInput("Please enter commission fee percentage between 0 - 3% !");
      return;
    }
    if (investment <= 0) {
      view.invalidInput("Please enter valid Amount value to invest in the portfolio! ");
      return;
    }
    if (!model.dateValidation(inpDate)) {
      view.invalidInput("invalid date format! please enter as yyyy-mm-dd");
      return;
    }
    if (model.futureDateValidation(inpDate)) {
      view.invalidInput("Its a future date, cant buy stocks in future dates!\n");
      return;
    }
    model.investmentStrategyOnExistingPortfolios(displayPortfolio, userName,
            portfolioType, inpDate,
            pairStocksWithWeightage, commissionFees, investment);
  }

  @Override
  public void setUserName(String userName) {
    this.currUserName = userName;
  }

  @Override
  public String getUserName() {
    return this.currUserName;
  }


}
