package notes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
  public static void main(String[] args) {
    System.out.println("starting up");
    try {
      System.out.println("reading notebook properties");
      InputStream input = new FileInputStream(System.getenv("NB_HOME") + File.separator + "notebook.d" + File.separator + "notebook.properties");
      System.getProperties().load(input);
    } catch (IOException e) {
      System.err.println("cannot load notepad properties");
      e.printStackTrace();
      System.exit(1);
    }
    
    System.out.println("extracting database connection properties");
    String host;
    if (System.getProperty("notebook.host") == null) {
      host = "localhost";
    } else {
      host = System.getProperty("notebook.host");
    }
    if (System.getProperty("notebook.database") == null) {
      System.err.println("notebook.database not set");
      System.exit(1);
    }
    String database = System.getProperty("notebook.database");
    
    if (System.getProperty("notebook.username") == null) {
      System.err.println("notebook.username not set");
      System.exit(1);
    }
    String username = System.getProperty("notebook.username");
    
    if (System.getProperty("notebook.password") == null) {
      System.err.println("notebook.password not set");
      System.exit(1);
    }
    String password = System.getProperty("notebook.password");
    
    System.out.println("connecting to database");
    DAO dao = new DAO();
    try {
      dao.connect(host, database, username, password);
    } catch(Exception e) {
      System.err.println("can't connect to database");
      e.printStackTrace();
      System.exit(1);
    }
    
    System.out.println("adding shutdown hook");
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        System.out.println("shutting down...");
        try {
          dao.close();
          System.out.println("database connection closed");
        } catch(Exception e) {
          System.err.println("could not close database connection");
          e.printStackTrace();
        }
      }
    });
    
    try {
      Controller controller = new Controller(dao);
      Model model = new Model();
      controller.setModel(model);
      Frame frame = new Frame(controller, model);
      controller.setFrame(frame);
      frame.setVisible(true);
    } catch(Exception e) {
      System.err.println("could not launch frame");
      e.printStackTrace();
      System.exit(1);
    }
  }
}
