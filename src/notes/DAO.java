package notes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DAO {
  @SuppressWarnings("unused")
  private static final Logger logger = new Logger(DAO.class);
  
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
    BufferedReader reader = null;
    try {
      if ("true".equals(System.getProperty("notes.usedefaultcharset"))) {
        reader = new BufferedReader(new FileReader(file));
      } else {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
      }
      StringBuffer sb = new StringBuffer();
      while (true) {
        int val = reader.read();
        if (val == -1) {
          break;
        }
        sb.append((char) val);
      }
      Note note = new Note();
      note.setTimestamp(new Date(file.lastModified()));
      note.setTitle(title);
      note.setText(sb.toString());
      return note;
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }

  public void write(String title, String text) throws Exception {
    BufferedWriter writer = null;
    try {
      if ("true".equals(System.getProperty("notes.usedefaultcharset"))) {
        writer = new BufferedWriter(new FileWriter(System.getProperty("notes.home") + File.separator + title + ".txt"));
      } else {
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("notes.home") + File.separator + title + ".txt"), "UTF-8"));
      }
      writer.write(text);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  public void delete(String title) throws Exception {
    File file = new File(System.getProperty("notes.home") + File.separator + title + ".txt");
    boolean ret = file.delete();
    if (!ret) {
      throw new Exception("could not delete file: " + title);
    }
  }
  
  public void rename(String title, String newTitle) throws Exception {
    File file = new File(System.getProperty("notes.home") + File.separator + title + ".txt");
    File fileTo = new File(System.getProperty("notes.home") + File.separator + newTitle + ".txt");
    boolean ret = file.renameTo(fileTo);
    if (!ret) {
      throw new Exception("could not rename file: " + title + " => " + newTitle);
    }
  }
}
