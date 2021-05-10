package main;
import java.awt.Graphics;
public class SnakeSegment {
	private int headX, headY;
	private int length; 
	private Snake.Direction direction;
	
	public SnakeSegment(int headX, int headY, int length, Snake.Direction direction) {
		this.headX = headX;
		this.headY = headY;
		this.direction = direction;
		this.length = length;
	}
	
	public void grow() {
		++length;
		switch (direction) {
		case LEFT:
			--headX;
			break;
		case RIGHT:
			++headX;
			break;
		case UP:
			--headY;
			break;
		case DOWN:
			++headY;
			break;
		}
	}
	
	public void shrink() {
		length--;
	}
	
	public int getLength() {
		return length;
	}
	

	public int getHeadX() {
		return headX;
	}
	
	public int getHeadY() {
		return headY;
	}
	
	private int getTailX() {
		if(direction == Snake.Direction.LEFT) {
			return headX + length -1;
		}else if(direction == Snake.Direction.RIGHT) {
			return headX - length +1;
		}
		else {
			return headX;
		}
		
	}
	
	private int getTailY() {
		if(direction == Snake.Direction.DOWN) {
			return headY - length + 1;
		}else if(direction == Snake.Direction.UP) {
			return headY + length -1;
		}
		else {
			return headY;
		}
	}
	
	public boolean contains(int x, int y) {
		switch(direction) {
		case LEFT:
			return ((y == this.headY) && ((x >= this.headX) && (x <= getTailX() )));
		case RIGHT:
			return ((y == this.headY) && ((x <= this.headX) && (x >= getTailX() )));
		case UP:
			return ((x == this.headX) && ((y >= this.headY) && (y <= getTailY() )));
		case DOWN:
			return ((x == this.headX) && ((y <= this.headY) && (y >= getTailY() )));
		
		}
		return false;
	}

	public void draw(Graphics g) {
		int x = headX;
		int y = headY;
		switch(direction) {
		case LEFT:
			for(int i =0; i< length; ++i) {
				g.fill3DRect(
						x * GameMain.CELL_SIZE, 
						y * GameMain.CELL_SIZE, 
						GameMain.CELL_SIZE - 1, 
						GameMain.CELL_SIZE - 1, true);
				++x;
			}
			break;
		case RIGHT:
			for(int i =0; i< length; ++i) {
				g.fill3DRect(
						x * GameMain.CELL_SIZE, 
						y * GameMain.CELL_SIZE, 
						GameMain.CELL_SIZE - 1, 
						GameMain.CELL_SIZE - 1, true);
				--x;
			}
			break;
		case UP:
			for(int i =0; i< length; ++i) {
				g.fill3DRect(
						x * GameMain.CELL_SIZE, 
						y * GameMain.CELL_SIZE, 
						GameMain.CELL_SIZE - 1, 
						GameMain.CELL_SIZE - 1, true);
				++y;
			}
			break;
		case DOWN:
			for(int i =0; i< length; ++i) {
				g.fill3DRect(
						x * GameMain.CELL_SIZE, 
						y * GameMain.CELL_SIZE, 
						GameMain.CELL_SIZE - 1, 
						GameMain.CELL_SIZE - 1, true);
				--y;
			}
			break;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
