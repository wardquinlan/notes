package notes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Comparator;
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
      String title = file.getName().substring(0, file.getName().length() - 4);
      Note note = read(title);
      if (title.toUpperCase().contains(filter.toUpperCase()) || 
          note.getText().toUpperCase().contains(filter.toUpperCase())) {
        list.add(note);
      }
    }
    list.sort(Comparator.reverseOrder());
    return list;
  }
  
  public boolean exists(String title) throws Exception {
    File file = new File(System.getProperty("notes.home") + File.separator + title + ".txt");
    return file.exists();
  }
  
  public Note read(String title) throws Exception {
    File file = new File(System.getProperty("notes.home") + File.separator + title + ".txt");
    FileReader reader = new FileReader(file);
    StringBuffer sb = new StringBuffer();
    while (true) {
      int val = reader.read();
      if (val == -1) {
        break;
      }
      sb.append((char) val);
    }
    reader.close();
    Note note = new Note();
    note.setTimestamp(new Date(file.lastModified()));
    note.setTitle(title);
    note.setText(sb.toString());
    return note;
  }

  public void write(String title, String text) throws Exception {
    BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("notes.home") + File.separator + title + ".txt"));
    writer.write(text);
    //writer.newLine();
    writer.close();
  }

  public void delete(String title) throws Exception {
    File file = new File(System.getProperty("notes.home") + File.separator + title + ".txt");
    file.delete();
  }
  
  public void rename(String title, String newTitle) throws Exception {
    File file = new File(System.getProperty("notes.home") + File.separator + title + ".txt");
    File fileTo = new File(System.getProperty("notes.home") + File.separator + newTitle + ".txt");
    file.renameTo(fileTo);
  }
}
