package main;

import java.awt.*;
import java.util.*;

public class Snake {
	private static final int INIT_LENGTH = 3;
	public static enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	private Color color = Color.BLACK;
	private Color colorHead = Color.GREEN;
	private Snake.Direction direction;
	
	private java.util.List<SnakeSegment> snakeSegments = new ArrayList<SnakeSegment>();
	
	private boolean dirUpdatePending;
	
	private Random random = new Random();
	
	public void regenerate() {
		snakeSegments.clear();
		int length = INIT_LENGTH; 
		int headX = random.nextInt(GameMain.COLUMNS - length * 2) + length;
		int headY = random.nextInt(GameMain.ROWS - length * 2) + length;
		direction = Snake.Direction
				.values()[random.nextInt(Snake.Direction.values().length)];
		snakeSegments.add(new SnakeSegment(headX, headY, length, direction));
		dirUpdatePending = false;
		
	}
	
	public void setDirection(Snake.Direction newDir) {
		if(!dirUpdatePending
				&& (newDir != direction)
				&&((newDir == Snake.Direction.UP && direction != Snake.Direction.DOWN)
				||(newDir == Snake.Direction.DOWN && direction != Snake.Direction.UP)
				||(newDir == Snake.Direction.LEFT && direction != Snake.Direction.RIGHT)
				||(newDir == Snake.Direction.RIGHT && direction != Snake.Direction.LEFT
				))) {
					SnakeSegment headSegment = snakeSegments.get(0);
					int x = headSegment.getHeadX();
					int y = headSegment.getHeadY();
					snakeSegments.add(0, new SnakeSegment(x, y, 0, newDir));
					direction = newDir;
					dirUpdatePending = true; 
				}
    
	}
	

	public void update() {
		SnakeSegment headSegment = snakeSegments.get(0);
		headSegment.grow();
		dirUpdatePending = false; 
	}
	
	public void shrink() {
		SnakeSegment tailSegment = snakeSegments.get(snakeSegments.size()-1);
		tailSegment.shrink();
		if(tailSegment.getLength() == 0) snakeSegments.remove(tailSegment);
	}
	
	public int getHeadX() {
		return snakeSegments.get(0).getHeadX();
	}
	public int getHeadY() {
		return snakeSegments.get(0).getHeadY();
	}
	
	public boolean contains(int x, int y) {
		for(int i =0; i< snakeSegments.size(); ++i) {
			SnakeSegment segment = snakeSegments.get(i);
			if(segment.contains(x,y))return true;
			
		}
		return false;
	}
	
	public boolean eatItself() {
		int headX = getHeadX();
		int headY = getHeadY();
		for(int i =3; i <snakeSegments.size(); ++i) {
			SnakeSegment segment = snakeSegments.get(i);
			if(segment.contains(headX, headY)) return true;
		}
		return false;
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		for(int i = 0; i< snakeSegments.size(); ++i) {
			snakeSegments.get(i).draw(g); 
		}
		
		if(snakeSegments.size() > 0) {
			g.setColor(colorHead);
			g.fill3DRect(
					getHeadX() * GameMain.CELL_SIZE, 
					getHeadY() * GameMain.CELL_SIZE, 
					GameMain.CELL_SIZE - 1, 
					GameMain.CELL_SIZE - 1,
					true
					);
		}
	}
	
	
	
	
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Snake[dir="+direction+"\n");
		for(SnakeSegment segment: snakeSegments) {
			sb.append("   ").append(segment).append("\n");
		}
		sb.append("]");
		return sb.toString();
	}
}
