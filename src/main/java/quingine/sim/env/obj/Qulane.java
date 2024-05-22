package quingine.sim.env.obj;

import quingine.sim.Math3D;
import quingine.sim.cam.Quamera;
import quingine.sim.env.Quworld;
import quingine.sim.pos.Quisition;
import quingine.util.Quaphics;
import quingine.util.win.Quicture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class allows you to make a binding plane that
 * attaches to a quobject to simulate a face.
 */

public class Qulane {

    private Quisition[] points;
    private Quisition[] texPoints;
    private boolean fill = true;
    private boolean outline = false;
    private Color outlineColor = Color.black;
    private Color color = Color.white;
    private boolean visible = true;
    private boolean alwaysLit = false;


    /**
     * Initializing a binding plane means that is takes the pointers
     * to the points of the object you want this to bind to.
     * @param points the points that the plane will bind to.
     */
    public Qulane(Quisition[] points){
        this.points = points;
    }

    /**
     * Get the current list of points that this plane is bind to.
     * @return the current list of points
     */
    public Quisition[] getPoints(){
        return points;
    }

    /**
     * Set the UV texture points with respect to points of the plane
     * @param texPoints list of texture points in respect to list of points of the plane
     */
    public void setTexturePoints(Quisition[] texPoints){
        this.texPoints = texPoints;
    }

    public Quisition[] getTexPoints(){
        return texPoints;
    }

    /**
     * If that plane can fill itself with color
     * @param fill true if you want it to fill. False if not.
     */
    public void fill(boolean fill){
        this.fill = fill;
    }

    /**
     * Is the current plane filling or not?
     * @return true if it is filling, false if not.
     */
    public boolean fill(){
        return fill;
    }

    /**
     * Set the current plane to outline.
     * @param outline true if outline, false if not
     */
    public void outline(boolean outline){
        this.outline = outline;
    }

    /**
     * Check if the plane is being outlined
     * @return true if being outlined. False if not.
     */
    public boolean outline(){
        return outline;
    }

    /**
     * Set the color that outlines will appear as
     * @param color Color you want.
     */
    public void setOutlineColor(Color color){
        outlineColor = color;
    }

    /**
     * Get the current outline color being used
     * @return current outline color being used.
     */
    public Color getOutlineColor(){
        return outlineColor;
    }

    /**
     * Set the current color the plane will use to fill itself.
     * That is if fill it set to true.
     * @param color what color you want.
     */
    public void setColor(Color color){
        this.color = color;
    }

    /**
     * Get the current color the plane is using.
     * @return the current color.
     */
    public Color getColor(){
        return color;
    }

    /**
     * Set a plane to be visible or not
     * @param visible true if visible, false if not
     */
    public void visible(boolean visible){
        this.visible = visible;
    }

    /**
     * Check if a plane is visible or not.
     * @return true if visible, false if not
     */
    public boolean visible(){
        return visible;
    }

    /**
     * Have a plane to be always lit or not.
     * Helps with debugging
     * @param alwaysLit true for always lit, false if not.
     */
    public void alwaysLit(boolean alwaysLit){
        this.alwaysLit = alwaysLit;
    }

    /**
     * Check if the current plane is always
     * being lit or not.
     * @return true if always lit, false if not.
     */
    public boolean alwaysLit(){
       return alwaysLit;
    }

    /**
     * Get the normal vector of the 3D plane
     * @return Quisition with the vectors.
     */
    public Quisition getNormal(){
        return Math3D.getCrossProduct(points[0], points[1], points[2]);
    }

    /**
     * Easy what to see the binding plane in string form.
     * @return a string.
     */
    @Override
    public String toString() {
        return "Points: " + Arrays.toString(points);
    }

    /**
     * The plane paints itself using the points it was given.
     * @param pic The current quicture the plane is on.
     * @param camera The current camera being used. To avoid inconsistencies when moving the camera.
     */
    public void paint(Quicture pic, Quamera camera, BufferedImage image){
        if (fill & outline)
            paint(pic, camera, image, points, texPoints, alwaysLit, color, outlineColor);
        else if (fill)
            paint(pic, camera, image, points, texPoints, alwaysLit, color, null);
        else
            paint(pic, camera, image, points, texPoints, alwaysLit, null, outlineColor);
    }


    public static void paint(Quicture pic, Quamera camera, BufferedImage image, Quisition[] points, Quisition[] texturePoints, boolean lit, Color fillColor, Color outlineColor){
        //Check point visibility
        int num = 0;
        for (int i = 0; i < points.length; i++)
            if (points[i].getDistance(camera.getPos()) > camera.getzFar())
                num++;
        if (num == points.length)
            return;

        //Lighting
        double lv = 0;
        if (!lit)
            for (int i = 0; i < pic.getWorld().getLightSources().size(); i++) {
                double light = (pic.getWorld().getLightSources().get(i)).getLightLevel(points);
                if (light > lv)
                    lv = light;
            }
        else
            lv = 1;

        //Translate Points
        Quisition[] newPoints = new Quisition[points.length];
        for (int i = 0; i < points.length; i++) {
            newPoints[i] = camera.translate(points[i]);
            if (texturePoints != null)
                newPoints[i].setUV(texturePoints[i].u, texturePoints[i].v);
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

        //Flashlight on camera.
        if (!lit) {
            double light = camera.getLightLevel(newPoints[0]);
            if (light > lv)
                lv = light;
        }

        //Draw
        for (Quisition[] triangle : triangles) {
            if (image != null)
                Quaphics.drawImageTri(triangle, camera, image);
            if (fillColor != null && image == null)
                Quaphics.fillTri(triangle, camera, fillColor, lv);
            if (outlineColor != null && image == null)
                Quaphics.drawPolygon(triangle, camera, outlineColor, lv);
        }
    }
}
