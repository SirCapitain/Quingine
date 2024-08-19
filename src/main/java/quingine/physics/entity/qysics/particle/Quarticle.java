package quingine.physics.entity.qysics.particle;

import quingine.physics.entity.QollidableQuobject;
import quingine.render.sim.Math3D;
import quingine.render.sim.env.Quworld;
import quingine.render.sim.pos.Quisition;

/**
 * A particle in 3D space that has simulated physics
 * Defaults to a gravity of -9.8 and has a drag
 * of .99.
 */

public class Quarticle extends QollidableQuobject {

    private double drag = .99;
    private double restitution = .75;

    /**
     * A particle in 3D space
     */
    public Quarticle(){
        super();
        setGravity(new Quisition(0,-9.8,0));
    }

    /**
     * Set how fast the particle loses its velocity
     * @param drag 1 (retains all velocity), 0 (loses all velocity)
     */
    public void setDrag(double drag){
        this.drag = drag;
    }

    /**
     * Get the current drag of the particle.
     * @return 1 (retains all velocity), 0 (loses all velocity)
     */
    public double getDrag(){
        return drag;
    }

    /**
     * Create a force on the particle based off direction and force.
     * Takes into account mass.
     * @param vectorOfImpact the Quisition of which the direction is going in.
     * @param force how powerful the force is.
     */
    public void hit(Quisition vectorOfImpact, double force){
        if (Double.isNaN(force))
            return;
        if (vectorOfImpact.hasNaN())
            return;
        Quisition vec = new Quisition(vectorOfImpact);
        vec.multiply(force/getMass());
        addAcceleration(vec);
    }

    /**
     * Set the restitution of the particle.
     * Set how bouncy the particle it.
     * @param restitution (0 - no velocity kept) (1 - all velocity kept)
     */
    public void setRestitution(double restitution){
        this.restitution = restitution;
    }

    /**
     * Get the restitution of the particle
     * How bouncy it is.
     * @return  (0 - no velocity kept) (1 - all velocity kept)
     */
    public double getRestitution(){
        return restitution;
    }

    /**
     * Update the particle on how contacting works. They tend to forget every time.
     * @param world the current quworld holding all the goodies like QollidableQuobjects
     */
    private void contact(Quworld world){
        ContactGenerator.resolveWall(new Quisition(0,1,0), new Quisition(0,-4,0), this, getRestitution());
        for (QollidableQuobject obj : world.getQollidableQuobjects())
            if (obj instanceof Quarticle particle) {
                if (particle == this)
                    continue;
                ContactGenerator.resolveVelocity(this, particle, particle.getRestitution());
                ContactGenerator.resolveCollision(this, particle, Math3D.calcNormalDirectionVector(this.getPos(), particle.getPos()),getQuobject().getSize() + particle.getQuobject().getSize() - getPos().getDistance(particle.getPos()));
            }
    }

    /**
     * Update the particle based off of current forces
     * @param world the quword the particle is in.
     */
    @Override
    public void update(Quworld world) {
        if (isLocked())
            return;
        if (hasCollision())
            contact(world);
        super.update(world);
        //Update Velocity
        Quisition vel = getVelocity();
        vel.multiply(drag);
        setVelocity(vel);
    }
}
