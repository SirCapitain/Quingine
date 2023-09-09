package quingine.sim.env.light;

import quingine.sim.Math3D;
import quingine.sim.Quisition;

/**
 * Create a light that is more directional
 *
 * W.I.P.
 */

public class DirectionalLight extends LightSource {

    /**
     * Create a light with a position and a direction
     * @param x point in 3D space
     * @param y point in 3D space
     * @param z point in 3D space
     * @param vx vector in 3D space
     * @param vy vector in 3D space
     * @param vz vector in 3D space
     */
    public DirectionalLight(double x, double y, double z, double vx, double vy, double vz){
        setPos(x, y, z);
        setDirection(vx, vy, vz);
    }

    /**
     * Gets the current light level with a set of points and its set direction
     * @param points points that are an object. typically 3
     * @return light level typically between -1 -> 1;
     */
    @Override
    public double getLightLevel(Quisition[] points){
        Quisition[] newPoints = new Quisition[points.length];
        for (int i = 0; i < points.length; i++) {
            Quisition p = new Quisition(points[i]);
            p.subtract(getPos());
            newPoints[i] = p;
        }
        Quisition point = Math3D.getCrossProduct(points[0], points[1], points[2]);
        return getPower() * (Math3D.getDotProduct(Math3D.normalize(direction), Math3D.normalize(point))/Math3D.getMagnitude(newPoints[0]));
    }

    private Quisition direction;

    /**
     * Set the direction the light is pointing in
     * @param direction vector of the light
     */
    public void setDirection(Quisition direction){
        this.direction = direction;
    }

    /**
     * Set the direction of the light
     * @param vx vector in 3D space
     * @param vy vector in 3D space
     * @param vz vector in 3D space
     */
    public void setDirection(double vx, double vy, double vz){
        setDirection(new Quisition(vx, vy, vz));
    }

    /**
     * Get the current direction of the light
     * @return current direction of the light.
     */
    public Quisition getDirection(){
        return direction;
    }
}
