package quingine.physics.entity.qysics;

import quingine.physics.entity.QollidableQuobject;
import quingine.render.sim.env.Quworld;
import quingine.render.sim.env.obj.Quobject;
import quingine.render.sim.pos.Quisition;
import quingine.render.sim.Math3D;

/**
 * A rigid body physics simulation that extends from the entity class
 */
public class RigidQysic extends QollidableQuobject {

    private double linearDamping;
    private Quisition orientation;
    private Quisition angularVelocity;

    /**
     * Create a new rigid body physic object
     */
    public RigidQysic(){
        super();
        setQuobject(new Quobject("cube.obj",0,0,0,1));
    }

    /**
     * Update the object based on the velocity vectors
     */
    @Override
    public void update(Quworld world){


    }
}