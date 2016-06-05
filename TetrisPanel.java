import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

public class TetrisPanel extends JPanel implements ActionListener, KeyListener {

  private static final int BLOCK_SIZE = 25;
  private static final int PWIDTH = BLOCK_SIZE*10;
  private static final int PHEIGHT = BLOCK_SIZE*18;

  private static final Color ORANGE = new Color(255,125,0);

  private static final int STICK = 0;
  private static final int SQUARE = 1;
  private static final int T = 2;
  private static final int S = 3;
  private static final int Z = 4;
  private static final int L = 5;
  private static final int J = 6;

  private boolean [][] tetField;
  private Color [][] colField;

  private TetrisPiece playPiece;

  private Timer timepiece;
  public int lineCount, nextPiece, gameScore;
  public boolean gameOn;

  public TetrisPanel(){
    //make a canvas that's PWIDTH by PHEIGHT in size
    setBackground(Color.DARK_GRAY);
    setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
    setFocusable(true);
    addKeyListener(this);

    lineCount = 0;
    gameScore = 0;
    gameOn = true;
    nextPiece = (int)(7*Math.random()); 
    tetField = new boolean[PWIDTH][PHEIGHT];
    colField = new Color[PWIDTH][PHEIGHT];
    for(int i = 0; i<PWIDTH; i++){
      for(int k = 0; k<PHEIGHT; k++){
        tetField[i][k] = false;
        colField[i][k] = null;
      }
    }		//end tetField initialization

    // playPiece = new TetrisPiece(new Point(3,0), (int)(7*Math.random()));
    playPiece = new TetrisPiece(new Point(3,0), nextPiece);

    timepiece = new Timer(1000,this);
    timepiece.start();

  }		//end TetrisPanel constructor


  public class TetrisPiece {
    Point pt;
    int type, xMax, yMax;
    Color c;
    int rotConfig;
    Point [] boxes;
    int [] minMax; 
    //boundaries of the piece: 
    // [0] = leftmost x; [1] = rightmost x; 
    // [2] = topmost y; [3] = bottommost y;

    // TetrisPiece constructor
    public TetrisPiece(Point p, int ptype){
      pt = p;
      type = ptype;
      rotConfig = 0;
      boxes = new Point[4];
      minMax = new int[4];

      if((type == STICK)){
        c = Color.red;
        boxes[0] = new Point(pt.x,pt.y-2);
        boxes[1] = new Point(pt.x,pt.y-1);
        boxes[2] = new Point(pt.x,pt.y);
        boxes[3] = new Point(pt.x,pt.y+1);

        minMax[0] = pt.x;
        minMax[1] = pt.x;
        minMax[2] = pt.y-2;
        minMax[3] = pt.y+1;
      }
      else if(type == SQUARE){
        c = Color.magenta;
        boxes[0] = new Point(pt.x,pt.y);
        boxes[1] = new Point(pt.x+1,pt.y);
        boxes[2] = new Point(pt.x,pt.y-1);
        boxes[3] = new Point(pt.x+1,pt.y-1);

        minMax[0] = pt.x;
        minMax[1] = pt.x+1;
        minMax[2] = pt.y-1;
        minMax[3] = pt.y;
      }
      else if(type == T){
        c = ORANGE;
        boxes[0] = new Point(pt.x,pt.y);
        boxes[1] = new Point(pt.x+1,pt.y);
        boxes[2] = new Point(pt.x-1,pt.y);
        boxes[3] = new Point(pt.x,pt.y-1);

        minMax[0] = pt.x-1;
        minMax[1] = pt.x+1;
        minMax[2] = pt.y-1;
        minMax[3] = pt.y;
      }
      else if(type == S){
        c = Color.cyan;
        boxes[0] = new Point(pt.x,pt.y);
        boxes[1] = new Point(pt.x-1,pt.y);
        boxes[2] = new Point(pt.x,pt.y-1);
        boxes[3] = new Point(pt.x+1,pt.y-1);

        minMax[0] = pt.x-1;
        minMax[1] = pt.x+1;
        minMax[2] = pt.y-1;
        minMax[3] = pt.y;
      }
      else if(type == Z){
        c = Color.green;
        boxes[0] = new Point(pt.x,pt.y);
        boxes[1] = new Point(pt.x+1,pt.y);
        boxes[2] = new Point(pt.x,pt.y-1);
        boxes[3] = new Point(pt.x-1,pt.y-1);

        minMax[0] = pt.x-1;
        minMax[1] = pt.x+1;
        minMax[2] = pt.y-1;
        minMax[3] = pt.y;
      }
      else if(type == L){
        c = Color.blue;
        boxes[0] = new Point(pt.x,pt.y);
        boxes[1] = new Point(pt.x,pt.y+1);
        boxes[2] = new Point(pt.x+1,pt.y+1);
        boxes[3] = new Point(pt.x,pt.y-1);

        minMax[0] = pt.x;
        minMax[1] = pt.x+1;
        minMax[2] = pt.y-1;
        minMax[3] = pt.y+1;
      }
      else if(type == J){
        c = Color.yellow;
        boxes[0] = new Point(pt.x,pt.y);
        boxes[1] = new Point(pt.x,pt.y-1);
        boxes[2] = new Point(pt.x,pt.y+1);
        boxes[3] = new Point(pt.x-1,pt.y+1);

        minMax[0] = pt.x-1;
        minMax[1] = pt.x;
        minMax[2] = pt.y-1;
        minMax[3] = pt.y+1;
      }
    }		//end TetrisPiece constructor

    public void rotateLeft(){
      rotConfig--;
      if(rotConfig < 0)
        rotConfig = 3;

      if(type == STICK){
        if(rotConfig == 0 || rotConfig == 2){
          boxes[0].x++;
          boxes[0].y = boxes[0].y-2;
          boxes[1].y--;
          boxes[2].x--;
          boxes[3].x = boxes[3].x-2;
          boxes[3].y++;

          minMax[0]++;
          minMax[1] = minMax[1]-2;
          minMax[2] = minMax[2]-2;
          minMax[3]++;	
        }
        else if(rotConfig == 1 || rotConfig == 3){
          boxes[0].x--;
          boxes[0].y = boxes[0].y+2;
          boxes[1].y++;
          boxes[2].x++;
          boxes[3].x = boxes[3].x+2;
          boxes[3].y--;

          minMax[0]--;
          minMax[1] = minMax[1]+2;
          minMax[2] = minMax[2]+2;
          minMax[3]--;	
        }
      }
      else if(type == T){
        if(rotConfig == 0){
          boxes[1].x++;
          boxes[1].y--;
          boxes[2].x--;
          boxes[2].y++;
          boxes[3].x--;
          boxes[3].y--;

          minMax[0]--;
          minMax[3]--;
        }
        else if(rotConfig == 1){
          boxes[1].x++;
          boxes[1].y++;
          boxes[2].x--;
          boxes[2].y--;
          boxes[3].x++;
          boxes[3].y--;

          minMax[0]++;
          minMax[2]--;
        }
        else if(rotConfig == 2){
          boxes[1].x--;
          boxes[1].y++;
          boxes[2].x++;
          boxes[2].y--;
          boxes[3].x++;
          boxes[3].y++;

          minMax[1]++;
          minMax[2]++;

        }
        else if(rotConfig == 3){
          boxes[1].x--;
          boxes[1].y--;
          boxes[2].x++;
          boxes[2].y++;
          boxes[3].x--;
          boxes[3].y++;

          minMax[1]--;
          minMax[3]++;
        }
      }
      else if(type == S){
        if(rotConfig == 0 || rotConfig == 2){
          boxes[1].x--;
          boxes[1].y++;
          boxes[2].x--;
          boxes[2].y--;
          boxes[3].y = boxes[3].y-2;

          minMax[0]--;
          minMax[3]--;
        }
        else if(rotConfig == 1 || rotConfig == 3){
          boxes[1].x++;
          boxes[1].y--;
          boxes[2].x++;
          boxes[2].y++;
          boxes[3].y = boxes[3].y+2;

          minMax[0]++;
          minMax[3]++;
        }
      }
      else if(type == Z){
        if(rotConfig == 0 || rotConfig == 2){
          boxes[1].x++;
          boxes[1].y--;
          boxes[2].x--;
          boxes[2].y--;
          boxes[3].x = boxes[3].x-2;

          minMax[0]--;
          minMax[3]--;	
        }
        else if(rotConfig == 1 || rotConfig == 3){
          boxes[1].x--;
          boxes[1].y++;
          boxes[2].x++;
          boxes[2].y++;
          boxes[3].x = boxes[3].x+2;

          minMax[0]++;
          minMax[3]++;
        }
      }
      else if(type == L){
        if(rotConfig == 0){
          boxes[0].x--;
          boxes[1].y++;
          boxes[2].x++;
          boxes[3].x = boxes[3].x-2;
          boxes[3].y--;

          minMax[1]--;
          minMax[2]--;
        }
        else if(rotConfig == 1){
          boxes[0].y--;
          boxes[1].x--;
          boxes[2].y++;
          boxes[3].y = boxes[3].y-2;
          boxes[3].x++;

          minMax[1]++;
          minMax[3]--;
        }
        else if(rotConfig == 2){
          boxes[0].x++;
          boxes[1].y--;
          boxes[2].x--;
          boxes[3].x = boxes[3].x+2;
          boxes[3].y++;

          minMax[0]++;
          minMax[3]++;
        }
        else if(rotConfig == 3){
          boxes[0].y++;
          boxes[1].x++;
          boxes[2].y--;
          boxes[3].y = boxes[3].y+2;
          boxes[3].x--;

          minMax[0]--;
          minMax[2]++;
        }
      }
      else if(type == J){
        if(rotConfig == 0){
          boxes[1].x--;
          boxes[1].y--;
          boxes[2].x++;
          boxes[2].y++;
          boxes[3].y = boxes[3].y+2;

          minMax[1]--;
          minMax[3]++;
        }
        else if(rotConfig == 1){
          boxes[1].x++;
          boxes[1].y--;
          boxes[2].x--;
          boxes[2].y++;
          boxes[3].x = boxes[3].x-2;

          minMax[0]--;
          minMax[3]--;
        }
        else if(rotConfig == 2){
          boxes[1].x++;
          boxes[1].y++;
          boxes[2].x--;
          boxes[2].y--;
          boxes[3].y = boxes[3].y-2;

          minMax[0]++;
          minMax[2]--;
        }
        else if(rotConfig == 3){
          boxes[1].x--;
          boxes[1].y++;
          boxes[2].x++;
          boxes[2].y--;
          boxes[3].x = boxes[3].x+2;

          minMax[1]++;
          minMax[2]++;
        }
      }
      //System.out.println(rotConfig);
    }

    public void rotateRight(){
      rotConfig++;
      if(rotConfig > 3)
        rotConfig = 0;

      if(type == STICK){
        if(rotConfig == 0 || rotConfig == 2){
          boxes[0].x++;
          boxes[0].y = boxes[0].y-2;
          boxes[1].y--;
          boxes[2].x--;
          boxes[3].x = boxes[3].x-2;
          boxes[3].y++;

          minMax[0]++;
          minMax[1] = minMax[1]-2;
          minMax[2] = minMax[2]-2;
          minMax[3]++;	
        }
        else if(rotConfig == 1 || rotConfig == 3){
          boxes[0].x--;
          boxes[0].y = boxes[0].y+2;
          boxes[1].y++;
          boxes[2].x++;
          boxes[3].x = boxes[3].x+2;
          boxes[3].y--;

          minMax[0]--;
          minMax[1] = minMax[1]+2;
          minMax[2] = minMax[2]+2;
          minMax[3]--;	
        }
      }
      else if(type == T){
        if(rotConfig == 0){
          boxes[1].x++;
          boxes[1].y++;
          boxes[2].x--;
          boxes[2].y--;
          boxes[3].x++;
          boxes[3].y--;

          minMax[1]++;
          minMax[3]--;
        }
        else if(rotConfig == 1){
          boxes[1].x--;
          boxes[1].y++;
          boxes[2].x++;
          boxes[2].y--;
          boxes[3].x++;
          boxes[3].y++;

          minMax[0]++;
          minMax[3]++;
        }
        else if(rotConfig == 2){
          boxes[1].x--;
          boxes[1].y--;
          boxes[2].x++;
          boxes[2].y++;
          boxes[3].x--;
          boxes[3].y++;

          minMax[0]--;
          minMax[2]++;
        }
        else if(rotConfig == 3){
          boxes[1].x++;
          boxes[1].y--;
          boxes[2].x--;
          boxes[2].y++;
          boxes[3].x--;
          boxes[3].y--;

          minMax[1]--;
          minMax[2]--;
        }
      }
      else if(type == S){
        if(rotConfig == 0 || rotConfig == 2){
          boxes[1].x--;
          boxes[1].y++;
          boxes[2].x--;
          boxes[2].y--;
          boxes[3].y = boxes[3].y-2;

          minMax[0]--;
          minMax[3]--;
        }
        else if(rotConfig == 1 || rotConfig == 3){
          boxes[1].x++;
          boxes[1].y--;
          boxes[2].x++;
          boxes[2].y++;
          boxes[3].y = boxes[3].y+2;

          minMax[0]++;
          minMax[3]++;
        }
      }
      else if(type == Z){
        if(rotConfig == 0 || rotConfig == 2){
          boxes[1].x++;
          boxes[1].y--;
          boxes[2].x--;
          boxes[2].y--;
          boxes[3].x = boxes[3].x-2;

          minMax[0]--;
          minMax[3]--;

        }
        else if(rotConfig == 1 || rotConfig == 3){
          boxes[1].x--;
          boxes[1].y++;
          boxes[2].x++;
          boxes[2].y++;
          boxes[3].x = boxes[3].x+2;

          minMax[0]++;
          minMax[3]++;
        }
      }
      else if(type == L){
        if(rotConfig == 0){
          boxes[0].y--;
          boxes[1].x--;
          boxes[2].y++;
          boxes[3].y = boxes[3].y-2;
          boxes[3].x++;

          minMax[0]++;
          minMax[2]--;
        }
        else if(rotConfig == 1){
          boxes[0].x++;
          boxes[1].y--;
          boxes[2].x--;
          boxes[3].x = boxes[3].x+2;
          boxes[3].y++;

          minMax[1]++;
          minMax[2]++;
        }
        else if(rotConfig == 2){
          boxes[0].y++;
          boxes[1].x++;
          boxes[2].y--;
          boxes[3].y = boxes[3].y+2;
          boxes[3].x--;

          minMax[1]--;
          minMax[3]++;
        }
        else if(rotConfig == 3){
          boxes[0].x--;
          boxes[1].y++;
          boxes[2].x++;
          boxes[3].x = boxes[3].x-2;
          boxes[3].y--;

          minMax[0]--;
          minMax[3]--;
        }
      }
      else if(type == J){
        if(rotConfig == 0){
          boxes[1].x++;
          boxes[1].y--;
          boxes[2].x--;
          boxes[2].y++;
          boxes[3].x = boxes[3].x-2;

          minMax[1]--;
          minMax[2]--;
        }
        else if(rotConfig == 1){
          boxes[1].x++;
          boxes[1].y++;
          boxes[2].x--;
          boxes[2].y--;
          boxes[3].y = boxes[3].y-2;

          minMax[1]++;
          minMax[3]--;
        }
        else if(rotConfig == 2){
          boxes[1].x--;
          boxes[1].y++;
          boxes[2].x++;
          boxes[2].y--;
          boxes[3].x = boxes[3].x+2;

          minMax[0]++;
          minMax[3]++;
        }
        else if(rotConfig == 3){
          boxes[1].x--;
          boxes[1].y--;
          boxes[2].x++;
          boxes[2].y++;
          boxes[3].y = boxes[3].y+2;

          minMax[0]--;
          minMax[2]++;
        }
      }
      //System.out.println(rotConfig);	
    }

    public boolean scanforLineFill(int line){
      if(line < 0){
        return false;
      }
      else{
        for(int i = 0; i < 10; i++){
          if(!tetField[i][line]){
            return false;
          }
        }
      }
      return true;
    }

    public void clearLine(int line){
      //clearLine iterates through every box in the line param,
      //set its tetField to false and set its colField to null

      for(int i = 0; i < 10; i++){
        tetField[i][line] = false;
        colField[i][line] = null;
      }

      //then scans every line preceding it drop it 1 row (add 1 to y)
      for(int k = line; k >= 1; k--){
        for(int i = 0; i < 10; i++){
          tetField[i][k] = tetField[i][k-1];
          colField[i][k] = colField[i][k-1];
        }
      }
    }

    public void renderPiece(){
      //NOTE: getting Index out of bounds exceptions here cuz of boxes being less than 0
      //when screen overflows

      tetField[boxes[0].x][boxes[0].y] = true;
      tetField[boxes[1].x][boxes[1].y] = true;
      tetField[boxes[2].x][boxes[2].y] = true;
      tetField[boxes[3].x][boxes[3].y] = true;

      colField[boxes[0].x][boxes[0].y] = c;
      colField[boxes[1].x][boxes[1].y] = c;
      colField[boxes[2].x][boxes[2].y] = c;
      colField[boxes[3].x][boxes[3].y] = c;

      //at this point, before creating a new TetrisPiece,
      //I need to be scanning the y-rows that the boxes are on to see 
      //if they are all set to true. if so,
      //they should be cleared and set to false;
      for(int k = 0; k < 4; k++){
        if(scanforLineFill(boxes[k].y)){
          clearLine(boxes[k].y);
          lineCount++;
          gameScore += 100;
          if(lineCount>10){
            timepiece.setDelay(500);
          }
        }
      }

      playPiece = null;
      playPiece = new TetrisPiece(new Point(3,0), nextPiece);
      nextPiece = (int)(7*Math.random());
    }   // end renderPiece 

    //at this point, need to somehow let ScorePanel & NextPiecePanel know to update

    public boolean testforContact(){
      //so here, iterate through every box. if it is touching a tetField that is true, 
      //then it should return true
      //if all boxes are checked and no tetField is true, 
      //then it should return false

      for(int i = 0; i < 4; i++){
        if((boxes[i].x  < 0) || (boxes[i].y < 0)){
          return false;
        }
        else if(tetField[boxes[i].x][boxes[i].y])
          return true;
      }
      return false;
    }		//end testforContact

    public void dropIt(){
      minMax[3]++;
      if(minMax[3] > 17){
        minMax[3] = 17;
        renderPiece();
        return;
      }
      else{
        minMax[2]++;

        boxes[0].y++;
        boxes[1].y++;
        boxes[2].y++;
        boxes[3].y++;	
      }
      // move the piece, and if a testforContact returns true, move it 
      // back to where it was & then render it
      if(testforContact()){
        boxes[0].y--;
        boxes[1].y--;
        boxes[2].y--;
        boxes[3].y--;

        renderPiece();
      }
    }		//end dropIt

    public void moveLeft(){
      minMax[0]--;
      if(minMax[0] < 0){
        minMax[0] = 0;
      }
      else{
        minMax[1]--;
        boxes[0].x--;
        boxes[1].x--;
        boxes[2].x--;
        boxes[3].x--;			
      }
      if(testforContact()){
        minMax[0]++;
        minMax[1]++;
        boxes[0].x++;
        boxes[1].x++;
        boxes[2].x++;
        boxes[3].x++;
      }
    }		//end moveLeft

    public void moveRight(){
      minMax[1]++;
      if(minMax[1] > 9){
        minMax[1] = 9;		
      }
      else{
        minMax[0]++;

        boxes[0].x++;
        boxes[1].x++;
        boxes[2].x++;
        boxes[3].x++;
      }
      if(testforContact()){
        minMax[0]--;
        minMax[1]--;

        boxes[0].x--;
        boxes[1].x--;
        boxes[2].x--;
        boxes[3].x--;
      }
    }

    public void moveDown(){
      minMax[3]++;
      if(minMax[3] > 17){
        minMax[3] = 17;
        renderPiece();
        return;
      }
      else{
        minMax[2]++;

        boxes[0].y++;
        boxes[1].y++;
        boxes[2].y++;
        boxes[3].y++;
      }

      if(testforContact()){
        boxes[0].y--;
        boxes[1].y--;
        boxes[2].y--;
        boxes[3].y--;

        renderPiece();
      }

    }

    public int getType(){
      return type;
    }		//end getType

    public Point[] getBoxes(){
      //returns an array of 4-points
      //equal to the coordinates for all 4 boxes in the piece
      return boxes;
    }		//end getBoxes()

    public Color getColor(){
      if(type == STICK)
        c = Color.red;
      else if(type == SQUARE)
        c = Color.magenta;
      else if(type == T)
        c = ORANGE;
      else if(type == S)
        c = Color.cyan;
      else if(type == Z)
        c = Color.green;
      else if(type == L)
        c = Color.blue;
      else if(type == J)
        c = Color.yellow;
      return c;
    }		//end getColor
  }		//end class TetrisPiece


  public int getNextPiece(){
    return nextPiece;
  }

  public int getLineCount(){
    return lineCount;
  }

  public int getScore() {
    return gameScore;
  }

  public void drawBlock(int x, int y, Color c, Graphics g){
    g.setColor(Color.black);
    g.drawRect(x*BLOCK_SIZE, y*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);

    g.setColor(c);
    g.fill3DRect((x*BLOCK_SIZE)+1, (y*BLOCK_SIZE)+1, BLOCK_SIZE-1, BLOCK_SIZE-1,true);
  }

  public void drawPiece(TetrisPiece p, Graphics g){

    drawBlock(p.boxes[0].x, p.boxes[0].y,p.c,g);
    drawBlock(p.boxes[1].x, p.boxes[1].y,p.c,g);
    drawBlock(p.boxes[2].x, p.boxes[2].y,p.c,g);
    drawBlock(p.boxes[3].x, p.boxes[3].y,p.c,g);
  }		//end drawPiece

  public void paintComponent(Graphics g){
    super.paintComponent(g);

    drawPiece(playPiece,g);

    for(int i = 0; i < 10; i++){
      for(int k = 0; k < 18; k++){
        if(tetField[i][k]){
          drawBlock(i,k,colField[i][k],g);
        }
      }
    }

  }		//end paintComponent

  @Override
  public void actionPerformed(ActionEvent e) {
    playPiece.dropIt();
    repaint();
  }		//end actionPerformed

  @Override
  public void keyTyped(KeyEvent e) {			
  }		//end keyTyped
  @Override
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    //int type = playPiece.getType();
    if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_H){
      playPiece.moveLeft();
      repaint();
    }
    else if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_L) {
      playPiece.moveRight();
      repaint();
    }
    else if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_J) {
      playPiece.dropIt();
      repaint();
    }
    else if(key == KeyEvent.VK_Z || key == KeyEvent.VK_S) {
      playPiece.rotateLeft();
      repaint();
    }
    else if(key == KeyEvent.VK_X || key == KeyEvent.VK_D) {
      playPiece.rotateRight();
      repaint();
    }
  }		//end keyPressed
  @Override
  public void keyReleased(KeyEvent e) {			
  }		//end keyReleased

}
//end TetrisPanel
