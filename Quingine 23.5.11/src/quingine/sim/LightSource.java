package quingine.sim;

import quingine.util.Quomponent;

public class LightSource extends Quomponent {

    public LightSource(){
        setDirection(new double[]{0,0,0});
    }
    public LightSource(double[] vec){
        setDirection(vec);
    }
    private double[] direction;
    public void setDirection(double[] vec){
        direction = vec;
    }
    public double[] getDirection(){
        return direction;
    }
}
