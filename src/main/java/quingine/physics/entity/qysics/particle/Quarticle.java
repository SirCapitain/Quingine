package quingine.physics.entity.qysics.particle;

import quingine.physics.entity.QollidableQuobject;
import quingine.render.sim.Math3D;
import quingine.render.sim.env.Quworld;
import quingine.render.sim.env.obj.Quobject;
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

    private void calculateVelocities(double separatingVelocity, Quisition direction, Quarticle particle, double duration){
        if (separatingVelocity > 0)//Is it moving away or not moving at all?
            return;
        double newSeparatingVelocity = -separatingVelocity * restitution;

        //Resolves the resting contact of the particle so that
        //it does not bounce constantly
        Quisition acceleration = new Quisition(getAcceleration());
        if (particle != null)
            acceleration.subtract(particle.getAcceleration());
        double separatingAccel = Math3D.getDotProduct(direction, acceleration);
        if (separatingAccel < 0){
            newSeparatingVelocity += restitution * separatingAccel;
            newSeparatingVelocity = Math.max(newSeparatingVelocity, 0);
        }

        //Calculate the impulse of the collision
        double deltaVelocity = newSeparatingVelocity - separatingVelocity;
        double massTotal = getMass();
        if (particle != null)
            massTotal += particle.getMass();
        double impulse = deltaVelocity / massTotal;
        Quisition impulsePerMass = new Quisition(direction);
        impulsePerMass.multiply(impulse);

        //Apply velocity to first particle
        Quisition velocity = new Quisition(getVelocity());
        impulsePerMass.multiply(getMass());
        velocity.add(impulsePerMass);
        setVelocity(velocity);

        //Apply velocity to second particle
        if (particle == null)
            return;
        velocity = new Quisition(particle.getVelocity());
        impulsePerMass.divide(getMass());
        impulsePerMass.multiply(-particle.getMass());
        velocity.add(impulsePerMass);
        particle.setVelocity(velocity);
    }

    private void resolveVelocity(Quworld world){
        for (QollidableQuobject qollidableQuobject : world.getQollidableQuobjects())
            if (qollidableQuobject instanceof Quarticle particle) {
                if (particle == this || Math3D.getDist(getPos(), particle.getPos()) > getQuobject().getSize()+particle.getQuobject().getSize())
                    continue;
                //Determine velocity and direction
                Quisition direction = Math3D.calcNormalDirectionVector(getPos(), particle.getPos());
                double separatingVelocity = Math3D.calcSeparatingVelocity(getVelocity(), particle.getVelocity(), direction);
                //Calculate
                calculateVelocities(separatingVelocity, direction, particle, world.getQysicSpeed());
            }
        //Quobjects
        for (Quobject object : world.getQuobjects()) {
            Quisition contactNormal = Math3D.calcNormalDirectionVector(object.getPos(), getPos());
            Quisition point = object.getVectorIntersectionPoint(getPos(), contactNormal);
            if (point == null)
                continue;
            Quisition edge = new Quisition(getPos());
            edge.add(contactNormal);
            Quisition direction = object.getPlanes().get((int)point.v).getNormal();
            point = object.getVectorIntersectionPoint(edge, direction);
            if (point == null || getPos().getDistance(point) > getQuobject().getSize())
                continue;
            double separatingVelocity = Math3D.calcSeparatingVelocity(getVelocity(), null, direction);
            calculateVelocities(separatingVelocity, direction, null, world.getQysicSpeed());
        }
    }

    private void resolveCollision(Quworld world){
        for (Quobject object : world.getQuobjects()){
            //Calculate Collision
            Quisition contactNormal = Math3D.calcNormalDirectionVector(object.getPos(), getPos());
            Quisition point = object.getVectorIntersectionPoint(getPos(), contactNormal);
            if (point == null)
                continue;
            Quisition normal = object.getPlanes().get((int)point.v).getNormal();
            point = object.getVectorIntersectionPoint(getPos(), normal);
            if (point == null)
                continue;
            normal.multiply(-1);
            Quisition particlePos = new Quisition(getPos());
            particlePos.add(normal);
            if (particlePos.getDistance(object.getPos()) > point.getDistance(object.getPos()))
                continue;
            //Calculate Velocity
            double penetration = point.getDistance(particlePos);
            Quisition impulsePerMass = new Quisition(normal);
            impulsePerMass.multiply(-1);
            impulsePerMass.multiply(penetration / getMass());
            impulsePerMass.multiply(getMass());
            changePosBy(impulsePerMass);
            System.out.println(getPos());
        }
    }

    private void contact(Quworld world){
        resolveCollision(world);
        resolveVelocity(world);
    }

    /**
     * Update the particle based off of current forces
     * @param world the quword the particle is in.
     */
    @Override
    public void update(Quworld world) {
        contact(world);
        super.update(world);
        //Update Velocity
        Quisition vel = getVelocity();
        vel.multiply(drag);
        setVelocity(vel);
    }
}
