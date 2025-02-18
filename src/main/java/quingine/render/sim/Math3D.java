package quingine.render.sim;

import quingine.render.sim.pos.Quaternion;
import quingine.render.sim.pos.Quisition;

import java.util.ArrayList;

/**
 * This class makes it easy to rotate points in a 3D space
 * and calculate some 3D equations like vectors.
 */

public class Math3D {
    /**
     * This method takes in one points and rotates it around another point.
     * Quaternions are used for this rotation.
     * @param pos the position that will be rotated.
     * @param origin the position that the rotated point will rotate around.
     * @param vx vector x.
     * @param vy vector y.
     * @param vz vector z.
     * <NOTE>ALL VECTORS WHEN SQUARED AND ADDED TOGETHER SHOULD EQUAL 1!</NOTE>
     * @param theta degrees of how much you want the position to rotate.
     */

    public static void rotate(Quisition pos, Quisition origin, double vx, double vy, double vz, double theta){
        if (vx == 0 && vy == 0 && vz == 0)
            return;
        if (Double.isNaN(vx) || Double.isNaN(vy) || Double.isNaN(vz))
            return;
        double sin = Math.sin(theta*.5);
        rotate(pos, origin, new Quaternion(vx*sin, vy*sin, vz*sin, Math.cos(theta*.5)));
    }

    /**
     * Rotate a point around another with the use of a Quaternion! Wow!
     * @param point the point the is being rotated
     * @param origin the point the other point is being rotated around
     * @param quaternion t
     */
    public static void rotate(Quisition point, Quisition origin, Quaternion quaternion){
        double x = point.x - origin.x;
        double y = point.y - origin.y;
        double z = point.z - origin.z;
        if (x == 0 && y == 0 && z == 0)
            return;
        double a = quaternion.w;
        double vx = quaternion.x;
        double vy = quaternion.y;
        double vz = quaternion.z;
        double[][] q =
                {{a, -vx, -vy, -vz}};
        double[][] p =
                {{1, -x, -y, -z},
                        {x, 1, -z, y},
                        {y, z, 1, -x},
                        {z, -y, x, 1}};
        double[] qp = matrixMult(q, p)[0];
        point.setPos(qp[0]*-vx + qp[1]*-a + qp[2]*vz + qp[3]*-vy, qp[0]*-vy + qp[1]*-vz + qp[2]*-a + qp[3]*vx, qp[0]*-vz + qp[1]*vy + qp[2]*-vx + qp[3]*-a);
        point.add(origin);
    }

    /**
     * Rotate a point around the origin with the use of a Quaternion!
     * @param point the point the is being rotated
     * @param quaternion the quaternion used to make the rotation
     */
    public static void rotateOrigin(Quisition point, Quaternion quaternion){
        double x = point.x;
        double y = point.y;
        double z = point.z;
        double a = quaternion.w;
        double vx = quaternion.x;
        double vy = quaternion.y;
        double vz = quaternion.z;
        double[][] q =
                {{a, -vx, -vy, -vz}};
        double[][] p =
                {{1, -x, -y, -z},
                        {x, 1, -z, y},
                        {y, z, 1, -x},
                        {z, -y, x, 1}};
        double[] qp = matrixMult(q, p)[0];
        point.setPos(qp[0]*-vx + qp[1]*-a + qp[2]*vz + qp[3]*-vy, qp[0]*-vy + qp[1]*-vz + qp[2]*-a + qp[3]*vx, qp[0]*-vz + qp[1]*vy + qp[2]*-vx + qp[3]*-a);
    }

    /**
     * Combines two quaternion rotation methods into one.
     * Order does matter here. I mean... try rotating something
     * in real life. Order really does matter.
     * @param quat1 The first quaternion
     * @param quat2 The second quaternion
     * @return a new Quaternion combined
     */
    public static Quaternion combineQuaternions(Quaternion quat1, Quaternion quat2){
        double a1 = quat1.w;
        double vx1 = quat1.x;
        double vy1 = quat1.y;
        double vz1 = quat1.z;
        double a2 = quat2.w;
        double vx2 = quat2.x;
        double vy2 = quat2.y;
        double vz2 = quat2.z;
        return new Quaternion(a1*vx2 + vx1*a2 + vy1*vz2 - vz1*vy2, a1*vy2 - vx1*vz2 + vy1*a2 + vz1*vx2, a1*vz2 + vx1*vy2 - vy1*vx2 + vz1*a2, a1*a2 - vx1*vx2 - vy1*vy2 - vz1*vz2);
    }

    /**
     * Change the Euler rotation of yaw, pitch, and roll into a quaternion.
     * The Euler rotation method being used is YXZ.
     * @param yaw rotation around the y-axis
     * @param pitch rotation around the x-axis
     * @param roll rotation around the z-axis
     * @return a Quaternion in the form of a Quisition
     */
    public static Quaternion eulerToQuaternion(double yaw, double pitch, double roll){
        yaw *= .5;
        pitch *= .5;
        roll *= .5;
        double cy = Math.cos(yaw);
        double sy = Math.sin(yaw);
        double cp = Math.cos(pitch);
        double sp = Math.sin(pitch);
        double cr = Math.cos(roll);
        double sr = Math.sin(roll);
        double w = cy * cp * cr - sy * sp * sr;
        double x = sy * cp * cr + cy * sp * sr;
        double y = cy * sp * cr - sy * cp * sr;
        double z = cy * cp * sr + sy * sp * cr;
        if (!Double.isFinite(w))
            w = 0;
        if (!Double.isFinite(x))
            x = 0;
        if (!Double.isFinite(y))
            y = 0;
        if (!Double.isFinite(z))
            z = 0;
        return new Quaternion(y,x,z,w);//Yeah, it's meant to be in that order.

    }

    /**
     * Changes a quaternion into Euler angles.
     * The Euler rotation method being used is ZXY.
     * @param quaternion Quisition representing a quaternion
     * @return new double[] {yaw, pitch, roll} (in radians)
     */
    public static double[] quaternionToEuler(Quaternion quaternion){
        double x = quaternion.x;
        double y = quaternion.y;
        double z = quaternion.z;
        double w = quaternion.w;
        double yaw = Math.atan2(2*(w*y - z*x), 1 - 2*(y*y + x*x));
        double pitch = Math.asin(2*(z*y + w*x));
        double roll = Math.atan2(2*(w*z - y*x), 1 - 2*(x*x + z*z));
        return new double[]{yaw, pitch, roll};
    }

    /**
     * This method takes in one points and rotates it around another point.
     * Quaternions are used for this rotation.
     * Quaternions are used for this rotation.
     * @param pos the position that will be rotated.
     * @param origin the position that the rotated point will rotate around.
     * @param vec vector of rotation.
     * <NOTE>ALL VECTORS WHEN SQUARED AND ADDED TOGETHER SHOULD EQUAL 1!</NOTE>
     * @param theta degrees of how much you want the position to rotate.
     */
    public static void rotate(Quisition pos, Quisition origin, Quisition vec, double theta){
        rotate(pos, origin, vec.x, vec.y, vec.z, theta);
    }

    /**
     * Rotate a point around another point.
     * Euler's method is used here; layered: YXZ - yaw first, pitch second, roll third
     * @param pos The position that will be rotated
     * @param origin The position that pos will rotate around
     * @param yaw Degrees of rotation around the y-axis
     * @param pitch Degrees of rotation around the x-axis
     * @param roll Degrees of rotation around the z-axis
     */
    public static void rotate(Quisition pos, Quisition origin, double yaw, double pitch, double roll){
        if (yaw == 0 && pitch == 0 && roll == 0)
            return;
        double x = pos.x - origin.x;
        double y = pos.y - origin.y;
        double z = pos.z - origin.z;
        double cosB = Math.cos(yaw);
        double cosY = Math.cos(pitch);
        double cosA = Math.cos(roll);
        double sinB = Math.sin(yaw);
        double sinY = Math.sin(pitch);
        double sinA = Math.sin(roll);
        pos.x = x*(cosA*cosB-sinA*sinB*sinY) + -y*sinA*cosY + z*(cosA*sinB+sinA*sinY*cosB) + origin.x;
        pos.y = x*(sinA*cosB+sinB*cosA*sinY) + y*cosA*cosY + z*(sinA*sinB-cosA*sinY*cosB) + origin.y;
        pos.z = -x*cosY*sinB + y*sinY+ z*cosB*cosY + origin.z;
    }
    /**
     * Return a new quisition of a rotation in 3D space.
     * Quaternions are used for rotation.
     * @param pos the position that will be rotated.
     * @param origin the position that the rotated point will rotate around.
     * @param vx vector x.
     * @param vy vector y.
     * @param vz vector z.
     * <NOTE>ALL VECTORS WHEN SQUARED AND ADDED TOGETHER SHOULD EQUAL 1!</NOTE>
     * @param theta degrees of how much you want the position to rotate.
     * @return a new rotated quisition.
     */
    public static Quisition getRotate(Quisition pos, Quisition origin, double vx, double vy, double vz, double theta){
        if (theta == 0)
            return pos;
        Quisition tempPos = new Quisition(pos);
        rotate(tempPos, origin, vx, vy, vz, theta);
        return pos;
    }

    /**
     * Get a quisition returned of a rotation.
     *  Euler's method is used here; layered: YXZ - yaw first, pitch second, roll third
     * @param pos the position that will be rotated.
     * @param origin the position that the point will rotate around.
     * @param yaw Degrees of rotation around the y-axis
     * @param pitch Degrees of rotation around the x-axis
     * @param roll Degrees of rotation around the z-axis
     * @return a new rotated quisition.
     */
    public static Quisition getRotate(Quisition pos, Quisition origin, double yaw, double pitch, double roll){
        Quisition tempPos = new Quisition(pos);
        rotate(tempPos, origin, yaw, pitch, roll);
        return pos;
    }

    /**
     * Get the cross product of two vectors.
     * @param origin the point from which both vectors start
     * @param vec1 first vector
     * @param vec2 second vector
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
     * Get the cross product to two vectors
     * @param vec1 first vector
     * @param vec2 second vector
     * @return new quisition that is the normal vector.
     */
    public static Quisition getCrossProduct(Quisition vec1, Quisition vec2){
        return new Quisition(vec1.y*vec2.z - vec1.z*vec2.y, vec1.z*vec2.x - vec1.x*vec2.z, vec1.x*vec2.y - vec1.y*vec2.x);
    }

    /**
     * Get the normal vector of a set of three points
     * @param points Quintion[]{QuisitionA, QuisitionB, QuisitionC}
     * @return a normalized Quisition representing the normal vector
     */
    public static Quisition getNormal(Quisition[] points){
        return normalize(getCrossProduct(points[1], points[0], points[2]));
    }

    /**
     * Get the normal vector of a set of three points
     * @param v1 Quisition in 3D space
     * @param v2 Quisition in 3D space
     * @param v3 Quisition in 3D space
     * @return a normalized Quisition representing the normal vector
     */
    public static Quisition getNormal(Quisition v1, Quisition v2, Quisition v3){
        Quisition cross = getCrossProduct(v2, v1, v3);
        cross.normalize();
        return cross;
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

    /**
     * Get the current magnitude of a vector
     * @param pos vector being used
     * @return a double of the magnitude
     */
    public static double getMagnitude(Quisition pos){
        return Math.sqrt(pos.x*pos.x+pos.y*pos.y+pos.z*pos.z);
    }

    /**
     * Takes in a vector and makes it into a unit vector
     * @param pos vector wanted to me normalized
     * @return normalized vector
     */
    public static Quisition normalize(Quisition pos){
        Quisition normal = new Quisition(pos);
        normal.divide(getMagnitude(pos));
        if (normal.isNaN())
            return new Quisition();
        return normal;
    }

    /**
     * Get the radians between two vectors
     * @param vec1 first vector
     * @param vec2 second vector
     * @return radians between
     */
    public static double getRadiansBetween(Quisition vec1, Quisition vec2){
        double r = Math.acos(getDotProduct(vec1, vec2)/(getMagnitude(vec1)*getMagnitude(vec2)));
        if (Double.isFinite(r))
            return r;
        return 0;
    }

    /**
     * Get the center position of a triangle easily with this one method!
     * @param pos1 first point on triangle
     * @param pos2 second point on triangle
     * @param pos3 third point on triangle
     * @return a new position of the center of the triangle.
     */
    public static Quisition getCenter(Quisition pos1, Quisition pos2, Quisition pos3){
        return new Quisition((pos1.x+pos2.x+pos3.x)/3,(pos1.y+pos2.y+pos3.y)/3, (pos1.z+pos2.z+pos3.z)/3);
    }

    /**
     * Calculate the z value of a plane using a list of points
     * and an x and y.
     * @param p1 point of plane
     * @param p2 point of plane
     * @param p3 point of plane
     * @param x value in the 3D world
     * @param y value in the 3D world
     * @return the z value of the position.
     */
    public static double calcZ(Quisition p1, Quisition p2, Quisition p3, double x, double y){
        Quisition vec = calcVector(p1, p2, p3);
        double z;
        if (vec.z == 0)
            z = p1.z;
        else
            z = (1/vec.z)*(getDotProduct(vec, p1) - vec.x*x - vec.y*y);
        if (Double.isInfinite(z))
            z = p1.z;
        return z;
    }

    /**
     * Get the plane equation of a set of points
     * @param points list of points in 3D space
     * @return a double[] in the form of {ax, by, cz, d}
     */
    public static double[] getPlane(Quisition[] points){
        Quisition vec = calcVector(points[0], points[1], points[2]);
        return new double[]{vec.x, vec.y, vec.z, -getDotProduct(vec, points[0])};
    }

    /**
     * Get the light map of a set of 3 points on the xy plane
     * @param points set of points on the xy plane with individual light levels
     * @return a double[] in the form of {ax, by, cz, d}
     */
    public static double[] get2DLightMap(Quisition[] points){
        Quisition vec = new Quisition((points[1].y - points[0].y)*(points[2].data[3] - points[0].data[3]) - (points[1].data[3] - points[0].data[3])*(points[2].y - points[0].y), (points[1].data[3] - points[0].data[3])*(points[2].x - points[0].x) - (points[1].x - points[0].x)*(points[2].data[3] - points[0].data[3]), (points[1].x - points[0].x)*(points[2].y - points[0].y) - (points[1].y - points[0].y)*(points[2].x - points[0].x));
        return new double[]{vec.x, vec.y, vec.z, -getDotProduct(vec, points[0])};
    }

    /**
     * Get the intersection point of a line into a 2D plane
     * @param planePos Position of the planes
     * @param planeNormal Normal vector of the plane
     * @param start Beginning of the Line
     * @param end End of the line
     * @return A new position that is the intersection point on that 2D plane
     */
    public static Quisition getIntersectionPoint(Quisition planePos, Quisition planeNormal, Quisition start, Quisition end) {
        Quisition point = new Quisition(start);
        point.subtract(end);
        double d = getDotProduct(planeNormal, planePos);
        double t = (d - getDotProduct(planeNormal, end)) / getDotProduct(planeNormal, point);
        Quisition intPoint = new Quisition(end.x + point.x*t, end.y + point.y*t, end.z + point.z*t);
        if (end.data != null && start.data != null) {
            intPoint.data = new double[4];
            for (int i = 0; i < 4; i++)
                intPoint.data[i] = end.data[i] + (start.data[i] - end.data[i]) * t;//Same for each piece of data
        }
        return intPoint;
    }

    /**
     * Get the point of intersection of a vector into a plane with a set of points
     * Takes into account if the point is within the points.
     * @param planePoints list of 3 points in 3D space the make a triangle
     * @param start origin of line
     * @param end end of line
     * @return point of intersection on plane. else null if not within points
     */
    public static Quisition getPlaneIntersectionPoint(Quisition[] planePoints, Quisition start, Quisition end){
        Quisition point = getIntersectionPoint(planePoints[0], getCrossProduct(planePoints[1], planePoints[0], planePoints[2]), start, end);
        Quisition a = getCrossProduct(point, planePoints[0], planePoints[1]);
        Quisition b = getCrossProduct(point, planePoints[1], planePoints[2]);
        Quisition c = getCrossProduct(point, planePoints[2], planePoints[0]);
        double ab = getDotProduct(a, b);
        double bc = getDotProduct(b, c);
        double ac = getDotProduct(a, c);
        if (Double.isInfinite(ab) || Double.isInfinite(bc) || Double.isNaN(ab) || Double.isNaN(bc) || ab < 0 || bc < 0 || ac < 0)
            return null;
        return point;
    }

    /**
     * Takes a list of points in a clips them according the plane specified.
     * It will clip your points.
     * @param points list of points that need to be clipped.
     * @param planePos The position of the plane.
     * @param planeNormal The normal vector of the plane
     */
    public static void clipObject(ArrayList<Quisition> points, Quisition planePos, Quisition planeNormal){
        if (points.isEmpty())
            return;
        int totalOutside;
        Quisition out1;
        Quisition out2;
        Quisition in1;
        Quisition in2;
        int size = points.size()/3;
        int offset = 0;
        for (int j = 0; j < size; j++) {
            totalOutside = 0;
            out1 = null;
            out2 = null;
            in1 = null;
            in2 = null;
            for (int i = 0; i < 3; i++) {
                if (getDotProduct(planeNormal, points.get(i+offset)) - getDotProduct(planeNormal, planePos) <= 0) {
                    totalOutside++;
                    if (out1 == null)
                        out1 = points.get(i+offset);
                    else
                        out2 = points.get(i+offset);
                } else {
                    if (in1 == null)
                        in1 = points.get(i+offset);
                    else
                        in2 = points.get(i+offset);
                }
            }
            if (totalOutside == 0) {
                offset += 3;
                continue;
            }
            points.remove(offset);
            points.remove(offset);
            points.remove(offset);
            if (totalOutside == 2) {
                points.add(getIntersectionPoint(planePos, planeNormal, in1, out1));
                points.add(getIntersectionPoint(planePos, planeNormal, in1, out2));
                points.add(in1);
            } else if (totalOutside == 1) {
                Quisition clip1 = getIntersectionPoint(planePos, planeNormal, in1, out1);
                Quisition clip2 = getIntersectionPoint(planePos, planeNormal, in2, out1);
                points.add(clip1);
                points.add(clip2);
                points.add(in1);

                points.add(new Quisition(clip2));
                points.add(new Quisition(in1));
                points.add(in2);
            }
        }
    }

    /**
     * Get the vector of three points.
     * @param pos1 point in 3D space.
     * @param pos2 point in 3D space.
     * @param pos3 point in 3D space.
     * @return returns a vector in the form of a Quisition.
     */
    public static Quisition calcVector(Quisition pos1, Quisition pos2, Quisition pos3){
        return new Quisition((pos2.y - pos1.y)*(pos3.z - pos1.z) - (pos2.z - pos1.z)*(pos3.y - pos1.y), (pos2.z - pos1.z)*(pos3.x - pos1.x) - (pos2.x - pos1.x)*(pos3.z - pos1.z), (pos2.x - pos1.x)*(pos3.y - pos1.y) - (pos2.y - pos1.y)*(pos3.x - pos1.x));
    }

    /**
     * Get the directional vector from point B to point A
     * by subtracting point B from point A
     * @param pointA first Quisition
     * @param pointB second Quisition
     * @return new Quisition of the direction between the points
     */
    public static Quisition calcDirectionVector(Quisition pointA, Quisition pointB){
        Quisition r = new Quisition(pointA);
        r.subtract(pointB);
        return r;
    }

    /**
     * Get the directional normal vector from point B to point A
     * by subtracting point B from point A then normalizing
     * @param pointA first Quisition
     * @param pointB second Quisition
     * @return new Quisition of the directional normal between the points
     */
    public static Quisition calcNormalDirectionVector(Quisition pointA, Quisition pointB){
        Quisition r = new Quisition(pointA);
        r.subtract(pointB);
        r.normalize();
        return r;
    }

    /**
     * Multiply two matrices togetherh
     * @param mat1 matrix 1
     * @param mat2 matrix 2
     * @return new double[][]
     */
    public static double[][] matrixMult(double[][] mat1, double[][] mat2){
        if (mat1 == null || mat2 == null || mat1[0].length != mat2.length)
            return null;
        double[][] result = new double[mat1.length][mat2[0].length];
        for (int row = 0; row < mat1.length; row++){
            for (int col = 0; col < mat2[0].length; col++)
                for (int i = 0; i < mat1[0].length; i++)
                    result[row][col] += mat1[row][i] * mat2[i][col];
        }
        return result;
    }
}