
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class test {

   public static void main(String[] args) {
      JFrame test = new JFrame();
      test.setLayout(new BorderLayout());
      JPanel testPanel = new JPanel();
      JPanel testPanel2 = new JPanel();
      try{
         BufferedImage bufferedImage = ImageIO.read(new File("art/pieces/BB.png"));
         testPanel2.add(new JLabel(new ImageIcon(bufferedImage)));
      }catch (IOException e){
      }
      try{
         BufferedImage bufferedImage = ImageIO.read(new File("art/pieces/WP.png"));
         testPanel.add(new JLabel(new ImageIcon(bufferedImage)));
      }catch (IOException e){
      }
      testPanel2.validate();
      
      testPanel.validate();
      test.add(testPanel, BorderLayout.CENTER);
      test.add(testPanel2, BorderLayout.CENTER);
      test.validate();
      test.setVisible(true);
   }
}