package notes;

import java.util.ArrayList;
import java.util.List;

public class Utils {
  static public List<Integer> getFilterIndexes(String filter, String text) {
    filter = filter.toUpperCase();
    text = text.toUpperCase();
    List<Integer> list = new ArrayList<>();
    if (filter.equals("")) {
      return list;
    }
    int index = 0;
    while (true) {
      index = text.indexOf(filter, index);
      if (index == -1) {
        break;
      }
      list.add(index);
      index += filter.length();
    }
    return list;
  }
}
