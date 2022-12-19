package portfolio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * LoadPortfolioModelImpl lets the user load and retrieve a portfolio.
 */
public abstract class LoadPortfolioModelImpl extends AnalysisModelImpl {
  /**
   * Constructor created for PortfolioModelImpl, which contains the path for file storing.
   *
   * @param username        username an input from user.
   * @param userName        username of the user, an input from user.
   * @param typeofPortfolio type of the portfolio the user wants to create.
   * @param newWrite        boolean value to see if the user creates a file or not.
   * @param portfolioType   type of the portfolio the user wants to create.
   */
  public LoadPortfolioModelImpl(String username, String userName, String typeofPortfolio,
                                Boolean newWrite, String portfolioType) {
    super(username, userName, typeofPortfolio, newWrite, portfolioType);
  }


  @Override
  public String loadAndRetrievePortfolio(String directory) throws FileNotFoundException {
    String portFolioReturn = "";
    String filename;

    if (!directory.contains(".txt")) {
      portFolioReturn = "Please enter a portfolio filename with .txt extension";
      return portFolioReturn;
    } else {
      filename = currPath + directory;
    }


    File file = new File(filename);
    if (!file.exists()) {
      portFolioReturn = "The specified file not exists!!  Please enter Valid One";
      return portFolioReturn;
    }
    FileReader fr = new FileReader(filename);
    Scanner scan = new Scanner(file);

    try {
      fr = new FileReader(filename);
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

}
