package portfolio;

import java.io.InputStreamReader;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This is the main class of the whole application which contains the objects of the
 * model,view and the controller.
 */
public class PortfolioMain {

  /**
   * This is the main class of the whole application which contains the objects of the
   * model,view and the controller.
   *
   * @param args main function argument.
   */
  public static void main(String[] args) {

    try {

      String username = "";
      String userName = "";
      String typeofPortfolio = "";
      boolean newWrite = false;
      String portfolioType = "";
      PortfolioViewGUI pfGUIView = null;
      PortfolioViewImpl pfView = null;
      String inpArg = "";

      if (args.length > 0) {
        inpArg = args[0];
      }

      if (inpArg.equals("C")) {
        PortfolioModel pfModel = new LoadPortfolioModelImpl(username, userName,
                typeofPortfolio, newWrite, portfolioType) {
        };
        pfView = new PortfolioViewImpl(System.out);
        PortfolioController pfController = new PortfolioControllerImpl(pfModel, pfView,
                new InputStreamReader(System.in));
        pfController.getInput();

      }
      if (inpArg.equals("G")) {

        PortfolioViewGUI.setDefaultLookAndFeelDecorated(false);
        PortfolioViewGUI frame = new PortfolioViewGUI();
        PortfolioModel pfModel = new LoadPortfolioModelImpl(username, userName,
                typeofPortfolio, newWrite, portfolioType) {
        };
        ControllerGUI controller = new ControllerGUI(pfModel);
        controller.setView(frame);


        try {
          UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

          UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
          UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
          for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
              UIManager.setLookAndFeel(info.getClassName());
              break;
            }
          }
        } catch (UnsupportedLookAndFeelException e) {
          e.printStackTrace();
        }

      }


    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
