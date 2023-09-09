package quingine.util;

import quingine.sim.cam.Quamera;
import quingine.sim.env.obj.Quobject;
import quingine.util.win.Quicture;

import java.util.ArrayList;

/**
 * A callable class that helps improve fps by splitting work
 * among other threads.
 */
public class Quread implements Runnable {

    /**
     * Creates a runnable that will draw a specified amount
     * of quobjects
     * @param pic current quicture to be drawn on
     * @param cam current camera to be used
     * @param threadNum the number assigned to the thread indicating what number it is.
     * @param totalThreads the total number of threads being used to draw quobjects
     */
    public Quread(Quicture pic, Quamera cam, int threadNum, int totalThreads){
        this.pic = pic;
        this.cam = cam;
        this.threadNum = threadNum;
        this.totalThreads = totalThreads;
    }

    private Quicture pic;
    private Quamera cam;
    private int threadNum, totalThreads;

    private boolean isDone = false;

    /**
     * Check if the quread is done or not.
     * @return true if done, false if not.
     */
    public boolean isDone(){
        return isDone;
    }

    @Override
    public void run() {
        ArrayList<Quobject> quobjects = pic.getWorld().getQuobjects();
        int start = threadNum*quobjects.size()/totalThreads;
        int end = (threadNum+1)*quobjects.size()/totalThreads;
        for (int i = start; i < end; i++)
            quobjects.get(i).paint(pic, cam);
        isDone = true;
    }
}
