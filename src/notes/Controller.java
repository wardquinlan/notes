package notes;

public class Controller {
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
    System.out.println("searching: " + filter);
    try {
      model.set(dao.search(filter));
    } catch(Exception e) {
      System.out.println("could not search");
      e.printStackTrace();
    }
  }
  
  public boolean isModified(String title, String text) {
    System.out.println("isModified: " + title);
    try {
      if (!dao.exists(title)) {
        // if it doesn't exist, it isn't really modified
        return false;
      }
      Note note = dao.read(title);
      return !text.equals(note.getText());
    } catch(Exception e) {
      System.out.println("could not detect if modified");
      e.printStackTrace();
      return true;
    }
  }
  
  public boolean exists(String title) {
    try {
      return dao.exists(title);
    } catch(Exception e) {
      System.out.println("could not test existence");
      e.printStackTrace();
      return true;
    }
  }
  
  public void add(String title, String text) {
    System.out.println("adding: " + title);
    try {
      dao.write(title, text);
      model.clear();
      model.add(dao.read(title));
    } catch(Exception e) {
      System.out.println("could not add");
      e.printStackTrace();
    }
  }

  public void edit(String title, String text) {
    System.out.println("editing: " + title);
    try {
      dao.write(title, text);
      Note note = dao.read(title);
      model.update(frame.getSelectedRow(), note.getTimestamp(), note.getTitle(), note.getText());
    } catch(Exception e) {
      System.out.println("could not edit");
      e.printStackTrace();
    }
  }
  
  public void rename(String title, String newTitle) {
    System.out.println("renaming: " + title + " to " + newTitle);
    try {
      dao.rename(title, newTitle);
      Note note = dao.read(newTitle);
      model.update(frame.getSelectedRow(), note.getTimestamp(), note.getTitle(), note.getText());
    } catch(Exception e) {
      System.out.println("could not rename");
      e.printStackTrace();
    }
  }
  
  public void delete(String title) {
    System.out.println("deleting: " + title);
    try {
      dao.delete(title);
      model.delete(frame.getSelectedRow());
    } catch(Exception e) {
      System.out.println("could not delete");
      e.printStackTrace();
    }
  }
}
