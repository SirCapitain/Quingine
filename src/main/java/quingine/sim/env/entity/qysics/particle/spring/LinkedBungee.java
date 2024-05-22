package quingine.sim.env.entity.qysics.particle.spring;

import quingine.sim.Math3D;
import quingine.sim.cam.Quamera;
import quingine.sim.env.Quworld;
import quingine.sim.env.entity.qysics.particle.Quarticle;
import quingine.sim.pos.Quisition;
import quingine.util.win.Quicture;

/**
 * A bungee cable that is anchor to another particle.
 * A bungee is a cable that is elastic beyond its resting length.
 */

public class LinkedBungee extends LinkedSpring{
    public LinkedBungee(double restLength, double constant, Quarticle particleA, Quarticle particleB){
        super(restLength, constant, particleA, particleB);
    }

    /**
     * Update the particles linked to the cable.
     * @param world the quworld the particles are currently in
     */
    @Override
    public void update(Quworld world) {
        //Change in length
        Quisition force = new Quisition(particleA.getPos());
        force.subtract(particleB.getPos());
        //Check if the length is less than the resting length
        if (Math3D.getMagnitude(force) <= getRestLength())
            return;
        //Apply forces to both particles.
        particleB.hit(Math3D.normalize(force), (Math3D.getMagnitude(force)-getRestLength())*getConstant()/world.getQysicSpeed());
        force.multiply(-1);
        particleA.hit(Math3D.normalize(force), (Math3D.getMagnitude(force)-getRestLength())*getConstant()/world.getQysicSpeed());
    }
}
