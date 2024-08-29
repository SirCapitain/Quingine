package quingine.render.sim.env.obj;

import quingine.render.sim.cam.Quamera;
import quingine.render.sim.pos.Quisition;
import quingine.render.util.Quaphics;
import quingine.render.util.win.Quicture;
import quingine.render.util.win.Quomponent;
import quingine.render.sim.Math3D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The class of the quobject.
 * This makes it possible to create 3D objects
 * and place them into the world!
 */

public class Quobject extends Quomponent {

    private double size = 1;

    public BufferedImage texture;
    private String textureFile;
    private String objectFile;

    private Quisition rotation = new Quisition();

    private Quisition[] points;
    private Quisition[][] faces;
    private Quisition[] tempPoints;

    private double[][] texPoints;
    private double[][][] texFaces;

    private boolean fill = true;
    private boolean outline = false;
    private boolean alwaysLit = false;

    private int fillColor = Color.white.getRGB();
    private int outlineColor = Color.black.getRGB();

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
     * @param x current position of the 3D object in space. Typically, the middle.
     * @param y current position of the 3D object in space. Typically, the middle.
     * @param z current position of the 3D object in space. Typically, the middle.
     */
    public Quobject(Quisition[] points, double x, double y, double z){
        super(x, y, z);
        setPoints(points);
    }

    public String getObjectFile(){
        return objectFile;
    }

    /**
     * Get the current size modifier of the quobject
     * @return current size
     */
    public double getSize(){
        return size;
    }

    /**
     * Initialize a quobject from a .obj file.
     * @param objectFile fileName.obj
     * @param x position of object in world
     * @param y position of object in world
     * @param z position of object in world
     * @param size multiplies how much bigger the object will be.
     */
    public Quobject(String objectFile, double x, double y, double z, double size){
        super(x, y, z);
        this.size = size;
        loadQuobjectFile(objectFile);
    }

    /**
     * Set the object file to load
     * @param objectFile fileName.obj
     */
    public void loadQuobjectFile(String objectFile){
        this.objectFile = objectFile;
        ArrayList<Quisition> points = new ArrayList<>();
        ArrayList<Double> texturePoints = new ArrayList<>();
        ArrayList<Integer> faces = new ArrayList<>();
        ArrayList<Integer> textureFaces = new ArrayList<>();
        File object =  new File(System.getProperty("user.dir") + "/src/main/resources/objects/" + objectFile);
        try {
            Scanner reader = new Scanner(object);
            while (reader.hasNext()){
                String data = reader.next();
                switch (data) {
                    case "v" ->
                            points.add(new Quisition(Double.parseDouble(reader.next()) * size + getPos().x, Double.parseDouble(reader.next()) * size + getPos().y, Double.parseDouble(reader.next()) * size + getPos().z));
                    case "vt" ->{
                        texturePoints.add(Double.parseDouble(reader.next()));
                        texturePoints.add(Double.parseDouble(reader.next()));
                    }
                    case "f" -> {
                        for (int i = 0; i < 3; i++) {
                            data = reader.next();
                            if (data.contains("/")) {
                                textureFaces.add(Integer.parseInt(data.substring(data.indexOf("/") + 1)) - 1);
                                faces.add(Integer.parseInt(data.substring(0, data.indexOf("/"))) - 1);
                            } else
                                faces.add(Integer.parseInt(data) - 1);
                        }
                    }
                }
            }
            setPoints(points);
            setFaces(faces);
            setTexturePoints(texturePoints);
            setTextureFaces(textureFaces);
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * Set the current texture of the object
     * @param file name of the file with extension.
     */
    public void setTexture(String file){
        if (file == null || file.equals("null"))
            return;
        fill(false);
        try {
            BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir") + "/src/main/resources/textures/" + file));
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            textureFile = file;
            texture = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = texture.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
        }catch (IOException io){io.printStackTrace();}
    }

    /**
     * Set the current texture of the quobject
     * @param texture bufferedImage of the texture
     */
    public void setTexture(BufferedImage texture){
        fill(false);
        this.texture = texture;
    }

    /**
     * Get the file of the texture being used
     * @return String of file location + name
     */
    public String getTextureFile(){
        return textureFile;
    }

    /**
     * Set where the points are for the quobject.
     * @param points list of points.
     */
    public void setPoints(Quisition[] points) {
        this.points = points;
        tempPoints = new Quisition[points.length];
        for (int i = 0; i < points.length; i++)
            tempPoints[i] = new Quisition(points[i]);
    }

    /**
     * Set where the points are for the quobject
     * based on an arraylist
     * @param points arraylist<Quisition>
     */
    public void setPoints(ArrayList<Quisition> points) {
        this.points = new Quisition[points.size()];
        tempPoints = new Quisition[points.size()];
        points.toArray(this.points);
        for (int i = 0; i < points.size(); i++)
            tempPoints[i] = new Quisition(points.get(i));
    }

    /**
     * Get the current list of points for the object.
     * @return current list of points.
     */
    public Quisition[] getPoints(){
        return points;
    }

    /**
     * Sets all the planes of the quobject based off of the points already set.
     * @param faces 2D-Array of integers that correspond to the points of the quobject
     */
    public void setFaces(int[][] faces){
        this.faces = new Quisition[faces.length][3];
        for (int i = 0; i < this.faces.length; i++)
            for (int j = 0; j < faces[i].length; j++)
                this.faces[i][j] = points[faces[i][j]];
    }

    /**
     * Sets all the planes of the quobject based off of the points already set.
     * @param faces 1D Arraylist of integers that correspond to the points of the quobject.
     *              Each face is 3 points. e.g. {0,4,1,5,2,1} A plane with points 0, 4, 1 is made and another with points 5, 2, 1
     */
    public void setFaces(ArrayList<Integer> faces){
        this.faces = new Quisition[faces.size()/3][3];
        for (int i = 0; i < this.faces.length; i++)
            for (int j = 0; j < this.faces[i].length; j++)
                this.faces[i][j] = points[faces.get(j+i*this.faces[i].length)];
    }

    /**
     * Get all the faces the quobject currently has.
     * @return a 2D array of Quisitions that are organized by face.
     */
    public Quisition[][] getFaces(){
        return faces;
    }

    /**
     * Set the list of UV coordinates for the quobject.
     * @param points 2D array e.g. {{u,v},{u2,v2}}
     */
    public void setTexturePoints(Double[][] points){
        texPoints = new double[points.length][2];
        for (int i = 0; i < texPoints.length/2; i++) {
            texPoints[i][0] = points[i][0];
            texPoints[i][1] = points[i][1];
        }
    }

    /**
     * Set the texture points based off an arraylist
     * @param points a 1D arraylist where it alternates u, v
     *               e.g. {u, v, u2, v2}
     */
    public void setTexturePoints(ArrayList<Double> points){
        texPoints = new double[points.size()][2];
        for (int i = 0; i < texPoints.length/2; i++) {
            texPoints[i][0] = points.get(i*2);
            texPoints[i][1] = points.get(i*2+1);
        }
    }

    /**
     * Set the what faces use what texture points.
     * @param faces 2D array of integers that correspond to
     *              the UV coordinates already set in the quobject.
     */
    public void setTextureFaces(int[][] faces){
        texFaces = new double[faces.length][3][2];
        for (int i = 0; i < texFaces.length; i++)
            for (int j = 0; j < texFaces[i].length; j++)
                texFaces[i][j] = texPoints[faces[i][j]];
    }

    /**
     * Set the what faces use what texture points.
     * @param faces 1D arrayList of integers that correspond to
     *              the UV coordinates already set in the quobject.
     *              Each face used 3 points at a time.
     */
    public void setTextureFaces(ArrayList<Integer> faces){
        texFaces = new double[faces.size()/3][3][2];
        for (int i = 0; i < texFaces.length; i++)
            for (int j = 0; j < texFaces[i].length; j++)
                texFaces[i][j] = texPoints[faces.get(j + i * this.texFaces[i].length)];
    }

    /**
     * Get the normal vector of any face of the quobject
     * @param face integer the corresponds to a face in the list.
     * @return a Quisition that is the normal vector
     */
    public Quisition getNormal(int face){
        return Math3D.getNormal(faces[face]);
    }

    /**
     * Rotate the object in 3D space. This rotates around the position of the object.
     * @param vx vector x
     * @param vy vector y
     * @param vz vector z
     * <NOTE>ALL VECTORS WHEN SQUARED AND ADDED TOGETHER SHOULD EQUAL 1!</NOTE>
     * @param theta in radians the amount you want to rotate the object by.
     */
    public synchronized void rotate(double vx, double vy, double vz, double theta){
        rotate(getPos(), vx, vy, vz, theta);
    }

    /**
     * Rotate the quobject around a specific point.
     * @param pos the point that the quobject will rotate around.
     * @param vx vector x
     * @param vy vector y
     * @param vz vector z
     * This should be a unit vector!
     * @param theta radians of rotation.
     */
    public synchronized void rotate(Quisition pos, double vx, double vy, double vz, double theta) {
        double sin = Math.sin(theta*.5);
        rotate(new Quisition(vx*sin,vy*sin,vz*sin,Math.cos(theta*.5)), pos);
    }

    /**
     * Rotate a quobject based off a quaternion.
     * @param quaternion a Quisition to represent the quaternion
     * @param pos a Quisition that represents what the quobject will rotate around
     */
    public synchronized void rotate(Quisition quaternion, Quisition pos){
        rotation.setPos(Math3D.combineQuaternions(quaternion, rotation));
        for (Quisition point : tempPoints)
            Math3D.rotate(point, pos, quaternion);
    }

    /**
     * Rotate a quobject based off a quaternion.
     * @param quaternion a Quisition to represent the quaternion
     */
    public synchronized void rotate(Quisition quaternion){
        rotate(quaternion, getPos());
    }

    /**
     * Rotate a quobject with respect to its own origin,
     * and yaw, pitch, and roll.
     * @param yaw radians of rotation on the y-axis
     * @param pitch radians of rotation on the x-axis
     * @param roll radians of rotation on the z-axis
     */
    public synchronized void rotate(double yaw, double pitch, double roll){
        rotate(Math3D.eulerToQuaternion(yaw, pitch, roll));
    }

    /**
     * Set the rotation of a quobject based off a quaternion and a point of rotation
     * @param quaternion a quisition to represent the quaternion
     * @param point a quisition to rotate around
     */
    public synchronized void setRotation(Quisition quaternion, Quisition point){
        Quisition rot = new Quisition(rotation);
        rot.w *= -1;
        rotation = new Quisition(quaternion);
        rot = Math3D.combineQuaternions(quaternion, rot);
        for (Quisition p : tempPoints)
            Math3D.rotate(p, point, rot);
    }

    /**
     * Set the rotation of the quobject based off a vector and a point of rotation
     * @param point A Quisition to rotate around
     * @param vx vector x
     * @param vy vector y
     * @param vz vector z
     * This should be a unit vector!
     * @param theta radians of rotation.
     */
    public synchronized void setRotation(Quisition point, double vx, double vy, double vz, double theta) {
        double sin = Math.sin(theta*.5);
        setRotation(new Quisition(vx*sin,vy*sin,vz*sin,Math.cos(theta*.5)), point);
    }

    /**
     * Set the rotation of the quobject based off a vector
     * @param vx vector x
     * @param vy vector y
     * @param vz vector z
     * This should be a unit vector!
     * @param theta radians of rotation.
     */
    public synchronized void setRotation(double vx, double vy, double vz, double theta) {
        double sin = Math.sin(theta*.5);
        setRotation(new Quisition(vx*sin,vy*sin,vz*sin,Math.cos(theta*.5)), getPos());
    }

    /**
     * Set the rotation of the quobject base off yaw, pitch, and roll.
     * The Euler rotation being used is YXZ
     * @param yaw radians of rotation on the y-axis
     * @param pitch radians of rotation on the x-axis
     * @param roll radians of rotation on the z-axis
     * @param point A quisition to rotate around
     */
    public synchronized void setRotation(double yaw, double pitch, double roll, Quisition point) {
        Quisition quaternion = Math3D.eulerToQuaternion(yaw, pitch, roll);
        setRotation(quaternion, point);
    }

    /**
     * Set the rotation of the quobject base off yaw, pitch, and roll.
     * The Euler rotation being used is YXZ
     * @param yaw radians of rotation on the y-axis
     * @param pitch radians of rotation on the x-axis
     * @param roll radians of rotation on the z-axis
     */
    public synchronized void setRotation(double yaw, double pitch, double roll) {
        setRotation(yaw, pitch, roll, getPos());
    }

    /**
     * Set the rotation of the quobject based off a quaternion
     * @param quaternion a Quisition to represent the quaternion
     */
    public synchronized void setRotation(Quisition quaternion){
       setRotation(quaternion, getPos());
    }

    /**
     * Get the current rotation of a quobject.
     * @return a Quaternion in the form of a Quisiition.
     */
    public Quisition getRotation(){
        return rotation;
    }

    /**
     * Get the point on a quobject where a vector intersects
     * @param origin where the vector came from
     * @param vector where the vector is going (the vector itself)
     * @return new quisition of the points (point.v = index of plane in the quobject)
     */

    public Quisition getVectorIntersectionPoint(Quisition origin, Quisition vector){
        Quisition point = new Quisition(0,0, Integer.MAX_VALUE);
        Quisition posV = new Quisition(vector);
        posV.add(origin);
        Quisition[] points;
        for (int i = 0; i < faces.length; i++) {
            points = faces[i];
            Quisition testPoint = Math3D.getPlaneIntersectionPoint(points, origin, posV);
            if (testPoint == null || Math3D.getDist(origin, testPoint) >= Math3D.getDist(origin, point))
                continue;
            point = new Quisition(testPoint);
            point.v = i;
        }
        if (point.z == Integer.MAX_VALUE || Math3D.getRadiansBetween(Math3D.getNormal(getFaces()[(int)point.v]), vector) >= Math.PI/4)
            return null;
        return point;
    }

    /**
     * Set the position of the quobject.
     * @param quisition position in 3D space.
     */
    @Override
    public synchronized void setPos(Quisition quisition){
        setPos(quisition.x, quisition.y, quisition.z);
    }

    /**
     * Set the current position of the quobject in 3-D space
     * @param x position in 3D space.
     * @param y position in 3D space.
     * @param z position in 3D space.
     */
    @Override
    public synchronized void setPos(double x, double y, double z){
        if (points == null){
            super.setPos(x, y, z);
            return;
        }
        for (Quisition point : tempPoints){
            point.subtract(getPos());
            point.add(x, y, z);
        }
        super.setPos(x,y,z);
    }

    /**
     * Change the position of the quobject by a value
     * @param x vector x
     * @param y vector y
     * @param z vector z
     */
    @Override
    public synchronized void changePosBy(double x, double y, double z){
        if (points == null){
            super.changePosBy(x, y, z);
            return;
        }
        for (Quisition point : tempPoints)
            point.add(x, y, z);
        super.changePosBy(x,y,z);
    }

    /**
     * Do you want the object to fill with color?
     * This sets all binding planes fill to true
     * @param fill true for fill false for empty
     */
    public void fill(boolean fill){
        this.fill = fill;
    }

    /**
     * Check if the object is in fill mode.
     * @return true if fill false if empty
     */
    public boolean fill(){
        return fill;
    }

    /**
     * Set the current object to outline.
     * Sets all binding planes to be in outline mode.
     * @param outline true if outline, false if not
     */
    public void outline(boolean outline){
        this.outline = outline;
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
    public void setFullColor(int color){
        fillColor = color;
    }

    /**
     * Set every face on the quobject to have the same outline color
     * @param color the color you want to outline the quobject in
     */
    public void setFullOutlineColor(int color){
        outlineColor = color;
    }

    /**
     * Sets all faces to be always lit
     * @param alwaysLit true if all are lit, false if not.
     */
    public void alwaysLit(boolean alwaysLit){
        this.alwaysLit = alwaysLit;
    }

    /**
     * Check whether or not the object is always lit.
     * @return true, always lit; false, it's not... come on man, use your head. It's simple.
     */
    public boolean alwaysLit(){
        return alwaysLit;
    }

    /**
     * Update the position of the quobject and its points.
     */
    private void updateComponents(){
    for (int i = 0; i < points.length; i++)
            points[i].setPos(tempPoints[i]);
    }

    /**
     * Check if this quobject is colliding with another
     * @param object the other quobject to test against.
     * @return true if colliding, false if not.
     */
    public boolean isColliding(Quobject object){
        Quisition normal = Math3D.calcNormalDirectionVector(getPos(), object.getPos());
        Quisition objectPoint = object.getVectorIntersectionPoint(getPos(), normal);
        normal.multiply(-1);
        Quisition thisPoint = getVectorIntersectionPoint(object.getPos(), normal);
        if (thisPoint == null || objectPoint == null)
            return false;
        if (thisPoint.getDistance(object.getPos()) <= objectPoint.getDistance(object.getPos()))
            return true;
        else
            return false;
    }

    /**
     * Reload the quobject based off the object file.
     */
    public void reload(){
        if (objectFile != null)
            loadQuobjectFile(objectFile);
        rotation = new Quisition();
    }

    /**
     * Paint the quobject.  yum
     * @param pic this current quicture that is wanting to draw this quomponent.
     * @param camera the current quamera looking at this.
     */

    @Override
    public void paint(Quicture pic, Quamera camera) {
        for (int i = 0; i < faces.length; i++) {
            double[][] texturePoints;
            if (texPoints != null && texFaces != null)
                texturePoints = texFaces[i];
            else
                texturePoints = null;
            paintPlane(pic, camera, faces[i], texturePoints);
        }
        updateComponents();
    }

    /**
     * Paint a 2D plane based off of points
     * @param pic this current quicture that is wanting to draw this quomponent.
     * @param camera the current quamera looking at this.
     * @param points the points of the plane
     * @param texturePoints the UV points of the texture.
     */
    public void paintPlane(Quicture pic, Quamera camera, Quisition[] points, double[][] texturePoints){
        int num = 0;
        for (int i = 0; i < points.length; i++) {
            points[i].lv = 0; //also reset light level
            if (points[i].getDistance(camera.getPos()) > camera.getzFar())
                num++;
        }
        if (num == points.length)
            return;

        //Lighting
        if (!alwaysLit){
            for (int i = 0; i < pic.getWorld().getLightSources().size(); i++) {
                Quisition normal = Math3D.normalize(Math3D.getCrossProduct(points[0], points[1], points[2]));
                for (int j = 0; j < points.length; j++)
                    points[j].lv += (pic.getWorld().getLightSources().get(i)).getLightLevel(points[j], normal) / pic.getWorld().getLightSources().size();
            }
        }else
            for (int i = 0; i < points.length; i++)
                points[i].lv = 1;
        //Translate Points
        Quisition[] newPoints = new Quisition[points.length];
        for (int i = 0; i < points.length; i++) {
            newPoints[i] = camera.translate(points[i]);
            if (texturePoints != null)
                newPoints[i].setUV(texturePoints[i][0], texturePoints[i][1]);
            newPoints[i].lv = points[i].lv;
        }

        //Check if face is visible to camera
        if(!camera.pointsAreVisible(newPoints))
            return;

        //Clip objects on frustum near plane
        ArrayList<Quisition[]> triangles = camera.clipZPlanes(newPoints);
        if (triangles == null)
            return;

        //Change to perspective
        for (Quisition[] pos : triangles)
            for (int j = 0; j < pos.length; j++)
                pos[j] = camera.perspective(pos[j]);

        //Clip on screen edges
        triangles = camera.clipScreenEdges(triangles);
        if (triangles == null)
            return;


        //Draw
        for (Quisition[] triangle : triangles) {
            if (texture != null)
                Quaphics.drawImageTri(triangle, camera, texture);
            if (fill && texture == null)
                Quaphics.fillTri(triangle, camera, fillColor);
            if (outline && texture == null)
                Quaphics.drawPolygon(triangle, camera, outlineColor,1);
        }
    }
}