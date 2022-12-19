package portfolio;

import java.io.IOException;

/**
 * Class implementation for PortfolioView interface. It has appendable out, that is, the
 * System out function.
 */

public class PortfolioViewImpl implements PortfolioView {
  final Appendable out;

  /**
   * Constructor for the viewImpl class.
   *
   * @param out out for output.
   */
  public PortfolioViewImpl(Appendable out) {
    this.out = out;
  }

  @Override
  public void printer(String s) throws IOException {
    this.out.append(s);
  }

}




