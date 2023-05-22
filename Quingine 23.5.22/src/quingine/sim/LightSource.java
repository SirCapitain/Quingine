package quingine.sim;

import quingine.util.Quomponent;

public class LightSource extends Quomponent {

    public LightSource(){
        setDirection(new Quisition());
    }
    public LightSource(Quisition vec){
        setDirection(vec);
    }
    private Quisition direction;
    public void setDirection(Quisition vec){
        direction = vec;
    }
    public Quisition getDirection(){
        return direction;
    }
}
