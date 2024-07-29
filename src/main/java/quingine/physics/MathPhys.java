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
        Quisition velocity = new Quisition(velocityA);
        if (velocityB != null)
            velocity.subtract(velocityB);
        return Math3D.getDotProduct(velocity, contactNormal);
    }


}
