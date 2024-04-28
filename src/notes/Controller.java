package notes;

public class Controller {
  private static final Logger logger = new Logger(Controller.class);
  private Frame frame;
  private DAO dao;
  private Model model;
  
  public Controller(DAO dao) {
    this.dao = dao;
  }
  
  public void setFrame(Frame frame) {
    this.frame = frame;
  }

  public void setModel(Model model) {
    this.model = model;
  }
  
  public void search(String filter) {
    logger.info("searching: " + filter);
    try {
      model.set(filter, dao.search(filter));
    } catch(Exception e) {
      logger.error("could not search", e);
    }
  }
  
  public boolean isModified(String title, String text) {
    logger.info("isModified: " + title);
    try {
      if (!dao.exists(title)) {
        // if it doesn't exist, it isn't really modified
        return false;
      }
      Note note = dao.read(title);
      return !text.equals(note.getText());
    } catch(Exception e) {
      logger.error("could not detect if modified", e);
      return true;
    }
  }
  
  public boolean exists(String title) {
    logger.info("exists: " + title);
    try {
      return dao.exists(title);
    } catch(Exception e) {
      logger.error("could not test existence", e);
      return true;
    }
  }
  
  public void add(String title, String text) {
    logger.info("adding: " + title);
    try {
      dao.write(title, text);
      model.clear();
      model.add(dao.read(title));
    } catch(Exception e) {
      logger.error("could not add", e);
    }
  }

  public void edit(String title, String text) {
    logger.info("editing: " + title);
    try {
      dao.write(title, text);
      Note note = dao.read(title);
      model.update(frame.getSelectedRow(), note.getTimestamp(), note.getTitle(), note.getText());
    } catch(Exception e) {
      logger.error("could not edit", e);
    }
  }
  
  public void rename(String title, String newTitle) {
    logger.info("renaming: " + title + " to " + newTitle);
    try {
      dao.rename(title, newTitle);
      Note note = dao.read(newTitle);
      model.update(frame.getSelectedRow(), note.getTimestamp(), note.getTitle(), note.getText());
    } catch(Exception e) {
      logger.error("could not rename", e);
    }
  }
  
  public void delete(String title) {
    logger.info("deleting: " + title);
    try {
      dao.delete(title);
      model.delete(frame.getSelectedRow());
    } catch(Exception e) {
      logger.error("could not delete", e);
    }
  }
}
