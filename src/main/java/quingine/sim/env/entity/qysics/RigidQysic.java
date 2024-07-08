package quingine.sim.env.entity.qysics;

import quingine.sim.Math3D;
import quingine.sim.env.Quworld;
import quingine.sim.env.entity.QollidableQuobject;
import quingine.sim.pos.Quisition;

/**
 * A rigid body physics simulation that extends from the entity class
 */
public class RigidQysic extends QollidableQuobject {

    private Quisition rotationVec, velocityVec;
    private double rotSpeed = 0;
    private double rotFriction = .5;

    /**
     * Create a new rigid body physic object
     */
    public RigidQysic(){
        super();
        rotationVec = new Quisition();
        velocityVec = new Quisition();
    }

    /**
     * Set the rotation vector the object will rotate around
     * @param vec unit vector for rotation
     */
    public void setRotationVec(Quisition vec){
        if (vec.hasNaN())
            return;
        rotationVec = vec;
    }

    /**
     * Get the current vector the object is
     * using to rotate around
     * @return vector quisition
     */
    public Quisition getRotationVec(){
        return rotationVec;
    }

    /**
     * Set the velocity the object is curring moving in
     * @param vec quisition vector for velocity
     */
    public void setVelocityVec(Quisition vec){
        velocityVec = vec;
    }

    /**
     * Add to vector to the current velocity
     * @param vec quisition vector to be added
     */
    public void addVelocityVec(Quisition vec){
        velocityVec.add(vec);
    }

    /**
     * Get the current velocity vector being used
     * by the object
     * @return quisition vector
     */
    public Quisition getVelocityVec(){
        return velocityVec;
    }

    /**
     * Create a force on the object from a point at a direction
     * @param pointOfImpact where the impact comes from
     * @param vectorOfImpact the direction the impact is going
     * @param force the amount of force applied.
     */
    public void hit(Quisition pointOfImpact, Quisition vectorOfImpact, double force){
        Quisition point = getQuobject().getVectorIntersectionPoint(pointOfImpact, vectorOfImpact);
        if (point == null)
            return;

        Quisition r = new Quisition(getPos());
        r.subtract(point);
        setRotationVec(Math3D.getRotationVector(point, pointOfImpact, vectorOfImpact, getPos()));
        rotSpeed = force;

    }

    /**
     * Update the object based on the velocity vectors
     */
    @Override
    public void update(Quworld world){
        rotate(rotationVec, rotSpeed/(world.getQysicSpeed()*getMass()));
        rotSpeed -= rotSpeed*(rotFriction/world.getQysicSpeed());
        if (rotSpeed < 0.001)
            rotSpeed = 0;


    }
}