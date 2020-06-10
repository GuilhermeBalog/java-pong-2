import java.awt.*;
import java.util.Deque;
import java.util.LinkedList;

public class FxBall extends Ball {
    private static int FX_LENGTH;
    private static int FADE_FACTOR;
    private static double DECREMENT_FACTOR;

    private Deque<double[]> rastro;

    public FxBall(double cx, double cy, double width, double height, Color color, double speed, double vx, double vy){

        super(cx, cy, width, height, color, speed, vx, vy);

        FX_LENGTH = 100;
        FADE_FACTOR = 5;
        DECREMENT_FACTOR = 0.9;

        rastro = new LinkedList<>();
    }
    
    public void draw(){

        double fator = 1.0;
        Color fxColor = getColor().darker();
        int count = 0;

        // Rastro
        for(double[] coord : rastro){
            GameLib.setColor(fxColor);
            GameLib.fillRect(coord[0], coord[1], getWidth() * fator, getHeight() * fator);

            if(count % FADE_FACTOR == 0){
                fxColor = fxColor.darker();
            }
            fator *= DECREMENT_FACTOR;
            count++;
        }
        
        // Bola principal
        GameLib.setColor(getColor());
        GameLib.fillRect(getCx(), getCy(), getWidth(), getHeight());
        
    }

    public void update(long delta){

        double[] coord = { getCx(), getCy() };
        rastro.addFirst(coord);

        if(rastro.size() > FX_LENGTH){
            rastro.removeLast();
        }

        super.update(delta);
    }


}