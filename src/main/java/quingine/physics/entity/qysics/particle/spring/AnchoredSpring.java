package quingine.physics.entity.qysics.particle.spring;

import quingine.render.sim.Math3D;
import quingine.render.sim.cam.Quamera;
import quingine.render.sim.env.Quworld;
import quingine.render.sim.pos.Quisition;
import quingine.render.util.win.Quicture;
import quingine.physics.entity.qysics.particle.Quarticle;

/**
 * A spring force the can hold onto a
 * particle and apply forces.
 */

public class AnchoredSpring extends Spring {

    private Quarticle particle;

    /**
     * A spring that is anchored to a point in 3D space
     * @param restLength the length at which the spring desire to be in
     * @param constant the force the spring will apply over the change in length
     * @param particle the particle linked to the spring
     */
    public AnchoredSpring(double restLength, double constant, Quarticle particle){
        super(restLength, constant);
        this.particle = particle;
        isLocked(true);
    }

    /**
     * Set the current particle linked to the spring
     * @param particle any Quarticle
     */
    public void setParticle(Quarticle particle){
        this.particle = particle;
    }

    /**
     * Get the current particle linked to the spring
     * @return the current Quarticle
     */
    public Quarticle getParticle(){
        return particle;
    }

    /**
     * Update the particle linked to the spring
     * with the correct forces
     * @param world the current quworld the spring is in.
     */
    @Override
    public void update(Quworld world) {
        super.update(world);
        //Change in length
        Quisition force = new Quisition(getPos());
        force.subtract(particle.getPos());
        //Apply force
        particle.hit(Math3D.normalize(force), (Math3D.getMagnitude(force)-getRestLength())*getConstant()/world.getQysicSpeed());
    }

    /**
     * Paint the cable linked to the particle
     * @param q this current quicture that is wanting to draw this quomponent.
     * @param camera the camera looking currently
     */
    @Override
    public void paint(Quicture q, Quamera camera) {
        paint(q, camera, getPos(), particle.getPos());
    }
}
