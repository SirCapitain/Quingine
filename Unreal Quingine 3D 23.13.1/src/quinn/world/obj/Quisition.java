package quinn.world.obj;

import qat.Quaternion;
import quinn.world.Quomponent;

import javax.management.DynamicMBean;
import java.awt.*;
import java.util.ArrayList;

/**
 * A point in 3D space that has rotation
 *
 * @author Quinn Graham
 */

public class Quisition {
    public double x, y, z;

    /**
     * Make a point but cooler cause its in 3D
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    public Quisition(double x, double y, double z){
        setPos(x,y,z);
    }

    /**
     * This needs no explaination
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     */
    public void setPos(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Rotating a point around another
     * @param theta Degrees rotated by
     * @param vx Vector x by which it is rotated by (Rotating on x plane)
     * @param vy Vector y by which it is rotated by (Rotating on y plane)
     * @param vz Vector z by which it is rotated by (Rotating on z plane)
     * @param x around point
     * @param y around point
     * @param z around point
     */
    public void rotate(double theta, double vx, double vy, double vz, double x, double y, double z){
        double dx = 0;
        double dy = 0;
        double dz = 0;
        theta = Math.toRadians(theta/2);
        Quaternion[] q1 = {new Quaternion(Math.cos(theta)), new Quaternion(Math.sin(theta) * vx, "i"), new Quaternion(Math.sin(theta) * vy, "j"), new Quaternion(Math.sin(theta) * vz, "k")};
        Quaternion[] p = {new Quaternion(this.x - x, "i"), new Quaternion(this.y - y, "j"), new Quaternion(this.z - z, "k")};
        Quaternion[] q2 = {new Quaternion(Math.cos(-theta)), new Quaternion(Math.sin(-theta) * vx, "i"), new Quaternion(Math.sin(-theta) * vy, "j"), new Quaternion(Math.sin(-theta) * vz, "k")};
        ArrayList<Quaternion> q1p = new ArrayList<>();
        for (Quaternion quat1 : q1)
            for (Quaternion quat2 : p)
                q1p.add(Quaternion.multiplyQuaternion(quat1, quat2));
        ArrayList<Quaternion> result = new ArrayList<>();
        for (Quaternion quat1 : q1p)
            for (Quaternion quat2 : q2)
                result.add(Quaternion.multiplyQuaternion(quat1, quat2));
        for (Quaternion quat : result){
            if (quat.type == null)
                continue;
            if (quat.type.equals("i"))
                dx += quat.value;
            else if (quat.type.equals("j"))
                dy += quat.value;
            else
                dz += quat.value;
        }
        this.x = dx + x;
        this.y = dy + y;
        this.z = dz + z;
    }
    public void rotate(double theta, double vx, double vy, double vz, Quisition quisition){
        rotate(theta, vx, vy, vz, quisition.x, quisition.y, quisition.z);
    }

    /**
     * Want to see where this point is?
     * Set this to true and find out!
     *
     * *Currently not working*
     */
    private boolean isVisible = false;
    public void isVisible(boolean isVisible){
        this.isVisible = isVisible;
    }
    public boolean isVisible(){
        return isVisible;
    }
    public void paint(Graphics g) {
        if (isVisible)
            g.fillOval((int)Math.round(x), (int)Math.round(y), 10, 10);
    }

    /**
     * Makes it easy to see where it is.
     * @return point in good form.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
