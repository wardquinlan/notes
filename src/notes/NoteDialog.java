package notes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Highlighter;

public class NoteDialog extends JDialog {
  private static final long serialVersionUID = 6299811276766639359L;
  @SuppressWarnings("unused")
  private static final Logger logger = new Logger(NoteDialog.class);
  private static final String DIALOG_TITLE = "Note";
  private static final int WIDTH = 1000;
  private static final int HEIGHT = 800;
  private static final Color BACKGROUND = new Color(0xe0, 0xe0, 0xe0);
  private static final int MAX_TITLE_LENGTH = 30;
  private static final DateFormat DF = new SimpleDateFormat("MMM d yyyy");
  private static final DateFormat DF2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private String text;
  private Controller controller;
  private Frame frame;
  private JTextField titleField = new JTextField();
  private JTextArea textArea = new JTextArea();
  private String title;

  public NoteDialog(Frame frame, Controller controller, String title, String text, boolean rename) {
    super(frame, DIALOG_TITLE, true);
    this.controller = controller;
    this.frame = frame;
    this.text = text;
    this.title = title;
    textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK), "none");
    setLayout(new BorderLayout());
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BorderLayout());
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    titleField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (titleField.getText().length() == MAX_TITLE_LENGTH) {
          e.consume();
        }
      }
    });
    if (title != null) {
      titleField.setText(title);
      if (rename) {
        titleField.selectAll();
      } else {
        titleField.setEditable(false);
      }
    }

    JTextField highlight = new JTextField(20);
    highlight.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          String filter = highlight.getText();
          String text = textArea.getText();
          List<Integer> list = Utils.getFilterIndexes(filter, text);
          Highlighter highlighter = textArea.getHighlighter();
          highlighter.removeAllHighlights();
          for (Integer index: list) {
            try {
              highlighter.addHighlight(index, index + filter.length(), Utils.PAINTER);
            } catch(Exception ex) {
              logger.error("unable to highlight: " + index, ex);
            }
          }
        }
      }
    });
    JButton clear = new JButton("Clear");
    clear.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        textArea.getHighlighter().removeAllHighlights();
        highlight.setText("");
        highlight.requestFocus();
      }
    });
    JPanel clearPanel = new JPanel();
    clearPanel.add(clear);
    LabeledComponent highlightComponent = new LabeledComponent("Highlight", highlight);
    highlightComponent.setBorder(new EmptyBorder(0, 5, 0, 0));
    JPanel highlightPanel = new JPanel();
    highlightPanel.setLayout(new BorderLayout());
    highlightPanel.add(highlightComponent, BorderLayout.CENTER);
    highlightPanel.add(clearPanel, BorderLayout.EAST);
    topPanel.add(new LabeledComponent("Title", titleField), BorderLayout.CENTER);
    topPanel.add(highlightPanel, BorderLayout.EAST);
    mainPanel.add(topPanel, BorderLayout.NORTH);
    textArea.setLineWrap(true);
    if (title != null) {
      textArea.setText(getText());
      if (rename) {
        textArea.setEditable(false);
        textArea.setBackground(BACKGROUND);
      }
    }
    textArea.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
          Date now = Calendar.getInstance().getTime();
          String date = DF.format(now);
          StringBuffer sb = new StringBuffer();
          for (int i = 0; i < date.length(); i++) {
            sb.append('-');
          }
          textArea.insert(date + "\n" + sb.toString() + "\n\n", textArea.getCaretPosition());
        }
        if (e.getKeyCode() == KeyEvent.VK_T && e.isControlDown()) {
          Date now = Calendar.getInstance().getTime();
          String date = DF2.format(now);
          textArea.insert(date + "\n", textArea.getCaretPosition());
        }
        if (e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {
          highlight.requestFocus();
          highlight.selectAll();
        }
      }
    });
    mainPanel.add(new LabeledComponent("Note", new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)), BorderLayout.CENTER);
    mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(mainPanel, BorderLayout.CENTER);
    
    JButton ok = new JButton("OK");
    ok.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (title == null) {
          if (!validateTitleField(frame, controller, titleField)) {
            return;
          }
          controller.add(titleField.getText(), textArea.getText());
          frame.setSelectedRow(0);
        } else if (rename) {
          if (!validateTitleField(frame, controller, titleField)) {
            return;
          }
          int row = frame.getSelectedRow();
          controller.rename(title, titleField.getText());
          frame.setSelectedRow(row);
        } else {
          if (controller.isModified(title, getText())) {
            JOptionPane.showMessageDialog(frame, "The note has been modified externally; you will have to cancel, refresh your search, and then try again", "Error", JOptionPane.ERROR_MESSAGE);
            return;
          }
          int row = frame.getSelectedRow();
          controller.edit(titleField.getText(), textArea.getText());
          frame.setSelectedRow(row);
        }
        dispose();
      }
    });
    
    JButton write = null;
    if (title != null && !rename) {
      write = new JButton("Write");
      write.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          write();
        }
      });
      textArea.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
            write();
          }
        }
      });
    }
    
    JButton cancel = new JButton("Cancel");
    cancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        frame.requestFocus();
        dispose();
      }
    });
    
    addWindowFocusListener(new WindowAdapter() {
      @Override
      public void windowGainedFocus(WindowEvent e) {
        if (title != null && !rename) {
          textArea.requestFocus();
        }
      }
    });
    
    textArea.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        if (!textArea.getText().equals(getText())) {
          setTitle("* " + DIALOG_TITLE);
        }
      }
    });

    JPanel buttonPanel = new JPanel();
    if (write != null) {
      buttonPanel.add(write);
    }
    buttonPanel.add(ok);
    buttonPanel.add(cancel);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    
    setSize(WIDTH, HEIGHT);
    setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - WIDTH / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - HEIGHT / 2);
    setVisible(true);
  }
  
  private void write() {
    if (controller.isModified(title, getText())) {
      JOptionPane.showMessageDialog(frame, "The note has been modified externally; you will have to cancel, refresh your search, and then try again", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    int row = frame.getSelectedRow();
    controller.edit(titleField.getText(), textArea.getText());
    frame.setSelectedRow(row);
    setText(textArea.getText());
    setTitle(DIALOG_TITLE);
    textArea.requestFocus();
  }
  
  private boolean validateTitleField(Frame frame, Controller controller, JTextField titleField) {
    if (titleField.getText().length() == 0) {
      JOptionPane.showMessageDialog(frame, "A title is required", "Error", JOptionPane.ERROR_MESSAGE);
      titleField.requestFocus();
      return false;
    }
    if (controller.exists(titleField.getText())) {
      JOptionPane.showMessageDialog(frame, "The title '" + titleField.getText() + "' is already being used", "Error", JOptionPane.ERROR_MESSAGE);
      titleField.selectAll();
      titleField.requestFocus();
      return false;
    }
    return true;
  }
  
  private String getText() {
    return text;
  }
  
  private void setText(String text) {
    this.text = text;
  }
}
