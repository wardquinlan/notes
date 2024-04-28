package notes;

import java.io.File;

public class Main {
  private static final Logger logger = new Logger(Main.class);
  
  public static void main(String[] args) {
    logger.info("starting up");
    if (args.length != 1) {
      System.out.println("usage: notes path");
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
