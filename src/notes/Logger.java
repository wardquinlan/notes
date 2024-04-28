package notes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Logger {
  private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private Class<?> clazz;
  
  public Logger(Class<?> clazz) {
    this.clazz = clazz;
  }
  
  public void info(String message) {
    Date now = Calendar.getInstance().getTime();
    System.out.println(DF.format(now) + " INFO " + clazz.getName() + " - " + message);
  }
  
  public void info(String message, Exception e) {
    info(message);
    e.printStackTrace();
  }
  
  public void error(String message) {
    Date now = Calendar.getInstance().getTime();
    System.out.println(DF.format(now) + " ERROR " + clazz.getName() + " - " + message);
  }

  public void error(String message, Exception e) {
    error(message);
    e.printStackTrace();
  }
}
