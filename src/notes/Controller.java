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
      System.err.println("could not search");
      e.printStackTrace();
    }
  }
  
  public void add(String title, String note) {
    System.out.println("adding: " + title);
    try {
      dao.add(title, note);
      model.clear();
      model.add(dao.getLast());
    } catch(Exception e) {
      System.err.println("could not add");
      e.printStackTrace();
    }
  }

  public void edit(int id, String title, String text) {
    System.out.println("editing: " + id + ", " + title);
    try {
      dao.edit(id, title, text);
      Note note = dao.get(id);
      model.edit(frame.getSelectedRow(), note.getTimestamp(), note.getTitle(), note.getText());
    } catch(Exception e) {
      System.err.println("could not edit");
      e.printStackTrace();
    }
  }
  
  public void delete(int id) {
    System.out.println("deleting: " + id);
    try {
      dao.delete(id);
      model.delete(frame.getSelectedRow());
    } catch(Exception e) {
      System.err.println("could not delete");
      e.printStackTrace();
    }
  }
}
