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

    private boolean isVisible = true;
    public Quisition(double x, double y, double z){
        setPos(x,y,z);
    }
    public void setPos(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Rotate point around another.
     *
     * @param x, y, z
     * Around the point.
     */
    public void rotate(double theta, double vx, double vy, double vz, double x, double y, double z){
        double dx = 0;
        double dy = 0;
        double dz = 0;
        theta = Math.toRadians(theta/2);
//            System.out.println("Origin: (" + originX + ", " + originY + ", " + originZ + ")");
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
//            System.out.println(result);
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
//            System.out.println(result);

//        System.out.println("Final Nums: (" + dx + ", " + dy + ", " + dz + ")");
        this.x = dx + x;
        this.y = dy + y;
        this.z = dz + z;
//        System.out.println("New point: (" + this.x + ", " + this.y + ", " + this.z + ")");
    }
    public void rotate(double theta, double vx, double vy, double vz, Quisition quisition){
        rotate(theta, vx, vy, vz, quisition.x, quisition.y, quisition.z);
    }

    public void isVisible(boolean isVisible){
        this.isVisible = isVisible;
    }
    public boolean isVisible(){
        return isVisible;
    }

//    @Override
    public void paint(Graphics g) {
        if (isVisible)
            g.fillOval((int)Math.round(x), (int)Math.round(y), 10, 10);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
