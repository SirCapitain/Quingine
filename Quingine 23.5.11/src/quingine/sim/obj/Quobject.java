package quingine.sim.obj;

import quingine.sim.Math3D;
import quingine.sim.Quisition;
import quingine.sim.cam.Quamera;
import quingine.util.Quicture;
import quingine.util.Quomponent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The class of the quobject.
 * This makes it possible to create 3D objects
 * and place them into the world!
 * @author Quinn Graham
 */

public class Quobject extends Quomponent {
    /**
     * Initializing using this will default all values to 0.
     * That includes list of points and position
     */
    public Quobject(){
        super();
    }

    /**
     * A fun way to create a 3D object using points in space.
     * @param points list of all points of the 3D shape.
     * @param quisition the current position of the 3D object. This should typically be in the middle.
     */
    public Quobject(Quisition[] points, Quisition quisition){
        super(quisition);
        setPoints(points);
     }

    /**
     * The best way to create any 3D object!
     * @param points list of all points of the 3D shape.
     * @param x current position of the 3D object in space. Typically the middle.
     * @param y current position of the 3D object in space. Typically the middle.
     * @param z current position of the 3D object in space. Typically the middle.
     */
    public Quobject(Quisition[] points, double x, double y, double z){
        super(x, y, z);
        setPoints(points);
    }

    /**
     * Initialize a quobject from a .obj file.
     * !NOTE! Only have points and faces in the file.
     * @param objectFile fileName.obj
     * @param x position of object in world
     * @param y position of object in world
     * @param z position of object in world
     * @param size multiplies how much bigger the object will be.
     */
    public Quobject(String objectFile, double x, double y, double z, double size){
        super(x, y, z);
        ArrayList<Quisition> points = new ArrayList<>();
        ArrayList<Quisition> faces = new ArrayList<>();
        try {
            File object =  new File(System.getProperty("user.dir") + "/src/objects/" + objectFile);
            Scanner reader = new Scanner(object);
            int length = 0;
            int line = 0;
            while (reader.hasNextLine()) {
                reader.nextLine();
                length++;
            }
            System.out.println(length + " lines of information have been read.");
            reader.close();
            reader = new Scanner(object);
            while (reader.hasNextLine()){
                line++;
                if (line % (length/10) == 0)
                    System.out.println(objectFile + " -- " + (int)(100*(line/(double)length)) + "%");
                int start = 2;
                int end = 3;
                double[] point = new double[3];
                String dat = reader.nextLine();
                if (!dat.startsWith("v ") && !dat.startsWith("f "))
                    continue;
                for (int i = 0; i < 3; i++) {
                    while (end < dat.length() && dat.charAt(end) != ' ')
                        end++;
                    point[i] = Double.parseDouble(dat.substring(start, end));
                    start = end + 1;
                    end = start + 1;
                }
                if (dat.startsWith("v "))
                    points.add(new Quisition(point[0]*size + x, point[1]*size + y, point[2]*size + z));
                else if (dat.startsWith("f "))
                    faces.add(new Quisition(point[0], point[1], point[2]));
            }
            reader.close();
        }catch (IOException e){e.printStackTrace();}
        this.points = new Quisition[points.size()];
        points.toArray(this.points);
        for (Quisition face : faces)
            addPlane(new int[]{(int) face.x - 1, (int) face.y - 1, (int) face.z - 1});
    }

    private Quisition[] points;

    /**
     * Set where the points are for the quobject.
     * @param points list of points.
     */
    public void setPoints(Quisition[] points) {
        this.points = points;
    }

    /**
     * Get the current list of points for the object.
     * @return current list of points.
     */
    public Quisition[] getPoints(){
        return points;
    }

    /**
     * Rotate the object in 3D space. This rotates around the position of the object.
     * @param vx vector x
     * @param vy vector y
     * @param vz vector z
     * <NOTE>ALL VECTORS WHEN SQUARED AND ADDED TOGETHER SHOULD EQUAL 1!</NOTE>
     * @param theta in degrees the amount you want to rotate the object by.
     */
    public void rotate(double vx, double vy, double vz, double theta){
        for (int i = 0; i < points.length; i++)
            Math3D.rotate(points[i], getPos(), vx, vy, vz, theta);
    }

    /**
     * Rotate the quobject around a specific point.
     * @param pos the point that the quobject will rotate around.
     * @param vx vector x
     * @param vy vector y
     * @param vz vector z
     * <NOTE>ALL VECTORS WHEN SQUARED AND ADDED TOGETHER SHOULD EQUAL 1!</NOTE>
     * @param theta degrees of rotation.
     */
    public void rotate(Quisition pos, double vx, double vy, double vz, double theta) {
        for (int i = 0; i < points.length; i++)
            Math3D.rotate(points[i], pos, vx, vy, vz, theta);
        Math3D.rotate(getPos(), pos, vx, vy, vz, theta);
    }

    private ArrayList<BindingPlane> planes = new ArrayList<>();

    /**
     * Add a binding plane to the object. This will act like a face
     * for the object.
     * @param points The points you want the plane to bind to. e.g. If you want to bind a face to a cube,
     *               the list of points for one face would be {0, 1, 2, 3} (The position of that points on
     *               the stored list of points for the object)
     */
    public void addPlane(int[] points){
        Quisition[] planePoints = new Quisition[points.length];
        for (int i = 0; i < points.length; i++)
            planePoints[i] = this.points[points[i]];
        planes.add(new BindingPlane(planePoints));
    }

    /**
     * Set the position of the quobject.
     * @param quisition position in 3D space.
     */
    @Override
    public void setPos(Quisition quisition){
        if (points != null)
            for (int i = 0; i < points.length; i++)
                points[i].setPos(points[i].x + quisition.x - getPos().x, points[i].y + quisition.y - getPos().y, points[i].z + quisition.z - getPos().z);
        super.setPos(quisition);
    }

    /**
     * Get the current list of binding planes for this object.
     * @return the current list of binding planes.
     */
    public ArrayList<BindingPlane> getPlanes(){
        return planes;
    }

    /**
     * Set the color of one of the planes.
     * @param plane the position of that plane on the list of planes.
     * @param color what color you want that plane to be
     */
    public void setPlaneColor(int plane, Color color){
        planes.get(plane).color = color;
    }

    private boolean fill = true;

    /**
     * Do you want the object to fill with color?
     * This sets all binding planes fill to true
     * @param fill true for fill false for empty
     */
    public void fill(boolean fill){
        this.fill = fill;
            for (BindingPlane plane : planes)
                plane.fill(fill);
    }

    /**
     * Check if the object is in fill mode.
     * @return true if fill false if empty
     */
    public boolean fill(){
        return fill;
    }

    private boolean outline = false;
    /**
     * Set the current object to outline.
     * Sets all binding planes to be in outline mode.
     * @param outline true if outline, false if not
     */
    public void outline(boolean outline){
        this.outline = outline;
        for (BindingPlane plane : planes)
            plane.outline(outline);
    }

    /**
     * Check if the object is being outlined
     * @return true if being outlined. False if not.
     */
    public boolean outline(){
        return outline;
    }

    /**
     * Set the color of every binding plane
     * @param color color you want the quobject to be
     */
    public void setFullColor(Color color){
        for (BindingPlane plane : getPlanes())
            plane.setColor(color);
    }

    /**
     * Painting all of the binding planes.
     * @param q this current quicture that is wanting to draw this quomponent.
     */

    @Override
    public void paint(Quicture q) {
        Quisition cameraPos = q.getQuamera().getPos();
        for (int i = 0; i < planes.size(); i++)
            planes.get(i).paint(q, cameraPos);
    }
}
