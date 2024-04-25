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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class NoteDialog extends JDialog {
  private static final long serialVersionUID = 6299811276766639359L;
  private static final int WIDTH = 1000;
  private static final int HEIGHT = 800;
  private static final Color BACKGROUND = new Color(0xe0, 0xe0, 0xe0);
  private static final int MAX_TITLE_LENGTH = 30;

  public NoteDialog(Frame frame, Controller controller, String title, String text, boolean rename) {
    super(frame, "Note", true);
    setLayout(new BorderLayout());
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    JTextField titleField = new JTextField();
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
    mainPanel.add(new LabeledComponent("Title", titleField), BorderLayout.NORTH);
    JTextArea textArea = new JTextArea();
    textArea.setLineWrap(true);
    if (title != null) {
      textArea.setText(text);
      if (rename) {
        textArea.setEditable(false);
        textArea.setBackground(BACKGROUND);
      }
    }
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
          if (controller.isModified(title, text)) {
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
        if (!textArea.getText().equals(text)) {
          setTitle("* Note");
        }
      }
    });

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(ok);
    buttonPanel.add(cancel);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    
    setSize(WIDTH, HEIGHT);
    setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - WIDTH / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - HEIGHT / 2);
    setVisible(true);
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
}
