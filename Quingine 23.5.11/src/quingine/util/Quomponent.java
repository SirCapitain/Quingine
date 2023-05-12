package quingine.util;

import quingine.sim.Quisition;

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

    private Quisition position;

    /**
     * Set the positon of the quomponent using a quisition.
     * @param quisition position in 3D space.
     */
    public void setPos(Quisition quisition){
        if (position != null)
            position.setPos(quisition);
        else
            position = quisition;
    }

    /**
     * Set the position of the quomponent using x y and z
     * @param x position in 3D space.
     * @param y position in 3D space.
     * @param z position in 3D space.
     */
    public void setPos(double x, double y, double z){
        setPos(new Quisition(x, y, z));
    }

    /**
     * Getting the current position of the quomponent.
     * @return current position in 3D space.
     */
    public Quisition getPos(){
        return position;
    }

    /**
     * Painting the quomponent.
     * @param q this current quicture that is wanting to draw this quomponent.
     */
    public void paint(Quicture q){
    }
}
