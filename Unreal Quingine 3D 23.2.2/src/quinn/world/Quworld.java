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
     */
    public void draw(){
        //Painting the objects
        for (int i = 0; i < quomponents.size(); i++)
            quomponents.get(i).draw();
    }
}