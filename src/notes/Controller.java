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
  
  public void add(String title, String text) {
    System.out.println("adding: " + title);
    try {
      dao.add(title, text);
      model.clear();
      model.add(dao.get(title));
    } catch(Exception e) {
      System.out.println("could not add");
      e.printStackTrace();
    }
  }

  public void edit(String title, String text) {
    System.out.println("editing: " + title);
    try {
      dao.edit(title, text);
      Note note = dao.get(title);
      model.edit(frame.getSelectedRow(), note.getTimestamp(), note.getTitle(), note.getText());
    } catch(Exception e) {
      System.out.println("could not edit");
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
