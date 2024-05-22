package quingine.sim.env.entity.qysics.particle;

import quingine.sim.Math3D;
import quingine.sim.env.Quworld;
import quingine.sim.env.entity.Entity;
import quingine.sim.pos.Quisition;

/**
 * A particle in 3D space that has simulated physics
 * Defaults to a gravity of -9.8 and has a drag
 * of .99.
 */

public class Quarticle extends Entity {

    private double drag = .99;

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
     * Update the particle based off of current forces
     * @param world the quword the particle is in.
     */
    @Override
    public void update(Quworld world) {
        super.update(world);
        //Update Velocity
        getVelocity().multiply(drag);
    }
}
