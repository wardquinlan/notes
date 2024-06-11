package notes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

public class Frame extends JFrame {
  private static final long serialVersionUID = 4319336198324776603L;
  private static final Logger logger = new Logger(Frame.class);
  private static final int WIDTH = 1200;
  private static final int HEIGHT = 800;
  private static final int DIVIDER_LOCATION = 550;
  private static final int DEFAULT_FONT_SIZE = 12;
  private HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
  
  private FilterPanel filterPanel;
  private JTable table;
  private JTextArea textArea;
  
  public Frame(Controller controller, Model model) throws Exception {
    super("Notes - " + System.getProperty("notes.home"));
    setSize(WIDTH, HEIGHT);
    
    int size = DEFAULT_FONT_SIZE;
    Font font;
    if (System.getProperty("notes.fontsize") != null) {
      size = Integer.parseInt(System.getProperty("notes.fontsize"));
    }
    font = new Font(Font.MONOSPACED, Font.PLAIN, size);
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    UIManager.put("TextArea.font", font);
    UIManager.put("Table.font", font);
    UIManager.put("TextField.font", font);
    getContentPane().setLayout(new BorderLayout());
    
    filterPanel = new FilterPanel(this, controller, model);
    add(filterPanel, BorderLayout.NORTH);
    
    table = new Table(this, controller, model);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setDividerLocation(DIVIDER_LOCATION);
    splitPane.setTopComponent(scrollPane);
    
    textArea = new JTextArea();
    textArea.setLineWrap(true);
    textArea.setEditable(false);
    textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
    splitPane.setBottomComponent(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    
    add(splitPane, BorderLayout.CENTER);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void setText(String filter, String text) {
    logger.info("filter:" + filter);
    textArea.setText(text);
    List<Integer> list = getFilterIndexes(filter, text);
    Highlighter highlighter = textArea.getHighlighter();
    for (Integer index: list) {
      try {
        highlighter.addHighlight(index, index + filter.length(), painter);
      } catch(Exception e) {
        logger.error("unable to highlight: " + index, e);
      }
    }
    textArea.setCaretPosition(0);
  }
  
  private List<Integer> getFilterIndexes(String filter, String text) {
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
  
  public int getSelectedRow() {
    return table.getSelectedRow();
  }

  public void setSelectedRow(int index) {
    table.setRowSelectionInterval(index, index);
    table.requestFocus();
  }
  
  public void requestFocus() {
    filterPanel.requestFocus();
  }
}
