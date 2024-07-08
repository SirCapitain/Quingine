package quingine.sim.env.entity.qysics.particle.liquid;

import quingine.sim.env.Quworld;
import quingine.sim.env.entity.QollidableQuobject;
import quingine.sim.env.entity.qysics.particle.Quarticle;
import quingine.sim.pos.Quisition;

/**
 * A liquid that will produce a buoyancy effect
 * on all particles in 3D space.
 * All particles are assumed to have
 * a volume of 1m^3 and a size of 1
 */

public class Liquid extends QollidableQuobject {

    private double density;

    /**
     * A liquid with a set density
     * @param density the density of the liquid
     */
    public Liquid(double density){
        setDensity(density);
    }

    /**
     * Get the current density of the liquid
     * @return the current density of the liquid
     */
    public double getDensity() {
        return density;
    }

    /**
     * Set the current density of the liquid
     * @param density the density of the liquid
     */
    public void setDensity(double density) {
        this.density = density;
    }

    /**
     * Check all particles and apply the correct forces
     * if the particles are in the liquid
     * @param world the current quworld the liquid is in.
     */
    @Override
    public void update(Quworld world) {
        super.update(world);
        for (int i = 0; i < world.getQollidableQuobjects().size(); i++){
            QollidableQuobject qollidableQuobject = world.getQollidableQuobjects().get(i);
            if (qollidableQuobject instanceof Quarticle particle) {
                double depth = particle.getPos().y;
                if (depth >= getPos().y + 1)//Is it in the liquid?
                    return;
                if (depth <= getPos().y - 1) {//This is all under the assumption that everything has a volume of 1m^3 and size is 1
                    particle.hit(new Quisition(0,1,0), density/(world.getQysicSpeed()));
                    return;
                }
                //Apply the force
                particle.hit(new Quisition(0,1,0), density*(depth-1-getPos().y)/(2*1*world.getQysicSpeed()));
            }

        }
    }
}
