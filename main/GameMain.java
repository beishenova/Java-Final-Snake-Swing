package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel{

	private static final long serialVersionUID = 1L;
	static final String TITLE = "Snake Jaa Swing";
	static final int ROWS = 40;
	static final int COLUMNS = 40;
	static final int CELL_SIZE = 15;
	static final int CANVAS_WIDTH = COLUMNS * CELL_SIZE;
	static final int CANVAS_HEIGHT = ROWS * CELL_SIZE;
	static final int UPDATE_PER_SEC = 3;
	static final long UPDATE_PERIOD_NSEC = 1000000000L / UPDATE_PER_SEC;


	enum GameState {
		INITIALIZED, PLAYING, PAUSED, GAMEOVER, DESTROYED
	}

	static JMenuBar menuBar;

	static GameState state;

	private Food food;
	private Snake snake;

	private GameCanvas pit;
	private ControlPanel control;
	private JLabel lblScore;
	int score = 0;

	public GameMain() {
		gameInit();

		setLayout(new BorderLayout());
		pit = new GameCanvas();
		pit.setPreferredSize(new Dimension(CANVAS_WIDTH,CANVAS_HEIGHT));
		add(pit, BorderLayout.CENTER);

		control = new ControlPanel();
		add(control, BorderLayout.SOUTH);

		setupMenuBar();

		gameStart();
	}


	public void gameInit() {
		snake = new Snake();
		food = new Food();
		state = GameState.INITIALIZED;
	}

	public void gameShutdown() {

	}

	public void gameStart() {
		Thread gameThread = new Thread() {
			//Override run() to provide the running behavior of this thread
			public void run() {
				gameLoop();
			}
		};
		gameThread.start();
	}

	private void gameLoop() {
		if(state == GameState.INITIALIZED || state == GameState.GAMEOVER) {
			snake.regenerate();

			int x, y;
			do {
				food.regenerate();
				x = food.getX();
				y = food.getY();
			}while(snake.contains(x,y));

			state = GameState.PLAYING;

		}
		long beginTime, timeTaken, timeLeft; //in msec
		while(state != GameState.GAMEOVER) {
			beginTime = System.nanoTime();
			if(state == GameState.PLAYING) {
				gameUpdate();
			}
			repaint();
			timeTaken = System.nanoTime() - beginTime;
			timeLeft = (UPDATE_PERIOD_NSEC - timeTaken)/ 1000000;
			if(timeLeft < 10) timeLeft = 10; //set a minium
			try {
				Thread.sleep(timeLeft);
			}catch(InterruptedException ex) {}

		}
	}

	public void gameUpdate() {
		snake.update();
		processCollision();
	}

	public void processCollision() {
		int headX = snake.getHeadX();
		int headY = snake.getHeadY();

		if(headX == food.getX() && headY == food.getY()) {
			score = score + 1;
			lblScore.setText("Score: "+score);

			int x, y;
			do {
				food.regenerate();
				x = food.getX();
				y = food.getY();
			}while(snake.contains(x, y));
		}else {
			snake.shrink();
		}

		if(!pit.contains(headX, headY)) {
			state = GameState.GAMEOVER;
			score = 0;
			lblScore.setText("Score: "+score);
			return;
		}

		if(snake.eatItself()) {
			state = GameState.GAMEOVER;
			score = 0;
			lblScore.setText("Score: "+score);
			return;
		}
	}

	private void gameDraw(Graphics g) {
		snake.draw(g);
		food.draw(g);

		g.setFont(new Font("Dialog", Font.PLAIN, 14));
		g.setColor(Color.BLACK);

		if(state == GameState.GAMEOVER) {
			g.setFont(new Font("Verdana", Font.BOLD, 30));
			g.setColor(Color.RED);
			g.drawString("GAME OVER!", 200, CANVAS_HEIGHT / 2);

		}

	}

	public void gameKeyPressed(int keyCode) {
		switch (keyCode) {
			case KeyEvent.VK_UP:
				snake.setDirection(Snake.Direction.UP);
				break;
			case KeyEvent.VK_DOWN:
				snake.setDirection(Snake.Direction.DOWN);
				break;
			case KeyEvent.VK_LEFT:
				snake.setDirection(Snake.Direction.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				snake.setDirection(Snake.Direction.RIGHT);
				break;
		}
	}


	class ControlPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private JButton btnStartPause;
		private ImageIcon iconStart = new ImageIcon(getClass().getResource("/images/start.png"), "START");
		private ImageIcon iconPause = new ImageIcon(getClass().getResource("/images/pause.png"), "PAUSE");


		public ControlPanel () {
			this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

			btnStartPause = new JButton(iconPause);
			btnStartPause.setToolTipText("Pause");
			btnStartPause.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btnStartPause.setEnabled(true);
			add(btnStartPause);



			lblScore = new JLabel("Score: 0");
			add(lblScore);

			btnStartPause.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switch(state) {
						case INITIALIZED:
						case GAMEOVER:
							btnStartPause.setIcon(iconPause);
							btnStartPause.setToolTipText("Pause");
							gameStart();
							score = 0;
							lblScore.setText("Score: "+score);
							break;
						case PLAYING:
							state = GameState.PAUSED;
							btnStartPause.setIcon(iconStart);
							btnStartPause.setToolTipText("Start");
							break;
						case PAUSED:
							state = GameState.PLAYING;
							btnStartPause.setIcon(iconPause);
							btnStartPause.setToolTipText("Pause");
							break;
					}
					pit.requestFocus();

				}
			});


		}

		public void reset() {
			btnStartPause.setIcon(iconStart);
			btnStartPause.setEnabled(true);
		}
	}

	class GameCanvas extends JPanel implements KeyListener {

		private static final long serialVersionUID = 1L;
		public GameCanvas() {
			setFocusable(true);
			requestFocus();
			addKeyListener(this);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			setBackground(Color.decode("0x3F919E"));
			gameDraw(g);
		}
		@Override
		public void keyPressed(KeyEvent e) {
			gameKeyPressed(e.getKeyCode());

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}

		@Override
		public void keyTyped(KeyEvent e) {

		}

		public boolean contains (int x, int y) {
			if((x<0)|| (x>=ROWS)) return false;
			if((y<0)|| (y>=COLUMNS)) return false;
			return true;
		}

	}
	private void setupMenuBar() {
		JMenu menu;
		JMenuItem menuItem;

		menuBar = new JMenuBar();
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menu);


		menuItem = new JMenuItem("About", KeyEvent.VK_A);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(GameMain.this,
						"Snake game. Made by Aisha Beishenova COM-20",
						"About", JOptionPane.PLAIN_MESSAGE);

			}

		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame(TITLE);
				frame.setContentPane(new GameMain());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setJMenuBar(menuBar);
				frame.setVisible(true);

			}
		});
	}











}
