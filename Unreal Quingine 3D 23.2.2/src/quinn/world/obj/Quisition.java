package quinn.world.obj;

import qat.Quaternion;
import quinn.win.Quicture;
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

    public void addQuisition(Quisition quisition){
        x += quisition.x;
        y += quisition.y;
        z += quisition.z;
    }

    /**
     * Calculate the vector between two points
     * @param pos1 first position
     * @param pos2 second position
     * @param pos3 third position
     * @return normal vector
     */
    public static double[] calcVector(Quisition pos1, Quisition pos2, Quisition pos3){
        return new double[]{(pos2.y - pos1.y)*(pos3.z - pos1.z) - (pos2.z - pos1.z)*(pos3.y - pos1.y), (pos2.z - pos1.z)*(pos3.x - pos1.x) - (pos2.x - pos1.x)*(pos3.z - pos1.z), (pos2.x - pos1.x)*(pos3.y - pos1.y) - (pos2.y - pos1.y)*(pos3.x - pos1.x)};
    }
    public static double[] calcVector(Quisition pos1, Quisition pos2, Quisition pos3, Quisition pos4){
        return new double[]{(pos2.y - pos1.y)*(pos4.z - pos3.z) - (pos2.z - pos1.z)*(pos4.y - pos3.y), (pos2.z - pos1.z)*(pos4.x - pos3.x) - (pos2.x - pos1.x)*(pos4.z - pos3.z), (pos2.x - pos1.x)*(pos4.y - pos3.y) - (pos2.y - pos1.y)*(pos4.x - pos3.x)};
    }

    /**
     * Calculate the normal vector but find if it s collinear
     * @param pos1 fist position
     * @param pos2 second position
     * @param pos3 third position
     * @return null if collinear   list of vector
     */
    public static double[] calcVectorNotCollinear(Quisition pos1, Quisition pos2, Quisition pos3){
        double[] ab = {pos2.x - pos1.x, pos2.y - pos1.y, pos2.z - pos1.z};
        double[] ac = {pos3.x - pos1.x, pos3.y - pos1.y, pos3.z - pos1.z};
        double mult = ab[0] / ac[0];
        if (ab[1]/mult == ac[1] && ab[2]/mult == ac[2])
            return null;
        return new double[]{ab[1] * ac[2] - ab[2] * ac[1], ab[2] * ac[0] - ab[0] * ac[2], ab[0] * ac[1] - ab[1] * ac[0]};
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
