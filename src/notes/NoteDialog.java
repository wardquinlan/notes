package notes;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
  private static final int WIDTH = 600;
  private static final int HEIGHT = 400;

  public NoteDialog(Frame frame, Controller controller, String title, String text) {
    super(frame, "Note", true);
    setLayout(new BorderLayout());
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    JTextField titleField = new JTextField(20);
    if (title != null) {
      titleField.setText(title);
    }
    mainPanel.add(new LabeledComponent("Title", titleField), BorderLayout.NORTH);
    JTextArea textArea = new JTextArea();
    if (text != null) {
      textArea.setText(text);
    }
    mainPanel.add(new LabeledComponent("Note", new JScrollPane(textArea)), BorderLayout.CENTER);
    mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(mainPanel, BorderLayout.CENTER);
    
    JButton ok = new JButton("OK");
    ok.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (titleField.getText().length() == 0) {
          JOptionPane.showMessageDialog(frame, "A title is required", "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }
        if (titleField.getText().length() > 30) {
          JOptionPane.showMessageDialog(frame, "Title is too long (limit of 30 characters)", "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }
        if (title == null) {
          controller.add(titleField.getText(), textArea.getText());
          frame.setSelectedRow(0);
        } else {
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

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(ok);
    buttonPanel.add(cancel);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    
    setSize(WIDTH, HEIGHT);
    setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - WIDTH / 2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - HEIGHT / 2);
    setVisible(true);
  }
}
