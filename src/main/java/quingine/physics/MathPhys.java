package quingine.physics;

import quingine.render.sim.Math3D;
import quingine.render.sim.pos.Quisition;

/**
 * All the math for 3D physics is in here.
 * Wow.
 */
public class MathPhys {


    /**
     * Calculate the separating velocity of two particles colliding
     * @param velocityA vector of first velocity
     * @param velocityB vector of second velocity (can be null)
     * @param contactNormal the normal vector of contact
     * @return Quisition of velocity
     */
    public static double calcSeparatingVelocity(Quisition velocityA, Quisition velocityB, Quisition contactNormal){
        Quisition velocity = new Quisition();
        if (velocityA != null)
            velocity.add(velocityA);
        if (velocityB != null)
            velocity.subtract(velocityB);
        return Math3D.getDotProduct(velocity, contactNormal);
    }

    /**
     * Get the vector of how an object should rotate based off
     * of an impact
     * @param impactPoint where the impact is occurring
     * @param impactOrigin where the impact originates from
     * @param impactVector the direction of the impact
     * @param objectPosition where the object is currently at
     * @return new Quisition vector of the axis of rotation
     */
    public static Quisition getRotationVector(Quisition impactPoint, Quisition impactOrigin, Quisition impactVector, Quisition objectPosition){
        Quisition iO = new Quisition(impactOrigin);
        iO.subtract(objectPosition);
        Quisition iP = new Quisition(impactPoint);
        iP.subtract(objectPosition);
        return Math3D.normalize(Math3D.getCrossProduct(Math3D.normalize(iO), Math3D.normalize(iP), impactVector));
    }


}
