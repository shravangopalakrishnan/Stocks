package portfolio;


import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * Interface PortfolioViewGUIInterface is the View interface that is implemented by class
 * PortfolioViewGUI.
 * This is responsible for implementing the Graphical User Interface,
 */
public interface PortfolioViewGUIInterface {
  /**
   * Function to add features in the GUI.
   *
   * @param features features present inside the GUI.
   */
  void addFeatures(Features features);

  /**
   * Function which gives a pop-up which is a dialog box.
   */

  void dialogBox();

  /**
   * Function which gives a pop-up which is a dialog box related to stocks.
   */

  void dialogStock();

  /**
   * Dialog box for get value of a portfolio function.
   *
   * @param valDialog contains the message to be displayed in the pop-up.
   */

  void getValueDialog(String valDialog);

  /**
   * Dialog box to get the value of cost basis.
   *
   * @param costBasisDialog contains the message to be displayed in the pop-up
   */
  void getCostBasisDialog(String costBasisDialog);

  /**
   * Display dialog box for portfolio display.
   *
   * @param dispDialog contains the message to be displayed in the pop-up
   */
  void getPortfolioDisplayDialog(String dispDialog);

  /**
   * Dialog box for any invalid input passed.
   *
   * @param ivpInp contains the message to be displayed in the pop-up
   */

  void invalidInput(String ivpInp);

  /**
   * Status label for the stock and weightage entered by the user.
   *
   * @param retStr contains the return string.
   */
  void setStatusLabel(String retStr);

  /**
   * Function which decides to keep one frame visible and the other invisible.
   *
   * @param f1 Frame 1 which is set to false.
   * @param f2 Frame 2 which is set to true.
   */

  void frameSetVisible(JFrame f1, JFrame f2);

  /**
   * Function to validated empty string using keylistener.
   *
   * @param textField textfield input to validate the particular textfield.
   */

  void emptyStringValidation(JTextField textField);


}
