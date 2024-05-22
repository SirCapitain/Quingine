package quingine.sim.env.entity.qysics.particle.spring;

import quingine.sim.Math3D;
import quingine.sim.cam.Quamera;
import quingine.sim.env.Quworld;
import quingine.sim.env.entity.qysics.particle.Quarticle;
import quingine.sim.pos.Quisition;
import quingine.util.win.Quicture;

/**
 * A bungee cable that is anchor to a point in 3D space.
 * A bungee is a cable that is elastic beyond its resting length.
 */

public class AnchoredBungee extends AnchoredSpring {
    public AnchoredBungee(double restLength, double constant, Quarticle particle){
        super(restLength, constant, particle);
    }

    /**
     * Update the particle linked to the cable.
     * @param world the quworld the particles are currently in
     */
    @Override
    public void update(Quworld world) {
        //Get length
        Quisition force = new Quisition(getPos());
        force.subtract(getParticle().getPos());
        //Makes sure the bungee does not apply force when the length
        //is less than its resting length.
        if (Math3D.getMagnitude(force) <= getRestLength())
            return;
        //Apply elastic force
        getParticle().hit(Math3D.normalize(force), (Math3D.getMagnitude(force)-getRestLength())*getConstant()/ world.getQysicSpeed());
    }
}
