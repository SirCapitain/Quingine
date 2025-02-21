package quingine.render.sim.env.obj;

import quingine.render.sim.cam.Quamera;
import quingine.render.sim.pos.Quaternion;
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

    private Quaternion rotation = new Quaternion();
    private volatile Quaternion deltaRotation = new Quaternion();
    private volatile Quisition newPosition;

    private Quisition[] points;
    private Quisition[] faces;
    private double[] texPoints;

    private boolean fill = true;
    private boolean outline = false;
    private boolean alwaysLit = true;
    private boolean isVisible = true;

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

    /**
     * Create a new quobject based off of another
     * *NOTE* This only works with .obj files!
     * @param object the quobject you wish to copyyyyy!
     */
    public Quobject(Quobject object){
        super(object.getPos());
        if (object.getObjectFile() == null)
            return;
        if (object.getName() != null)
            setName(object.getName().concat(" - copy"));
        loadQuobjectFile(object.getObjectFile());
        setTexture(object.getTextureFile());
        setRotation(object.getRotation());
        setSize(object.getSize());
        alwaysLit(object.alwaysLit());
        isVisible(object.isVisible());
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
     * Initialize a quobject from a .obj file.
     * @param objectFile fileName.obj
     * @param pos a Quisition
     * @param size multiplies how much bigger the object will be.
     */
    public Quobject(String objectFile, Quisition pos, double size){
        super(pos);
        this.size = size;
        loadQuobjectFile(objectFile);
    }

    /**
     * Get the location of the object file the quobject pulled from.
     * @return string if file used, null if an inside job.
     */
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
     * Set the size of the quobject
     * @param size percent of normal size. 0 < size < big number
     */
    public synchronized void setSize(double size){
        if (size <= 0)
            return;
        double ratio = size / this.size;
        this.size = size;
        for (Quisition point : points) {
            point.subtract(getPos());
            point.multiply(ratio);
            point.add(getPos());
        }
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
        File object =  new File(System.getProperty("user.dir") + "/src/main/resources/objects/" + objectFile + "/" + objectFile + ".obj");
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

            //Read .mtl file
            object = new File(System.getProperty("user.dir") + "/src/main/resources/objects/" + objectFile + "/" + objectFile + ".mtl");
            try {
                reader = new Scanner(object);
                while (reader.hasNext()) {
                    String data = reader.next();
                    if (data.contains("map_Kd"))
                        setTexture(reader.next());
                }
            }catch (Exception e){System.out.println("Object has no material file! " + objectFile);}

        }catch (Exception e){
            e.printStackTrace();
            setPoints(new Quisition[0]);
            setFaces(new int[0]);
        }
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
        }catch (IOException io){
            io.printStackTrace();
            System.out.println("This texture does not exist! " + file);
        }
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
    }

    /**
     * Set where the points are for the quobject
     * based on an arraylist
     * @param points arraylist<Quisition>
     */
    public void setPoints(ArrayList<Quisition> points) {
        this.points = new Quisition[points.size()];
        points.toArray(this.points);
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
    public void setFaces(int[] faces){
        this.faces = new Quisition[faces.length];
        for (int i = 0; i < faces.length; i++)
            this.faces[i] = points[faces[i]];
    }

    /**
     * Sets all the planes of the quobject based off of the points already set.
     * @param faces 1D Arraylist of integers that correspond to the points of the quobject.
     *              Each face is 3 points. e.g. {0,4,1,5,2,1} A plane with points 0, 4, 1 is made and another with points 5, 2, 1
     */
    public void setFaces(ArrayList<Integer> faces){
        this.faces = new Quisition[faces.size()];
        for (int i = 0; i < this.faces.length; i++)
            this.faces[i] = points[faces.get(i)];
    }

    /**
     * Get all the faces the quobject currently has.
     * @return an array of Quisitions that are organized by face.
     */
    public Quisition[] getFaces(){
        return faces;
    }

    /**
     * Set the list of UV coordinates for the quobject.
     * @param points an array e.g. {u1,v1, u2,v2}
     */
    public void setTexturePoints(double[] points){
        texPoints = points.clone();
    }

    /**
     * Set the texture points based off an arraylist
     * @param points a 1D arraylist where it alternates u, v
     *               e.g. {u, v, u2, v2}
     */
    public void setTexturePoints(ArrayList<Double> points){
        texPoints = points.stream().mapToDouble(i -> i).toArray();
    }

    /**
     * Set the what faces use what texture points.
     * @param faces an array of integers that correspond to
     *              the UV coordinates already set in the quobject.
     */
    public void setTextureFaces(int[] faces){
        double[] texClone = texPoints.clone();
        texPoints = new double[faces.length];
        for (int i = 0; i < texPoints.length; i++)
            texPoints[i] = texClone[faces[i]];
    }

    /**
     * Set the what faces use what texture points.
     * @param faces 1D arrayList of integers that correspond to
     *              the UV coordinates already set in the quobject.
     *              Each face used 3 points at a time.
     */
    public void setTextureFaces(ArrayList<Integer> faces){
        double[] texClone = texPoints.clone();
        texPoints = new double[faces.size()*2];
        for (int i = 0; i < faces.size(); i++) {
            texPoints[i*2] = texClone[faces.get(i)*2];
            texPoints[i*2+1] = texClone[faces.get(i)*2+1];
        }
    }

    /**
     * Get the list of UV coordinates for a face of your choice!
     * @param face the number of the face.
     * @return an array containing UV coordinates {u1, v1, u2, v2, u3, v3}
     */
    public double[] getTexturePoints(int face){
        double[] points = new double[6];
        for (int i = 0; i < points.length/2; i++)
            points[i] = texPoints[face * 2 + i];
        return null;
    }

    /**
     * Get all the texture points of the quobject
     * @return double[] {{u1, v1, u2, v2...}
     */
    public double[] getTexturePoints() {
        return texPoints;
    }

    /**
     * Get the normal vector of any face of the quobject
     * @param face integer the corresponds to a face in the list.
     * @return a Quisition that is the normal vector
     */
    public Quisition getNormal(int face){
        int index = face*3;
        return Math3D.getNormal(faces[index], faces[index+1], faces[index+2]);
    }

    /**
     * Get the face of your dreams!
     * @param face integer corresponding to the face your want!
     * @return an array of quisitions that correspond to your face!
     */
    public Quisition[] getFace(int face){
        Quisition[] f = new Quisition[3];
        for (int i = 0; i < 3; i++)
            f[i] = new Quisition(faces[face*3 + i]);
        return f;
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
        rotate(newPosition, vx, vy, vz, theta);
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
        rotate(new Quaternion(vx*sin,vy*sin,vz*sin, Math.cos(theta*.5)), pos);
    }

    /**
     * Rotate a quobject based off a quaternion.
     * @param quaternion rotational quaternion
     * @param pos a Quisition that represents what the quobject will rotate around
     */
    public synchronized void rotate(Quaternion quaternion, Quisition pos){
        deltaRotation = Math3D.combineQuaternions(quaternion, deltaRotation);
        Math3D.rotate(newPosition, pos, quaternion);
    }

    /**
     * Rotate a quobject based off a quaternion.
     * @param quaternion the rotational quaternion
     */
    public synchronized void rotate(Quaternion quaternion){
        rotate(quaternion, newPosition);
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
     * @param quaternion the quaternion to set to
     * @param point a quisition to rotate around
     */
    public synchronized void setRotation(Quaternion quaternion, Quisition point){
        if (quaternion.isZero())
            return;
        Quaternion inverse = new Quaternion(rotation);
        inverse.multiply(-1);
        rotate(Math3D.combineQuaternions(inverse, quaternion), point);
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
        setRotation(new Quaternion(vx*sin,vy*sin,vz*sin,Math.cos(theta*.5)), point);
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
        setRotation(new Quaternion(vx*sin,vy*sin,vz*sin,Math.cos(theta*.5)),newPosition);
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
        if (yaw == 0 && pitch == 0 && roll == 0)
            return;
        Quaternion quaternion = Math3D.eulerToQuaternion(yaw, pitch, roll);
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
        setRotation(yaw, pitch, roll, newPosition);
    }

    /**
     * Set the rotation of the quobject based off a quaternion
     * @param quaternion A QUATERNION!!! ITS REAL!
     */
    public synchronized void setRotation(Quaternion quaternion){
       setRotation(quaternion,newPosition);
    }

    /**
     * Get the current rotation of a quobject.
     * @return a Quaternion in the form of a Quisiition.
     */
    public Quaternion getRotation(){
        return rotation;
    }

    /**
     * Get the point on a quobject where a vector intersects
     * @param origin where the vector came from
     * @param vector where the vector is going (the vector itself)
     * @return new quisition of the points (point.data[1] = index of plane in the quobject)
     */

    public Quisition getVectorIntersectionPoint(Quisition origin, Quisition vector){
        Quisition point = new Quisition(0,0, Integer.MAX_VALUE);
        Quisition posV = new Quisition(vector);
        posV.add(origin);
        Quisition[] points;
        for (int i = 0; i < faces.length/3; i++) {
            points = getFace(i);
            Quisition testPoint = Math3D.getPlaneIntersectionPoint(points, origin, posV);
            if (testPoint == null || Math3D.getDist(origin, testPoint) >= Math3D.getDist(origin, point))
                continue;
            point = new Quisition(testPoint);
            point.data = new double[]{0,i};
        }
        if (point.z == Integer.MAX_VALUE || Math3D.getRadiansBetween(Math3D.getNormal(getFace((int)point.data[0])), vector) >= Math.PI/4)
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
        if (newPosition == null)
            newPosition = new Quisition(x, y, z);
        else
            newPosition.setPos(x, y, z);
    }

    /**
     * Set the individual x component of the quobject in 3-D space
     * @param x position in 3D space
     */
    @Override
    public synchronized void setX(double x){
        setPos(x, getPos().y, getPos().z);
    }

    /**
     * Set the individual y component of the quobject in 3-D space
     * @param y position in 3D space
     */
    @Override
    public synchronized void setY(double y){
        setPos(getPos().x, y, getPos().z);
    }

    /**
     * Set the individual z component of the quobject in 3-D space
     * @param z position in 3D space
     */
    @Override
    public synchronized void setZ(double z){
        setPos(getPos().x, getPos().y, z);
    }

    /**
     * Change the position of the quobject by a value
     * @param x vector x
     * @param y vector y
     * @param z vector z
     */
    @Override
    public synchronized void changePosBy(double x, double y, double z){
        newPosition.changeBy(x, y, z);
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
     * @return true if fill, false if empty
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
     * Set the visibility of the quobject
     * @param isVisible true if visible, false is not
     */
    public void isVisible(boolean isVisible){
        this.isVisible = isVisible;
    }

    /**
     * Check the visibility of the quobject
     * @return true if visible, false is not
     *
     */
    public boolean isVisible(){
        return isVisible;
    }

    /**
     * Update the position of the quobject and its points.
     * It stores the rotations and position changes and after the frame
     * is rendered, makes the changes.
     */
    private synchronized void updatePoints(){
        Quaternion deltaRotation = new Quaternion(this.deltaRotation);
        Quisition newPosition = new Quisition(this.newPosition);
        Quisition deltaPos = new Quisition(newPosition);
        this.deltaRotation.reset();
        Math3D.rotateOrigin(getPos(), deltaRotation);
        deltaPos.subtract(getPos());
        for (int i = 0; i < points.length; i++) {
            Math3D.rotateOrigin(points[i], deltaRotation);
            points[i].add(deltaPos);
        }
        getPos().setPos(newPosition);
        this.newPosition.setPos(newPosition);
        rotation = Math3D.combineQuaternions(rotation, deltaRotation);
        rotation.normalize();
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
        return thisPoint.getDistance(object.getPos()) <= objectPoint.getDistance(object.getPos());
    }

    /**
     * Reload the quobject based off the object file.
     */
    public void reload(){
        if (objectFile != null)
            loadQuobjectFile(objectFile);
        rotation = new Quaternion();
    }

    /**
     * Paint the quobject.  yum
     * @param pic this current quicture that is wanting to draw this quomponent.
     * @param camera the current quamera looking at this.
     */

    @Override
    public void paint(Quicture pic, Quamera camera) {
        if (!isVisible)
            return;
        for (int i = 0; i < faces.length/3; i++)
            paintPlane(pic, camera, i);
        updatePoints();
    }

    /**
     * Paint a 2D plane based off of points
     * @param pic this current quicture that is wanting to draw this quomponent.
     * @param camera the current quamera looking at this.
     */
    public void paintPlane(Quicture pic, Quamera camera, int faceIndex){
        //Copy the points
        ArrayList<Quisition> newPoints = new ArrayList<>();

        int num = 0;
        for (int i = 0; i < 3; i++) {
            Quisition p = new Quisition(faces[faceIndex*3+i]);
            newPoints.add(p);
            if (texPoints != null)
                p.data = new double[]{1,texPoints[faceIndex*6+i*2],texPoints[faceIndex*6+i*2+1], 0};
            else
                p.data = new double[]{1, 0, 0, 0};
            if (p.getDistance(camera.getPos()) > camera.getzFar())
                num++;
        }
        if (num == 3)//If out of render distance. Return.
            return;

        //Lighting
        if (!alwaysLit){
            for (int i = 0; i < pic.getWorld().getLightSources().size(); i++) {
                Quisition normal = Math3D.getNormal(newPoints.get(1), newPoints.get(0), newPoints.get(2));
                for (Quisition p : newPoints)
                    p.data[3] += (pic.getWorld().getLightSources().get(i)).getLightLevel(p, normal) / pic.getWorld().getLightSources().size();//Yeah... this makes no sense. Light-wise that is.
            }
        }else
            for (Quisition p : newPoints)//If always lit, set light level to 1
                p.data[3] = 1;

        //Translate Points
        for (Quisition p : newPoints)
            camera.translate(p);

        //Check if face is visible to camera
        if(!camera.pointsAreVisible(newPoints))
            return;

        //Clip objects on frustum near plane
        camera.clipZPlanes(newPoints);
        if (newPoints.isEmpty())
            return;

        //Change to perspective
        for (Quisition newPoint : newPoints)
            camera.perspective(newPoint);

        //Clip on screen edges
        camera.clipScreenEdges(newPoints);
        if (newPoints.isEmpty())
            return;

        //Draw
        for (int i = 0; i < newPoints.size()/3; i++) {
            if (texture != null)
                Quaphics.drawImageTri(newPoints.get(i*3),newPoints.get(i*3+1),newPoints.get(i*3+2), camera, texture);
            if (fill && texture == null)
                Quaphics.fillTri(newPoints.get(i*3),newPoints.get(i*3+1),newPoints.get(i*3+2), camera, fillColor);
            if (outline && texture == null)
                Quaphics.drawPolygon(newPoints.get(i*3), newPoints.get(i*3+1), newPoints.get(i*3+2), camera, outlineColor,-0.01);
        }
    }
}