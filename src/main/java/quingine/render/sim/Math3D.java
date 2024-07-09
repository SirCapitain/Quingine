package quingine.render.sim;

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
        double x = pos.x - origin.x;
        double y = pos.y - origin.y;
        double z = pos.z - origin.z;
        theta *= .5;
        double a = Math.cos(theta);
        vx *= Math.sin(theta);
        vy *= Math.sin(theta);
        vz *= Math.sin(theta);
        double[][] q =
                       {{a, -vx, -vy, -vz},
                        {vx, a, -vz, vy},
                        {vy, vz, a, -vx},
                        {vz, -vy, vx, a}};
        double[][] p =
                       {{1, -x, -y, -z},
                        {x, 1, -z, y},
                        {y, z, 1, -x},
                        {z, -y, x, 1}};
        double[][] iq =
                       {{a, vx, vy, vz},
                        {-vx, a, vz, -vy},
                        {-vy, -vz, a, vx},
                        {-vz, vy, -vx, a}};
        double[][] result = matrixMult(matrixMult(q, p), iq);
        pos.setPos(-result[0][1], -result[0][2], -result[0][3]);
        pos.add(origin);
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
     * Get the cross product to two vectors
     * @param vec1 first vector  (i)
     * @param vec2 second vector (j)
     * @return new quisition that is the normal vector.
     */
    public static Quisition getCrossProduct(Quisition vec1, Quisition vec2){
        return new Quisition(vec1.y*vec2.z - vec1.z*vec2.y, vec1.z*vec2.x - vec1.x*vec2.z, vec1.x*vec2.y - vec1.y*vec2.x);
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
     * Get the vector of how an object should rotate based off
     * of an impact
     * @param impactPoint where the impact is occurring
     * @param impactOrigin where the impact originates from
     * @param impactVector the direction of the impact
     * @param objectPosition where the object is currently at
     * @return new Quisition vector of the axis of rotation
     */
    public static Quisition getRotationVector(Quisition impactPoint, Quisition impactOrigin, Quisition impactVector, Quisition objectPosition){
        Quisition iO = new Quisition(impactOrigin);
        iO.subtract(objectPosition);
        Quisition iP = new Quisition(impactPoint);
        iP.subtract(objectPosition);
        return Math3D.normalize(Math3D.getCrossProduct(Math3D.normalize(iO), Math3D.normalize(iP), impactVector));
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
     * @param points list of points of the plane
     * @param x value in the 3D world
     * @param y value in the 3D world
     * @return the z value of the position.
     */
    public static double calcZ(Quisition[] points, double x, double y){
        Quisition vec = calcVector(points[0], points[1], points[2]);
        double z;
        if (vec.z == 0)
            z = points[0].z;
        else
            z = (1/vec.z)*(getDotProduct(vec, points[0]) - vec.x*x - vec.y*y);
        if (Double.isInfinite(z))
            z = points[0].z;
        return z;
    }

    /**
     * Get the plane equation of a set of points
     * @param points list of points in 3D space
     * @return a double[] in the form of ax, by, cz, d
     */
    public static double[] getPlane(Quisition[] points){
        Quisition vec = calcVector(points[0], points[1], points[2]);
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
        return new Quisition(end.x + point.x*t, end.y + point.y*t, end.z + point.z*t, end.w + point.w*t, end.getUV()[0] + (start.getUV()[0] - end.getUV()[0])*t, end.getUV()[1] + (start.getUV()[1] - end.getUV()[1])*t);
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

    public static Quisition getPlaneIntersectionPoint(Quisition[] planePoints, Quisition start, Quisition end, boolean sdfsd){
        Quisition point = getIntersectionPoint(planePoints[0], getCrossProduct(planePoints[1], planePoints[0], planePoints[2]), start, end);
        Quisition a = getCrossProduct(point, planePoints[0], planePoints[1]);
        Quisition b = getCrossProduct(point, planePoints[1], planePoints[2]);
        Quisition c = getCrossProduct(point, planePoints[2], planePoints[0]);
        double ab = getDotProduct(a, b);
        double bc = getDotProduct(b, c);
        double ac = getDotProduct(a, c);
        System.out.println(point);
        System.out.println(ab);
        System.out.println(bc);
        System.out.println(ac);
        System.out.println();
        if (Double.isInfinite(ab) || Double.isInfinite(bc) || Double.isNaN(ab) || Double.isNaN(bc) || ab < 0 || bc < 0 || ac < 0)
            return null;
        return point;
    }

    /**
     * Takes a list of points in a clips them according the plane specified.
     * @param points list of points that need to be clipped
     * @param planePos The position of the plane.
     * @param planeNormal The normal vector of the plane
     * @return new list of positions that are clipped.
     */
    public static Quisition[][]clipObject(Quisition[] points, Quisition planePos, Quisition planeNormal){
        ArrayList<Quisition> outsidePoints = new ArrayList<>();
        planeNormal = normalize(planeNormal);
        for (int i = 0; i < 3; i++)
            if (planeNormal.x * points[i].x + planeNormal.y * points[i].y + planeNormal.z * points[i].z - getDotProduct(planeNormal, planePos) <= 0)
                outsidePoints.add(points[i]);

        ArrayList<Quisition> clippedPoints = new ArrayList<>();
        ArrayList<Quisition> insidePoints = new ArrayList<>();
        for (int i = 0; i < 3; i++) {//Determine which points are intersecting the plane
            if (outsidePoints.contains(points[i]))
                continue;
            insidePoints.add(points[i]);
            for (Quisition outsidePoint : outsidePoints)
                clippedPoints.add(Math3D.getIntersectionPoint(planePos, planeNormal, points[i], outsidePoint));
        }
        //Set the points
        if (outsidePoints.size() == 1)
            return new Quisition[][]{new Quisition[]{clippedPoints.get(0), clippedPoints.get(1), insidePoints.get(0)}, new Quisition[]{clippedPoints.get(1), insidePoints.get(0), insidePoints.get(1)}};
        else if (outsidePoints.size() == 2)
            return new Quisition[][]{new Quisition[]{clippedPoints.get(0), clippedPoints.get(1), insidePoints.get(0)}};
        else if (outsidePoints.size() == 3)
            return null;
        else
            return new Quisition[][]{points.clone()};
    }

    /**
     * Get the vector of three points.
     * @param pos1 point in 3D space.
     * @param pos2 point in 3D space.
     * @param pos3 point in 3D space.
     * @return returns a vector in the form of a double[].
     */
    public static Quisition calcVector(Quisition pos1, Quisition pos2, Quisition pos3){
        return new Quisition((pos2.y - pos1.y)*(pos3.z - pos1.z) - (pos2.z - pos1.z)*(pos3.y - pos1.y), (pos2.z - pos1.z)*(pos3.x - pos1.x) - (pos2.x - pos1.x)*(pos3.z - pos1.z), (pos2.x - pos1.x)*(pos3.y - pos1.y) - (pos2.y - pos1.y)*(pos3.x - pos1.x));
    }

    /**
     * Calculate the separating velocity of two particles colliding
     * @param velocityA vector of first velocity
     * @param velocityB vector of second velocity (can be null)
     * @param contactNormal the normal vector of contact
     * @return Quisition of velocity
     */
    public static double calcSeparatingVelocity(Quisition velocityA, Quisition velocityB, Quisition contactNormal){
        Quisition velocity = new Quisition(velocityA);
        if (velocityB != null)
            velocity.subtract(velocityB);
        return Math3D.getDotProduct(velocity, contactNormal);
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
     * Multiply two matrices together
     * @param mat1 matrix 1
     * @param mat2 matrix 2
     * @return new double[][]
     */
    public static double[][] matrixMult(double[][] mat1, double[][] mat2){
        if (mat1 == null || mat2 == null || mat1.length != mat2[0].length)
            return null;
        double[][] result = new double[mat1.length][mat2[0].length];
        for (int row = 0; row < mat1.length; row++){
            for (int col = 0; col < mat2[0].length; col++)
                for (int i = 0; i < mat1[0].length; i++)
                    result[row][col] += mat1[row][i]*mat2[i][col];
        }
        return result;
    }
}