
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class test {
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JLabel statusLabel;
   private JPanel controlPanel;
   private int cnt = 0;

   public test() {
      prepareGUI();
   }

   public static void main(String[] args) {
      test demo = new test();
      demo.showMouseListenerDemo();
   }

   private void prepareGUI() {
      mainFrame = new JFrame("Vi du Java Swing");
      mainFrame.setSize(400, 300);
      mainFrame.setLayout(new GridLayout(3, 1));
      headerLabel = new JLabel("", JLabel.CENTER);
      statusLabel = new JLabel("", JLabel.CENTER);
      statusLabel.setSize(350, 100);
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent) {
            System.exit(0);
         }
      });
      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());
      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);
   }

   private void showMouseListenerDemo() {
      headerLabel.setText("Listener in action: MouseListener");
      JPanel panel = new JPanel();
      panel.setBackground(Color.magenta);
      panel.setLayout(new FlowLayout());
      panel.addMouseListener(new CustomMouseListener());
      JLabel msglabel = new JLabel("Click vao day de lay toa do.",
            JLabel.CENTER);
      panel.add(msglabel);
      msglabel.addMouseListener(new CustomMouseListener());
      panel.add(msglabel);
      controlPanel.add(panel);
      mainFrame.setVisible(true);
   }

   class CustomMouseListener implements MouseListener {
      public void mouseClicked(MouseEvent e) {
         statusLabel.setText("Mouse Clicked: "
               + "(" + e.getX() + ", " + e.getY() + ")");
      }

      public void mousePressed(MouseEvent e) {
      }

      public void mouseReleased(MouseEvent e) {
      }

      public void mouseEntered(MouseEvent e) {
         cnt = cnt + 1;
         System.out.println("abc" + cnt);
      }

      public void mouseExited(MouseEvent e) {
      }
   }
}