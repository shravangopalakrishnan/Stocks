package portfolio;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * AnalysisModelImpl is responsible for the analytical and mathematical operations
 * on the data present in a portfolio.
 */
public abstract class AnalysisModelImpl extends StockModelImpl {
  /**
   * Constructor created for PortfolioModelImpl, which contains the path for file storing.
   *
   * @param username        username an input from user.
   * @param userName        username of the user, an input from user.
   * @param typeofPortfolio type of the portfolio the user wants to create.
   * @param newWrite        boolean value to see if the user creates a file or not.
   * @param portfolioType   type of the portfolio the user wants to create.
   */
  public AnalysisModelImpl(String username, String userName, String typeofPortfolio,
                           Boolean newWrite, String portfolioType) {
    super(username, userName, typeofPortfolio, newWrite, portfolioType);
  }

  private float differenceInValueOfStock(String date, String stockName, String noOfShares,
                                         String totAmount) throws IOException, ParseException {
    String retStr;
    retStr = getStockValue(stockName, 0);
    float fAmtOfThatDay = 0;

    int indexOfString = outputStock.indexOf(date);
    if (indexOfString == -1) {
      fAmtOfThatDay = (float) 0.01;
      return fAmtOfThatDay;
    }
    int endOfString = indexOfString;
    for (int i = indexOfString; i < outputStock.length(); i++) {
      if (outputStock.charAt(i) == '\n') {
        endOfString = i;
        break;
      }
    }
    if (endOfString > indexOfString) {
      String lineFoundFromApi = outputStock.substring(indexOfString, endOfString);
      float retValue = getPortfolioValue(lineFoundFromApi, noOfShares, totAmount);
      fAmtOfThatDay += retValue;

    }

    return fAmtOfThatDay;
  }


  @Override
  public String processValueForFile(String inpDate, String totFileStr)
          throws IOException, ParseException {

    float fAmtOfThatDay = 0;
    String stockName = "";
    String noOfShares = "";
    String totalAmount = "";

    String[] portfolioLines = totFileStr.split("\r\n|\r|\n");

    String s = null;
    float fTotalAmount = 0;
    float retValue = 0;
    String strCommission = "";

    for (int loop = 1; loop < portfolioLines.length; loop++) {
      String currentValueLine = portfolioLines[loop];

      StringTokenizer st = new StringTokenizer(currentValueLine, ",");
      String[] portfolioLineValues = new String[6];
      int i = 0;
      while (st.hasMoreTokens()) {
        portfolioLineValues[i] = st.nextToken();
        i++;
      }
      stockName = portfolioLineValues[1];
      noOfShares = portfolioLineValues[3];
      totalAmount = portfolioLineValues[4];
      fTotalAmount += Float.parseFloat(totalAmount);

      Collections.sort(valueList);

      String firstLine = valueList.get(0);
      StringTokenizer st1 = new StringTokenizer(firstLine, ",");
      String chkDate = st1.nextToken();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date date1 = null;
      try {
        date1 = simpleDateFormat.parse(inpDate);
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
      Date date2 = null;
      try {
        date2 = simpleDateFormat.parse(chkDate);
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
      if (date1.compareTo(date2) < 0) {

        String str1 = "The date given " + inpDate + " is before your first purchase of the stock. "
                + chkDate
                + "So the Value of Portfolio is 0.0 \n";
        return str1;
      }

      retValue = differenceInValueOfStock(inpDate, stockName, noOfShares, totalAmount);
      if (retValue == (float) 0.01) {

        break;
      }
      fAmtOfThatDay += retValue;
    }

    if (retValue == (float) 0.01) {
      valueOnCertainDate = "Holiday! No Data Found on Given Date " + inpDate + "\n";
    } else {
      valueOnCertainDate = String.valueOf(fAmtOfThatDay);
    }
    return valueOnCertainDate;
  }

  @Override
  public String processValueForInflexibleFile(String inpDate, String totFileStr)
          throws IOException, ParseException {

    float fAmtOfThatDay = 0;
    String stockName = "";
    String noOfShares = "";
    String totalAmount = "";

    String[] portfolioLines = totFileStr.split("\r\n|\r|\n");

    String s = null;
    float fTotalAmount = 0;
    float retValue = 0;

    for (int loop = 1; loop < portfolioLines.length; loop++) {
      String currentValueLine = portfolioLines[loop];

      StringTokenizer st = new StringTokenizer(currentValueLine, ",");
      String[] portfolioLineValues = new String[5];
      int i = 0;
      while (st.hasMoreTokens()) {
        portfolioLineValues[i] = st.nextToken();
        i++;
      }
      stockName = portfolioLineValues[1];
      noOfShares = portfolioLineValues[3];
      totalAmount = portfolioLineValues[4];
      fTotalAmount += Float.parseFloat(totalAmount);
      retValue = differenceInValueOfStock(inpDate, stockName, noOfShares, totalAmount);
      if (retValue == (float) 0.01) {

        break;
      }
      fAmtOfThatDay += retValue;
    }

    if (retValue == (float) 0.01) {
      valueOnCertainDate = "Holiday! No Data Found on Given Date " + inpDate + "\n";
    } else {
      valueOnCertainDate = String.valueOf(fAmtOfThatDay);
    }
    return valueOnCertainDate;
  }

  private String findTheDateToCalculate(String inpDate) {

    LocalDate lastDayOfMonth = LocalDate.parse(inpDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .with(TemporalAdjusters.lastDayOfMonth());

    return lastDayOfMonth.toString();
  }

  private float processValueForGraph(String inpDate, String totFileStr,
                                     Character ch) throws IOException, ParseException {

    float fAmtOfThatDay = 0;
    String stockName = "";
    String noOfShares = "";
    String totalAmount = "";

    String dateToCalculate = "";
    if (ch == 'M') {
      dateToCalculate = findTheDateToCalculate(inpDate);
      inpDate = dateToCalculate;
    }

    if (ch == 'Y') {
      String toForm = inpDate + "-12-" + "31";
      dateToCalculate = findTheDateToCalculate(toForm);
      inpDate = dateToCalculate;
    }

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


    String[] portfolioLines = totFileStr.split("\r\n|\r|\n");

    String s = null;
    float retValue = 0;

    for (int loop = 0; loop < portfolioLines.length; loop++) {
      String currentValueLine = portfolioLines[loop];
      StringTokenizer st = new StringTokenizer(currentValueLine, ",");
      String[] portfolioLineValues = new String[6];
      int i = 0;
      while (st.hasMoreTokens()) {
        portfolioLineValues[i] = st.nextToken();
        i++;
      }
      stockName = portfolioLineValues[1];
      noOfShares = portfolioLineValues[3];
      totalAmount = portfolioLineValues[4];
      retValue = differenceInValueOfStock(inpDate, stockName, noOfShares, totalAmount);
      if (retValue == (float) 0.01) {

        break;
      }
      fAmtOfThatDay += retValue;
    }
    return fAmtOfThatDay;
  }

  @Override
  public int getNoOfBuys() {
    return noOfBuys;
  }

  @Override
  public int getNoOfSharesToSell() {
    return totShareSell;
  }

  @Override
  public void createListOfPortfolio(String portfolioContent) {
    valueList.clear();
    String[] portfolioLines = portfolioContent.split("\r\n|\r|\n");
    for (int loop = 1; loop < portfolioLines.length; loop++) {
      String currentValueLine = portfolioLines[loop];
      valueList.add(currentValueLine);
    }
  }

  @Override
  public boolean checkBetween(Date dateToCheck, Date startDate, Date endDate) {
    return dateToCheck.compareTo(startDate) >= 0 && dateToCheck.compareTo(endDate) <= 0;
  }

  private String getStrTobeCalculated(int loop, String line, String dateStr, String costDate,
                                      boolean between) throws ParseException {
    String strToBeCalculated = "";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date1 = simpleDateFormat.parse(dateStr);
    Date date2 = simpleDateFormat.parse(costDate);
    if (between) {
      Date searchDate = simpleDateFormat.parse(line);
      if (checkBetween(searchDate, date1, date2)) {
        strToBeCalculated = "yes";
      }
    } else {
      if ((date1.compareTo(date2) < 0) || (date1.compareTo(date2) == 0)) {
        if (!line.contains(",-")) {
          strToBeCalculated += line + "\r\n";
        }
      }
      if (loop == 0 && (date1.compareTo(date2) > 0)) {
        strToBeCalculated = "";
      }
    }
    return strToBeCalculated;
  }


  @Override
  public String calculateCostBasis(String costDate) throws ParseException {
    Collections.sort(valueList);
    String strToBeCalculated = "";

    for (int i = 0; i < valueList.size(); i++) {
      String line = valueList.get(i);
      StringTokenizer st = new StringTokenizer(line, ",");
      String dateStr = st.nextToken();
      strToBeCalculated += getStrTobeCalculated(i, line, dateStr, costDate, false);
    }
    String retStr = getTotalValue(strToBeCalculated);
    return retStr;

  }

  @Override
  public float getPortfolioValue(String foundStr, String noOfShare, String totalAmount) {
    float nNoOfShares;
    float fTotalAmount;
    float fCloseValue;
    float fAmtOfThatDay = 0;
    String close;

    fTotalAmount = Float.parseFloat(totalAmount);
    nNoOfShares = Float.parseFloat(noOfShare);

    StringTokenizer st = new StringTokenizer(foundStr, ",");
    String[] stockValues = new String[6];
    int i = 0;
    while (st.hasMoreTokens()) {

      stockValues[i] = st.nextToken();
      i++;
    }
    close = stockValues[4];
    fCloseValue = Float.parseFloat(close);
    fAmtOfThatDay += nNoOfShares * fCloseValue;
    return fAmtOfThatDay;
  }


  @Override
  public String getBuysOfStock(String stockSymbol, String portfolioContent) {

    String stockLine = "";
    int nNoofTimes = 0;
    String[] portfolioLines = portfolioContent.split("\r\n|\r|\n");
    totShareSell = 0;
    for (int loop = 1; loop < portfolioLines.length; loop++) {
      String currentValueLine = portfolioLines[loop];
      if (currentValueLine.contains(stockSymbol + ",")) {
        String[] valueArray = currentValueLine.split(",");
        String strNo = valueArray[3];
        int nNo = (int) Float.parseFloat(strNo);
        totShareSell += nNo;
        if (nNo > 0) {
          stockLine += currentValueLine + "\r\n";
          nNoofTimes++;
        }
      }
    }
    noOfBuys = nNoofTimes;
    return stockLine;
  }

  private String getTotalValue(String strTobeCalculated) {
    if (strTobeCalculated.length() == 0) {
      return "0.0";
    }
    String[] portfolioLines = strTobeCalculated.split("\r\n|\r|\n");
    String totalAmount;
    String commFee;
    float fTotalAmount = 0;
    float fTotalCommission = 0;

    for (int loop = 0; loop < portfolioLines.length; loop++) {
      String currentValueLine = portfolioLines[loop];

      StringTokenizer st = new StringTokenizer(currentValueLine, ",");
      String[] portfolioLineValues = new String[6];
      int i = 0;
      while (st.hasMoreTokens()) {
        portfolioLineValues[i] = st.nextToken();
        i++;
      }
      totalAmount = portfolioLineValues[4];
      commFee = portfolioLineValues[5];


      fTotalAmount += Float.parseFloat(totalAmount);
      fTotalCommission += Float.parseFloat(commFee);

    }
    String tobeRet = "";
    float fTotalCost = fTotalAmount + fTotalCommission;
    tobeRet = "The Total Amount is $" + fTotalAmount
            + " And the Total Commission Fee is $" + fTotalCommission
            + "\nTotal Cost Basis of Portfolio = $" + fTotalCost + "\n";
    return tobeRet;
  }


  private int daysInBetween(String startDate, String endDate) throws ParseException {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date d1 = simpleDateFormat.parse(startDate);
    Date d2 = simpleDateFormat.parse(endDate);
    return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
  }

  private int calculateAstisksScale(List<String> separatedList) {
    float fHigh = 0;
    float fLow = 0;
    int nHigh = 0;
    int nLow = 0;

    int scaleOfAstrisks = 0;
    for (int i = 0; i < separatedList.size(); i++) {
      String thisLine = separatedList.get(i);
      StringTokenizer st1 = new StringTokenizer(thisLine, ",");
      st1.nextToken();
      String value = st1.nextToken();
      if (i == 0) {
        fLow = Float.parseFloat(value);
      }
      if (Float.parseFloat(value) > fHigh) {
        fHigh = Float.parseFloat(value);
      }
      if (Float.parseFloat(value) < fLow) {
        fLow = Float.parseFloat(value);
      }
    }

    nHigh = (int) fHigh;
    nLow = (int) fLow;
    String strHigh = String.valueOf(nHigh);
    int len = strHigh.length();
    if (len == 2) {
      scaleOfAstrisks = 2;
    }
    if (len == 3) {
      scaleOfAstrisks = 20;
    }
    if (len == 4) {
      scaleOfAstrisks = 200;
    }
    if (len == 5) {
      scaleOfAstrisks = 2000;
    }
    if (len == 6) {
      scaleOfAstrisks = 20000;
    }
    if (len == 7) {
      scaleOfAstrisks = 200000;
    }
    if (len == 8) {
      scaleOfAstrisks = 2000000;
    }
    if (len == 9) {
      scaleOfAstrisks = 20000000;
    }

    if (scaleOfAstrisks > nLow && nLow != 0) {
      scaleOfAstrisks = nLow;
    }

    return scaleOfAstrisks;
  }

  @Override
  public List<String> createDateRangeList(String startDate, String endDate, int nTotal)
          throws ParseException {
    String strNextDate = "";
    List<String> dateRangeList = new ArrayList<>();
    dateRangeList.add(startDate);
    for (int i = 0; i < nTotal; i++) {
      strNextDate = getTheNextDate(startDate);
      dateRangeList.add(strNextDate);
      startDate = strNextDate;
    }
    return dateRangeList;

  }

  @Override
  public List<String> getresultListforGUI() {
    return retListforGui;
  }

  @Override
  public String showPerformance(String startDate, String endDate)
          throws ParseException, IOException {
    int totLines = 0;
    String[] monthsArray = new DateFormatSymbols().getShortMonths();

    Collections.sort(valueList);
    String strToBeCalculated = "";
    List<String> separatedMonthList = null;
    List<String> separatedWeekList = null;
    List<String> separatedList = null;
    String retChart = "";


    int nNoOfDays = daysInBetween(startDate, endDate);
    System.out.println("No of Day... " + nNoOfDays);
    List<String> dateRangeList = createDateRangeList(startDate, endDate, nNoOfDays);
    System.out.println("The Data Range List = " + dateRangeList);
    for (int i = 0; i < valueList.size(); i++) {
      String line = valueList.get(i);
      StringTokenizer st = new StringTokenizer(line);
      String dateToCheck = st.nextToken();
      String result;
      result = getStrTobeCalculated(i, dateToCheck, startDate, endDate, true);
      if (result.equals("yes")) {
        strToBeCalculated += line + "\r\n";
        totLines++;
      }
    }

    List<String> lstForGraph = new ArrayList<>();


    for (int i = 0; i < dateRangeList.size(); i++) {
      String dtRangeDate = dateRangeList.get(i);
      int indexOfString = strToBeCalculated.indexOf(dtRangeDate);
      if (indexOfString == -1) {
        lstForGraph.add(dtRangeDate + ",*");
        continue;
      }
      int endOfString = indexOfString;
      for (int j = indexOfString; j < strToBeCalculated.length(); j++) {
        if (strToBeCalculated.charAt(j) == '\r') {
          endOfString = j;
          break;
        }
      }
      if (endOfString > indexOfString) {
        String lineFoundFromString = strToBeCalculated.substring(indexOfString, endOfString);
        lstForGraph.add(lineFoundFromString);
      }
    }
    if (strToBeCalculated.length() <= 0) {

      String retString = "The date range is either before the First purchase Date or "
              + "after the Last purchase Date of the Portfolio. "
              + "Performance Graph not available";
      return retString;

    }
    Collections.sort(lstForGraph);
    int months = nNoOfDays / 30;
    int years = nNoOfDays / 365;
    boolean bWeek = false;
    boolean bMonth = false;
    boolean bDays = false;
    boolean bYears = false;
    boolean bFourMonths = false;

    if (nNoOfDays >= 5 && nNoOfDays <= 30) {
      bDays = true;
      separatedList = separatedDateWise(strToBeCalculated, startDate, endDate, lstForGraph);
    }
    if (nNoOfDays > 30 && nNoOfDays < 210) {
      bWeek = true;
      separatedList = separateWeekWise(strToBeCalculated, startDate, endDate, lstForGraph);
    }
    if (nNoOfDays > 210 && nNoOfDays < 900) {
      separatedList = separateMonthWise(strToBeCalculated, startDate, endDate, lstForGraph);
      bMonth = true;
    }
    if (nNoOfDays > 900 && nNoOfDays < 1825) {
      separatedList = separateFourMonthWise(strToBeCalculated, startDate, endDate, lstForGraph);
      bFourMonths = true;
    }
    if (nNoOfDays > 1825 && nNoOfDays < 10957) {
      bYears = true;
      separatedList = separatedYearWise(strToBeCalculated, startDate, endDate, lstForGraph);
    }

    Collections.sort(separatedList);

    int nAsteriskScale = calculateAstisksScale(separatedList);

    retListforGui = separatedList;

    for (int i = 0; i < separatedList.size(); i++) {
      String getThatLine = separatedList.get(i);
      StringTokenizer st1 = new StringTokenizer(getThatLine, ",");
      String strMonth = st1.nextToken();
      String strDisplayMonth = "";
      String value = st1.nextToken();
      int nValue = (int) Float.parseFloat(value);
      int nNoOfAsterisks = 0;

      if (nValue > 0) {
        nNoOfAsterisks = nValue / nAsteriskScale;
      }

      if (nNoOfAsterisks > 50) {
        nNoOfAsterisks = 50;
      }

      if (bMonth) {
        String month = "";
        String year = "";
        month = strMonth.substring(0, 2);
        year = strMonth.substring(2, 6);
        String nMonth = monthsArray[Integer.parseInt(month) - 1];
        strDisplayMonth = nMonth + "\t" + year;
      }
      if (bWeek) {
        strDisplayMonth = strMonth;
      }
      if (bDays) {
        strDisplayMonth = strMonth;
      }
      if (bYears) {
        strDisplayMonth = strMonth;
      }
      if (bFourMonths) {
        strDisplayMonth = strMonth;
      }

      retChart += strDisplayMonth + "\t:";

      for (int j = 0; j < nNoOfAsterisks; j++) {
        retChart += "*";
      }
      retChart += "\r\n";
    }
    retChart += "Scale : * = $" + nAsteriskScale;
    return retChart;
  }

  private List<String> separateMonthWise(String strToBeCalculated,
                                         String startDate, String endDate, List<String> lstForGraph)
          throws IOException, ParseException {
    String[] monthsArray = new DateFormatSymbols().getShortMonths();

    String thisDate = "";
    String prevDate = "";
    String thisMonth = "";
    String prevMonth = "";
    float fAmount = 0;
    float fMinusAmount = 0;
    float fPlusAmount = 0;

    List<String> strMonthWise = new ArrayList<String>();
    String prevMonthPlusStocks = "";
    String prevMonthMinusStocks = "";
    String thisYear = "";
    String prevYear = "";
    String toCheckValue = "";
    String toAdd = "";

    for (int loop = 0; loop < lstForGraph.size(); loop++) {
      String currentValueLine = lstForGraph.get(loop);

      StringTokenizer st = new StringTokenizer(currentValueLine, ",");
      thisDate = st.nextToken();
      toCheckValue = st.nextToken();

      StringTokenizer stm = new StringTokenizer(thisDate, "-");
      thisYear = stm.nextToken();
      thisMonth = stm.nextToken();

      if (loop == 0) {
        prevDate = thisDate;
        prevMonth = thisMonth;
        prevYear = thisYear;
      }
      if (thisMonth.equals(prevMonth)) {
        if (toCheckValue.equals("*") && !(prevMonth.equals(thisMonth))) {
          toAdd = prevMonth + "," + fAmount;
          strMonthWise.add(toAdd);
          prevMonth = thisMonth;
          continue;
        }

        // String[] curValues = currentValueLine.split(",");
        if (!toCheckValue.equals("*")) {
          if (currentValueLine.contains(",-")) {
            prevMonthMinusStocks += currentValueLine;
          } else {
            prevMonthPlusStocks += currentValueLine + "\r\n";
          }
        }
      } else {
        fPlusAmount += processValueForGraph(prevDate, prevMonthPlusStocks, 'M');
        fMinusAmount += processValueForGraph(prevDate, prevMonthMinusStocks, 'M');

        fAmount = fPlusAmount + fMinusAmount;
        toAdd = prevMonth + prevYear + "," + fAmount;
        strMonthWise.add(toAdd);
      }
      prevMonth = thisMonth;
      prevDate = thisDate;
      prevYear = thisYear;
    }
    toAdd = prevMonth + prevYear + "," + fAmount;
    strMonthWise.add(toAdd);

    return strMonthWise;
  }

  private List<String> separateFourMonthWise(String strToBeCalculated,
                                             String startDate, String endDate,
                                             List<String> lstForGraph)
          throws IOException, ParseException {
    String[] monthsArray = new DateFormatSymbols().getShortMonths();

    String thisDate = "";
    String prevDate = "";
    String thisMonth = "";
    String prevMonth = "";
    float fAmount = 0;
    float fMinusAmount = 0;
    float fPlusAmount = 0;

    List<String> strMonthWise = new ArrayList<String>();
    String prevMonthPlusStocks = "";
    String prevMonthMinusStocks = "";
    String thisYear = "";
    String prevYear = "";
    String toCheckValue = "";
    String toAdd = "";
    int nMonth = 0;
    for (int loop = 0; loop < lstForGraph.size(); loop++) {
      String currentValueLine = lstForGraph.get(loop);

      StringTokenizer st = new StringTokenizer(currentValueLine, ",");
      thisDate = st.nextToken();
      toCheckValue = st.nextToken();

      StringTokenizer stm = new StringTokenizer(thisDate, "-");
      thisYear = stm.nextToken();
      thisMonth = stm.nextToken();

      if (loop == 0) {
        prevDate = thisDate;
        prevMonth = thisMonth;
        prevYear = thisYear;
      }
      if (thisMonth.equals(prevMonth)) {
        if (toCheckValue.equals("*") && !(prevMonth.equals(thisMonth))) {
          toAdd = prevYear + "-" + prevMonth + "," + fAmount;
          strMonthWise.add(toAdd);
          prevMonth = thisMonth;
          continue;
        }
        if (!toCheckValue.equals("*")) {
          if (currentValueLine.contains(",-")) {
            prevMonthMinusStocks += currentValueLine;
          } else {
            prevMonthPlusStocks += currentValueLine + "\r\n";
          }
        }
      } else {
        nMonth++;
        if (nMonth > 4) {
          fPlusAmount += processValueForGraph(prevDate, prevMonthPlusStocks, 'M');
          fMinusAmount += processValueForGraph(prevDate, prevMonthMinusStocks, 'M');

          fAmount += fPlusAmount + fMinusAmount;
          toAdd = prevYear + "-" + prevMonth + "," + fAmount;
          strMonthWise.add(toAdd);
          nMonth = 0;
        }
      }
      prevMonth = thisMonth;
      prevDate = thisDate;
      prevYear = thisYear;
    }
    toAdd = prevYear + "-" + prevMonth + "," + fAmount;
    strMonthWise.add(toAdd);

    return strMonthWise;
  }


  private String getTheWeekEnd(String thisWeekStart) {
    String thisWeekEnd;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Calendar cal = Calendar.getInstance();
    try {
      cal.setTime(sdf.parse(thisWeekStart));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    cal.add(Calendar.DAY_OF_MONTH, 7);
    thisWeekEnd = sdf.format(cal.getTime());
    return thisWeekEnd;
  }

  private List<String> separateWeekWise(String strToBeCalculated,
                                        String startDate, String endDate, List<String> lstForGraph)
          throws ParseException, IOException {

    String thisDate = "";
    String thisWeekStart = "";
    String thisWeekEnd = "";
    String prevWeekEnd = "";

    String prevWeekPlusStocks = "";
    String prevWeekMinusStocks = "";
    float fAmount = 0;
    float fPlusAmount = 0;
    float fMinusAmount = 0;

    List<String> strWeekWise = new ArrayList<String>();

    Date dtThisDate;
    Date dtThisWeekEnd = null;
    String toCheckValue = "";
    String toAdd = "";

    for (int loop = 0; loop < lstForGraph.size(); loop++) {
      String currentValueLine = lstForGraph.get(loop);

      StringTokenizer st = new StringTokenizer(currentValueLine, ",");
      thisDate = st.nextToken();
      toCheckValue = st.nextToken();

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      try {
        dtThisDate = simpleDateFormat.parse(thisDate);
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }

      if (loop == 0) {
        thisWeekStart = thisDate;
        thisWeekEnd = getTheWeekEnd(thisWeekStart);
        prevWeekEnd = thisWeekEnd;
        dtThisWeekEnd = simpleDateFormat.parse(thisWeekEnd);
      }


      if (!dtThisDate.before(dtThisWeekEnd)) {
        if (toCheckValue.equals("*") && !(prevWeekEnd.equals(thisWeekEnd))) {
          toAdd = prevWeekEnd + "," + fAmount;
          strWeekWise.add(toAdd);
          prevWeekEnd = thisWeekEnd;
          continue;
        }
        fPlusAmount += processValueForGraph(thisWeekEnd, prevWeekPlusStocks, 'W');
        fMinusAmount += processValueForGraph(thisWeekEnd, prevWeekMinusStocks, 'W');
        fAmount = fPlusAmount + fMinusAmount;

        toAdd = thisWeekEnd + "," + fAmount;
        prevWeekEnd = thisWeekEnd;

        thisWeekStart = thisDate;
        thisWeekEnd = getTheWeekEnd(thisWeekStart);
        dtThisWeekEnd = simpleDateFormat.parse(thisWeekEnd);
      } else {
        if (!toCheckValue.equals("*")) {
          if (currentValueLine.contains(",-")) {
            prevWeekMinusStocks += currentValueLine + "\r\n";
          } else {
            prevWeekPlusStocks += currentValueLine + "\r\n";
          }
        }
      }
    }

    return strWeekWise;
  }

  private boolean isTheDateBefore(String toCheckDate, String tobeCheckedDate)
          throws ParseException {
    String strToBeCalculated = "";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date1 = simpleDateFormat.parse(toCheckDate);
    Date date2 = simpleDateFormat.parse(tobeCheckedDate);
    return toCheckDate.compareTo(tobeCheckedDate) < 0;
  }

  private boolean isTheDateEqual(String toCheckDate, String tobeCheckedDate)
          throws ParseException {
    String strToBeCalculated = "";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date1 = simpleDateFormat.parse(toCheckDate);
    Date date2 = simpleDateFormat.parse(tobeCheckedDate);
    return toCheckDate.compareTo(tobeCheckedDate) == 0;
  }

  private boolean isTheDateAfter(String toCheckDate, String tobeCheckedDate)
          throws ParseException {
    String strToBeCalculated = "";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date1 = simpleDateFormat.parse(toCheckDate);
    Date date2 = simpleDateFormat.parse(tobeCheckedDate);
    return toCheckDate.compareTo(tobeCheckedDate) > 0;
  }

  private String getTheNextDate(String startDate) throws ParseException {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date1 = simpleDateFormat.parse(startDate);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date1);
    calendar.add(Calendar.DAY_OF_YEAR, 1);
    return simpleDateFormat.format(calendar.getTime());
  }


  private List<String> separatedDateWise(String strToBeCalculated,
                                         String startDate,
                                         String endDate, List<String> dateRangeList)
          throws ParseException, IOException {

    String thisDate = "";
    String prevDate = "";
    String thisDateMinusStocks = "";
    String thisDatePlusStocks = "";
    boolean bPortFolioEnd = false;

    String toAdd = "";
    float fAmount = 0;
    float fPlusAmount = 0;
    float fMinusAmount = 0;
    String valToCheck = "";

    List<String> lstDayWise = new ArrayList<String>();

    int dateRangeTotal = dateRangeList.size();
    String[] portfolioLines = strToBeCalculated.split("\r\n|\r|\n");
    int portFolioTotal = portfolioLines.length;
    for (int dateloop = 0; dateloop < dateRangeTotal; dateloop++) {
      String currentValueLine = dateRangeList.get(dateloop);
      StringTokenizer st = new StringTokenizer(currentValueLine, ",");
      thisDate = st.nextToken();
      valToCheck = st.nextToken();

      if (dateloop == 0) {
        prevDate = thisDate;
      }

      if (thisDate.equals(prevDate)) {
        if (valToCheck.equals("*")) {
          toAdd = prevDate + "," + fAmount;
          lstDayWise.add(toAdd);
          prevDate = thisDate;
          continue;
        }
        fPlusAmount = processValueForGraph(thisDate, currentValueLine, 'D');
        fMinusAmount = processValueForGraph(thisDate, currentValueLine, 'D');
        fAmount += fPlusAmount + fMinusAmount;

        toAdd = thisDate + "," + fAmount;
        lstDayWise.add(toAdd);
      } else {

        fPlusAmount = processValueForGraph(thisDate, currentValueLine, 'D');
        fMinusAmount = processValueForGraph(thisDate, currentValueLine, 'D');
        fAmount += fPlusAmount + fMinusAmount;

        toAdd = thisDate + "," + fAmount;
        lstDayWise.add(toAdd);
        //currentValueLine += "\r\n";
      }

      prevDate = thisDate;

    }

    return lstDayWise;
  }

  private List<String> separatedYearWise(String strToBeCalculated,
                                         String startDate,
                                         String endDate, List<String> lstForGraph)
          throws ParseException, IOException {

    String thisDate = "";
    String prevYear = "";
    String thisYear = "";
    String thisYearMinusStocks = "";
    String thisYearPlusStocks = "";
    String toAdd = "";
    String valueToCheck = "";

    float fAmount = 0;
    float fPlusAmount = 0;
    float fMinusAmount = 0;

    List<String> lstYearWise = new ArrayList<String>();

    for (int loop = 0; loop < lstForGraph.size(); loop++) {
      String currentValueLine = lstForGraph.get(loop);
      StringTokenizer st = new StringTokenizer(currentValueLine, ",");
      thisDate = st.nextToken();
      valueToCheck = st.nextToken();

      thisYear = thisDate.substring(0, 4);

      if (loop == 0) {
        prevYear = thisYear;
      }

      if (thisYear.equals(prevYear)) {

        if (valueToCheck.equals("*") && !(prevYear.equals(thisYear))) {
          toAdd = prevYear + "," + fAmount;
          lstYearWise.add(toAdd);
          prevYear = thisYear;
          continue;
        }
        if (!valueToCheck.equals("*")) {
          if (currentValueLine.contains(",-")) {
            thisYearMinusStocks += currentValueLine + "\r\n";
          } else {
            thisYearPlusStocks += currentValueLine + "\r\n";
          }
        }
      } else {

        fPlusAmount = processValueForGraph(prevYear, thisYearPlusStocks, 'Y');
        fMinusAmount = processValueForGraph(prevYear, thisYearMinusStocks, 'Y');
        fAmount += fPlusAmount + fMinusAmount;

        toAdd = prevYear + "," + fAmount;
        lstYearWise.add(toAdd);
        prevYear = thisYear;
      }
    }

    toAdd = prevYear + "," + fAmount;
    lstYearWise.add(toAdd);

    return lstYearWise;
  }


}
