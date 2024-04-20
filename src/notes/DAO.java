package notes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAO {
  private Connection conn;
  
  public void connect(String host, String database, String username, String password) throws Exception {
    Class.forName("org.postgresql.Driver");
    String url = "jdbc:postgresql://" + host + "/" + database + "?user=" + username;
    System.out.println("connecting to database using " + url);
    if (password != null) {
      url = url + "&password=" + password;
    }
    conn = DriverManager.getConnection(url);  
  }
  
  public void close() throws Exception {
    if (conn != null) {
      conn.close();
    }
  }
  
  public List<Note> search(String filter) throws Exception {
    List<Note> list = new ArrayList<>();
    PreparedStatement ps;
    if ("".equals(filter)) {
      ps = conn.prepareStatement("select * from notebook order by ts desc");
    } else {
      ps = conn.prepareStatement("select * from notebook where upper(title) like upper('%' || ? || '%') or upper(note) like upper('%' || ? || '%') order by ts desc");
      ps.setString(1, filter);
      ps.setString(2, filter);
    }
    ResultSet resultSet = ps.executeQuery();
    while (resultSet.next()) {
      Note note = new Note();
      note.setId(resultSet.getInt(1));
      note.setTimestamp(resultSet.getTimestamp(2));
      note.setTitle(resultSet.getString(3));
      note.setText(resultSet.getString(4));
      list.add(note);
    }
    return list;
    // select * from notebook where UPPER(note) like UPPER('%myx%') or UPPER(title) like UPPER('%my%');
  }
  
  public void add(String title, String text) throws Exception {
    PreparedStatement ps = conn.prepareStatement("insert into notebook(ts, title, note) values(NOW(), ?, ?)");
    ps.setString(1, title);
    ps.setString(2, text);
    ps.executeUpdate();
  }

  public void edit(int id, String title, String text) throws Exception {
    PreparedStatement ps = conn.prepareStatement("update notebook set ts = NOW(), title = ?, note = ? where id = ?");
    ps.setString(1, title);
    ps.setString(2, text);
    ps.setInt(3, id);
    ps.executeUpdate();
  }
  
  public void delete(int id) throws Exception {
    PreparedStatement ps = conn.prepareStatement("delete from notebook where id = ?");
    ps.setInt(1, id);
    ps.executeUpdate();
  }
  
  public Note get(int id) throws Exception {
    PreparedStatement ps = conn.prepareStatement("select id, ts, title, note from notebook where id = ?");
    ps.setInt(1, id);
    ResultSet resultSet = ps.executeQuery();
    if (!resultSet.next()) {
      return null;
    }
    Note note = new Note();
    note.setId(resultSet.getInt(1));
    note.setTimestamp(resultSet.getTimestamp(2));
    note.setTitle(resultSet.getString(3));
    note.setText(resultSet.getString(4));
    return note;
  }

  public Note getLast() throws Exception {
    PreparedStatement ps = conn.prepareStatement("select id, ts, title, note from notebook where id = (select MAX(id) from notebook)");
    ResultSet resultSet = ps.executeQuery();
    if (!resultSet.next()) {
      return null;
    }
    Note note = new Note();
    note.setId(resultSet.getInt(1));
    note.setTimestamp(resultSet.getTimestamp(2));
    note.setTitle(resultSet.getString(3));
    note.setText(resultSet.getString(4));
    return note;
  }
}
