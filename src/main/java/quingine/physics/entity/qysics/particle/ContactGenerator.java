package quingine.physics.entity.qysics.particle;

import quingine.physics.MathPhys;
import quingine.physics.entity.QollidableQuobject;
import quingine.render.sim.Math3D;
import quingine.render.sim.pos.Quisition;

/**
 * A class to resolve any contact thrown at it.
 */
public class ContactGenerator {


    /**
     * Resolves the velocity of two particles when they collide
     * @param particleA a QollidableQuobject
     * @param particleB another QollidableQuobject
     * @param restitution bounciness of the situation
     */
    public static void resolveVelocity(QollidableQuobject particleA, QollidableQuobject particleB, double restitution){
        if (particleB == null || particleA == particleB || !particleB.hasCollision() || Math3D.getDist(particleA.getPos(), particleB.getPos()) > particleA.getQuobject().getSize()+particleB.getQuobject().getSize())
            return;
        //Determine velocity and direction
        Quisition direction = Math3D.calcNormalDirectionVector(particleA.getPos(), particleB.getPos());
        double separatingVelocity = MathPhys.calcSeparatingVelocity(particleA.getVelocity(), particleB.getVelocity(), direction);
        //Calculate
        calculateVelocities(particleA, particleB, separatingVelocity, direction, restitution);
    }

    /**
     * Resolves the velocity contact of a 2D plane locate ANYWHERE! CAN EVEN BE AT YOUR HOUSE!!! :O
     * @param normal the normal facing direction of the plane in the form of a Quisition
     * @param position a Quisition of the plane in 3D space
     * @param particle a QollidableQuobject
     */
    public static void resolveWall(Quisition normal, Quisition position, QollidableQuobject particle, double restitution){
        //floor
        Quisition end = new Quisition(particle.getPos());
        end.subtract(normal);

        if (particle.getPos().getDistance(Math3D.getIntersectionPoint(position, normal, particle.getPos(), end)) > particle.getQuobject().getSize())
            return;
        double separatingVelocity = MathPhys.calcSeparatingVelocity(particle.getVelocity(), null, normal);
        calculateVelocities(particle, null, separatingVelocity, normal, restitution);
    }

    /**
     * Calculate the velocity of two QollidableQuobject based off more parameters.
     * @param particleA any QollidableQuobject
     * @param particleB another QollidableQuobject
     * @param separatingVelocity the velocity at which they separate at. (look... you are going to have to figure it out yourself if you want to know more)
     * @param direction the direction at which the contact is made at in the form of a Quisition
     * @param restitution the bounciness... or the boinginess of the contact.
     */
    public static void calculateVelocities(QollidableQuobject particleA, QollidableQuobject particleB, double separatingVelocity, Quisition direction, double restitution){
        if (separatingVelocity > 0)//Is it moving away or not moving at all?
            return;
        double newSeparatingVelocity = -separatingVelocity * restitution;

        boolean nullLockA = particleA == null || particleA.isLocked();
        boolean nullLockB = particleB == null || particleB.isLocked();

        //If particle locked, treat it as a brick wall.
        if (nullLockA && nullLockB)
            return;

        //Resolves the resting contact of the particle so that
        //it does not bounce constantly
        Quisition acceleration = new Quisition();
        if (!nullLockA)
            acceleration.add(particleA.getAcceleration());
        if (!nullLockB)
            acceleration.subtract(particleB.getAcceleration());
        double separatingAccel = Math3D.getDotProduct(direction, acceleration);
        if (separatingAccel < 0){
            newSeparatingVelocity += restitution * separatingAccel;
            newSeparatingVelocity = Math.max(newSeparatingVelocity, 0);
        }

        //Calculate the impulse of the collision
        double deltaVelocity = newSeparatingVelocity - separatingVelocity;
        double massTotal = 0;
        if (!nullLockA)
            massTotal += 1/particleA.getMass();
        if (!nullLockB)
            massTotal += 1/particleB.getMass();
        double impulse = deltaVelocity / massTotal;

        Quisition impulsePerMass = new Quisition(direction);
        impulsePerMass.multiply(impulse);
        Quisition velocity = new Quisition();
        //Apply velocity to first particle
        if (!nullLockA) {
            velocity.add(particleA.getVelocity());
            impulsePerMass.divide(particleA.getMass());
            velocity.add(impulsePerMass);
            particleA.setVelocity(velocity);
        }

        //Apply velocity to second particle
        if (nullLockB)
            return;
        impulsePerMass = new Quisition(direction);
        impulsePerMass.multiply(impulse);
        velocity = new Quisition(particleB.getVelocity());
        impulsePerMass.divide(-particleB.getMass());
        velocity.add(impulsePerMass);
        particleB.setVelocity(velocity);
    }

    /**
     * Resolve the collision of two QollidableQuobjects.
     * Well... its more like... when they are penetrating one another (I swear! do not take the out of context! You try picking better words!)
     * Not really a collision per-say
     * @param particleA a QollidableQuobject
     * @param particleB yet another QollidableQuobject
     * @param normal the direction at which they are penetrating in the form of a quisition
     * @param penetration the depth of the penetration.
     */
    public static void resolveCollision(QollidableQuobject particleA, QollidableQuobject particleB, Quisition normal, double penetration){
        if (particleA == particleB || !particleB.hasCollision())
            return;
        //Calculate Velocity
        if (penetration <= 0)
            return;
        Quisition impulsePerMass = new Quisition(normal);
        double totalMass = 0;
        if (!particleA.isLocked())
            totalMass += 1/particleA.getMass();
        if (!particleB.isLocked())
            totalMass += 1/particleB.getMass();
        impulsePerMass.multiply(penetration / totalMass);
        impulsePerMass.divide(particleA.getMass());
        if (!particleA.isLocked())
            particleA.changePosBy(impulsePerMass);
        impulsePerMass.multiply(-particleA.getMass()/particleB.getMass());
        if (!particleB.isLocked())
            particleB.changePosBy(impulsePerMass);
    }
}