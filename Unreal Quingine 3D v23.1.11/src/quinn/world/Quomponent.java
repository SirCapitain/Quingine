package quinn.world;

import quinn.win.Quicture;
import quinn.world.obj.Quisition;

import java.awt.*;

public class Quomponent {
    protected Quisition quisition;
    public void setPos(double x, double y, double z){
        setPos(new Quisition(x,y,z));
    }
    public void setPos(double x, double y){
        setPos(new Quisition(x, y, quisition.z));
    }
    public void setPos(Quisition quisition){
        this.quisition = quisition;
    }
    public Quisition getPos(){
        return quisition;
    }

    protected Quicture pic;
    public void setQuicture(Quicture quicture){
        pic = quicture;
    }
    public Quicture getQuicture(){
        return pic;
    }

    public void paint(Graphics g){
    }

}
