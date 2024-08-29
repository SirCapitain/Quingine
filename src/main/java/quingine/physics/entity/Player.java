package quingine.physics.entity;

import quingine.render.sim.env.Quworld;
import quingine.render.sim.Math3D;
import quingine.render.sim.cam.Quamera;
import quingine.physics.entity.qysics.particle.Quarticle;
import quingine.physics.entity.qysics.RigidQysic;
import quingine.render.sim.pos.Quisition;

/**
 * A player that can interact with the quworld
 * around it
 */

public class Player extends QollidableQuobject {

    private Quamera cam;
    private Quworld world;

    /**
     * Create a new player and set was quamera
     * it will use to see
     * @param quamera the quamera to be used
     */
    public Player(Quamera quamera){
        cam = quamera;
    }

    /**
     * Set the home quworld the player is bound to
     * @param world a quworld
     */
    public void setQuworld(Quworld world){
        this.world = world;
    }

    /**
     * An action that will only affect entities that the
     * player is looking at with a specific force
     * @param force amount of power behind the hit.
     */
    public void hit(double force){
        Quisition point = world.getQuameraLookingAt();
        if (point != null){
            QollidableQuobject qollidableQuobject = world.getQollidableQuobjects().get((int)point.u);
            if (qollidableQuobject instanceof RigidQysic qys){
                force *= -Math.cos(Math3D.getRadiansBetween(qys.getQuobject().getNormal((int)point.v), cam.getVector()));
//                qys.hit(cam.getPos(), cam.getVector(), force);
                }
            else if (qollidableQuobject instanceof Quarticle particle)
                particle.hit(cam.getVector(), force);
            }
    }

    /**
     * Set the position of the player in 3D space
     * @param x position in 3D space.
     * @param y position in 3D space.
     * @param z position in 3D space.
     */
    @Override
    public void setPos(double x, double y, double z){
        super.setPos(x, y, z);
//        if (cam != null)
//            cam.setPos(x, y, z);
    }

    public void setPos(Quisition pos){
        setPos(pos.x, pos.y, pos.z);
    }

    /**
     * Change the position of the entity by a vector
     * @param vector quisition has a vector
     */
    public void changePosBy(Quisition vector){
        super.changePosBy(vector);
        if (cam != null)
            cam.changePosBy(vector.x, vector.y, vector.z);
    }

    /**
     * Get the current quamera being used by the player
     * @return current quamera being used
     */
    public Quamera getQuamera(){
        return cam;
    }
}
