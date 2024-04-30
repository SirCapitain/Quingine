package quingine.sim.env.entity;

import quingine.sim.cam.Quamera;
import quingine.sim.env.Quworld;
import quingine.sim.env.entity.qysics.RigidQysic;
import quingine.sim.pos.Quisition;

/**
 * A player that can interact with the quworld
 * around it
 */

public class Player extends Entity{

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
            Entity entity = world.getEntities().get((int)point.u);
            if (entity instanceof RigidQysic qys){
                qys.hit(cam.getPos(), cam.getVector(), force);
                }
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
        if (cam != null)
            cam.setPos(x, y, z);
    }

    public void setPos(Quisition pos){
        setPos(pos.x, pos.y, pos.z);
    }

    /**
     * Get the current quamera being used by the player
     * @return current quamera being used
     */
    public Quamera getQuamera(){
        return cam;
    }
}
