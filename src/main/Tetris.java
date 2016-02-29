package main;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JComponent;

import java.util.Timer;
import java.util.TimerTask;

public class Tetris extends JComponent{
	//TODO clean up variable and method permissions
	
	Color[][] board;
	Color[][] boardBorders;
	
	//Colors
	Color backgroundColor= new Color(41, 128, 185);
	Color emptyColor=new Color(127, 140, 141);
	//Debug Purposes else set borderColor=emptyColor
	Color borderColor= emptyColor;//Color.WHITE;
	
	int innerBorderWidth=9;
	
	boolean groundHit=false;
	
	//Piece Colors
	static Color I_PIECE_COLOR= new Color(52, 152, 219);
	static Color J_PIECE_COLOR= new Color(52, 73, 94);
	static Color L_PIECE_COLOR= new Color(230, 126, 34);
	static Color O_PIECE_COLOR= new Color(241, 196, 15);
	static Color S_PIECE_COLOR= new Color(46, 204, 113);
	static Color T_PIECE_COLOR= new Color(155, 89, 182);
	static Color Z_PIECE_COLOR= new Color(231, 76, 60);
	
	//Piece Definitions
	private static final boolean[][] I_PIECE = {
		{ true,  true,  true,  true}
	};	  
	private static final boolean[][] J_PIECE = {
		{ true, false, false },
		{ true, true,  true}
	};	  
	private static final boolean[][] L_PIECE = {
		{ false, false, true},
		{ true,  true,  true}
	};	  
	private static final boolean[][] O_PIECE = {
		{ true, true},
		{ true, true}
	};	  
	private static final boolean[][] S_PIECE = {
		{ false, true, true},
		{ true,  true, false }
	};	  
	private static final boolean[][] T_PIECE = {
		{ false, true, false },
		{ true,  true, true}
	};
	private static final boolean[][] Z_PIECE = {
		{ true,  true, false },
		{ false, true, true}
	};
	
	//Master Piece Array (3D)
	private static boolean[][][] pieces={I_PIECE,J_PIECE,L_PIECE,O_PIECE,S_PIECE,T_PIECE,Z_PIECE};
	//Master Color Array
	private static Color[] pieceColors= {I_PIECE_COLOR,J_PIECE_COLOR,L_PIECE_COLOR,O_PIECE_COLOR,S_PIECE_COLOR,T_PIECE_COLOR,Z_PIECE_COLOR};
	
	int rows=0;
	int cols=0;
	
	//falling piece properties
	boolean[][] fallingPiece;
	Color fallingPieceColor;
	Color fallingPieceBorderColor;
	int fallingPieceRow=0;
	int fallingPieceCol=0;
	int fallingPieceRows;
	int fallingPieceCols;
	
	static int cellSize=40;

	public static void main(String[] args) {
		JFrame frame= new JFrame("Tetris");
		
		frame.getContentPane().setPreferredSize(new Dimension(cellSize*15,cellSize*15));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new Tetris());
		frame.pack();
		frame.getContentPane().setLocation(0,0);
		
		
		java.awt.EventQueue.invokeLater(new Runnable() {
	          public void run() {
	               frame.setVisible(true);
	          }
	    });	
	}
	
	//TODO Custom row/column constructor
	public Tetris(){
		int rows=15;
		int cols=10;
		
		this.rows=rows;
		this.cols=cols;
		
		board= new Color[rows][cols];
		boardBorders= new Color[rows][cols];
		
		for(int i=0; i<rows; i++)
			for(int j=0; j<cols; j++){
				board[i][j]=emptyColor;
				boardBorders[i][j]=borderColor;
			}
		
		newFallingPiece();
		
		//TODO change speed based on level
		Timer timer= new Timer();
		timer.schedule(new TimerOutput(),0,250);
		
		
		
		
	}
	
	public class TimerOutput extends TimerTask{
		public void run(){
			movePiece(1,0);
		}
	}
	
	/**
	 * void method for moving piece (down by timer and left and right by ActionListener)
	 * @param x spaces to move x on grid (negative is left, positive is right)
	 * @param y spaces to move y on grid (negative is up, positive is down); 
	 */
	public void movePiece(int x, int y){
		if(moveIsLegal(x,y)){
			fallingPieceRow+=x;
			fallingPieceCol-=y;
			repaint();
		}
		else if(!moveIsLegal(x,y) && groundHit){
			placeFallingPiece();
			newFallingPiece();
		}
	}
	
	public boolean moveIsLegal(int x, int y){
		if(fallingPieceRow+y+fallingPieceRows>=rows){
			groundHit=true;
			return false;
		}
		for(int i=0; i<fallingPieceCols; i++)
			if(board[fallingPieceRow+y+fallingPieceRows][fallingPieceCol+i]!=emptyColor){
				groundHit=true;
				return false;
			}

		return true;
	}
	
	public void newFallingPiece(){
		//TODO weight piece appearances
		groundHit=false;
		
		int pieceNumber= (int)(Math.random()*pieces.length);
		fallingPiece= pieces[pieceNumber];
		fallingPieceColor= pieceColors[pieceNumber];
		fallingPieceBorderColor= fallingPieceColor.darker();
		
		fallingPieceRows= fallingPiece.length;
		fallingPieceCols= fallingPiece[0].length;
		
		fallingPieceRow=0;
		fallingPieceCol=(cols/2)-(fallingPieceCols/2)-1;
	}
	
	public void placeFallingPiece(){
		for(int i=0; i<fallingPieceRows; i++)
			for(int j=0; j<fallingPieceCols; j++)
				if(fallingPiece[i][j]){
					board[fallingPieceRow+i][fallingPieceCol+j]=fallingPieceColor;
					boardBorders[fallingPieceRow+i][fallingPieceCol+j]=fallingPieceBorderColor;
				}
	}
	
	public void paint(Graphics g){
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		paintBoard(g);
		paintPiece(g);
	}
	
	public void paintBoard(Graphics g){
		for(int i=0; i<rows; i++)
			for(int j=0; j<cols; j++)
				paintCell(g, i, j, board[i][j], boardBorders[i][j]);
	}
	
	public void paintPiece(Graphics g){
		for(int i=0; i<fallingPieceRows; i++)
			for(int j=0; j<fallingPieceCols; j++){
				if(fallingPiece[i][j])
					paintCell(g, i+fallingPieceRow, j+fallingPieceCol, fallingPieceColor, fallingPieceBorderColor);
				
			}
				
	}
	
	/**
	 * Paint method to paint each individual cell (called by paintBoard)
	 * @param g Graphics variable
	 * @param c Color the cell will be painted in 
	 * @param b Color the cell border will be if a piece is drawn
	 * @param rowPos, colPos where the loop in paintBoard is (used to find x and y)
	 */
	public void paintCell(Graphics g, int rowPos, int colPos, Color c, Color b){
		Graphics2D g2= (Graphics2D)g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		int x=colPos*cellSize;
		int y=rowPos*cellSize;
		
		g2.setColor(c);
		g2.fillRect(x, y, cellSize, cellSize);
		g2.setColor(b);
		g2.drawRect(x, y, cellSize, cellSize);
		g2.fillRoundRect(x+innerBorderWidth, y+innerBorderWidth, cellSize-(2*innerBorderWidth), cellSize-(2*innerBorderWidth), 1,1);
		
	}

}
