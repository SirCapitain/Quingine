package quinn.world;

import quinn.win.Quicture;
import quinn.world.obj.Quisition;

import java.awt.*;

/**
 * Like JComponent... but better.
 * Quomponents can be added to quindows, quictures, and quworlds.
 * This class really just holds the x, y, and z of any quomponent.
 * @author Quinn Graham
 */

public class Quomponent {
    /**
     * You need to know where something is at all times.
     * This helps with that.
     */
    protected Quisition quisition;

    /**
     *
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    public void setPos(double x, double y, double z){
        setPos(new Quisition(x,y,z));
    }

    public void setPos(Quisition quisition){
        this.quisition = quisition;
    }
    public Quisition getPos() {
        return quisition;
    }

    /**
     * You need to know what quicture holds this object.
     */
    protected Quicture pic;

    /**
     * @param quicture holding the object/quworld.
     */
    public void setQuicture(Quicture quicture){
        pic = quicture;
    }
    public Quicture getQuicture() {
        return pic;
    }

    /**
     * Knowing the quworld can be useful to identify where the quobject is.
     */
    protected Quworld world;
    public void setQuworld(Quworld quworld){
        world = quworld;
    }
    public Quworld getQuworld(){
        return world;
    }

    /**
     * Get the distances between two 3D points.
     * @param pos The point you want to find the distance to from this quomponent.
     * @return The distance between the points.
     */
    public double getDistance(Quisition pos){
        return Math.sqrt(Math.pow(quisition.x-pos.x,2)+Math.pow(quisition.y-pos.y,2)+Math.pow(quisition.z-pos.z,2));
    }

    /**
     *
     * The quobject that calls this method is considered to be the center of origin for
     * the parameters of x, y, and z.
     * This method takes into account how much smaller things look like in the distance.
     *
     * @param x Point of x that needs to have its coordinate changed due to distance
     * @param y Point of y that needs to have its coordinate changed due to distance
     * @param z Point of z which determines how far away the object is.
     * @return The new points of x, y, and z
     */
    protected Quisition calcDist(double x, double y, double z){
        double vec = Math.sqrt(Math.pow(x-quisition.x,2)+Math.pow(y-quisition.y,2)+Math.pow(-z+quisition.z,2));//Vector from point to origin of shape.
        double delta = 2*Math.toDegrees(Math.atan((vec/(z-world.getCameraPos().z))/2))/world.getFov();//Percent of how much the point needs to shrink by.
        return new Quisition(x+(x-quisition.x)*(delta-1), y+(y-quisition.y)*(delta-1), 0);
    }
    public Quisition calcDist(Quisition quisition){
        return calcDist(quisition.x, quisition.y, quisition.z);
    }

    /**
     * mmmmmmmm... paint....
     *
     * @param g graphics from Quicture
     */
    public void paint(Graphics g){
    }

}
