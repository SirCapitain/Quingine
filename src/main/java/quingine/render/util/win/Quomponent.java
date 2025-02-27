package quingine.render.util.win;

import quingine.render.sim.cam.Quamera;
import quingine.render.sim.pos.Quisition;

/**
 * Something the could be drawn and added to the quicture
 * Just holds a position really...
 */

public class Quomponent {

    private Quisition position = new Quisition();

    private String name;


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
     * Set the individual x component of the quobject in 3-D space
     * @param x position in 3D space
     */
    public void setX(double x){
        setPos(x, getPos().y, getPos().z);
    }

    /**
     * Set the individual y component of the quobject in 3-D space
     * @param y position in 3D space
     */
    public void setY(double y){
        setPos(getPos().x, y, getPos().z);
    }

    /**
     * Set the individual z component of the quobject in 3-D space
     * @param z position in 3D space
     */
    public void setZ(double z){
        setPos(getPos().x, getPos().y, z);
    }

    /**
     * Getting the current position of the quomponent.
     * @return current position in 3D space.
     */
    public Quisition getPos(){
        if (position != null)
            return position;
        return null;
    }

    /**
     * Set the name of the quobject so it is easier to find.
     * @param name any String
     */
    public void setName(String name){
        this.name = null;
        if (!name.equals("null"))
            this.name = name;
    }

    /**
     * Get the name the quobject
     * @return String
     */
    public String getName(){
        return name;
    }

    /**
     * Painting the quomponent.
     * @param q this current quicture that is wanting to draw this quomponent.
     */
    public void paint(Quicture q, Quamera camera){
    }
}
