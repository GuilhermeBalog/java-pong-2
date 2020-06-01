import java.awt.*;

/**
	Classe que ilustra a criação de um novo tipo de bola, que redefine o comportamento de desenho.
	Use esta classe para testar a execução do jogo, especificando um tipo de bola diferente pela linha de comando.
*/

public class DiamondBall extends Ball {

	/**
		Construtor da classe DiamondBall. Recebe o mesmo conjunto de parâmetros que o construtor da superclasse.

		@param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
		@param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
		@param width largura do retangulo que representa a bola.
		@param height altura do retangulo que representa a bola.
		@param color cor da bola.
		@param speed velocidade da bola (em pixels por millisegundo).
		@param vx componente x do vetor unitário (normalizado) que representa a direção da bola.
		@param vy componente y do vetor unitário (normalizado) que representa a direção da bola.
	*/

	public DiamondBall(double cx, double cy, double width, double height, Color color, double speed, double vx, double vy){

		super(cx, cy, width, height, color, speed, vx, vy);
	}

	/**
		Método que redefine o comportamento de desenho definido na superclasse.
	*/

	public void draw(){

		GameLib.setColor(getColor());

		for(double d = 0.0; d <= 3.0; d+=0.1){

			GameLib.drawDiamond(getCx(), getCy(), getWidth()/2 + d);
		}
	}
}

