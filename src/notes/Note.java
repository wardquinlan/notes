package notes;

import java.util.Date;

public class Note implements Comparable<Note> {
  private Date timestamp;
  private String title;
  private String text;
  
  public Date getTimestamp() {
    return timestamp;
  }
  
  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
  
  @Override
  public int compareTo(Note note) {
    return this.timestamp.compareTo(note.getTimestamp());
  }
}
