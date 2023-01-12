package quinn.world.obj;

import quinn.win.Quicture;
import quinn.world.Quomponent;

import java.awt.*;

public class Quobject3D extends Quomponent {
    public Quobject3D(){}
    public Quobject3D(double x, double y, double z, Qulane[] planes){
        setPos(x, y, z);
        for (Qulane plane : planes)
            plane.setQuicture(pic);
        this.planes = planes;

    }
    private Qulane[] planes;
    public void setPlanes(Qulane[] planes){
        this.planes = planes;
    }

    public Qulane[] getPlanes() {
        return planes;
    }

    public void rotate(double theta, double vx, double vy, double vz){
        for (Qulane plane : planes)
            plane.rotate(theta, vx, vy,vz, quisition);
    }

    @Override
    public void setQuicture(Quicture quicture){
       super.setQuicture(quicture);
       for (Qulane plane : planes)
           plane.setQuicture(quicture);
    }

    @Override
    public void paint(Graphics g) {
        for (Qulane plane : planes)
            plane.paint(g);
    }
}
