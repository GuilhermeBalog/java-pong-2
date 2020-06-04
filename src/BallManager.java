import java.awt.Color;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
	Classe que gerencia uma ou mais bolas presentes em uma partida. Esta classe é a responsável por instanciar 
	e gerenciar a bola principal do jogo (aquela que existe desde o ínicio de uma partida), assim como eventuais 
	bolas extras que apareçam no decorrer da partida. Esta classe também deve gerenciar a interação da(s) bola(s)
	com os alvos, bem como a aplicação dos efeitos produzidos para cada tipo de alvo atingido.
*/

public class BallManager {
	/**
		Atributo privado que representa a velocidade inicial da bola, definida no construtor
	 */

	private double initialSpeed;

	/**
		Atributo privado que armazena o momento que começou o Boost
	 */

	private long startBoostTime; 

	/**
		Atributo privado que representa as eventuais bolas extras e seus respectivos momentos de começo de boost e de aparecimento 
	 */

	private Map<IBall, Long[]> extraBalls;

	/**
		Atributo privado que representa a bola principal do jogo.	
	*/

	private IBall theBall = null;

	/**
		Atributo privado que representa o tipo (classe) das instâncias de bola que serão criadas por esta classe.
	*/

	private Class<?> ballClass = null;

	/**
		Construtor da classe BallManager.
		
		@param className nome da classe que define o tipo das instâncias de bola que serão criadas por esta classe. 
	*/

	public BallManager(String className){

		try{
			ballClass = Class.forName(className);
		}
		catch(Exception e){

			System.out.println("Classe '" + className + "' não reconhecida... Usando 'Ball' como classe padrão.");
			ballClass = Ball.class;
		}

		extraBalls = new HashMap<>();
	}

	/**
		Recebe as componetes x e y de um vetor, e devolve as componentes x e y do vetor normalizado (isto é, com comprimento igual a 1.0).
	
		@param x componente x de um vetor que representa uma direção.
		@param y componente y de um vetor que represetna uma direção.

		@return array contendo dois valores double que representam as componentes x (índice 0) e y (índice 1) do vetor normalizado (unitário).
	*/
	private double [] normalize(double x, double y){

		double length = Math.sqrt(x * x + y * y);

		return new double [] { x / length, y / length };
	}
	
	/**
		Cria uma instancia de bola, a partir do tipo (classe) cujo nome foi passado ao construtor desta classe.
		O vetor direção definido por (vx, vy) não precisa estar normalizado. A implemntação do método se encarrega
		de fazer a normalização.

		@param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
		@param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
		@param width largura do retangulo que representa a bola.
		@param height altura do retangulo que representa a bola.
		@param color cor da bola.
		@param speed velocidade da bola (em pixels por millisegundo).
		@param vx componente x do vetor (não precisa ser unitário) que representa a direção da bola.
		@param vy componente y do vetor (não precisa ser unitário) que representa a direção da bola.
	*/

	private IBall createBallInstance(double cx, double cy, double width, double height, Color color, double speed, double vx, double vy){

		IBall ball = null;
		double [] v = normalize(vx, vy);
		initialSpeed = speed;

		try{
			Constructor<?> constructor = ballClass.getConstructors()[0];
			ball = (IBall) constructor.newInstance(cx, cy, width, height, color, speed, v[0], v[1]);
		}
		catch(Exception e){

			System.out.println("Falha na instanciação da bola do tipo '" + ballClass.getName() + "' ... Instanciando bola do tipo 'Ball'");
			ball = new Ball(cx, cy, width, height, color, speed, v[0], v[1]);
		}

		return ball;
	} 

	/**
		Cria a bola principal do jogo. Este método é chamado pela classe Pong, que contem uma instância de BallManager.

		@param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
		@param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
		@param width largura do retangulo que representa a bola.
		@param height altura do retangulo que representa a bola.
		@param color cor da bola.
		@param speed velocidade da bola (em pixels por millisegundo).
		@param vx componente x do vetor (não precisa ser unitário) que representa a direção da bola.
		@param vy componente y do vetor (não precisa ser unitário) que representa a direção da bola.
	*/

	public void initMainBall(double cx, double cy, double width, double height, Color color, double speed, double vx, double vy){

		theBall = createBallInstance(cx, cy, width, height, color, speed, vx, vy);
	}

	/**
		Método que desenha todas as bolas gerenciadas pela instância de BallManager.
		Chamado sempre que a(s) bola(s) precisa ser (re)desenhada(s).
	*/

	public void draw(){

		theBall.draw();

		for(IBall ball: extraBalls.keySet()){
			ball.draw();
		}
	}
	
	/**
		Método que atualiza todas as bolas gerenciadas pela instância de BallManager, em decorrência da passagem do tempo.
		
		@param delta quantidade de millisegundos que se passou entre o ciclo anterior de atualização do jogo e o atual.
	*/

	public void update(long delta){
		
		theBall.update(delta);
		
		for(IBall ball: extraBalls.keySet()){
			ball.update(delta);
		}

		long currentTime = System.currentTimeMillis();

		if(currentTime - startBoostTime >= BoostTarget.BOOST_DURATION){
			theBall.setSpeed(initialSpeed);
		}

		for(Iterator<IBall> it = extraBalls.keySet().iterator(); it.hasNext(); ){
			IBall currentBall = it.next();

			Long currentBoostDuration = currentTime - extraBalls.get(currentBall)[0];
			Long currentDuplicationDuration = currentTime - extraBalls.get(currentBall)[1];

			if(currentBoostDuration >= BoostTarget.BOOST_DURATION){
				currentBall.setSpeed(initialSpeed);
			}
			
			if(currentDuplicationDuration >= DuplicatorTarget.EXTRA_BALL_DURATION){
				it.remove();
			}
		}

	}
	
	/**
		Método que processa as colisões entre as bolas gerenciadas pela instância de BallManager com uma parede.

		@param wall referência para uma instância de Wall para a qual será verificada a ocorrência de colisões.
		@return um valor int que indica quantas bolas colidiram com a parede (uma vez que é possível que mais de 
		uma bola tenha entrado em contato com a parede ao mesmo tempo).
	*/

	public int checkCollision(Wall wall){

		int hits = 0;

		if(theBall.checkCollision(wall)) hits++;
		
		for(IBall ball: extraBalls.keySet()){
			if(ball.checkCollision(wall)) hits++;
		}

		return hits;
	}

	/**
		Método que processa as colisões entre as bolas gerenciadas pela instância de BallManager com um player.

		@param player referência para uma instância de Player para a qual será verificada a ocorrência de colisões.
	*/
	
	public void checkCollision(Player player){

		theBall.checkCollision(player);

		for(IBall ball: extraBalls.keySet()){
			ball.checkCollision(player);
		}
	}

	/**
		Método que processa as colisões entre as bolas gerenciadas pela instância de BallManager com um alvo.

		@param target referência para uma instância de Target para a qual será verificada a ocorrência de colisões.
	*/

	public void checkCollision(Target target){
		Map<IBall, Long[]> ballsToAdd = new HashMap<>();
		
		if(theBall.checkCollision(target)){
			if(target instanceof BoostTarget){
				boostBall(theBall);

			} else if(target instanceof DuplicatorTarget){
				duplicateBall(theBall, ballsToAdd);
			}
		}

		for(IBall ball: extraBalls.keySet()){
			if(ball.checkCollision(target)){
				
				if(target instanceof BoostTarget){
					boostBall(ball);
	
				} else if(target instanceof DuplicatorTarget){
					duplicateBall(ball, ballsToAdd);
				}
			}
		}

		extraBalls.putAll(ballsToAdd);
	}

	private void boostBall(IBall ball){
		double boostedSpeed = initialSpeed * BoostTarget.BOOST_FACTOR;
		ball.setSpeed(boostedSpeed);

		if(ball.equals(theBall)){
			startBoostTime = System.currentTimeMillis();
		} else {
			Long[] times = { System.currentTimeMillis(), extraBalls.get(ball)[1] };
			extraBalls.put(ball, times);
		}
	}

	private void duplicateBall(IBall ball, Map<IBall, Long[]> ballsToAdd){
		Random rdm = new Random();
		
		IBall newBall = createBallInstance(
			ball.getCx(), 
			ball.getCy(), 
			theBall.getWidth() * 0.9, 
			theBall.getHeight() * 0.9, 
			Color.RED, 
			initialSpeed, 
			ball.getVx() + rdm.nextDouble(), 
			ball.getVy() + rdm.nextDouble());

		Long[] times = { (long) 0, System.currentTimeMillis() };

		ballsToAdd.put(newBall, times);
	}
}


