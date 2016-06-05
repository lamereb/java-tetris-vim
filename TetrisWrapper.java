import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.*;

public class TetrisWrapper extends JPanel {

  // this class sets a Layout, with a TetrisPanel taking up LEFT side
  // another panel, RIGHT TOP, to display the next TetrisPiece (nested class?)
  // and another panel, RIGHT BOTTOM, to display variables such as--
  // score, # of lines completed, and also a checkmark field toggling a boolean
  // variable which, if TRUE, shows or hides the next TetrisPiece.
  // This whole panel gets added to the TetrisFrame JFrame

  private static final int BLOCK_SIZE = 25;
  private static final int BORDER_SIZE = 10;
  private static final int PWIDTH = (10*BLOCK_SIZE + 6*BLOCK_SIZE + 3*BORDER_SIZE);
  private static final int PHEIGHT = (18*BLOCK_SIZE + 2*BORDER_SIZE);

  JLabel linesDisplay, scoreDisplay;	
  JTextArea infoBox;
  String linesMsg, scoreMsg, infoMsg;

  TetrisPanel contentLt;
  NextPiecePanel contentRtTop;
  ScorePanel contentRtBttm;

  // default constructor 
  public TetrisWrapper(){
    setLayout(null);
    setBackground(Color.GRAY);
    // setLayout(new GridLayout(0,2,5,5));
    setBorder(BorderFactory.createEtchedBorder());
    setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

    contentLt = new TetrisPanel();
    contentRtTop = new NextPiecePanel();
    contentRtBttm = new ScorePanel();

    add(contentLt);
    add(contentRtTop);
    add(contentRtBttm);

    contentLt.setBounds(BORDER_SIZE, BORDER_SIZE, BLOCK_SIZE*10, BLOCK_SIZE*18);
    contentRtTop.setBounds((BORDER_SIZE*2 + BLOCK_SIZE*10), BORDER_SIZE, BLOCK_SIZE*6, BLOCK_SIZE*6);
    contentRtBttm.setBounds((BORDER_SIZE*2 + BLOCK_SIZE*10), (BORDER_SIZE*2 + BLOCK_SIZE*6), BLOCK_SIZE*6, (BLOCK_SIZE*11+15));

  }	

  // nested class for panel displaying next piece
  public class NextPiecePanel extends JPanel {

    // default constructor	
    public NextPiecePanel(){
      setPreferredSize(new Dimension(BLOCK_SIZE*6, BLOCK_SIZE*6));
      setBackground(Color.DARK_GRAY);						
    }
  }

  // nested class for score panel
  public class ScorePanel extends JPanel implements ActionListener {

    public int linesTotal;		
    public Timer getLines;	
    JSlider hold;

    public ScorePanel(){
      setPreferredSize(new Dimension(BLOCK_SIZE*6, BLOCK_SIZE*12));
      setBackground(Color.DARK_GRAY);
      setLayout(new GridLayout(4,2));

      getLines = new Timer(100, this);
      getLines.start();

      linesMsg = "Lines:  0";
      scoreMsg = "Score:  0";
      infoMsg = "Z: rotate left\n";
      infoMsg += "X: rotate right\n";
      infoMsg += "<-: move left\n";
      infoMsg += "->: move right\n";

      linesDisplay = new JLabel(linesMsg);
      linesDisplay.setForeground(Color.BLACK);
      linesDisplay.setFont(new Font("Courier New", Font.PLAIN, 14));
      add(linesDisplay);

      scoreDisplay = new JLabel(scoreMsg);
      scoreDisplay.setForeground(Color.BLACK);
      scoreDisplay.setFont(new Font("Courier New", Font.PLAIN, 14));
      add(scoreDisplay);

      infoBox = new JTextArea(infoMsg);
      infoBox.setBackground(Color.DARK_GRAY);
      infoBox.setForeground(Color.BLACK);
      infoBox.setFont(new Font("Courier New", Font.PLAIN, 10));
      add(infoBox);
    }	


    public void paintComponent(Graphics g){
      super.paintComponent(g);

      linesMsg = "Lines:  " + contentLt.getLineCount();
      linesDisplay.setText(linesMsg);

      scoreMsg = "Score:  " + contentLt.getScore();
      scoreDisplay.setText(scoreMsg);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      repaint();
    }
  }
}
