package notes;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;

public class Table extends JTable {
  private static final long serialVersionUID = 3882178188030656062L;
  private Frame frame;
  private Model model;

  public Table(Frame frame, Controller controller, Model model) {
    super(model);
    this.frame = frame;
    this.model = model;
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);

    getColumnModel().getColumn(0).setCellRenderer(centerRenderer);    
    getColumnModel().getColumn(0).setPreferredWidth(80);
    getColumnModel().getColumn(0).setMaxWidth(80);
    getColumnModel().getColumn(1).setCellRenderer(centerRenderer);    
    getColumnModel().getColumn(1).setPreferredWidth(200);
    getColumnModel().getColumn(1).setMaxWidth(200);
    
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          Note note = model.get(frame.getSelectedRow());
          new NoteDialog(frame, controller, note.getTitle(), note.getText(), false);
        }
      }
    });
    
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
          Note note = model.get(frame.getSelectedRow());
          if (JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete '" + note.getTitle() + "'?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            controller.delete(note.getTitle());
            frame.requestFocus();
          }
        }
      }
    });
  }
  
  @Override
  public boolean isCellEditable(int row, int column) {
    return false;
  }
  
  @Override
  public void valueChanged(ListSelectionEvent e) {
    super.valueChanged(e);
    int index = getSelectedRow();
    if (index == -1) {
      frame.setText("");
    } else {
      frame.setText(model.get(index).getText());
    }
  }
}
