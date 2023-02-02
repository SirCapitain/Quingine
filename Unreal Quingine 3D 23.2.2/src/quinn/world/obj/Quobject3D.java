package quinn.world.obj;

import quinn.win.Quicture;
import quinn.world.Quomponent;
import quinn.world.Quworld;

import java.awt.*;

/**
 * Quobject 3D uses a list of qulanes to
 * make a 3D quobject.
 */

public class Quobject3D extends Quomponent {
    public Quobject3D(){}

    /**
     * Making your own 3D object is super easy!
     *
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @param planes List of planes that make up the object.
     */
    public Quobject3D(double x, double y, double z, Qulane[] planes){
        setPos(x, y, z);
        for (Qulane plane : planes) {
            plane.setQuicture(pic);
            plane.setParentQuobject(this);
        }
        this.planes = planes;

    }

    private Qulane[] planes;
    /**
     * List of qulanes that the object is holding.
     * @param planes List of qulanes to make object.
     */
    public void setPlanes(Qulane[] planes){
        this.planes = planes;
    }

    public Qulane[] getPlanes() {
        return planes;
    }

    /**
     * When rotating, you've got to make sure all the qulanes also rotate.
     * @param theta degrees that you rotate the object by
     * @param vx vector x that the quobject will rotate on (x axis)
     * @param vy vector y that the quobject will rotate on (y axis)
     * @param vz vector z that the quobject will rotate on (z axis)
     */
    public void rotate(double theta, double vx, double vy, double vz){
        for (Qulane plane : planes)
            plane.rotate(theta, vx, vy,vz, quisition);
    }

    /**
     * Sometimes, I mean all the times. A fill object is a good object
     */
    private boolean fill;
    public boolean fill(){
        return fill;
    }
    public void fill(boolean fill){
        this.fill = fill;
        for (Qulane plane : planes)
            plane.fill(fill);
    }

    /**
     * Outlining the quobject helps it stand out.
     */
    private boolean outline;
    public boolean isOutlined(){
        return outline;
    }
    public void outlined(boolean outlined){
        outline = outlined;
        for (Qulane plane : planes)
            plane.outlined(outlined);
    }

    private Color outlineColor = Color.black;
    /**
     * Setting an outline color can help you... do something.
     * Personally, just stick with black.
     * @param color
     */
    public void setOutline(Color color){
        outlineColor = color;
        for (Qulane plane : planes)
            plane.setOutlineColor(color);
    }
    public Color getOutlineColor(){
        return outlineColor;
    }

    /**
     * Perpective is cool and should always be on.
     * Unless its something that you don't want to have perspective.
     */
    private boolean isPerspective = true;
    public void isPerspective(boolean isPerspective){
        this.isPerspective = isPerspective;
        for (Qulane plane : planes)
            plane.isPerspective(isPerspective);
    }
    public boolean isPerspective(){
        return isPerspective;
    }

    /**
     * I need to set the position of the quobject and qulanes.
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    @Override
    public void setPos(double x, double y, double z){
        setPos(new Quisition(x,y,z));
    }
    @Override
    public void setPos(Quisition quisition){
        if (planes != null)
            for (Qulane plane : planes)
                plane.setPos(plane.getPos().x+(this.quisition.x - quisition.x),plane.getPos().y+(this.quisition.y - quisition.y),plane.getPos().z+(this.quisition.z - quisition.z));
        super.setPos(quisition);
    }

    /**
     * All qulanes need a Quicture
     * @param quicture holding the object/quworld.
     */
    @Override
    public void setQuicture(Quicture quicture){
       super.setQuicture(quicture);
       for (Qulane plane : planes)
           plane.setQuicture(quicture);
    }

    @Override
    public void setQuworld(Quworld quworld){
        for (Qulane plane : planes)
            plane.setQuworld(quworld);
        super.setQuworld(quworld);
    }

    /**
     * Painting planes. What do you want me to say?
     */
    public void draw() {
        for (Qulane plane : planes)
            plane.draw();
    }
}
