package quingine.sim.env.entity;

import quingine.sim.cam.Quamera;
import quingine.sim.env.obj.Quobject;
import quingine.sim.pos.Quisition;
import quingine.util.win.Quicture;
import quingine.util.win.Quomponent;

/**
 * An entity can interact more with the environment it is a part of.
 * Can hold a quobject to give it identification / more stuff
 */

public class Entity extends Quomponent {

    private Quobject object;
    private double gravity = 5;
    private double mass = 1;

    /**
     * Create a new entity
     */
    public Entity() {
        super();
    }

    /**
     * Set the current quobject that
     * will be a part of the entity
     * @param quobject any quobject
     */
    public void setQuobject(Quobject quobject){
        object = quobject;
    }

    /**
     * Get the current quobject assigned to
     * the entity
     * @return current quobject assigned
     */
    public Quobject getQuobject(){
        return object;
    }

    /**
     * Set the position of the entity is 3D space
     * @param x position in 3D space.
     * @param y position in 3D space.
     * @param z position in 3D space.
     */
    @Override
    public void setPos(double x, double y, double z){
        super.setPos(x, y, z);
        if (object != null)
            object.setPos(x, y, z);
    }

    /**
     * Set the mass of the entity
     * @param mass any double
     */
    public void setMass(double mass){
        this.mass = mass;
    }

    /**
     * Get the mass of the entity
     * @return mass of the entity
     */
    public double getMass(){
        return mass;
    }

    /**
     * Rotate the entity using Quaternions
     * @param vec unit vector to rotate around
     * @param theta radians of rotation
     */
    public void rotate(Quisition vec, double theta){
        object.rotate(vec.x, vec.y, vec.z, theta);
    }

    /**
     * Paint the quobject assigned to the entity
     * @param q this current quicture that is wanting to draw this quomponent.
     * @param camera
     */
    @Override
    public void paint(Quicture q, Quamera camera){
        object.paint(q, camera);
    }
}
