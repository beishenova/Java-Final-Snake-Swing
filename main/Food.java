package main;

import java.awt.*;
import java.util.*;

public class Food {
	private int x,y;
	private Color color = Color.BLUE;
	private Random rand = new Random();
	public Food() {
		x = -1;
		y = -1;
	}
	
	public void regenerate() {
		x = rand.nextInt(GameMain.COLUMNS - 4) + 2;
		y = rand.nextInt(GameMain.ROWS - 4) + 2;
		
	}
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.fill3DRect(x * GameMain.CELL_SIZE,
				y* GameMain.CELL_SIZE, 
				 GameMain.CELL_SIZE,
				 GameMain.CELL_SIZE, 
				true);
	}
	
	
	
	
	
	
	
	
	
}
