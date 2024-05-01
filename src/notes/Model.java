package notes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class Model extends DefaultTableModel {
  private static final long serialVersionUID = -3950929583108303584L;
  private static final Logger logger = new Logger(Model.class);
  private String filter = "";
  private List<Note> list = new ArrayList<>();
  private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
  
  @Override
  public String getColumnName(int column) {
    switch(column) {
    case 0:
      return "Row";
    case 1:
      return "Timestamp";
    case 2:
      return "Title";
    default:
      logger.error("unexpected column: " + column);
      return null;
    }
  }
  
  @Override
  public int getRowCount() {
    return list == null ? 0 : list.size();
  }
  
  @Override
  public Object getValueAt(int row, int column) {
    Note note = list.get(row);
    switch (column) {
    case 0:
      return row + 1;
    case 1:
      return DF.format(note.getTimestamp());
    case 2:
      return note.getTitle();
    default:
      logger.error("unexpected column: " + column);
      return null;
    }
  }
  
  @Override
  public int getColumnCount() {
    return 3;
  }

  public void add(Note note) {
    list.add(note);
    fireTableDataChanged();
  }
  
  public void update(int index, Date timestamp, String title, String text) {
    Note note = list.get(index);
    note.setTimestamp(timestamp);
    note.setTitle(title);
    note.setText(text);
    fireTableDataChanged();
  }
  
  public void delete(int index) {
    list.remove(index);
    fireTableDataChanged();
  }
  
  public Note get(int index) {
    return list.get(index);
  }
  
  public void clear() {
    list.clear();
    fireTableDataChanged();
  }
  
  public String getFilter() {
    return filter;
  }
  
  public void set(String filter, List<Note> list) {
    this.filter = filter;
    this.list.clear();
    for (Note note: list) {
      this.list.add(note);
    }
    fireTableDataChanged();
  }
}

