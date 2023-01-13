package quinn.world;


import quinn.win.Quicture;
import quinn.world.obj.Quisition;
import quinn.world.obj.Qulane;
import quinn.world.obj.Quobject3D;

import java.awt.*;
import java.util.ArrayList;

/**
 * I will show you the quworld!
 * A 3D world that is super cool.
 * Can also hold 2D stuff but I mean... that's not really that cool.
 * @author Quinn Graham.
 */

public class Quworld extends Quomponent{
    /**
     * Initializing the Quword is very important step to making one.
     * @param quicture the quicture that will be drawing the quworld.
     */
    public Quworld(Quicture quicture){
        quicture.add(this);
    }

    /**
     * List of quomponents that they quword is holding and has to draw.
     */
    private ArrayList<Quomponent> quomponents = new ArrayList<>();

    /**
     * Add any quomponent you want to the quworld!
     * @param quomponent you want added and to be draw.
     * @return the quomponent added.
     */
    public Quomponent add(Quomponent quomponent){
        if (!quomponents.contains(quomponent)) {
            quomponent.setQuicture(getQuicture());
            quomponent.setQuworld(this);
            quomponents.add(quomponent);
        }
        return quomponent;
    }

    /**
     * Don't like that quomonent? Well just remove!
     * @param quomponent you hate.
     * @return the quomponent you still hate.
     */
    public Quomponent remove(Quomponent quomponent){
        quomponents.remove(quomponent);
        return quomponent;
    }
    public ArrayList<Quomponent> getQuomponents() {
        return quomponents;
    }
    /**
     * The camera is build right into the Quworld.
     */
    private Quisition camera = new Quisition(0,0,0);

    /**
     * Position you want to camera to be located at.
     * @param x coordinate of camera.
     * @param y coordinate of camera.
     * @param z coordinate of camera.
     */
    public void setCameraPos(double x, double y, double z){
        setCameraPos(new Quisition(x,y,z));
    }
    public void setCameraPos(Quisition quisition){
        camera = quisition;
    }

    /**
     * Change the camera position by a certain amount.
     * @param x change by
     * @param y change by
     * @param z change by
     */
    public void changeCameraPos(double x, double y, double z){
        setCameraPos(camera.x + x, camera.y + y, camera.z + z);
    }
    public void changeCameraPos(Quisition quisition){
        setCameraPos(camera.x + quisition.x, camera.y + quisition.y, camera.z + quisition.z);
    }
    public Quisition getCameraPos(){
        return camera;
    }

    /**
     * FOV (Field of View)
     * Helps you see more or less.
     * Your choice.
     * Low FOV -- Bigger Objects
     * High FOV -- Smaller Objects
     */
    private int fov = 60;
    /**
     * Set it!
     * @param fov you want to experience
     */
    public void setFov(int fov){
        this.fov = fov;
    }
    public int getFov(){
        return fov;
    }

    /**
     * All that you added to the quworld will be painted here.
     * Watch the magic happen.
     * @param g graphics from Quicture
     */
    public void paint(Graphics g){
        for (int i = 0; i < quomponents.size(); i++)
            if (quomponents.get(i) instanceof Quobject3D){
                Quobject3D quobject = (Quobject3D) quomponents.get(i);
                //Order the objects due to the z.
                for (int j = 0; j < quobject.getPlanes().length-1; j++){
                    if (quobject.getPlanes()[j].getPos().z > quobject.getPlanes()[j+1].getPos().z) {
                        Qulane temp = quobject.getPlanes()[j];
                        quobject.getPlanes()[j] = quobject.getPlanes()[j + 1];
                        quobject.getPlanes()[j + 1] = temp;
                        i = 0;
                    }
                }
            }
        //Painting the objects
        for (int i = 0; i < quomponents.size(); i++)
            quomponents.get(i).paint(g);
    }
}