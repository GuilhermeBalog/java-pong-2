import java.awt.*;

public class ShadowBall extends Ball {

    public ShadowBall(double cx, double cy, double width, double height, Color color, double speed, double vx, double vy){

		super(cx, cy, width, height, color, speed, vx, vy);
    }
    
    public void draw(){

        GameLib.setColor(getColor().darker());

        for(long i = 0; i < 10; i++){
            Ball bolaRastro = new Ball(getCx()*0.9, getCy()*0.9, getWidth(), getHeight(), getColor().darker(), getSpeed(), getVx(), getVy());
            bolaRastro.update(- i);
            bolaRastro.draw();
        }

        GameLib.setColor(getColor());
        GameLib.fillRect(getCx(), getCy(), getWidth(), getHeight());
        
	}
}