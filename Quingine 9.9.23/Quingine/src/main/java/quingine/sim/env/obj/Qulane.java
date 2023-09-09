package quingine.sim.env.obj;

import quingine.sim.Quisition;
import quingine.sim.Texquisition;
import quingine.sim.cam.Quamera;
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

    /**
     * Initializing a binding plane means that is takes the pointers
     * to the points of the object you want this to bind to.
     * @param points the points that the plane will bind to.
     */
    public Qulane(Quisition[] points){
        this.points = points;
    }

    /**
     * Create a binding plane that has points in 3D space and has texture points.
     * The index of the point and texture point means that they are bound together.
     * @param points the quisitions to bind to
     * @param texturePoints the texture points to bind to.
     */
    public Qulane(Quisition[] points, Texquisition[] texturePoints){
        this.points = points;
        setTexPoints(texturePoints);
    }

    private Quisition[] points;

    /**
     * Get the current list of points that this plane is bind to.
     * @return the current list of points
     */
    public Quisition[] getPoints(){
        return points;
    }

    private Texquisition[] texPoints = null;

    /**
     * Set the current UV mapping of the texture points.
     * @param texPoints A texquisition of the UV.
     */
    public void setTexPoints(Texquisition[] texPoints){
        this.texPoints = texPoints;
    }

    /**
     * Get the current texture points being used by
     * the plane
     * @return current Texqisitions being used.
     */
    public Texquisition[] getTexPoints(){
        return texPoints;
    }

    private boolean fill = true;

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


    private boolean outline = false;
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

    private Color outlineColor = Color.black;

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

    Color color = Color.white;

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

    private boolean visible = true;

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

    private boolean alwaysLit = false;

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
     * Easy what to see the binding plane in string form.
     * @return a string.
     */
    @Override
    public String toString() {
        return "Points: " + Arrays.toString(points) + "\tTexture points: " + Arrays.toString(texPoints);
    }

    /**
     * The plane paints itself using the points it was given.
     * @param q The current quicture the plane is on.
     * @param camera The current camera being used. To avoid inconsistencies when moving the camera.
     */
    public void paint(Quicture q, Quamera camera, BufferedImage image){
        if (!visible)
            return;

        //Check point visibility
        int num = 0;
        for (int i = 0; i < points.length; i++)
            if (points[i].getDistance(camera.getPos()) > camera.getzFar())
                num++;
        if (num == points.length)
            return;

        //Lighting
        double lv = 0;
        if (!alwaysLit)
            for (int i = 0; i < q.getWorld().getLightSources().size(); i++) {
                double light = (q.getWorld().getLightSources().get(i)).getLightLevel(points);
                if (light > lv)
                    lv = light;
            }
        else
            lv = 1;

        //Translate Points
        Quisition[] newPoints = new Quisition[points.length];
        for (int i = 0; i < points.length; i++)
            newPoints[i] = camera.translate(points[i]);

        //Check if face is visible to camera
        if(!camera.pointsAreVisible(newPoints))
            return;

        //Bind texquisitions and quisitions
        if (texPoints != null)
            for (int i = 0; i < newPoints.length; i++)
                newPoints[i].t = texPoints[i];

        //Clip objects on frustum near plane
        ArrayList<Quisition[]> triangles = camera.clipZPlanes(newPoints);
        if (triangles == null)
            return;

        //Change to perspective
        for (int i = 0; i < triangles.size(); i++)
            for (int j = 0; j < triangles.get(i).length; j++)
                triangles.get(i)[j] = camera.perspective(q, triangles.get(i)[j]);


        //Clip on screen edges
        triangles = camera.clipScreenEdges(triangles, q);
        if (triangles == null)
            return;

        //Flashlight on camera.
        if (!alwaysLit) {
            double light = camera.getLightLevel(newPoints[0]);
            if (light > lv)
                lv = light;
        }

        //Draw
        for (int i = 0; i < triangles.size(); i++){
            if (fill)
                Quaphics.fillTri(triangles.get(i), q, color, lv);
            if (image != null)
                Quaphics.drawImageTri(triangles.get(i), q, image, lv);
            if (outline)
                Quaphics.drawPolygon(triangles.get(i), q, outlineColor, lv);
        }
    }
}
