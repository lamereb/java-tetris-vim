import javax.swing.JFrame;

public class TetrisFrame {
  public static void main(String[] args) {
    JFrame window = new JFrame("Tetris");
    TetrisWrapper content = new TetrisWrapper();
    window.setContentPane(content);
    window.setLocation(420, 100);
    window.pack();
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.setVisible(true);	
  }
}
