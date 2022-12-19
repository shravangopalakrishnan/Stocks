package portfolio;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * StockModelImpl handles all manipulation features with respect to Stocks.
 */
public abstract class StockModelImpl extends PortfolioModelImpl {
  /**
   * Constructor created for PortfolioModelImpl, which contains the path for file storing.
   *
   * @param username        username an input from user.
   * @param userName        username of the user, an input from user.
   * @param typeofPortfolio type of the portfolio the user wants to create.
   * @param newWrite        boolean value to see if the user creates a file or not.
   * @param portfolioType   type of the portfolio the user wants to create.
   */
  public StockModelImpl(String username, String userName, String typeofPortfolio,
                        Boolean newWrite, String portfolioType) {
    super(username, userName, typeofPortfolio, newWrite, portfolioType);
  }


  @Override
  public String buyStock(String portfolioChosen, String stockSymbol, float noOfShares,
                         String inpDate, String userName, String portfolioType,
                         float commissionFee) {
    String dateStr = "";
    String stockName = "";
    String closeValue = "";
    String sharesOnPortfolio = "";
    String totalAmount = "";
    String strTobeWritten = "";
    String currentValueLine = "";
    String strCommission = "";
    String retString = "";

    boolean bStockAndDate = false;
    String[] portfolioLines = portfolioChosen.split("\r\n|\r|\n");
    String s = null;
    float fTotalAmount = 0;
    float nNumberOfShares = 0;
    int brokenLineNumber = 0;
    for (int loop = 1; loop < portfolioLines.length; loop++) {
      currentValueLine = portfolioLines[loop];
      StringTokenizer st = new StringTokenizer(currentValueLine, ",");
      String[] portfolioLineValues = new String[6];
      int i = 0;
      while (st.hasMoreTokens()) {
        portfolioLineValues[i] = st.nextToken();
        i++;
      }
      dateStr = portfolioLineValues[0];
      stockName = portfolioLineValues[1];
      closeValue = portfolioLineValues[2];
      sharesOnPortfolio = portfolioLineValues[3];
      totalAmount = portfolioLineValues[4];
      strCommission = portfolioLineValues[5];
      if (dateStr.equals(inpDate) && stockName.equals(stockSymbol)) {
        nNumberOfShares = Float.parseFloat(sharesOnPortfolio);
        nNumberOfShares += noOfShares;
        sharesOnPortfolio = String.valueOf(nNumberOfShares);
        fTotalAmount = Float.parseFloat(closeValue) * noOfShares;
        float commFeeValue = Float.parseFloat(totalAmount) * (commissionFee / 100);
        commFeeValue += Float.parseFloat(strCommission);
        fTotalAmount += Float.valueOf(totalAmount);
        totalAmount = String.valueOf(fTotalAmount);
        String comFeeValueStr = String.valueOf(commFeeValue);
        currentValueLine = dateStr + "," + stockName + "," + closeValue + "," + sharesOnPortfolio
                + "," + totalAmount + "," + comFeeValueStr + "\r\n";
        strTobeWritten += currentValueLine;
        brokenLineNumber = loop;
        bStockAndDate = true;
        break;
      } else {
        strTobeWritten += dateStr + "," + stockName + "," + closeValue + ","
                + sharesOnPortfolio + "," + totalAmount
                + "," + strCommission + "\r\n";
      }
    }
    if (bStockAndDate) {
      if (brokenLineNumber < portfolioLines.length) {
        for (int n = brokenLineNumber + 1; n < portfolioLines.length; n++) {
          currentValueLine = portfolioLines[n];
          strTobeWritten += currentValueLine + "\r\n";
        }
        stockTobeWritten = strTobeWritten;
        writeToPortfolio(userName, portfolioType, true);
        retString = "Stock purchased Successfully!";
      }
    }
    if (!bStockAndDate) {
      try {
        retString = differenceInValueOfStock(inpDate, stockSymbol,
                String.valueOf(noOfShares), true);
        if (retString.equals("0.01")) {
          retString = "The Values not found for given Date! It is a Holiday";
          return retString;
        }
        if (retString.equals("0.02")) {
          retString = "The given Stock Symbol is Invalid! Please try a valid one.";
          return retString;
        }

        apiValueFetch(retString, noOfShares, commissionFee, true); //apifetch w boolean
        writeToPortfolio(userName, portfolioType, false);
        retString = "Stock purchased successfully!";
      } catch (IOException | ParseException e) {
        throw new RuntimeException(e);
      }
    }
    return retString;
  }

  @Override
  public String sellStock(String userName, String portfolioType, String portfolioContent,
                          float noOfShares, String inpDate,
                          String stockSymbol, String choiceDate,
                          float commissionFee) throws IOException, ParseException {

    String dateStr = "";
    String stockName = "";
    String closeValue = "";
    String sharesOnPortfolio = "";
    String totalAmount = "";
    String strCommission = "";

    String[] portfolioLines = portfolioContent.split("\r\n|\r|\n");

    String s = null;
    float fTotalAmount = 0;
    float retValue = 0;
    boolean bStockFound = false;
    boolean bNotWritePortfolio = false;
    String strToUpdatePortfolio = "";
    float nCloseValue;
    String retStr = "";

    for (int loop = 1; loop < portfolioLines.length; loop++) {
      String currentValueLine = portfolioLines[loop];

      StringTokenizer st = new StringTokenizer(currentValueLine, ",");
      String[] portfolioLineValues = new String[6];
      int i = 0;
      while (st.hasMoreTokens()) {
        portfolioLineValues[i] = st.nextToken();
        i++;
      }

      dateStr = portfolioLineValues[0];
      stockName = portfolioLineValues[1];
      closeValue = portfolioLineValues[2];

      nCloseValue = Float.parseFloat(closeValue);

      sharesOnPortfolio = portfolioLineValues[3];
      totalAmount = portfolioLineValues[4];
      strCommission = portfolioLineValues[5];
      fTotalAmount = Float.parseFloat(totalAmount);


      if (stockName.equals(stockSymbol) && (dateStr.equals(choiceDate)
              || choiceDate.equals("0000-00-00"))) {
        if (Float.parseFloat(sharesOnPortfolio) < 0) {
          strToUpdatePortfolio += currentValueLine + "\r\n";
          continue;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = simpleDateFormat.parse(dateStr);
        Date date2 = simpleDateFormat.parse(inpDate);
        if (date1.compareTo(date2) > 0) {

          bNotWritePortfolio = true;
          retStr = "The date given " + inpDate
                  + " is before the date you purchased your first stock " + dateStr;
          return retStr;
        }

        if (noOfShares > Float.parseFloat(sharesOnPortfolio)) {
          bNotWritePortfolio = true;
          break;
        } else {
          float fShareAmount;

          float nBalance = Float.parseFloat(sharesOnPortfolio) - noOfShares;

          String strStockValue = differenceInValueOfStock(inpDate, stockName,
                  String.valueOf(noOfShares), true);
          if (strStockValue.equals("0.01")) {
            retStr = "The date given is a Holiday. No trading happened on that Day";
            return retStr;
          }
          if (strStockValue.equals("0.02")) {
            retStr = "The given Stock Symbol is Invalid! Please try a valid one.";
            return retStr;
          }
          String closeVal = apiValueFetch(strStockValue, noOfShares, commissionFee, true);
          fShareAmount = noOfShares * Float.parseFloat(closeVal);

          float commision = (fShareAmount * commissionFee) / 100;

          strToUpdatePortfolio += inpDate + "," + stockName + "," + closeVal
                  + "," + "-" + noOfShares + ","
                  + "-" + fShareAmount + "," + commision + "\r\n";
          if (nBalance >= 0) {
            currentValueLine = dateStr + "," + stockName + "," + closeValue
                    + "," + sharesOnPortfolio
                    + "," + totalAmount + "," + strCommission;
          } else {
            currentValueLine = "";
          }

        }
        bStockFound = true;
      }

      if (currentValueLine.length() > 0) {
        strToUpdatePortfolio += currentValueLine + "\r\n";

      }
    }
    if (!bNotWritePortfolio) {
      stockTobeWritten = strToUpdatePortfolio;
      writeToPortfolio(userName, portfolioType, true);
      retStr = "Shares Sold Successfully!";
    }
    return retStr;
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
  public String differenceInValueOfStock(String date, String stockName, String noOfShares,
                                         boolean toBuy) throws IOException, ParseException {
    String retStr;
    retStr = getStockValue(stockName, 0);
    if (retStr.contains("Invalid Stock Symbol")) {
      retStr = "0.02";
      return retStr;
    }
    String lineFoundFromApi = "";
    float fAmtOfThatDay = 0;
    int indexOfString = outputStock.indexOf(date);
    if (indexOfString == -1) {
      fAmtOfThatDay = (float) 0.01;
      return String.valueOf(fAmtOfThatDay);
    }
    int endOfString = indexOfString;
    for (int i = indexOfString; i < outputStock.length(); i++) {
      if (outputStock.charAt(i) == '\n') {
        endOfString = i;
        break;
      }
    }
    if (endOfString > indexOfString) {
      lineFoundFromApi = outputStock.substring(indexOfString, endOfString);
    }
    return lineFoundFromApi;
  }

  @Override
  public Float getAmount(float totAmtInvested, float weightage) {
    float amount = totAmtInvested * (weightage / 100);
    return amount;
  }

}


