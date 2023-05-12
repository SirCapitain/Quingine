package quingine.sim;

import java.util.ArrayList;

/**
 * This class makes it easy to rotate points in a 3D space
 * and calculate some 3D equations like vectors.
 * @author Quinn Graham
 */

public class Math3D {
    /**
     * This methods takes in one points and rotates it around another point.
     * Quaternions are used for this rotation.
     * @param pos1 the position that will be rotated.
     * @param pos2 the position that the rotated point will rotate around.
     * @param vx vector x.
     * @param vy vector y.
     * @param vz vector z.
     * <NOTE>ALL VECTORS WHEN SQUARED AND ADDED TOGETHER SHOULD EQUAL 1!</NOTE>
     * @param theta degrees of how much you want the position to rotate.
     */
    public static void rotate(Quisition pos1, Quisition pos2, double vx, double vy, double vz, double theta){
        if (theta == 0)
            return;
        double dx = 0;
        double dy = 0;
        double dz = 0;
        theta = Math.toRadians(theta*.5);
        Quaternion[] q1 = {new Quaternion(Math.cos(theta)), new Quaternion(Math.sin(theta) * vx, "i"), new Quaternion(Math.sin(theta) * vy, "j"), new Quaternion(Math.sin(theta) * vz, "k")};
        Quaternion[] p = {new Quaternion(pos1.x - pos2.x, "i"), new Quaternion(pos1.y - pos2.y, "j"), new Quaternion(pos1.z - pos2.z, "k")};
        Quaternion[] q2 = {new Quaternion(Math.cos(-theta)), new Quaternion(Math.sin(-theta) * vx, "i"), new Quaternion(Math.sin(-theta) * vy, "j"), new Quaternion(Math.sin(-theta) * vz, "k")};
        ArrayList<Quaternion> q1p = new ArrayList<>();
        for (Quaternion quat1 : q1)
            if (quat1.value != 0)
                for (Quaternion quat2 : p)
                    if (quat2.value != 0)
                        q1p.add( Quaternion.m(quat1, quat2));
        ArrayList<Quaternion> result = new ArrayList<>();
        for (Quaternion quat1 : q1p)
            if (quat1.value != 0)
                for (Quaternion quat2 : q2)
                    if (quat2.value != 0)
                        result.add( Quaternion.m(quat1, quat2));
        pos1.setPos(0,0,0);
        for (Quaternion quat : result){
            if (quat.type == null)
                continue;
            if (quat.type.equals("i"))
                dx += quat.value;
            else if (quat.type.equals("j"))
                dy += quat.value;
            else
                dz += quat.value;
            pos1.setPos(dx + pos2.x, dy + pos2.y, dz + pos2.z);
        }

    }

    /**
     * Rotate a point around another point.
     * Euler's method is used here; layered: YXZ - yaw first, pitch second, roll third
     * @param pos1 The position that will be rotated
     * @param pos2 The position that pos1 will rotate around
     * @param yaw Degrees of rotation around the y-axis
     * @param pitch Degrees of rotation around the x-axis
     * @param roll Degrees of rotation around the z-axis
     */
    public static void rotate(Quisition pos1, Quisition pos2, double yaw, double pitch, double roll){
        if (yaw == 0 && pitch == 0 && roll == 0)
            return;
        yaw = Math.toRadians(yaw);
        pitch = Math.toRadians(pitch);
        roll = Math.toRadians(roll);
        double x = pos1.x - pos2.x;
        double y = pos1.y - pos2.y;
        double z = pos1.z - pos2.z;
        double cosB = Math.cos(yaw);
        double cosY = Math.cos(pitch);
        double cosA = Math.cos(roll);
        double sinB = Math.sin(yaw);
        double sinY = Math.sin(pitch);
        double sinA = Math.sin(roll);
        pos1.x = x*(cosA*cosB-sinA*sinB*sinY) + -y*sinA*cosY + z*(cosA*sinB+sinA*sinY*cosB) + pos2.x;
        pos1.y = x*(sinA*cosB+sinB*cosA*sinY) + y*cosA*cosY + z*(sinA*sinB-cosA*sinY*cosB) + pos2.y;
        pos1.z = -x*cosY*sinB + y*sinY+ z*cosB*cosY + pos2.z;
    }
    /**
     * Return a new quisition of a rotation in 3D space.
     * Quaternions are used for rotation.
     * @param pos1 the position that will be rotated.
     * @param pos2 the position that the rotated point will rotate around.
     * @param vx vector x.
     * @param vy vector y.
     * @param vz vector z.
     * <NOTE>ALL VECTORS WHEN SQUARED AND ADDED TOGETHER SHOULD EQUAL 1!</NOTE>
     * @param theta degrees of how much you want the position to rotate.
     * @return a new rotated quisition.
     */
    public static Quisition getRotate(Quisition pos1, Quisition pos2, double vx, double vy, double vz, double theta){
        if (theta == 0)
            return pos1;
        Quisition pos = new Quisition(pos1);
        rotate(pos, pos2, vx, vy, vz, theta);
        return pos;
    }

    /**
     * Get a quisition returned of a rotation.
     *  Euler's method is used here; layered: YXZ - yaw first, pitch second, roll third
     * @param pos1 the position that will rotated.
     * @param pos2 the position that the point will rotate around.
     * @param yaw Degrees of rotation around the y-axis
     * @param pitch Degrees of rotation around the x-axis
     * @param roll Degrees of rotation around the z-axis
     * @return a new rotated quisition.
     */
    public static Quisition getRotate(Quisition pos1, Quisition pos2, double yaw, double pitch, double roll){
        Quisition pos = new Quisition(pos1);
        rotate(pos, pos2, yaw, pitch, roll);
        return pos;
    }

    /**
     * Get the cross product of two vectors.
     * @param origin the point from which both vectors start
     * @param vec1 first vector  (i)
     * @param vec2 second vector (j)
     * @return new quisition that is the normal vector.
     */
    public static Quisition getCrossProduct(Quisition origin, Quisition vec1, Quisition vec2){
        double x1 = vec1.x - origin.x;
        double x2 = vec2.x - origin.x;
        double y1 = vec1.y - origin.y;
        double y2 = vec2.y - origin.y;
        double z1 = vec1.z - origin.z;
        double z2 = vec2.z - origin.z;
        return new Quisition(y1*z2 - z1*y2, z1*x2 - x1*z2, x1*y2 - y1*x2);
    }

    /**
     * Get the dot product of two vectors
     * @param vec1 first vector
     * @param vec2 second vector
     * @return dot product
     */
    public static double getDotProduct(Quisition vec1, Quisition vec2){
        return vec1.x*vec2.x+vec1.y*vec2.y+vec1.z*vec2.z;
    }

    /**
     * Get the distance between two points in space
     * @param pos1 first position
     * @param pos2 second position
     * @return distance between the points.
     */
    public static double getDist(Quisition pos1, Quisition pos2){
        return Math.sqrt(Math.pow(pos2.x - pos1.x,2)+Math.pow(pos2.y - pos1.y,2)+Math.pow(pos2.z-pos1.z,2));
    }

    public static double getNorm(Quisition pos){
        return Math.sqrt(pos.x*pos.x+pos.y*pos.y+pos.z*pos.z);
    }

    public static Quisition getCenter(Quisition pos1, Quisition pos2, Quisition pos3){
        return new Quisition((pos1.x+pos2.x+pos3.x)/3,(pos1.y+pos2.y+pos3.y)/3, (pos1.z+pos2.z+pos3.z)/3);
    }

    /**
     * Calculate the z value of a plane using a list of points
     * and an x and y.
     * @param points list of points of the plane
     * @param x value in the 3D world
     * @param y value in the 3D world
     * @return the z value of the position.
     */
    public static double calcZ(Quisition[] points, double x, double y){
        double[] vec = calcVector(points[0], points[1], points[2]);
        double z;
        if (vec[2] == 0)
            z = points[0].z;
        else
            z = (1/vec[2])*(vec[0]*points[0].x + vec[1]*points[0].y + vec[2]*points[0].z - vec[0]*x - vec[1]*y);
        if (Double.isInfinite(z))
            z = points[0].z;
        return z;
    }

    /**
     * Get the vector of three points.
     * @param pos1 point in 3D space.
     * @param pos2 point in 3D space.
     * @param pos3 point in 3D space.
     * @return returns a vector in the form of a double[].
     */
    public static double[] calcVector(Quisition pos1, Quisition pos2, Quisition pos3){
        return new double[]{(pos2.y - pos1.y)*(pos3.z - pos1.z) - (pos2.z - pos1.z)*(pos3.y - pos1.y), (pos2.z - pos1.z)*(pos3.x - pos1.x) - (pos2.x - pos1.x)*(pos3.z - pos1.z), (pos2.x - pos1.x)*(pos3.y - pos1.y) - (pos2.y - pos1.y)*(pos3.x - pos1.x)};
    }
}

/**
 * The basic class of the quaternion.
 * Allows for complex numbers to be calculated.
 * @author Quinn Graham
 */
class Quaternion{
    public double value;
    public String type;

    /**
     * Creates a complex number.
     * @param value how much is in the number
     * @param type the type of complex number.
     */
    protected Quaternion(double value, String type){
        this.value = value;
        this.type = type;
    }

    /**
     * Creates a normal number
     * @param value how much is in the number.
     */
    protected Quaternion(double value){
        this.value = value;
    }

    /**
     * An easy way to multiply two quaternions.
     * Order matters!
     * @param q1 first number
     * @param q2 second number
     * @return new quaternion.
     */
    public static Quaternion m(Quaternion q1, Quaternion q2){
        if (q1.type == null || q2.type == null)
            if (q1.type != null)
                return new Quaternion(q1.value * q2.value, q1.type);
            else if (q2.type != null)
                return new Quaternion(q1.value * q2.value, q2.type);
            else
                return new Quaternion(q1.value * q2.value, null);
        if (q1.type.equals(q2.type))
            return new Quaternion(-(q1.value* q2.value), null);
        switch(q1.type){
            case "i":
                switch (q2.type){
                    case "j":
                        return new Quaternion(q1.value * q2.value, "k");
                    case"k":
                        return new Quaternion(-(q1.value * q2.value), "j");
                }
            case "j":
                switch (q2.type){
                    case "i":
                        return new Quaternion(-(q1.value * q2.value), "k");
                    case"k":
                        return new Quaternion(q1.value * q2.value, "i");
                }
            case "k":
                switch (q2.type){
                    case "i":
                        return new Quaternion(q1.value * q2.value, "j");
                    case"j":
                        return new Quaternion(-(q1.value * q2.value), "i");
                }
        }
        return null;
    }
}
