package notes;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAO {
  public List<Note> search(String filter) throws Exception {
    List<Note> list = new ArrayList<>();
    File home = new File(System.getProperty("notes.home"));
    File[] files = home.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".txt");
      }
    });
    for (File file: files) {
      Note note = new Note();
      note.setTimestamp(new Date(file.lastModified()));
      note.setTitle(file.getName().substring(0, file.getName().length() - 4));
      list.add(note);
    }
    return list;
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
