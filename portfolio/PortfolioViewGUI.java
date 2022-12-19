package portfolio;


import java.awt.GridBagLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;


import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Contains the view class of the Graphical User Interface.
 */
public class PortfolioViewGUI extends JFrame implements PortfolioViewGUIInterface {


  static JMenuBar menuBar;
  static JMenu portfolioMenu;
  static JMenu stockMenu;
  static JMenu AnalysisMenu;
  static JMenu loadMenu;
  static JMenu exitMenu;
  static JMenuItem createPortfolio;
  static JMenuItem portfolioWithStrategy;
  static JMenuItem performanceOfPortfolio;
  static JMenuItem displayPortfolio;
  static JMenuItem buyStock;
  static JMenuItem sellStock;
  static JMenuItem valueDate;
  static JMenuItem costBasis;
  static JMenuItem dollarCost;
  static JMenuItem logout;
  static JLabel label;
  JTextField userName;
  JTextField portfolioType;
  JTextField stockName;
  JTextField enterDate;
  JTextField enterCommission;
  JTextField enterNoOfShares;
  JButton create;
  JButton buy;
  JButton sell;
  JFrame frameForBuyStrat;
  JLabel userLabel;
  JLabel passLabel;
  JPanel panelInvestmentFrame;
  JLabel stock;
  JFrame investmentStratFrame;
  JPanel tfPanel;
  JLabel date;
  JButton costBasisButton;
  JLabel commission;
  JLabel noOfShares;
  JFrame frameForCreateStrat;
  JFrame mainFrame;
  JFrame frame;
  JLabel dateChoice;
  JButton value;
  JFileChooser fileChooser;
  JPanel panelBuyStrat;
  JPanel panelCreateStrat;
  JLabel statusLabel;
  boolean updateStatusLabel;
  String name;
  JLabel compDate;
  JFrame frameForPerformanceGraph;
  JPanel panelGraph;
  JLabel startDateForGraph;
  JTextField startDateForGraphTextField;
  JLabel endDateForGraph;
  JTextField endDateForGraphTextField;
  JTextField compDateTextField;
  JTextField startDateTextField;
  JTextField endDateTextField;
  JTextField investmentAmtTextField;
  JTextField frequencyTextField;
  JTextField weightageTextField;
  JLabel investmentAmount;
  JLabel frequency;
  JLabel weightage;
  JLabel startDate;
  JLabel lblStockSelection;
  JLabel endDate;
  JTextField dateOfChoice;
  JButton load;
  JButton exitButton;
  JButton dollarCostAvg;
  JMenuItem loadPortfolio;
  File selectedFile;
  JList<String> portfolioTypeList;
  String[] portfolioNames;
  JLabel investDate;
  JTextField investDateTextField;
  JButton investBtn;
  JLabel comboBoxLabel;
  JComboBox comboBox;
  Features thisFeature;
  JPanel panelCreate;
  JPanel panelBuy;
  JPanel panelSell;
  JPanel panelValue;
  JPanel panelCostBasis;
  JPanel panelDollarCostAvg;
  JFrame frameForCreate;
  JFrame frameForBuy;
  JFrame frameForSell;
  JFrame frameForValue;
  JFrame frameForCostBasis;
  JFrame frameForDollarCostAvg;
  Set<String> uniqueStockName;
  String pairStockWithWeightage;
  JMenuItem strategizePortfolio;
  int nPercent;

  /**
   * This is the constructor of portfolioViewGUI, which contains the view, of the
   * Graphical User Interface.
   */
  public PortfolioViewGUI() {
    name = JOptionPane.showInputDialog(mainFrame, "Enter UserName:");
    if (name.equals("")) {
      invalidInput("Please enter valid Username!");
      return;
    }
    mainFrame = new JFrame("Portfolio Management");
    mainFrame.setSize(500, 500);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setVisible(true);
    mainFrame.setLayout(new GridBagLayout());
    label = new JLabel("Welcome to Portfolio Management System, " + name + "!");
    label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
    menuBar = new JMenuBar();
    portfolioMenu = new JMenu("Portfolio");
    stockMenu = new JMenu("Stock");
    AnalysisMenu = new JMenu("Analysis of Portfolio");
    loadMenu = new JMenu("Persist a Portfolio");
    exitMenu = new JMenu("Exit");

    createPortfolio = new JMenuItem("Create Portfolio");

    portfolioWithStrategy = new JMenuItem("Create Portfolio With Strategy");

    displayPortfolio = new JMenuItem("Display Portfolio Composition");

    buyStock = new JMenuItem("Buy Stock");

    sellStock = new JMenuItem("Sell Stock");

    valueDate = new JMenuItem("Value On A Certain Date");

    costBasis = new JMenuItem("Cost Basis");

    dollarCost = new JMenuItem("Dollar Cost Averaging");

    performanceOfPortfolio = new JMenuItem("Performance of Portfolio");
    loadPortfolio = new JMenuItem("Persist a Portfolio");
    logout = new JMenuItem("Logout!");
    strategizePortfolio = new JMenuItem("Strategize Existing Portfolio");

    portfolioMenu.add(createPortfolio);
    portfolioMenu.add(portfolioWithStrategy);
    portfolioMenu.add(displayPortfolio);
    stockMenu.add(buyStock);
    stockMenu.add(sellStock);
    AnalysisMenu.add(valueDate);
    AnalysisMenu.add(costBasis);
    AnalysisMenu.add(strategizePortfolio);
    AnalysisMenu.add(dollarCost);
    AnalysisMenu.add(performanceOfPortfolio);
    loadMenu.add(loadPortfolio);
    exitMenu.add(logout);

    menuBar.add(portfolioMenu);
    menuBar.add(stockMenu);
    menuBar.add(AnalysisMenu);
    menuBar.add(loadMenu);
    menuBar.add(exitMenu);

    mainFrame.setJMenuBar(menuBar);


    mainFrame.add(label);

    create = new JButton("Create");
    buy = new JButton("Buy Stock");
    sell = new JButton("Sell Stock");
    value = new JButton("Value Of Portfolio");
    costBasisButton = new JButton("Calculate Cost Basis");
    investBtn = new JButton("Invest");
    load = new JButton("Click Here!");
    dollarCostAvg = new JButton("Dollar Cost Averaging");
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);


  }

  @Override
  public void addFeatures(Features features) {
    thisFeature = features;
    strategizePortfolio.addActionListener(evt -> {
      try {
        frameForInvestmentStrat();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    createPortfolio.addActionListener(evt -> userNamePortfolioFrame());
    portfolioWithStrategy.addActionListener(evt -> userNamePortfolioFrameForStrategy());
    buy.addActionListener(evt -> {
      try {
        features.buyAStock(userName.getText(),
                (String) comboBox.getSelectedItem(), stockName.getText(), enterDate.getText(),
                Float.parseFloat(enterCommission.getText()),
                Float.parseFloat(enterNoOfShares.getText()));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    sell.addActionListener(evt -> {
      try {
        features.sellAStock(userName.getText(), (String) comboBox.getSelectedItem(),
                Float.parseFloat(enterNoOfShares.getText()), enterDate.getText(),
                (float) 0.2, stockName.getText(),
                dateOfChoice.getText(), Float.parseFloat(enterCommission.getText()));
      } catch (IOException | ParseException e) {
        throw new RuntimeException(e);
      }
    });
    value.addActionListener(evt -> {
      try {
        features.calculateValue(userName.getText(), (String) comboBox.getSelectedItem(),
                compDateTextField.getText());
      } catch (IOException | ParseException e) {
        throw new RuntimeException(e);
      }
    });
    costBasisButton.addActionListener(evt -> {
      try {
        features.costBasisCalculation(userName.getText(),
                (String) comboBox.getSelectedItem(), compDateTextField.getText());
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
    });
    displayPortfolio.addActionListener(evt ->
            displayPortfolioFrame());


    logout.addActionListener(evt -> exitFrame());
    buyStock.addActionListener(evt -> buyStockFrame());
    sellStock.addActionListener(evt -> sellStockFrame());
    valueDate.addActionListener(evt -> valueOfPortfolioFrame());
    costBasis.addActionListener(evt -> costBasisFrame());
    loadPortfolio.addActionListener(evt -> {
      try {
        loadPortfolio();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    dollarCost.addActionListener(evt -> {
      try {
        dollarCostAveragingFrame();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    dollarCostAvg.addActionListener(evt -> {
      try {
        if (nPercent != 100 && updateStatusLabel) {
          invalidInput("Sorry! The Total Percentage should be 100%. Your total percentage"
                  + " input-->\n" + nPercent);
          pairStockWithWeightage = "";
          nPercent = 0;
          weightageTextField.requestFocusInWindow();
        } else {
          features.dollarCostAveragingCalculation(userName.getText(),
                  (String) comboBox.getSelectedItem(), startDateTextField.getText(),
                  endDateTextField.getText(),
                  Float.parseFloat(enterCommission.getText()),
                  Float.parseFloat(investmentAmtTextField.getText()),
                  Integer.parseInt(frequencyTextField.getText()), pairStockWithWeightage);
        }
      } catch (IOException | ParseException e) {
        throw new RuntimeException(e);
      }
    });
    portfolioNames = features.portfolioList(name);

    performanceOfPortfolio.addActionListener(e -> performanceGraph());
  }

  private void exitFrame() {
    JFrame exitFrame = new JFrame("Logout Window");
    exitFrame.setSize(200, 200);
    exitFrame.setVisible(true);
    exitFrame.setLocationRelativeTo(null);
    JLabel exitLabel;
    exitLabel = new JLabel("Thankyou for using our Portfolio Management Services!",
            JLabel.CENTER);
    exitFrame.add(exitLabel);
    exitButton = new JButton("Click to Logout!");
    exitButton.addActionListener(e -> System.exit(0));
    exitFrame.add(exitButton);

  }

  @Override
  public void setStatusLabel(String retStr) {
    statusLabel.setText(retStr);
  }

  private void userNamePortfolioFrame() {
    userLabel = new JLabel();
    userLabel.setText("Username");
    userName = new JTextField(20);
    userName.setText(name);
    userName.setEnabled(false);
    passLabel = new JLabel();
    passLabel.setText("PortfolioType");
    portfolioType = new JTextField(20);
    emptyStringValidation(portfolioType);
    portfolioTypeList = new JList();
    frameForCreate = new JFrame("Create a Portfolio");
    frameForCreate.setSize(1000, 500);
    frameForCreate.setVisible(true);
    frameForCreate.setLayout(new GridBagLayout());
    frameForCreate.setLocationRelativeTo(null);
    panelCreate = new JPanel(new GridLayout(3, 2, 10, 2));
    panelCreate.setBorder(BorderFactory.createTitledBorder("Create A Portfolio!"));
    panelCreate.add(userLabel);
    panelCreate.add(userName);
    panelCreate.add(passLabel);
    panelCreate.add(portfolioType);
    panelCreate.add(create);
    create.addActionListener(e -> {
      boolean pfExist = thisFeature.portfolioExists(userName.getText(), portfolioType.getText());
      if (pfExist) {
        invalidInput("Portfolio Already Exists!! Please create different Portfolio");
        return;
      } else {
        thisFeature.createPortfolio(userName.getText(), portfolioType.getText());
        dialogBox();
        return;
      }
    });
    JButton closeCreate = new JButton("Close");
    panelCreate.add(closeCreate);
    closeCreate.addActionListener(e -> frameSetVisible(frameForCreate, mainFrame));

    frameForCreate.add(panelCreate);
    frameForCreate.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  private void loadPortfolio() throws IOException {
    frame = new JFrame("Load a portfolio");
    frame.setSize(1000, 500);
    frame.setVisible(true);
    frame.setLayout(new GridBagLayout());
    frame.setLocationRelativeTo(null);
    JTextArea textArea = new JTextArea();
    fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    int result = fileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      selectedFile = fileChooser.getSelectedFile();
      BufferedReader in;
      in = new BufferedReader(new FileReader(selectedFile));
      String line = in.readLine();
      while (line != null) {
        Font boldFont = new Font(textArea.getFont().getName(), Font.BOLD,
                textArea.getFont().getSize());
        textArea.setFont(boldFont);
        textArea.setSize(375, 250);
        textArea.setText(textArea.getText() + "\n" + line);
        line = in.readLine();
      }
    }
    frame.add(textArea);
    textArea.setEnabled(false);
    frame.setVisible(true);
  }

  private void dollarCostAveragingFrame() throws IOException {
    pairStockWithWeightage = "";
    updateStatusLabel = false;
    nPercent = 0;
    userLabel = new JLabel();
    userLabel.setText("Username");
    userName = new JTextField(20);
    userName.setText(name);
    userName.setEnabled(false);

    comboBoxLabel = new JLabel();
    comboBoxLabel.setText("List of portfolio types under the username:");
    portfolioNames = thisFeature.portfolioList(name);
    comboBox = new JComboBox(portfolioNames);

    startDate = new JLabel();
    startDate.setText("Start Date for Strategy [yyyy-MM-dd] (After Last purchase date)");
    startDateTextField = new JTextField(20);
    emptyStringValidation(startDateTextField);
    buttonClickEmptyStringValidation(startDateTextField);

    endDate = new JLabel();
    endDate.setText("End Date for Strategy [yyyy-MM-dd] / if no end date, please enter 9999-12-31");
    endDateTextField = new JTextField(20);
    emptyStringValidation(endDateTextField);
    buttonClickEmptyStringValidation(endDateTextField);


    investmentAmount = new JLabel();
    investmentAmount.setText("Amount to Invest");
    investmentAmtTextField = new JTextField(20);
    emptyStringValidation(investmentAmtTextField);
    buttonClickEmptyStringValidation(investmentAmtTextField);


    frequency = new JLabel();
    frequency.setText("Frequency of Investment [in Days] ");
    frequencyTextField = new JTextField(20);
    emptyStringValidation(frequencyTextField);
    buttonClickEmptyStringValidation(frequencyTextField);


    lblStockSelection = new JLabel();
    lblStockSelection.setText("Stocks present inside the portfolio");
    String strEntirePortfolio = thisFeature.displayPortfolioTwo(userName.getText(),
            (String) comboBox.getSelectedItem());
    uniqueStockName = thisFeature.displayUniqueStockNames(strEntirePortfolio);

    JComboBox comboBoxStockSelection = new JComboBox(uniqueStockName.toArray());


    comboBox.addActionListener(e -> {
      pairStockWithWeightage = "";
      nPercent = 0;
      JComboBox comboBox = (JComboBox) e.getSource();
      String strEntirePortfolio1 = null;
      try {
        strEntirePortfolio1 = thisFeature.displayPortfolioTwo(userName.getText(),
                (String) comboBox.getSelectedItem());

        boolean isPropExists = thisFeature.propertyFileExist(userName.getText(),
                (String) comboBox.getSelectedItem());

        if (isPropExists) {
          String propertyFile = thisFeature.loadPropertyFileValues(userName.getText(),
                  (String) comboBox.getSelectedItem());
          String setPropElement = "";
          String[] propLines = propertyFile.split("\r\n|\r|\n");
          for (int i = 2; i < propLines.length; i++) {
            String currLine = propLines[i];
            StringTokenizer stm = new StringTokenizer(currLine, "=");
            while (stm.hasMoreTokens()) {
              setPropElement = stm.nextToken();
            }
            if (i == 2) {
              investmentAmtTextField.setText(setPropElement);
              investmentAmtTextField.setEnabled(false);
            }
            if (i == 4) {
              enterCommission.setText(setPropElement);
              enterCommission.setEnabled(false);
            }
            if (i == 5) {
              StringTokenizer stmi = new StringTokenizer(setPropElement, "\\r\\n");


              String strToDisplay = "";
              while (stmi.hasMoreTokens()) {
                strToDisplay += stmi.nextToken() + " | ";
              }
              weightageTextField.setText(strToDisplay);
              weightageTextField.setEnabled(false);
            }

            if (i == 6) {
              frequencyTextField.setText(setPropElement);
              frequencyTextField.setEnabled(false);
            }
            if (i == 7) {
              startDateTextField.setText(setPropElement);
              startDateTextField.setEnabled(false);
            }
            if (i == 8) {
              endDateTextField.setText(setPropElement);
              endDateTextField.setEnabled(false);
              break;
            }
          }
          String returnString = thisFeature.dollarCostAveragingCalculation(userName.getText(),
                  (String) comboBox.getSelectedItem(), startDateTextField.getText(),
                  endDateTextField.getText(),
                  Float.parseFloat(enterCommission.getText()),
                  Float.parseFloat(investmentAmtTextField.getText()),
                  Integer.parseInt(frequencyTextField.getText()), pairStockWithWeightage);
          if (returnString.contains("true")) {
            invalidInput("This Portfolio has been already processed!"
                    + " Please try a different one.");
          } else {
            JOptionPane.showMessageDialog(frameForDollarCostAvg,
                    "This portfolio has already" + " been processed today.",
                    "Information Message", JOptionPane.INFORMATION_MESSAGE);
          }
        } else {
          investmentAmtTextField.setEnabled(true);
          weightageTextField.setEnabled(true);
          endDateTextField.setEnabled(true);
          startDateTextField.setEnabled(true);
          frequencyTextField.setEnabled(true);
          enterCommission.setEnabled(true);
          uniqueStockName = thisFeature.displayUniqueStockNames(strEntirePortfolio1);
          comboBoxStockSelection.removeAllItems();
          Object[] stkNames = uniqueStockName.toArray();
          for (int i = 0; i < stkNames.length; i++) {
            comboBoxStockSelection.addItem(stkNames[i]);
          }
        }
      } catch (IOException | ParseException ex) {
        throw new RuntimeException(ex);
      }

    });


    weightage = new JLabel();
    weightage.setText("Weightage for Stock [in %]");
    weightageTextField = new JTextField(20);
    buttonClickEmptyStringValidation(weightageTextField);


    weightageTextField.addFocusListener(new java.awt.event.FocusAdapter() {
      private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {

        if (weightageTextField.getText().length() > 0 && !(pairStockWithWeightage.contains((String)
                comboBoxStockSelection.getSelectedItem()))) {
          pairStockWithWeightage += comboBoxStockSelection.getSelectedItem() + ","
                  + weightageTextField.getText() + "\r\n";
          JOptionPane.showMessageDialog(panelDollarCostAvg, pairStockWithWeightage);
          nPercent += Integer.parseInt(weightageTextField.getText());
          updateStatusLabel = true;
        }

      }

      public void focusGained(java.awt.event.FocusEvent evt) {
        //empty
      }

      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextField1FocusLost(evt);
      }
    });


    commission = new JLabel();
    commission.setText("Commission Fee for this Strategy");
    enterCommission = new JTextField(20);
    buttonClickEmptyStringValidation(enterCommission);

    frameForDollarCostAvg = new JFrame("Dollar Cost Averaging");
    frameForDollarCostAvg.setSize(1000, 500);
    frameForDollarCostAvg.setVisible(true);
    frameForDollarCostAvg.setLocationRelativeTo(null);
    panelDollarCostAvg = new JPanel(new GridLayout(16, 5,
            20, 2));
    panelDollarCostAvg.setBorder(BorderFactory.createTitledBorder("Dollar Cost Averaging"));
    panelDollarCostAvg.add(userLabel);
    panelDollarCostAvg.add(userName);
    panelDollarCostAvg.add(comboBoxLabel);
    panelDollarCostAvg.add(comboBox);
    panelDollarCostAvg.add(startDate);
    panelDollarCostAvg.add(startDateTextField);
    panelDollarCostAvg.add(endDate);
    panelDollarCostAvg.add(endDateTextField);
    panelDollarCostAvg.add(investmentAmount);
    panelDollarCostAvg.add(investmentAmtTextField);
    panelDollarCostAvg.add(frequency);
    panelDollarCostAvg.add(frequencyTextField);
    panelDollarCostAvg.add(lblStockSelection);
    panelDollarCostAvg.add(comboBoxStockSelection);
    panelDollarCostAvg.add(weightage);
    panelDollarCostAvg.add(weightageTextField);
    panelDollarCostAvg.add(commission);
    panelDollarCostAvg.add(enterCommission);
    JButton displayPortfolioButton;
    displayPortfolioButton = new JButton("Display Portfolio");
    panelDollarCostAvg.add(displayPortfolioButton);
    displayPortfolioButton.addActionListener(e -> {
      try {
        thisFeature.displayPortfolio(userName.getText(),
                (String) comboBox.getSelectedItem());
        return;
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
    panelDollarCostAvg.add(dollarCostAvg);
    JButton closeDollar = new JButton("Close");
    panelDollarCostAvg.add(closeDollar);
    closeDollar.addActionListener(e -> frameSetVisible(frameForDollarCostAvg, mainFrame));
    dollarCostAvg.addActionListener(e -> JOptionPane.showMessageDialog(panelDollarCostAvg,
            "Dollar cost averaging "
                    + "is successfully done!"));
    frameForDollarCostAvg.add(panelDollarCostAvg);
    frameForDollarCostAvg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }


  private void buyStockFrame() {
    userLabel = new JLabel();
    userLabel.setText("Username");
    userName = new JTextField(20);
    userName.setText(name);
    userName.setEnabled(false);
    comboBoxLabel = new JLabel();
    comboBoxLabel.setText("List of portfolio types under the username:");
    portfolioNames = thisFeature.portfolioList(name);
    comboBox = new JComboBox(portfolioNames);
    frameForBuy = new JFrame("Buy a stock!");
    frameForBuy.setSize(1000, 500);
    frameForBuy.setVisible(true);
    frameForBuy.setLayout(new FlowLayout());
    stock = new JLabel();
    stock.setText("Enter Stock Symbol:");
    stockName = new JTextField(20);
    emptyStringValidation(stockName);
    buttonClickEmptyStringValidation(stockName);


    date = new JLabel();
    date.setText("Enter the date you want to buy stock in:");
    enterDate = new JTextField(20);
    emptyStringValidation(enterDate);
    buttonClickEmptyStringValidation(enterDate);


    commission = new JLabel();
    commission.setText("Enter the commission Fee:");
    enterCommission = new JTextField(20);
    emptyStringValidation(enterCommission);
    buttonClickEmptyStringValidation(enterCommission);


    noOfShares = new JLabel();
    noOfShares.setText("Enter the no of shares you'd like to buy");
    enterNoOfShares = new JTextField(20);
    emptyStringValidation(enterNoOfShares);
    buttonClickEmptyStringValidation(enterNoOfShares);

    panelBuy = new JPanel(new GridLayout(12, 2, 10, 2));
    panelBuy.setBorder(BorderFactory.createTitledBorder("Buy A Stock!"));
    panelBuy.add(userLabel);
    panelBuy.add(userName);
    panelBuy.add(comboBoxLabel);
    panelBuy.add(comboBox);
    panelBuy.add(stock);
    panelBuy.add(stockName);
    panelBuy.add(date);
    panelBuy.add(enterDate);
    panelBuy.add(noOfShares);
    panelBuy.add(enterNoOfShares);
    panelBuy.add(commission);
    panelBuy.add(enterCommission);
    JButton displayPortfolioButton;
    displayPortfolioButton = new JButton("Display Portfolio");
    panelBuy.add(displayPortfolioButton);
    displayPortfolioButton.addActionListener(e -> {
      try {
        thisFeature.displayPortfolio(userName.getText(),
                (String) comboBox.getSelectedItem());
        return;
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
    panelBuy.add(buy);
    JButton closeBuy = new JButton("Close");
    panelBuy.add(closeBuy);
    closeBuy.addActionListener(e -> frameSetVisible(frameForBuy, mainFrame));
    frameForBuy.add(panelBuy);

    frameForBuy.setLocationRelativeTo(null);

    frameForBuy.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }


  private void buyStockFrameForStrat() {
    userLabel = new JLabel();
    userLabel.setText("Username");
    userName = new JTextField(20);
    userName.setText(name);
    userName.setEnabled(false);
    passLabel = new JLabel();
    passLabel.setText("PortfolioType");
    String portfolioName = portfolioType.getText();
    portfolioType.setText(portfolioName);
    portfolioType.setEnabled(false);
    frameForBuyStrat = new JFrame("Buy stock!");
    frameForBuyStrat.setSize(1000, 500);
    frameForBuyStrat.setVisible(true);
    frameForBuyStrat.setLayout(new FlowLayout());
    stock = new JLabel();
    stock.setText("Enter Stock Symbol:");
    stockName = new JTextField(20);
    emptyStringValidation(stockName);
    buttonClickEmptyStringValidation(stockName);

    date = new JLabel();
    date.setText("Enter the date you want to buy stock in:");
    enterDate = new JTextField(20);
    emptyStringValidation(enterDate);
    buttonClickEmptyStringValidation(enterDate);

    commission = new JLabel();
    commission.setText("Enter the commission Fee:");
    enterCommission = new JTextField(20);
    emptyStringValidation(enterCommission);
    buttonClickEmptyStringValidation(enterCommission);

    noOfShares = new JLabel();
    noOfShares.setText("Enter the no of shares you'd like to buy");
    enterNoOfShares = new JTextField(20);
    emptyStringValidation(enterNoOfShares);
    buttonClickEmptyStringValidation(enterNoOfShares);

    panelBuyStrat = new JPanel(new GridLayout(12, 2, 10, 2));
    panelBuyStrat.setBorder(BorderFactory.createTitledBorder("Buy A Stock!"));
    panelBuyStrat.add(userLabel);
    panelBuyStrat.add(userName);
    panelBuyStrat.add(passLabel);
    panelBuyStrat.add(portfolioType);
    panelBuyStrat.add(stock);
    panelBuyStrat.add(stockName);
    panelBuyStrat.add(date);
    panelBuyStrat.add(enterDate);
    panelBuyStrat.add(noOfShares);
    panelBuyStrat.add(enterNoOfShares);
    panelBuyStrat.add(commission);
    panelBuyStrat.add(enterCommission);
    JButton displayPortfolioButton;
    displayPortfolioButton = new JButton("Display Portfolio");
    panelBuyStrat.add(displayPortfolioButton);
    displayPortfolioButton.addActionListener(e -> {
      String portfolio = thisFeature.displayPortfolioTwo(userName.getText(),
              portfolioType.getText());
      JOptionPane.showMessageDialog(panelBuyStrat, portfolio, "Display Portfolio",
              JOptionPane.INFORMATION_MESSAGE);
      return;

    });

    JButton buyStockBtn = new JButton("Buy Stock");
    panelBuyStrat.add(buyStockBtn);
    buyStockBtn.addActionListener(e -> {
      try {
        thisFeature.buyAStock(userName.getText(), portfolioType.getText(), stockName.getText(),
                enterDate.getText(), Float.parseFloat(enterCommission.getText()),
                Float.parseFloat(enterNoOfShares.getText()));
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
    JButton dollarCostAvgBtn = new JButton("Dollar Cost Averaging");
    panelBuyStrat.add(dollarCostAvgBtn);
    dollarCostAvgBtn.addActionListener(e -> {
      try {
        dollarCostAveragingFrame();
        frameSetVisible(frameForBuyStrat, frameForDollarCostAvg);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
    JButton closeBuy = new JButton("Close");
    panelBuyStrat.add(closeBuy);
    closeBuy.addActionListener(e -> frameSetVisible(frameForBuyStrat, mainFrame));
    frameForBuyStrat.add(panelBuyStrat);


    frameForBuyStrat.setLocationRelativeTo(null);

    frameForBuyStrat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  @Override
  public void emptyStringValidation(JTextField textField) {
    textField.addFocusListener(new java.awt.event.FocusAdapter() {
      private void textFieldFocusLost(java.awt.event.FocusEvent evt) {
        if (textField.getText().equals("")) {
          invalidInput("Please enter a valid entry!");
          return;
        }

      }

      public void focusGained(java.awt.event.FocusEvent evt) {
        //empty
      }

      public void focusLost(java.awt.event.FocusEvent evt) {
        textFieldFocusLost(evt);
      }
    });

  }

  private void userNamePortfolioFrameForStrategy() {
    userLabel = new JLabel();
    userLabel.setText("Username");
    userName = new JTextField(20);
    userName.setText(name);
    userName.setEnabled(false);
    passLabel = new JLabel();
    passLabel.setText("PortfolioType");
    portfolioType = new JTextField(20);
    emptyStringValidation(portfolioType);
    buttonClickEmptyStringValidation(portfolioType);

    portfolioTypeList = new JList();
    frameForCreateStrat = new JFrame("Create a Portfolio");
    frameForCreateStrat.setSize(1000, 500);
    frameForCreateStrat.setVisible(true);
    frameForCreateStrat.setLayout(new GridBagLayout());
    frameForCreateStrat.setLocationRelativeTo(null);
    panelCreateStrat = new JPanel(new GridLayout(12, 4, 10, 2));
    panelCreateStrat.setBorder(BorderFactory.createTitledBorder("Create A Portfolio!"));
    panelCreateStrat.add(userLabel);
    panelCreateStrat.add(userName);
    panelCreateStrat.add(passLabel);
    panelCreateStrat.add(portfolioType);
    panelCreateStrat.add(create);
    create.addActionListener(e -> {
      boolean pfExist = thisFeature.portfolioExists(userName.getText(), portfolioType.getText());
      if (!pfExist) {
        thisFeature.createPortfolio(userName.getText(), portfolioType.getText());
        dialogBox();
        return;
      }
    });
    JButton strategyBuy = new JButton("Buy stocks for this portfolio");
    panelCreateStrat.add(strategyBuy);
    strategyBuy.addActionListener(e -> {
      buyStockFrameForStrat();
      frameSetVisible(frameForCreateStrat, frameForBuyStrat);
    });
    JButton closeCreate = new JButton("Close");
    panelCreateStrat.add(closeCreate);
    closeCreate.addActionListener(e -> frameSetVisible(frameForCreateStrat, mainFrame));
    frameForCreateStrat.add(panelCreateStrat);
    frameForCreateStrat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  private void sellStockFrame() {
    userLabel = new JLabel();
    userLabel.setText("Username");
    userName = new JTextField(20);
    userName.setText(name);
    userName.setEnabled(false);
    comboBoxLabel = new JLabel();
    comboBoxLabel.setText("List of portfolio types under the username:");
    comboBox = new JComboBox(portfolioNames);

    frameForSell = new JFrame("Sell A Stock!");
    frameForSell.setSize(1000, 500);
    frameForSell.setVisible(true);
    frameForSell.setLayout(new FlowLayout());

    stock = new JLabel();
    stock.setText("Enter Stock Symbol:");
    stockName = new JTextField(20);
    emptyStringValidation(stockName);
    buttonClickEmptyStringValidation(stockName);

    date = new JLabel();
    date.setText("Enter the date you want to sell stock in:");
    enterDate = new JTextField(20);
    emptyStringValidation(enterDate);
    buttonClickEmptyStringValidation(enterDate);

    commission = new JLabel();
    commission.setText("Enter the commission Fee:");
    enterCommission = new JTextField(20);
    emptyStringValidation(enterCommission);
    buttonClickEmptyStringValidation(enterCommission);

    dateChoice = new JLabel();
    dateChoice.setText("Enter the choice date [If Any],"
            + "/If not enter the date shown in the status label below:");
    dateOfChoice = new JTextField(20);
    emptyStringValidation(dateOfChoice);
    buttonClickEmptyStringValidation(dateOfChoice);

    noOfShares = new JLabel();
    noOfShares.setText("Enter the no of shares you'd like to sell");
    enterNoOfShares = new JTextField(20);
    emptyStringValidation(enterNoOfShares);
    buttonClickEmptyStringValidation(enterNoOfShares);

    statusLabel = new JLabel();
    statusLabel.setText("status");
    stockName.addFocusListener(new java.awt.event.FocusAdapter() {
      private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {
        if (stockName.getText().equals("")) {
          invalidInput("Please enter a valid Stock name");
          return;
        } else {
          String myString = thisFeature.setLabelStatus(userName.getText(),
                  (String) comboBox.getSelectedItem(), stockName.getText().toUpperCase());
          setStatusLabel("<HTML>" + myString + "</HTML>");
        }

      }

      public void focusGained(java.awt.event.FocusEvent evt) {
        //empty
      }

      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextField1FocusLost(evt);
      }
    });


    panelSell = new JPanel(new GridLayout(12, 2, 10, 2));
    panelSell.setBorder(BorderFactory.createTitledBorder("Sell A Stock!"));
    panelSell.setFocusTraversalKeysEnabled(false);
    panelSell.add(userLabel);
    panelSell.add(userName);
    panelSell.add(comboBoxLabel);
    panelSell.add(comboBox);
    panelSell.add(stock);
    panelSell.add(stockName);
    panelSell.add(dateChoice);
    panelSell.add(dateOfChoice);
    panelSell.add(date);
    panelSell.add(enterDate);
    panelSell.add(noOfShares);
    panelSell.add(enterNoOfShares);
    panelSell.add(commission);
    panelSell.add(enterCommission);
    JButton displayPortfolioButton;
    displayPortfolioButton = new JButton("Display Portfolio");
    panelSell.add(displayPortfolioButton);
    displayPortfolioButton.addActionListener(e -> {
      try {
        thisFeature.displayPortfolio(userName.getText(),
                (String) comboBox.getSelectedItem());
        return;
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
    panelSell.add(sell);
    panelSell.add(statusLabel);
    JButton closeSell = new JButton("Close");
    panelSell.add(closeSell);
    closeSell.addActionListener(e -> frameSetVisible(frameForSell, mainFrame));
    frameForSell.add(panelSell);
    frameForSell.setLocationRelativeTo(null);
    frameForSell.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

  }

  private void valueOfPortfolioFrame() {
    frameForValue = new JFrame("Value of A Portfolio");
    frameForValue.setSize(1000, 500);
    frameForValue.setVisible(true);
    frameForValue.setLayout(new FlowLayout());
    userLabel = new JLabel();
    userLabel.setText("Username");
    userName = new JTextField(20);
    userName.setText(name);
    userName.setEnabled(false);
    comboBoxLabel = new JLabel();
    comboBoxLabel.setText("List of portfolio types under the username:");
    portfolioNames = thisFeature.portfolioList(name);
    comboBox = new JComboBox(portfolioNames);
    compDate = new JLabel();
    compDate.setText("Enter Date to Compare:");
    compDateTextField = new JTextField(20);
    emptyStringValidation(compDateTextField);
    buttonClickEmptyStringValidation(compDateTextField);

    panelValue = new JPanel(new GridLayout(12, 2, 10, 2));
    panelValue.setBorder(BorderFactory.createTitledBorder("Value of Portfolio"));
    panelValue.add(userLabel);
    panelValue.add(userName);
    panelValue.add(comboBoxLabel);
    panelValue.add(comboBox);
    panelValue.add(compDate);
    panelValue.add(compDateTextField);
    JButton displayPortfolioButton;
    displayPortfolioButton = new JButton("Display Portfolio");
    panelValue.add(displayPortfolioButton);
    displayPortfolioButton.addActionListener(e -> {
      try {
        thisFeature.displayPortfolio(userName.getText(),
                (String) comboBox.getSelectedItem());
        return;
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
    panelValue.add(value);
    JButton closeVal = new JButton("Close");
    panelValue.add(closeVal);
    closeVal.addActionListener(e -> frameSetVisible(frameForValue, mainFrame));
    frameForValue.add(panelValue);
    frameForValue.setLocationRelativeTo(null);
    frameForValue.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  private void costBasisFrame() {
    frameForCostBasis = new JFrame("Cost Basis of A Portfolio");
    frameForCostBasis.setSize(1000, 500);
    frameForCostBasis.setVisible(true);
    frameForCostBasis.setLayout(new FlowLayout());
    userLabel = new JLabel();
    userLabel.setText("Username");
    userName = new JTextField(20);
    userName.setText(name);
    userName.setEnabled(false);
    comboBoxLabel = new JLabel();
    comboBoxLabel.setText("List of portfolio types under the username:");
    portfolioNames = thisFeature.portfolioList(name);
    comboBox = new JComboBox(portfolioNames);
    compDate = new JLabel();
    compDate.setText("Enter date to find cost basis:");
    compDateTextField = new JTextField(20);
    panelCostBasis = new JPanel(new GridLayout(12, 2, 10, 2));
    panelCostBasis.setBorder(BorderFactory.createTitledBorder("Cost Basis"));
    panelCostBasis.add(userLabel);
    panelCostBasis.add(userName);
    panelCostBasis.add(comboBoxLabel);
    panelCostBasis.add(comboBox);
    panelCostBasis.add(compDate);
    panelCostBasis.add(compDateTextField);
    JButton displayPortfolioButton;
    displayPortfolioButton = new JButton("Display Portfolio");
    panelCostBasis.add(displayPortfolioButton);
    displayPortfolioButton.addActionListener(e -> {
      try {
        thisFeature.displayPortfolio(userName.getText(),
                (String) comboBox.getSelectedItem());
        return;
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
    panelCostBasis.add(costBasisButton);
    JButton closeCostBasis = new JButton("Close");
    panelCostBasis.add(closeCostBasis);
    closeCostBasis.addActionListener(e -> frameSetVisible(frameForCostBasis, mainFrame));
    frameForCostBasis.add(panelCostBasis);
    frameForCostBasis.setLocationRelativeTo(null);
    frameForCostBasis.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

  }

  private void frameForInvestmentStrat() throws IOException {
    pairStockWithWeightage = "";
    userLabel = new JLabel();
    userLabel.setText("Username");
    userName = new JTextField(20);
    userName.setText(name);
    userName.setEnabled(false);

    comboBoxLabel = new JLabel();
    comboBoxLabel.setText("List of portfolio types under the username:");
    portfolioNames = thisFeature.portfolioList(name);
    comboBox = new JComboBox(portfolioNames);

    investmentAmount = new JLabel();
    investmentAmount.setText("Amount to Invest");
    investmentAmtTextField = new JTextField(20);
    emptyStringValidation(investmentAmtTextField);
    buttonClickEmptyStringValidation(investmentAmtTextField);


    lblStockSelection = new JLabel();
    lblStockSelection.setText("Stocks present inside the portfolio");
    String strEntirePortfolio = thisFeature.displayPortfolioTwo(userName.getText(),
            (String) comboBox.getSelectedItem());
    Set<String> uniqueStockName1 = thisFeature.displayUniqueStockNames(strEntirePortfolio);
    JComboBox comboBoxStockSelection = new JComboBox(uniqueStockName1.toArray());


    comboBox.addActionListener(e -> {
      pairStockWithWeightage = "";
      JComboBox comboBox = (JComboBox) e.getSource();

      String strFullPortfolio = null;
      strFullPortfolio = thisFeature.displayPortfolioTwo(userName.getText(),
              (String) comboBox.getSelectedItem());
      Set<String> uniqueStockName11 = thisFeature.displayUniqueStockNames(strFullPortfolio);
      comboBoxStockSelection.removeAllItems();
      Object[] stkNames = uniqueStockName11.toArray();
      for (int i = 0; i < stkNames.length; i++) {

        comboBoxStockSelection.addItem(stkNames[i].toString());
      }
    });


    weightage = new JLabel();
    weightage.setText("Weightage for Stock [in %]");
    weightageTextField = new JTextField(20);
    emptyStringValidation(weightageTextField);
    buttonClickEmptyStringValidation(weightageTextField);


    weightageTextField.addFocusListener(new java.awt.event.FocusAdapter() {
      private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {

        pairStockWithWeightage += comboBoxStockSelection.getSelectedItem() + ","
                + weightageTextField.getText() + "\r\n";
        JOptionPane.showMessageDialog(panelDollarCostAvg, pairStockWithWeightage);


      }

      public void focusGained(java.awt.event.FocusEvent evt) {
        //empty
      }

      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextField1FocusLost(evt);
      }
    });
    investDate = new JLabel();
    investDate.setText("Date for Strategy [yyyy-MM-dd] (After Last purchase date)");
    investDateTextField = new JTextField(20);
    emptyStringValidation(investDateTextField);
    buttonClickEmptyStringValidation(investDateTextField);


    commission = new JLabel();
    commission.setText("Commission Fee for this Strategy");
    enterCommission = new JTextField(20);
    emptyStringValidation(enterCommission);
    buttonClickEmptyStringValidation(enterCommission);


    investmentStratFrame = new JFrame("Strategize Existing Portfolio");
    investmentStratFrame.setSize(1000, 500);
    investmentStratFrame.setVisible(true);
    investmentStratFrame.setLayout(new GridBagLayout());
    investmentStratFrame.setLocationRelativeTo(null);
    panelInvestmentFrame = new JPanel(new GridLayout(16, 2,
            20, 2));
    panelInvestmentFrame.setBorder(BorderFactory.createTitledBorder("Investment Strategy for"
            + "Existing Portfolio"));
    panelInvestmentFrame.add(userLabel);
    panelInvestmentFrame.add(userName);
    panelInvestmentFrame.add(comboBoxLabel);
    panelInvestmentFrame.add(comboBox);
    panelInvestmentFrame.add(lblStockSelection);
    panelInvestmentFrame.add(comboBoxStockSelection);
    panelInvestmentFrame.add(weightage);
    panelInvestmentFrame.add(weightageTextField);
    panelInvestmentFrame.add(investDate);
    panelInvestmentFrame.add(investDateTextField);
    panelInvestmentFrame.add(investmentAmount);
    panelInvestmentFrame.add(investmentAmtTextField);
    panelInvestmentFrame.add(commission);
    panelInvestmentFrame.add(enterCommission);
    JButton displayPortfolioButton;
    displayPortfolioButton = new JButton("Display Portfolio");
    panelInvestmentFrame.add(displayPortfolioButton);
    displayPortfolioButton.addActionListener(e -> {
      try {
        thisFeature.displayPortfolio(userName.getText(),
                (String) comboBox.getSelectedItem());
        return;
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
    panelInvestmentFrame.add(investBtn);
    JButton closeDollar = new JButton("Close");
    closeDollar.addActionListener(e -> frameSetVisible(investmentStratFrame, mainFrame));
    panelInvestmentFrame.add(closeDollar);
    investBtn.addActionListener(e -> {
      try {
        thisFeature.investmentStrategyExisting(strEntirePortfolio, userName.getText(),
                (String) comboBox.getSelectedItem(),
                investDateTextField.getText(), pairStockWithWeightage,
                Float.parseFloat(enterCommission.getText()),
                Float.parseFloat(investmentAmtTextField.getText()));
        JOptionPane.showMessageDialog(panelInvestmentFrame, "You have successfully invested"
                + " in this portfolio!");

      } catch (ParseException | IOException ex) {
        throw new RuntimeException(ex);
      }

    });
    investmentStratFrame.add(panelInvestmentFrame);
    investmentStratFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

  }

  private void displayPortfolioFrame() {
    frame = new JFrame("Display Portfolio");
    frame.setSize(1000, 500);
    frame.setVisible(true);
    frame.setLayout(new FlowLayout());
    userLabel = new JLabel();
    userLabel.setText("Username");
    userName = new JTextField(20);
    userName.setText(name);
    userName.setEnabled(false);
    comboBoxLabel = new JLabel();
    comboBoxLabel.setText("List of portfolio types under the username:");
    portfolioNames = thisFeature.portfolioList(name);
    comboBox = new JComboBox(portfolioNames);
    tfPanel = new JPanel(new GridLayout(12, 2, 10, 2));
    tfPanel.setBorder(BorderFactory.createTitledBorder("Displaying a portfolio"));
    tfPanel.add(userLabel);
    tfPanel.add(userName);
    tfPanel.add(comboBoxLabel);
    tfPanel.add(comboBox);
    JButton displayPortfolioButton;
    displayPortfolioButton = new JButton("Display Portfolio");
    tfPanel.add(displayPortfolioButton);
    displayPortfolioButton.addActionListener(e -> {
      try {
        thisFeature.displayPortfolio(userName.getText(),
                (String) comboBox.getSelectedItem());
        return;
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
    JButton closeDisp = new JButton("Close");
    tfPanel.add(closeDisp);
    closeDisp.addActionListener(e -> frameSetVisible(frame, mainFrame));
    frame.add(tfPanel);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    return;
  }


  /**
   * Function to plot performance graph.
   */

  public void performanceGraph() {
    frameForPerformanceGraph = new JFrame("Performance Graph");
    frameForPerformanceGraph.setSize(1000, 500);
    frameForPerformanceGraph.setVisible(true);
    frameForPerformanceGraph.setLayout(new FlowLayout());
    userLabel = new JLabel();
    userLabel.setText("Username");
    userName = new JTextField(20);
    userName.setText(name);
    userName.setEnabled(false);
    comboBoxLabel = new JLabel();
    comboBoxLabel.setText("List of portfolio types under the username:");
    comboBox = new JComboBox(portfolioNames);
    startDateForGraph = new JLabel();
    startDateForGraph.setText("Enter the start Date for the performance graph:");
    startDateForGraphTextField = new JTextField(20);
    endDateForGraph = new JLabel();
    endDateForGraph.setText("Enter the end Date for the performance graph:");
    endDateForGraphTextField = new JTextField(20);
    panelGraph = new JPanel(new GridLayout(12, 2, 10, 2));
    panelGraph.setBorder(BorderFactory.createTitledBorder("Performance Graph"));
    panelGraph.setFocusTraversalKeysEnabled(false);
    panelGraph.add(userLabel);
    panelGraph.add(userName);
    panelGraph.add(comboBoxLabel);
    panelGraph.add(comboBox);
    panelGraph.add(startDateForGraph);
    panelGraph.add(startDateForGraphTextField);
    panelGraph.add(endDateForGraph);
    panelGraph.add(endDateForGraphTextField);
    JButton performanceGraphBtn = new JButton("Click to see the graph!");
    panelGraph.add(performanceGraphBtn);
    performanceGraphBtn.addActionListener(e -> {
      try {
        List<String> resultSet = thisFeature.performanceGraph(userName.getText(),
                (String) comboBox.getSelectedItem(),
                startDateForGraphTextField.getText(), endDateForGraphTextField.getText());
      } catch (ParseException | IOException ex) {
        throw new RuntimeException(ex);
      }
    });
    frameForPerformanceGraph.add(panelGraph);
    frameForPerformanceGraph.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

  }

  @Override
  public void frameSetVisible(JFrame f1, JFrame f2) {
    f2.setVisible(true);
    f1.setVisible(false);
  }

  @Override
  public void dialogBox() {
    JOptionPane.showMessageDialog(frame, "Portfolio Created Successfully!");
    return;
  }

  @Override
  public void dialogStock() {
    JOptionPane.showMessageDialog(frame, "Stock Bought Successfully!");
    return;
  }

  @Override
  public void getValueDialog(String valDialog) {
    JOptionPane.showMessageDialog(frame, "The total value is: $" + valDialog,
            "Total Value On A Certain Date", JOptionPane.INFORMATION_MESSAGE);
    return;

  }

  @Override
  public void getCostBasisDialog(String costBasisDialog) {
    JOptionPane.showMessageDialog(frame, costBasisDialog,
            "Information Dialog", JOptionPane.INFORMATION_MESSAGE);
    return;
  }

  @Override
  public void getPortfolioDisplayDialog(String dispDialog) {
    JOptionPane.showMessageDialog(frame, dispDialog, "Portfolio Display",
            JOptionPane.INFORMATION_MESSAGE);
    return;

  }

  @Override
  public void invalidInput(String invInp) {
    JOptionPane.showMessageDialog(frame, invInp, "Error Message", JOptionPane.ERROR_MESSAGE);
    return;
  }

  private void buttonClickEmptyStringValidation(JTextField textField) {
    textField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (textField.getText().equals("")) {
          invalidInput("Empty Value!");
          return;
        }

      }
    });

  }
}


