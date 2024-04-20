package notes;

import java.util.ArrayList;
import java.util.List;

public class DAO {
  public List<Note> search(String filter) throws Exception {
    return new ArrayList<Note>();
  }
  
  public void add(String title, String text) throws Exception {
  }

  public void edit(String title, String text) throws Exception {
  }
  
  public void delete(String title) throws Exception {
  }
  
  public Note get(String title) throws Exception {
    return null;
  }

  public Note getLast() throws Exception {
    return null;
  }
}
