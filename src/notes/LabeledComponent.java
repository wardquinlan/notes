package notes;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabeledComponent extends JPanel {
  private static final long serialVersionUID = -229695402574729170L;

  public LabeledComponent(String label, JComponent component) {
    super();
    setLayout(new BorderLayout());
    add(new JLabel(label), BorderLayout.NORTH);
    add(component, BorderLayout.CENTER);
  }
}
