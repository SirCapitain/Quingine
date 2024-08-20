package quingine.physics.entity.qysics.link.cable;

import quingine.physics.MathPhys;
import quingine.physics.entity.QollidableQuobject;
import quingine.physics.entity.qysics.particle.ContactGenerator;
import quingine.render.sim.Math3D;
import quingine.render.sim.env.Quworld;
import quingine.render.sim.pos.Quisition;

/**
 * I couldn't think of anything that started with a Q...
 * A way to link two Particles to
 */

public class Rod extends Quable{
    public Rod(QollidableQuobject particleA, QollidableQuobject particleB){
        super(particleA, particleB);
        setRestitution(0);
    }

    /**
     * Update the particles attached to the rod.
     * @param world the quword the QollidableQuobject is in.
     */
    @Override public void update(Quworld world) {
        double length = getLength();
        if (length == getMaxLength())
            return;
        Quisition normal = Math3D.calcNormalDirectionVector(particleB.getPos(), particleA.getPos());
        double penetration = length-getMaxLength();
        if (length <= getMaxLength()) {
            normal.multiply(-1);
            penetration = getMaxLength()-length;
        }
        ContactGenerator.calculateVelocities(particleA, particleB, MathPhys.calcSeparatingVelocity(particleA.getVelocity(), particleB.getVelocity(), normal), normal,0);
        ContactGenerator.resolveCollision(particleA, particleB, normal, penetration);

    }
}
