package notes;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Locale;

public class Main {
  private static final Logger logger = new Logger(Main.class);
  
  public static void main(String[] args) {
    logger.info("starting up");
    logger.info("java vendor=" + System.getProperty("java.vendor"));
    logger.info("java version=" + System.getProperty("java.version"));
    logger.info("default locale=" + Locale.getDefault());
    logger.info("default charset=" + Charset.defaultCharset());
    logger.info("using default charset: " + ("true".equals(System.getProperty("notes.usedefaultcharset")) ? "true" : "false"));
    if (args.length != 1) {
      logger.error("usage: notes path");
      System.exit(1);
    }
    File file = new File(args[0]);
    if (!file.isDirectory()) {
      logger.error(args[0] + " does not exist");
      System.exit(1);
    }
    System.getProperties().put("notes.home", args[0]);
    try {
      DAO dao = new DAO();
      Controller controller = new Controller(dao);
      Model model = new Model();
      controller.setModel(model);
      Frame frame = new Frame(controller, model);
      controller.setFrame(frame);
      frame.setVisible(true);
    } catch(Exception e) {
      logger.error("could not launch frame", e);
      System.exit(1);
    }
  }
}
