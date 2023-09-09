package quingine.util.win;

import quingine.sim.Quisition;
import quingine.sim.cam.Quamera;

/**
 * Something the can be drawn and added to the quicture
 * @author Quinn Graham
 */

public class Quomponent {
    /**
     * The lame way of making a quomponent.
     * The positional values are set to 0.
     */
    public Quomponent(){
        setPos(0,0,0);
    }

    /**
     * Creating a quomponent and stating where it is.
     * @param x position in 3D space.
     * @param y position in 3D space.
     * @param z position in 3D space.
     */
    public Quomponent(double x, double y, double z){
        setPos(x, y, z);
    }

    /**
     * Creating a quomponent using a quisition.
     * @param quisition position in 3D space.
     */
    public Quomponent(Quisition quisition){
        setPos(quisition);
    }

    private Quisition position = new Quisition();

    /**
     * Set the positon of the quomponent using a quisition.
     * @param quisition position in 3D space.
     */
    public void setPos(Quisition quisition){
        position.setPos(quisition);
    }

    /**
     * Set the position of the quomponent using x y and z
     * @param x position in 3D space.
     * @param y position in 3D space.
     * @param z position in 3D space.
     */
    public void setPos(double x, double y, double z){
        position.x = x;
        position.y = y;
        position.z = z;
    }

    /**
     * Change position of quomponent by a value
     * @param x vector x
     * @param y vector y
     * @param z vector z
     */
    public void changePosBy(double x, double y, double z){
        position.x += x;
        position.y += y;
        position.z += z;
    }

    /**
     * Getting the current position of the quomponent.
     * @return current position in 3D space.
     */
    public Quisition getPos(){
        if (position != null)
            return position;
        else
            return new Quisition(0,0,0);
    }

    /**
     * Painting the quomponent.
     * @param q this current quicture that is wanting to draw this quomponent.
     */
    public void paint(Quicture q, Quamera camera){
    }
}
