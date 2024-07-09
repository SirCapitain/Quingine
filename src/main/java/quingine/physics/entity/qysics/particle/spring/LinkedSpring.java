package quingine.physics.entity.qysics.particle.spring;

import quingine.render.sim.Math3D;
import quingine.render.sim.cam.Quamera;
import quingine.render.sim.env.Quworld;
import quingine.render.sim.pos.Quisition;
import quingine.render.util.win.Quicture;
import quingine.physics.entity.qysics.particle.Quarticle;

/**
 * A spring that applies forces to both particles
 * linked to the spring.
 */

public class LinkedSpring extends Spring {

    public Quarticle particleA, particleB;

    /**
     * A spring that is linked to two particles.
     * @param restLength the length at which the spring desires to be in
     * @param constant the force the spring applies over the change in length
     * @param particleA the first particle linked to the spring
     * @param particleB the second particle linked to the spring
     */
    public LinkedSpring(double restLength, double constant, Quarticle particleA, Quarticle particleB){
        super(restLength, constant);
        setLinkedParticles(particleA, particleB);
    }

    /**
     * Set the particles linked to the spring
     * @param particleA first Quarticle
     * @param particleB second Quarticle
     */
    public void setLinkedParticles(Quarticle particleA, Quarticle particleB){
        this.particleA = particleA;
        this.particleB = particleB;
    }

    /**
     * Apply the correct forces to the particles
     * that are linked to the spring
     * @param world the quword the spring is in.
     */
    @Override
    public void update(Quworld world) {
        super.update(world);
        //change in length
        Quisition force = new Quisition(particleA.getPos());
        force.subtract(particleB.getPos());
        //Apply the forces
        particleB.hit(Math3D.normalize(force), (Math3D.getMagnitude(force)-getRestLength())*getConstant()/world.getQysicSpeed());
        force.multiply(-1);
        particleA.hit(Math3D.normalize(force), (Math3D.getMagnitude(force)-getRestLength())*getConstant()/world.getQysicSpeed());
    }

    /**
     * Paint the cable between the particles
     * @param q this current quicture that is wanting to draw this quomponent.
     * @param camera the quamera painting the picture
     */
    @Override
    public void paint(Quicture q, Quamera camera) {
        paint(q, camera, particleA.getPos(), particleB.getPos());
    }
}
