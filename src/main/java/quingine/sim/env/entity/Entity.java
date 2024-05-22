package quingine.sim.env.entity;

import quingine.sim.cam.Quamera;
import quingine.sim.env.Quworld;
import quingine.sim.env.obj.Quobject;
import quingine.sim.pos.Quisition;
import quingine.util.win.Quicture;
import quingine.util.win.Quomponent;

import java.awt.*;

/**
 * An entity can interact more with the environment it is a part of.
 * Can hold a quobject to give it identification / more stuff
 * Has velocity and acceleration
 */

public class Entity extends Quomponent {

    private Quobject object;
    private Quisition gravity, velocity, acceleration;
    private double mass = 1;

    /**
     * Create a new entity
     */
    public Entity() {
        super();
        velocity = new Quisition();
        acceleration = new Quisition();
        gravity = new Quisition();
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
     * Set the position in 3D space based off another position
     * @param pos position in 3D space.
     */
    @Override
    public void setPos(Quisition pos){
        super.setPos(pos);
        if (object != null)
            object.setPos(pos);
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
     * Set the gravity of the entity.
     * @param gravity a quisition of the vector of the gravity
     */
    public void setGravity(Quisition gravity){
        this.gravity = gravity;
    }

    /**
     * Get the current gravity being exerted on the entity.
     * @return Quinsition of current gravity.
     */
    public Quisition getGravity(){
        return gravity;
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
     * Change the position of the entity by a vector
     * @param vector quisition has a vector
     */
    public void changePosBy(Quisition vector){
        if (object != null)
            object.changePosBy(vector.x, vector.y, vector.z);
        changePosBy(vector.x, vector.y, vector.z);
    }

    /**
     * Set the current velocity of the particle
     * @param velocity vector in the form of a Quisition.
     */
    public void setVelocity(Quisition velocity){
        this.velocity.setPos(velocity);
    }

    /**
     * Get the current velocity of the particle
     * @return
     */
    public Quisition getVelocity(){
        return velocity;
    }

    /**
     * Add a vector to the velocity
     * @param vector a Quisition
     */
    public void addVelocity(Quisition vector){
        velocity.add(vector);
    }

    /**
     * Set the acceleration of the entity
     * @param acceleration a vector in the form of a Quisition
     */
    public void setAcceleration(Quisition acceleration){
        this.acceleration.setPos(acceleration);
    }

    /**
     * Get the current acceleration of the entity
     * @return a Quisition vector of the acceleration
     */
    public Quisition getAcceleration(){
        return acceleration;
    }

    /**
     * Add acceleration to the entity
     * @param vector a Quisition of the acceleration vector
     */
    public void addAcceleration(Quisition vector){
        acceleration.add(vector);
    }

    /**
     * Update the position of the particle based off
     * of velocity and acceleration
     * @param world the quword the entity is in.
     */
    public void update(Quworld world){
        //Properly adjust for tick speed
        Quisition vel = new Quisition(velocity);
        Quisition accel = new Quisition(acceleration);
        Quisition g = new Quisition(getGravity());
        g.divide(world.getQysicSpeed());
        vel.divide(world.getQysicSpeed());
        velocity.add(accel);
        //Update Position
        changePosBy(vel);
        //Gravity
        setAcceleration(g);
    }

    /**
     * Paint the quobject assigned to the entity
     * @param q this current quicture that is wanting to draw this quomponent.
     * @param camera
     */
    @Override
    public void paint(Quicture q, Quamera camera){
        if (object != null)
            object.paint(q, camera);
    }
}
