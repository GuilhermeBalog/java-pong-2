import java.awt.Color;

/**
	Classe principal do jogo.
*/

public class Pong {

	/**
		Constante usada para definir a largura da janela do jogo.
	*/

	public static final int FIELD_WIDTH = 800;

	/**
		Constante usada para definir a altura da janela do jogo.
	*/


	public static final int FIELD_HEIGHT = 600;

	/**
		Constante usada para identificar o jogador 1.
	*/

	public static final String PLAYER1 = "Player 1";

	/**
		Constante usada para identificar o jogador 2.
	*/

	public static final String PLAYER2 = "Player 2";

	/**
		Constante usada para identificar a parede do topo.
	*/

	public static final String TOP = "Top";

	/**
		Constante usada para identificar a parede de baixo.
	*/

	public static final String BOTTOM = "Bottom";

	/**
		Constante usada para identificar a parede da esquerda.
	*/

	public static final String LEFT = "Left";

	/**
		Constante usada para identificar a parede da direita.
	*/

	public static final String RIGHT = "Right";

	/**
		Constante usada para identificar o placar associado ao player 1.
	*/

	private static final int PLAYER1_SCORE = 0;

	/**
		Constante usada para identificar o placar associado ao player 2.
	*/

	private static final int PLAYER2_SCORE = 1;
	
	private static void busyWait(long time){
		
		while(System.currentTimeMillis() < time) Thread.yield();
	}

	private static BallManager initBallManager(String ballClassName){

		double vx = 0.85 + Math.random() * 0.15;
		double vy = Math.sqrt(1.0 - vx * vx);

		if(Math.random() < 0.5) vx = -vx;

		BallManager ballManager = new BallManager(ballClassName);
		ballManager.initMainBall(FIELD_WIDTH/2, 100 + (FIELD_HEIGHT - 100)/2, 20, 20, Color.YELLOW, 0.65, vx, vy);

		return ballManager;
	}

	private static Target [] initTargets(){

		Target [] targets = new Target[3];

		targets[0] = new BoostTarget(FIELD_WIDTH/2, 100 + (FIELD_HEIGHT - 100) * 0.20, 50, 50);
		targets[1] = new DuplicatorTarget(FIELD_WIDTH/2, 100 + (FIELD_HEIGHT - 100) * 0.50, 50, 50);
		targets[2] = new BoostTarget(FIELD_WIDTH/2, 100 + (FIELD_HEIGHT - 100) * 0.80, 50, 50);

		return targets;
	}

	private static Wall [] initWalls(){

		Wall [] walls = new Wall[4];

		walls[0] = new Wall(10, (FIELD_HEIGHT - 100)/2 + 100, 20, FIELD_HEIGHT - 100, Color.WHITE, LEFT);
		walls[1] = new Wall(FIELD_WIDTH - 10, (FIELD_HEIGHT - 100)/2 + 100, 20, FIELD_HEIGHT - 100, Color.WHITE, RIGHT);
		walls[2] = new Wall(FIELD_WIDTH/2, 110, FIELD_WIDTH * 1.0, 20, Color.WHITE, TOP);
		walls[3] = new Wall(FIELD_WIDTH/2, FIELD_HEIGHT - 10, FIELD_WIDTH * 1.0, 20, Color.WHITE, BOTTOM);

		return walls;
	}

 	private static Player [] initPlayers(){

		Player [] players = new Player[2];

		double [] v_limit = new double[2];
		v_limit[0] = 110 + 10;
		v_limit[1] = FIELD_HEIGHT - 10 - 10;

		players[0] = new Player(FIELD_WIDTH * 0.1, 100 + (FIELD_HEIGHT - 100)/2, 20, 100, Color.GREEN, PLAYER1, v_limit, 0.5); 
		players[1] = new Player(FIELD_WIDTH * 0.9, 100 + (FIELD_HEIGHT - 100)/2, 20, 100, Color.BLUE, PLAYER2, v_limit, 0.5);

		return players;
	}

 	private static Score [] initScores(){

		Score [] scores = new Score[2];

		scores[PLAYER1_SCORE] = new Score(PLAYER1); 
		scores[PLAYER2_SCORE] = new Score(PLAYER2);

		return scores;
	}

	/**
		O método principal do jogo. Este método cria os objetos participantes do jogo
		(paredes, players, placares e bola) e gerencia a interação entre estes objetos.
		Quando eventos relevantes são identificados, métodos destes objetos são invocados
		para atualizar o estado interno de cada um deles.

		@param args argumentos passados ao jogo através da linha de comando. Ao executar o 
		jogo é possível especificar um parâmetro opcional que determina o intervalo mínimo
		de tempo que se passa entre o processamento de dois frames consecutivos.
	*/

	public static void main(String [] args){

		String ballClassName = (args.length >= 1) ? args[0] : "Ball"; 

		long delay = (args.length >= 2) ? Long.parseLong(args[1]) : 3;
		boolean safe_mode = (args.length >= 3) ? Boolean.parseBoolean(args[2]) : false;

		boolean running = true;
		boolean started = false;

		System.out.println("safe_mode = " + safe_mode);
		if(safe_mode) GameLib.initGraphics_SAFE_MODE("Pong!", 800, 600);
		else GameLib.initGraphics("Pong!", 800, 600);

		BallManager ballManager = initBallManager(ballClassName);
		Wall [] walls = initWalls();
		Target [] targets = initTargets();
		Player [] players = initPlayers();
		Score [] scores = initScores();

		long t0 = System.currentTimeMillis();

		while(running){

			long now = System.currentTimeMillis();

			if(GameLib.isKeyPressed(GameLib.KEY_SPACE)) started = true;

			if(started){

				ballManager.update(now - t0);

				if(GameLib.isKeyPressed(GameLib.KEY_A)){

					players[0].moveUp(now - t0);
				}
				if(GameLib.isKeyPressed(GameLib.KEY_Z)){

					players[0].moveDown(now - t0);
				}
				if(GameLib.isKeyPressed(GameLib.KEY_K)){

					players[1].moveUp(now - t0);
				}
				if(GameLib.isKeyPressed(GameLib.KEY_M)){

					players[1].moveDown(now - t0);
				}

				for(Player p : players) ballManager.checkCollision(p);
				for(Target t : targets) ballManager.checkCollision(t);
	
				for(Wall w : walls) {

					int nHits = ballManager.checkCollision(w);

					if(nHits > 0){

						if(w.getId().equals(LEFT)){
						
							for(int i = 0; i < nHits; i++) scores[PLAYER2_SCORE].inc();
						}

						if(w.getId().equals(RIGHT)){

							for(int i = 0; i < nHits; i++) scores[PLAYER1_SCORE].inc();
						}
					}
				}
			}
			else{
				if( (System.currentTimeMillis()/500) % 2 == 0){

					GameLib.setColor(Color.YELLOW);
					GameLib.drawText("Pressione <ESPAÇO> para começar", 440, GameLib.ALIGN_CENTER);
				}

				GameLib.setColor(Color.GREEN);
				GameLib.drawText("A/Z: move o jogador da esquerda", 260, GameLib.ALIGN_CENTER);

				GameLib.setColor(Color.BLUE);
				GameLib.drawText("K/M: move o jogador da direita", 300, GameLib.ALIGN_CENTER);
			}
			
			GameLib.setColor(Color.YELLOW);
			GameLib.drawText("Pong! 2.0", 70, GameLib.ALIGN_CENTER);

			ballManager.draw();
			for(Wall w : walls) w.draw();
			for(Target t : targets) t.draw();
			for(Player p : players) p.draw();
			for(Score s : scores) s.draw();

			GameLib.display();
			busyWait(now + delay);
			t0 = now;
		}
	}
}
