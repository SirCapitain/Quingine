package quingine.sim.light;

import quingine.sim.Math3D;
import quingine.sim.Quisition;
import quingine.sim.cam.Quamera;
import quingine.util.Quindow;
import quingine.util.Quomponent;

/**
 * A light source to bring light into this gloomy world
 * Currently does not check for if an object is blocking the light.
 */

public class LightSource extends Quomponent {

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
     * @param points points that are an object
     * @return Typically a number from -1 to 1 indicating amount of light touching it.
     */
    public double getLightLevel(Quisition[] points){
        Quisition[] newPoints = new Quisition[points.length];
        for (int i = 0; i < points.length; i++) {
            Quisition p = new Quisition(points[i]);
            p.subtract(getPos());
            newPoints[i] = p;
        }
        Quisition point = Math3D.getCrossProduct(points[0], points[1], points[2]);
        Quisition direction = new Quisition(getPos());
        direction.subtract(points[0]);
        return power * (Math3D.getDotProduct(Math3D.normalize(direction), Math3D.normalize(point))/Math3D.getMagnitude(newPoints[0]));
    }

    private double power = 10;

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
