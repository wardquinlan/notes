package notes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.HighlightPainter;

public class Utils {
  static public final HighlightPainter PAINTER = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
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
