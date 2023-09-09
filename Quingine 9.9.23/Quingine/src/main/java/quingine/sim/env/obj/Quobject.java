package quingine.sim.env.obj;

import quingine.sim.Math3D;
import quingine.sim.Quisition;
import quingine.sim.Texquisition;
import quingine.sim.cam.Quamera;
import quingine.util.win.Quicture;
import quingine.util.win.Quomponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The class of the quobject.
 * This makes it possible to create 3D objects
 * and place them into the world!
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
        fill(false);
        ArrayList<Quisition> points = new ArrayList<>();
        ArrayList<Texquisition> texturePoints = new ArrayList<>();
        ArrayList<Integer> faces = new ArrayList<>();
        ArrayList<Integer> textureFaces = new ArrayList<>();
        File object =  new File(System.getProperty("user.dir") + "/src/main/resources/objects/" + objectFile);
        try {
            Scanner reader = new Scanner(object);
            while (reader.hasNext()){
                String data = reader.next();
                if (data.equals("v"))
                    points.add(new Quisition(Double.parseDouble(reader.next())*size+x,Double.parseDouble(reader.next())*size+y,Double.parseDouble(reader.next())*size+z));
                else if (data.equals("vt"))
                    texturePoints.add(new Texquisition(Double.parseDouble(reader.next()),Double.parseDouble(reader.next())));
                else if (data.equals("f"))
                    for (int i = 0; i < 3; i++) {
                        data = reader.next();
                        if (data.contains("/")) {
                            textureFaces.add(Integer.parseInt(data.substring(data.indexOf("/")+1))-1);
                            faces.add(Integer.parseInt(data.substring(0,data.indexOf("/")))-1);
                        }
                        else
                            faces.add(Integer.parseInt(data)-1);
                    }
            }
            this.points = new Quisition[points.size()];
            points.toArray(this.points);
            for (int i = 0; i < faces.size() / 3; i++){
                if (!texturePoints.isEmpty())
                    addPlane(new Qulane(new Quisition[]{this.points[faces.get(i*3)], this.points[faces.get(i*3+1)], this.points[faces.get(i*3+2)]}, new Texquisition[]{texturePoints.get(textureFaces.get(i*3)), texturePoints.get(textureFaces.get(i*3+1)), texturePoints.get(textureFaces.get(i*3+2))}));
                else
                    addPlane(new Qulane(new Quisition[]{this.points[faces.get(i*3)], this.points[faces.get(i*3+1)], this.points[faces.get(i*3+2)]}));
            }

        }catch (Exception e){e.printStackTrace();}
    }

    public BufferedImage texture;

    /**
     * Set the current texture of the object
     * @param file name of the file with extension.
     */
    public void setTexture(String file){
        fill(false);
        try {
            texture = ImageIO.read(new File(System.getProperty("user.dir") + "/src/main/resources/textures/" + file));
        }catch (IOException io){io.printStackTrace();}
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
        Quisition pos = new Quisition(getPos());
        for (int i = 0; i < points.length; i++)
            Math3D.rotate(points[i], pos, vx, vy, vz, theta);
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

    private ArrayList<Qulane> planes = new ArrayList<>();

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
        planes.add(new Qulane(planePoints));
    }

    /**
     * Add an existing binding plane
     * @param plane a BindingPlane
     */
    public void addPlane(Qulane plane){
        if (!planes.contains(plane))
            planes.add(plane);
    }

    /**
     * Set the position of the quobject.
     * @param quisition position in 3D space.
     */
    @Override
    public void setPos(Quisition quisition){
        setPos(quisition.x, quisition.y, quisition.z);
    }

    /**
     * Set the current position of the quobject in 3-D space
     * @param x position in 3D space.
     * @param y position in 3D space.
     * @param z position in 3D space.
     */
    @Override
    public void setPos(double x, double y, double z){
        if (points == null)
            return;
        if (tempPoints == null) {
            tempPoints = new Quisition[points.length];
            for (int i = 0; i < points.length; i++)
                tempPoints[i] = new Quisition(points[i].x + x - getPos().x, points[i].y + y - getPos().y, points[i].z + z - getPos().z);
        }
        else
            for (int i = 0; i < tempPoints.length; i++)
                if (tempPoints[i] !=null)
                tempPoints[i].setPos(tempPoints[i].x + x - getPos().x, tempPoints[i].y + y - getPos().y, tempPoints[i].z + z - getPos().z);
        super.setPos(x, y, z);
    }

    private Quisition[] tempPoints;

    /**
     * Change the position of the quobject by a value
     * @param x vector x
     * @param y vector y
     * @param z vector z
     */
    @Override
    public void changePosBy(double x, double y, double z){
        if (points == null)
            return;
        if (tempPoints == null) {
            tempPoints = new Quisition[points.length];
            for (int i = 0; i < points.length; i++) {
                tempPoints[i] = new Quisition(points[i]);
                tempPoints[i].changeBy(x, y, z);
            }
        }else
            for (int i = 0; i < points.length; i++)
                tempPoints[i].changeBy(x, y, z);
        super.changePosBy(x, y, z);
    }

    /**
     * Updates the current position of the points of the quobject.
     */
    private void updatePos(){
        for (int i = 0; i < points.length; i++)
            if (tempPoints[i] != null)
                points[i].setPos(tempPoints[i]);
        tempPoints = null;
    }

    /**
     * Get the current list of binding planes for this object.
     * @return the current list of binding planes.
     */
    public ArrayList<Qulane> getPlanes(){
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
            for (Qulane plane : planes)
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
        for (Qulane plane : planes)
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
        for (Qulane plane : getPlanes())
            plane.setColor(color);
    }

    /**
     * Set every face on the quobject to have the same outline color
     * @param color the color you want to outline the quobject in
     */
    public void setFullOutlineColor(Color color){
        for (Qulane plane : getPlanes())
            plane.setOutlineColor(color);
    }

    /**
     * Sets all faces to be always lit
     * @param alwaysLit true if all are lit, false if not.
     */
    public void alwaysLit(boolean alwaysLit){
        for (Qulane plane : getPlanes())
            plane.alwaysLit(alwaysLit);
    }

    /**
     * Set random colors for every face on the quobject
     */
    public void randomColors(){
        for (Qulane plane : planes)
            plane.setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
    }

    /**
     * Painting all of the binding planes.
     * @param q this current quicture that is wanting to draw this quomponent.
     */

    @Override
    public void paint(Quicture q, Quamera camera) {
        if (tempPoints != null)
            updatePos();
        for (int i = 0; i < planes.size(); i++)
            planes.get(i).paint(q, camera, texture);
    }
}
