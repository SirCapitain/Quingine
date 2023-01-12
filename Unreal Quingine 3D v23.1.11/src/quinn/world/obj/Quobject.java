package quinn.world.obj;

import quinn.win.Quicture;
import quinn.world.Quomponent;

import java.awt.*;


/**
 * Holds The info for a 3D object using Quinsitions.
 */

public class Quobject extends Quomponent {

    private Quisition[] points;
    public Quobject(){}
    public Quobject(double x, double y, double z){
        setPos(x,y,z);
    }
    public Quobject(double x, double y, double z, Quisition[] points){
        setPos(x, y, z);
        this.points = points;
    }
    public Quobject(Quisition quisition, Quisition[] points){
        setPos(quisition);
        this.points = points;
    }
    public void setPos(Quisition quisition){
        if (points != null)
            for (Quisition point : points)
                point.setPos(point.x + getPos().x - quisition.x, point.y + getPos().y - quisition.y,point.z + getPos().z - quisition.z);
        this.quisition = quisition;
    }
    public Quisition[] getPoints(){
        return points;
    }
    public void setPoints(Quisition[] points){
        this.points = points;
    }

    public void rotate(double theta, double vx, double vy, double vz){
        rotate(theta, vx, vy, vz, quisition);
    }
    public void rotate(double theta, double vx, double vy, double vz, Quisition quisition){
        for (Quisition point : points)
            point.rotate(theta, vx, vy,vz, quisition);
        this.quisition.rotate(theta, vx, vy, vz, quisition);
    }
    public void rotate(double theta, double vx, double vy, double vz, double x, double y, double z){
        rotate(theta, vx, vy, vz, new Quisition(x, y, z));
    }

    @Override
    public void paint(Graphics g) {
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            if (pic != null) {
                xPoints[i] = (int) Math.round(pic.getScreenPosX(getPoints()[i].x));
                yPoints[i] = (int) Math.round(pic.getScreenPosY(getPoints()[i].y));
            }else {
                xPoints[i] = (int) Math.round(getPoints()[i].x);
                yPoints[i] = (int) Math.round(getPoints()[i].y);
            }
        }
        g.drawPolygon(xPoints, yPoints, points.length);
    }
}
