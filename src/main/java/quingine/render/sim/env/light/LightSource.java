package quingine.render.sim.env.light;

import quingine.render.sim.Math3D;
import quingine.render.sim.pos.Quisition;
import quingine.render.util.win.Quomponent;

/**
 * A light source to bring light into this gloomy world
 * Currently does not check for if an object is blocking the light.
 */

public class LightSource extends Quomponent {

    private double power = 10;

    /**
     * Make a light source that appears in the quicture
     * Defaults position to zeroes
     */
    public LightSource(){
        super();
    }

    /**
     * Create a light source with a position
     * @param x point in 3D space
     * @param y point in 3D space
     * @param z point in 3D space
     */
    public LightSource(double x, double y, double z){
        super(x, y, z);
    }

    /**
     * Make a light that brings light into your life
     * @param pos position of the light in the 3D world
     */
    public LightSource(Quisition pos){
        super(pos);
    }

    /**
     * Get the light level of a set of points, particularly 3.
     * @param point a point in space.
     * @param normal direction at which the point is facing
     * Set the light value onto the position; typically a number from -1 to 1 indicating amount of light touching it.
     */
    public double getLightLevel(Quisition point, Quisition normal){
         Quisition newPoint = new Quisition(point);
         newPoint.subtract(getPos());
         Quisition direction = new Quisition(getPos());
         direction.subtract(point);
         direction.normalize();
         return power * Math3D.getDotProduct(direction, normal)/((Math3D.getMagnitude(newPoint)));
    }

    /**
     * Set the brightness of a light
     * Default is 10
     * @param power how much light you want
     */
    public void setPower(double power){
        this.power = power;
    }

    /**
     * Get the current power level of the light
     * @return current amount of power being used.
     */
    public double getPower(){
        return power;
    }
}
