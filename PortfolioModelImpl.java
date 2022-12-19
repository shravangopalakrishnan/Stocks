package portfolio;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.io.File;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Class implementation for PortfolioModel interface. It contains functions such as check portfolio,
 * getPortfolioValue().
 */

public abstract class PortfolioModelImpl implements PortfolioModel {

  List<String> retListforGui = null;

  String valueOnCertainDate;
  String outputStock;
  String valueString;
  String[] portfolioNames;
  String portfolioType;
  String stockSymbol = "";
  String stockTobeWritten = "";

  int noOfBuys;
  int totShareSell;

  public String currPath;
  List<String> valueList = new ArrayList<String>();

  /**
   * Constructor created for PortfolioModelImpl, which contains the path for file storing.
   *
   * @param username        username an input from user.
   * @param userName        username of the user, an input from user.
   * @param typeofPortfolio type of the portfolio the user wants to create.
   * @param newWrite        boolean value to see if the user creates a file or not.
   * @param portfolioType   type of the portfolio the user wants to create.
   */
  public PortfolioModelImpl(String username, String userName, String typeofPortfolio,
                            Boolean newWrite, String portfolioType) {


    currPath = System.getProperty("user.dir");
    if (currPath.contains(":")) {
      int endIndex = currPath.indexOf(':');
      String drive = currPath.substring(0, endIndex);
      currPath = drive + ":\\Portfolios\\";
    } else {
      int endIndex = currPath.indexOf('/');
      String rootPath = currPath.substring(0, endIndex);
      currPath = rootPath + "/portfolios/";
    }

    File directory = new File(currPath);
    if (!directory.exists()) {
      directory.mkdir();
    }

  }

  @Override
  public boolean createPortfolio(String userName, String portfolioType) {
    String filename = currPath + userName + "." + portfolioType + ".txt";
    PrintWriter portfolioFile;
    try {
      portfolioFile = new PrintWriter(filename);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    portfolioFile.write("Date," + "Ticker Symbol,"
            + "Value for that day," + "Shares," + "Amount," + "Commission Fee" + "\r\n");
    portfolioFile.close();
    return true;
  }


  @Override
  public String readPortfolio(String userName, String portfolioType) {
    String filename = currPath + userName + "." + portfolioType + ".txt";
    String portFolioReturn = "";

    boolean exists = checkPortfolio(userName, portfolioType);

    if (!exists) {
      return portFolioReturn;
    }

    File file = new File(filename);
    FileReader fr = null;
    try {
      fr = new FileReader(file);
    } catch (FileNotFoundException e) {

      throw new RuntimeException(e);
    }
    BufferedReader br = new BufferedReader(fr);
    String line;
    while (true) {
      try {
        if (((line = br.readLine()) == null)) {
          break;
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      portFolioReturn += line + "\r\n";
    }
    try {
      fr.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return portFolioReturn;
  }


  @Override
  public void writeToInfPortfolio(String userName, String portfolioType) {

    String filename = currPath + userName + "." + portfolioType + ".txt";

    try {
      PrintWriter portfolioFile = new PrintWriter(filename);
      portfolioFile.write("Date," + "Ticker Symbol,"
              + "Value for that day," + "Shares," + "Amount" + "\r\n");
      portfolioFile.write(stockTobeWritten);
      portfolioFile.close();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void writeToPortfolio(String userName, String portfolioType, boolean newWrite) {

    String filename = currPath + userName + "." + portfolioType + ".txt";
    PrintWriter portfolioFile;

    try {
      if (newWrite) {
        portfolioFile = new PrintWriter(filename);
        portfolioFile.write("Date," + "Ticker Symbol,"
                + "Value for that day," + "Shares," + "Amount" + "," + "Commission Fee" + "\r\n");
        portfolioFile.write(stockTobeWritten);
      } else {
        portfolioFile = new PrintWriter(new FileOutputStream(filename, true));
        portfolioFile.append(stockTobeWritten);
      }
      stockTobeWritten = "";
      portfolioFile.close();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  private void writeStockFile(String stockSymbol, String valueString) {

    String filename = currPath + stockSymbol + ".txt";
    PrintWriter stockFile;
    try {
      stockFile = new PrintWriter(filename);
      stockFile.write(valueString);
      stockFile.close();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

  }

  private boolean checkStockFileExists(String stockSymbol) throws IOException, ParseException {
    boolean fExists = false;
    String filename = currPath + stockSymbol + ".txt";
    File stkFile = new File(filename);
    if (stkFile.exists()) {
      Path file = Paths.get(filename);

      BasicFileAttributes attr =
              Files.readAttributes(file, BasicFileAttributes.class);

      Date currDate = new Date();

      SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
      String dateCreated = df.format(attr.creationTime().toMillis());
      String currentDate = df.format(currDate);
      Date dtCreated = df.parse(dateCreated);
      Date dtCurrDate = df.parse(currentDate);
      if (dtCreated.before(dtCurrDate)) {
        ApiFetchClass stockObj = new ApiFetchClass();
        outputStock = stockObj.getStock(stockSymbol);
        writeStockFile(stockSymbol, outputStock);
      }
      fExists = true;
    }
    return fExists;
  }

  private String readStockFile(String stockSymbol) throws IOException {
    String filename = currPath + stockSymbol + ".txt";
    String content = new String(Files.readAllBytes(Paths.get(filename)));
    return content;
  }

  @Override
  public String getStockValue(String stockSymbol, float noOfShares)
          throws IOException, ParseException {
    String retStr = "Found";
    boolean bWriteToStock = false;
    boolean fExists = checkStockFileExists(stockSymbol);

    if (!fExists) {
      ApiFetchClass stockObj = new ApiFetchClass();
      outputStock = stockObj.getStock(stockSymbol);
      bWriteToStock = true;
    } else {
      outputStock = readStockFile(stockSymbol);
    }

    if (outputStock.contains("Error Message")) {
      retStr = "Invalid Stock Symbol";
    } else {
      this.stockSymbol = stockSymbol;
      if (bWriteToStock) {
        writeStockFile(stockSymbol, outputStock);
      }
      if (noOfShares > 0) {
        valueString = apiValueFetch(outputStock, noOfShares);
      }
    }
    return retStr;
  }

  @Override
  public String getStockValueForAvgPortfolio(String stockSymbol, float amount, float commissionFee,
                                             boolean toGet) throws IOException, ParseException {
    String retStr = "Found";
    boolean bWriteToStock = false;
    boolean fExists = checkStockFileExists(stockSymbol);

    if (!fExists) {
      ApiFetchClass stockObj = new ApiFetchClass();
      outputStock = stockObj.getStock(stockSymbol);
      bWriteToStock = true;
    } else {
      outputStock = readStockFile(stockSymbol);
    }

    if (outputStock.contains("Error Message")) {
      retStr = "Invalid Stock Symbol";
    } else {
      this.stockSymbol = stockSymbol;
      if (bWriteToStock) {
        writeStockFile(stockSymbol, outputStock);
      }
      if (amount > 0) {
        valueString = strToWriteInAvgPortfolio(outputStock, amount, commissionFee, toGet);
      }
    }
    return retStr;
  }


  private String apiValueFetch(String outputStock, float noOfShares) {

    String date;
    String close;

    String[] stockLines = outputStock.split("\r\n|\r|\n");

    String currentValueLine = stockLines[1];

    StringTokenizer st = new StringTokenizer(currentValueLine, ",");
    String[] stockValues = new String[6];
    int i = 0;
    while (st.hasMoreTokens()) {

      stockValues[i] = st.nextToken();
      i++;
    }
    date = stockValues[0];
    close = stockValues[4];

    float totValue = Float.parseFloat(close) * noOfShares;

    stockTobeWritten += date + "," + stockSymbol + "," + close + ","
            + noOfShares + "," + totValue + "\r\n";
    valueString = date + "," + close;


    return valueString;
  }

  /**
   * Function for fetching value from API.
   *
   * @param lineFound     output line from the API.
   * @param noOfShares    no of shares the user wants to purchase.
   * @param commissionFee commission fee the user would like to do the transaction with.
   * @param toGet         boolean toGet.
   * @return returns the stock line from api.
   */

  public String apiValueFetch(String lineFound, float noOfShares, float commissionFee,
                              boolean toGet) {

    String date;
    String close;

    StringTokenizer st = new StringTokenizer(lineFound, ",");
    String[] stockValues = new String[6];
    int i = 0;
    while (st.hasMoreTokens()) {

      stockValues[i] = st.nextToken();
      i++;
    }
    date = stockValues[0];
    close = stockValues[4];

    float totValue = Float.parseFloat(close) * noOfShares;
    float fCommission = (totValue * commissionFee) / 100;

    if (!toGet) {
      totValue = noOfShares;
      noOfShares = totValue / Float.parseFloat(close);
      fCommission = (totValue * commissionFee) / 100;
    }

    stockTobeWritten += date + "," + stockSymbol + "," + close + ","
            + noOfShares + "," + totValue + "," + fCommission + "\r\n";
    valueString = close;

    return valueString;
  }

  /**
   * String to be written in Dollar Cost Average Portfolio.
   *
   * @param lineFound     stock line output found in api.
   * @param totValue      total value of the stock.
   * @param commissionFee commission fee the user would like to do the transaction with.
   * @param toGet         toGet boolean.
   * @return returns the string to be written in dollar cost average portfolio.
   */

  public String strToWriteInAvgPortfolio(String lineFound, float totValue, float commissionFee,
                                         boolean toGet) {

    String date;
    String close;

    StringTokenizer st = new StringTokenizer(lineFound, ",");
    String[] stockValues = new String[6];
    int i = 0;
    while (st.hasMoreTokens()) {

      stockValues[i] = st.nextToken();
      i++;
    }
    date = stockValues[0];
    close = stockValues[4];

    float noOfShares = totValue / Float.parseFloat(close);
    float fCommission = (totValue * commissionFee) / 100;

    stockTobeWritten += date + "," + stockSymbol + "," + close + ","
            + noOfShares + "," + totValue + "," + fCommission + "\r\n";
    valueString = close;

    return valueString;
  }

  @Override
  public boolean checkPortfolio(String userName, String portfolioType) {
    boolean portfolioExists = false;
    String strPortfolioName = userName + "." + portfolioType + ".txt";
    File dir = new File(currPath);
    this.portfolioNames = dir.list();

    for (String filename : this.portfolioNames) {
      if (filename.equals(strPortfolioName)) {
        portfolioExists = true;
        break;
      }
    }
    return portfolioExists;
  }

  @Override
  public boolean checkUserName(String userName) {
    boolean userExists = false;
    String[] userNames = null;
    String strFileName = userName + "." + "*.txt";
    File dir = new File(currPath);
    userNames = dir.list();

    for (String filename : userNames) {
      if (filename.equals(strFileName)) {
        userExists = true;
        break;
      }
    }
    return userExists;
  }


  @Override
  public String getLasteDateOfPortfolio(String portfolioContent) {
    createListOfPortfolio(portfolioContent);
    Collections.reverse(valueList);
    String lastline = valueList.get(0);
    StringTokenizer stm = new StringTokenizer(lastline, ",");
    return stm.nextToken();
  }

  @Override
  public String getInitialPurchaseDate(String portfolioContent) {
    List<String> contentOfPortfolio;
    contentOfPortfolio = new ArrayList<>(Collections.singleton(portfolioContent));
    String firstLine = contentOfPortfolio.get(0);
    StringTokenizer stm = new StringTokenizer(firstLine, ",");
    return stm.nextToken();
  }

  @Override
  public Set<String> getUniqueStockNames(String portfolioContent) {
    String[] stockLines = portfolioContent.split("\r\n|\r|\n");
    Set<String> uniqueStocks = new LinkedHashSet<String>();

    String stockNames = "";
    for (int i = 1; i < stockLines.length; i++) {
      String currentLine = stockLines[i];
      StringTokenizer stm = new StringTokenizer(currentLine, ",");
      stm.nextToken();
      String stkName = stm.nextToken();
      uniqueStocks.add(stkName);
    }
    return uniqueStocks;
  }

  @Override
  public String displayPortfolioNames(String userName) {
    String portretfolioNames = "";
    String strPortfolioName = currPath + userName + "." + portfolioType + ".txt";
    String tmpUserName = userName + ".";

    File dir = new File(currPath);

    this.portfolioNames = dir.list();

    for (String filename : this.portfolioNames) {
      if (filename.startsWith(tmpUserName) && filename.endsWith(".txt")) {
        portretfolioNames += filename + "\r\n";
      }
    }
    return portretfolioNames;

  }


  @Override
  public boolean dateValidation(String inpDate) {
    boolean isValidFormat = inpDate.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})");
    return isValidFormat;
  }

  @Override
  public boolean futureDateValidation(String inpDate) throws IOException {
    LocalDate ld = LocalDate.parse(LocalDate.now().toString());
    LocalDate localD = LocalDate.parse(inpDate);
    return localD.compareTo(ld) > 0;
  }

  private boolean writeToPropertiesFile(String userName, String portfolioType,
                                        String startDate, String endDate,
                                        String pairStockWithWeightage, Float commissionFees,
                                        Float totAmtInvested, int frequency) throws IOException {
    boolean fExist = false;

    String filename = currPath + userName + "." + portfolioType + ".properties";

    File fname = new File(filename);
    if (fname.exists()) {
      fExist = true;
      return fExist;
    }
    Properties props = new Properties();


    //Populating the properties file
    props.put("Start_Date", startDate);
    props.put("End_Date", endDate);
    props.put("Stocks", pairStockWithWeightage);
    props.put("Commission_Fee", commissionFees.toString());
    props.put("Investment_Amount", totAmtInvested.toString());
    props.put("Frequency", String.valueOf(frequency));
    props.put("Processed_Date", "");
    props.put("Finished", "");
    //Instantiating the FileInputStream for output file

    FileOutputStream outputStrem = new FileOutputStream(filename);
    //Storing the properties file
    props.store(outputStrem, "This is Dollar Cost Averaging Properties File");

    return fExist;

  }

  private Properties readPropertiesFile(String propfileName) throws IOException {
    FileInputStream fis = null;
    Properties prop = null;
    try {
      fis = new FileInputStream(propfileName);
      prop = new Properties();
      prop.load(fis);
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      fis.close();
    }
    return prop;

  }

  private List<String> createRangeList(String startDate, String endDate, int frequency)
          throws ParseException {
    String strnextDate;

    List<String> lstRange = new ArrayList<String>();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Date dtendDate = sdf.parse(endDate);

    Date dtnextDate = sdf.parse(startDate);

    strnextDate = startDate;
    Calendar cal = Calendar.getInstance();
    /// lstRange.add(startDate);

    while (dtnextDate.before(dtendDate)) {
      lstRange.add(strnextDate);
      try {
        cal.setTime(sdf.parse(strnextDate));
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
      cal.add(Calendar.DAY_OF_MONTH, frequency);
      strnextDate = sdf.format(cal.getTime());
      dtnextDate = sdf.parse(strnextDate);
    }
    return lstRange;
  }

  private String getTheWorkinginpDate(String inpDate, String totFileStr)
          throws IOException, ParseException {
    createListOfPortfolio(totFileStr);
    String str1 = processValueForFile(inpDate, totFileStr);
    while (str1.contains("Holiday")) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Calendar cal = Calendar.getInstance();
      cal.setTime(sdf.parse(inpDate));
      cal.add(Calendar.DAY_OF_YEAR, -1);
      Date oneDayBefore = cal.getTime();
      String strOndeDay = sdf.format(oneDayBefore);
      inpDate = strOndeDay;
      str1 = processValueForFile(inpDate, totFileStr);
    }
    return inpDate;
  }

  private void updatePropertyFile(String userName, String portfolioType, String strToday,
                                  boolean bFinished) throws IOException {
    String filename = currPath + userName + "." + portfolioType + ".properties";


    Properties props = new Properties();

    FileInputStream inStream = new FileInputStream(filename);
    props.load(inStream);
    props.setProperty("Processed_Date", strToday);
    if (bFinished) {
      props.setProperty("Finished", "Y");
    } else {
      props.setProperty("Finished", "N");
    }

    FileOutputStream outputStrem = new FileOutputStream(filename);
    props.store(outputStrem, "This is Dollar Cost Averaging Properties File--- Updated");
  }

  private void processAndBuyStocks(String userName, String portfolioType, String entirePortfolio,
                                   String stocks, String investment, String commissionFees,
                                   String frequency, String startDate, String endDate)
          throws ParseException, IOException {
    boolean bFinished = false;

    String[] separateStocks = stocks.split("\r\n|\r|\n");
    Float fInvestment = Float.parseFloat(investment);
    List<String> lstfreqDates;
    List<String> finalList = null;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date1 = simpleDateFormat.parse(startDate);
    Date date2 = simpleDateFormat.parse(endDate);
    Date today = new Date();
    String strToday = simpleDateFormat.format(today);

    lstfreqDates = createRangeList(startDate, endDate, Integer.parseInt(frequency));

    if (date2.before(today) || date2.equals(today)) {
      bFinished = true;
    } else {
      // lstfreqDates = createRangeList(StartDate, EndDate, Integer.parseInt(Frequency));
      List<String> subList = new ArrayList<String>();

      for (int i = 0; i < lstfreqDates.size(); i++) {
        String thisDate = lstfreqDates.get(i);
        Date dtthisDate = simpleDateFormat.parse(thisDate);

        if (dtthisDate.after(today)) {
          subList = lstfreqDates.subList(0, i);
          break;
        }
      }

      String strPortfolioLastDate = getLasteDateOfPortfolio(entirePortfolio);
      Date dtPortfolioLastDate = simpleDateFormat.parse(strPortfolioLastDate);
      for (int j = 0; j < subList.size(); j++) {
        String curDate = subList.get(j);
        Date dtCurDate = simpleDateFormat.parse(curDate);
        if (dtCurDate.equals(dtPortfolioLastDate) || dtCurDate.after(dtPortfolioLastDate)) {
          if (j == subList.size() - 1) {
            finalList = subList.subList(j, subList.size());
          } else {
            finalList = subList.subList(j + 1, subList.size());
          }
          break;
        }
      }
      lstfreqDates = finalList;

    }

    for (int i = 0; i < separateStocks.length; i++) {
      String curStockLine = separateStocks[i];
      StringTokenizer stm = new StringTokenizer(curStockLine, ",");
      String curStock = stm.nextToken();
      int nWeightage = Integer.parseInt(stm.nextToken());
      float nWeightageShare = (fInvestment * nWeightage) / 100;
      for (int j = 0; j < lstfreqDates.size(); j++) {
        String strInpDate = lstfreqDates.get(j);
        String inpDate = getTheWorkinginpDate(strInpDate, entirePortfolio);
        String retStr = differenceInValueOfStock(inpDate, curStock, "10", true);
        apiValueFetch(retStr, nWeightageShare, Float.parseFloat(commissionFees), false);
        writeToPortfolio(userName, portfolioType, false);
      }
    }
    updatePropertyFile(userName, portfolioType, strToday, bFinished);
  }

  @Override
  public boolean isPropertyFileExists(String userName, String portfolioType) throws IOException {
    boolean fexists = false;

    String filename = currPath + userName + "." + portfolioType + ".properties";

    File fname = new File(filename);
    if (fname.exists()) {
      fexists = true;
    }
    return fexists;
  }

  @Override
  public String dollarCostAveraging(String entirePortfolio, String userName, String portfolioType,
                                    String startDate, String endDate, String pairStockWithWeightage,
                                    Float commissionFees, Float totAmtInvested, int frequency)
          throws IOException, ParseException {
    boolean bPropsFile = false;
    String retString = "false";

    String propfileName = currPath + userName + "." + portfolioType + ".properties";

    bPropsFile = isPropertyFileExists(userName, portfolioType);

    if (!bPropsFile) {
      writeToPropertiesFile(userName, portfolioType, startDate, endDate,
              pairStockWithWeightage, commissionFees, totAmtInvested, frequency);
      bPropsFile = true;
    }

    if (bPropsFile) {
      Properties thisProps = readPropertiesFile(propfileName);
      String dateStart = "";
      String dateEnd = "";
      String thisFrequency = "";
      String thisInvestment = "";
      String thisStocks = "";
      String thisCommissionFees = "";
      String thisProcessedDate = "";
      String thisStrFinished = "";
      boolean bFinished = false;

      thisStrFinished = thisProps.getProperty("Finished");


      if (thisStrFinished.equals("Y")) {
        retString = "true";


        return retString;
      }


      dateStart = thisProps.getProperty("Start_Date");
      dateEnd = thisProps.getProperty("End_Date");
      thisFrequency = thisProps.getProperty("Frequency");
      thisInvestment = thisProps.getProperty("Investment_Amount");
      thisStocks = thisProps.getProperty("Stocks");
      thisCommissionFees = thisProps.getProperty("Commission_Fee");
      thisProcessedDate = thisProps.getProperty("Processed_Date");

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date today = new Date();
      String strToday = simpleDateFormat.format(today);
      if (strToday.equals(thisProcessedDate)) {
        retString = "This Portfolio is processed already for Today " + strToday;
        return retString;
      }
      processAndBuyStocks(userName, portfolioType, entirePortfolio, thisStocks,
              thisInvestment, thisCommissionFees, thisFrequency, dateStart, dateEnd);
      retString = "true";
    }

    return retString;
  }

  @Override
  public String readEntirePropertyFile(String username, String portfolioType) throws IOException {

    String propfileName = currPath + username + "." + portfolioType + ".properties";
    Path fileName
            = Path.of(propfileName);
    String str = Files.readString(fileName);
    return str;
  }

  @Override
  public String investmentStrategyOnExistingPortfolios(String entirePortfolio, String userName,
                                                       String portfolioType, String inpDate,
                                                       String pairStocksWithWeightage,
                                                       float commissionFees, float investment)
          throws ParseException, IOException {
    String retString = "false";
    String[] separateStocks = pairStocksWithWeightage.split("\r\n|\r|\n");
    for (int i = 0; i < separateStocks.length; i++) {
      String curStockLine = separateStocks[i];
      StringTokenizer stm = new StringTokenizer(curStockLine, ",");
      String curStock = stm.nextToken();
      int nWeightage = Integer.parseInt(stm.nextToken());
      float nWeightageShare = (investment * nWeightage) / 100;
      String retStr = differenceInValueOfStock(inpDate, curStock, "10", true);
      apiValueFetch(retStr, nWeightageShare, commissionFees, false);
      writeToPortfolio(userName, portfolioType, false);
      retString = "true";
    }
    return retString;
  }


}




