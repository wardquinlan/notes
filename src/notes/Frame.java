package notes;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Frame extends JFrame {
  private static final long serialVersionUID = 4319336198324776603L;
  private static final int WIDTH = 1200;
  private static final int HEIGHT = 800;
  private static final int DIVIDER_LOCATION = 400;
  private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 11);
  
  private FilterPanel filterPanel;
  private JTable table;
  private JTextArea textArea;
  
  public Frame(Controller controller, Model model) throws Exception {
    super("Notes - " + System.getProperty("notes.home"));
    setSize(WIDTH, HEIGHT);
    
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    UIManager.put("TextArea.font", FONT);
    UIManager.put("Table.font", FONT);
    UIManager.put("TextField.font", FONT);
    getContentPane().setLayout(new BorderLayout());
    
    filterPanel = new FilterPanel(this, controller, model);
    add(filterPanel, BorderLayout.NORTH);
    
    table = new Table(this, controller, model);
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
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

  public void setText(String text) {
    textArea.setText(text);
    textArea.setCaretPosition(0);
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
