package notes;

public class Main {
  public static void main(String[] args) {
    System.out.println("starting up");
    if (args.length != 1) {
      System.out.println("usage: notes path");
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
      System.out.println("could not launch frame");
      e.printStackTrace();
      System.exit(1);
    }
  }
}
