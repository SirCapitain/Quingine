package quingine.physics.entity.qysics;

import quingine.physics.entity.QollidableQuobject;
import quingine.render.sim.env.Quworld;
import quingine.render.sim.env.obj.Quobject;
import quingine.render.sim.pos.Quaternion;
import quingine.render.sim.pos.Quisition;
import quingine.render.sim.Math3D;

/**
 * A rigid body physics simulation that extends from the entity class
 */
public class RigidQysic extends QollidableQuobject {

    private double linearDamping = .99;
    private Quaternion angularVelocity;
    private Quisition inverseInertia;

    /**
     * Create a new rigid body physic object
     */
    public RigidQysic(double size){
        super();
        setQuobject(new Quobject("Cube.obj",0,0,0,size));
        double i = 1/(12*getMass()*size*size*2);//Subject to change. Just want to deal with cubes right now.
        inverseInertia = new Quisition(i, i, i);
    }

    public void hit(Quisition origin, Quisition direction, double force){
        Quisition objPoint = getQuobject().getVectorIntersectionPoint(origin, direction);
        direction = new Quisition(getQuobject().getNormal((int)objPoint.data[1]));
        objPoint.subtract(getPos());
        direction.multiply(force);
        angularVelocity = new Quaternion(Math3D.getCrossProduct(direction, objPoint));
        angularVelocity.multiply(inverseInertia);
        double theta = Math3D.getMagnitude(angularVelocity)/2;
        angularVelocity.w = Math.cos(theta);
        angularVelocity.normalize();
        angularVelocity.multiply(Math.sin(theta));
    }

    /**
     * Update the object based on the velocity vectors
     */
    @Override
    public void update(Quworld world){
        if (angularVelocity != null) {
            double w = angularVelocity.w;
            angularVelocity.w /= world.getQysicSpeed();
            w *= linearDamping;
            getQuobject().rotate(angularVelocity);
            angularVelocity.w = w;
        }
    }
}