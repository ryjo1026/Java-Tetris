package main;

import java.awt.event.*;

public class Controller implements KeyListener{
	
	Tetris game;
	
	public Controller(Tetris game){
		this.game=game;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_LEFT){
			System.out.println("Left");
			game.movePiece(1, 0);
		}
		if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			System.out.println("Right");
			game.movePiece(-1, 0);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
