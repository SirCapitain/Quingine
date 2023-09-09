package quingine.sim.env;

import quingine.sim.cam.Quamera;
import quingine.sim.env.light.LightSource;
import quingine.sim.env.obj.Quobject;
import quingine.util.Quread;
import quingine.util.win.Quicture;
import quingine.util.win.Quomponent;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A Quworld holds quobjects and light sources.
 */

public class Quworld{
    /**
     * Creating a regular quworld with no quicture
     */
    public Quworld(){}

    /**
     * Creates a quworld and automatically sets
     * the quworld in the quicture to it.
     * @param q quicture that the quworld is assigned to.
     */
    public Quworld(Quicture q){
        q.setQuworld(this);
    }

    private ArrayList<LightSource> lightSources = new ArrayList<>();
    private ArrayList<Quobject> quobjects = new ArrayList<>();

    /**
     * Add a quobject or a light source to the quworld.
     * Quomponents will be ignored
     * @param quomponent either a quobject or a light source
     * @return true if successfully added, false if failed.
     */
    public boolean add(Quomponent quomponent){
        if (quomponent instanceof Quobject)
            if (!quobjects.contains(quomponent))
                return quobjects.add((Quobject) quomponent);
        if (quomponent instanceof LightSource)
            if (!lightSources.contains(quomponent))
                return lightSources.add((LightSource) quomponent);
        return false;
    }

    /**
     * Remove a quobject or a light source from the quworld.
     * Quomponents will be ignored.
     * @param quomponent either a quobject or a light source
     * @return true if successfully removed, false if nothing was removed.
     */
    public boolean remove(Quomponent quomponent){
        if (quomponent instanceof Quobject)
            return quobjects.remove(quomponent);
        if (quomponent instanceof LightSource)
            return lightSources.remove(quomponent);
        return false;
    }

    /**
     * Get the current list of quobjects the quworld currently has.
     * @return an array list of quobjects currently in use.
     */
    public ArrayList<Quobject> getQuobjects(){
        return quobjects;
    }

    /**
     * Get the current list of light sources the quworld currently has.
     * @return an array list of light sources currently in use.
     */
    public ArrayList<LightSource> getLightSources(){
        return lightSources;
    }

    private int nThreads = 1;

    /**
     * Set the current number of threads being used to render
     * quobjects. Threads do not speed up the render of one quobject
     * and its points but rather many quobjects equally split among other threads.
     * @param nThreads number of threads wanting to be used
     */
    public void setMultiThread(int nThreads){
        this.nThreads = nThreads;
        executor = Executors.newFixedThreadPool(nThreads);
    }

    /**
     * Get the current count of threads being used to render
     * quobjects
     * @return nThreads
     */
    public int getMultiThreadCount(){
        return nThreads;
    }
    private ExecutorService executor = Executors.newFixedThreadPool(nThreads);


    /**,
     * Paints the current quobjects in the quworld.
     * Also tastes good
     * @param q the quicture to be drawn on
     * @param camera the camera to be used
     */
    public void paint(Quicture q, Quamera camera){
        if (nThreads >= 1) {
            ArrayList<Quread> threads = new ArrayList<>();
            for (int i = 0; i < nThreads; i++) {
                threads.add(new Quread(q, camera, i, nThreads));
                executor.submit(threads.get(i));
            }

            while (true) {
                int done = 0;
                for (int i = 0; i < nThreads; i++)
                    if (threads.get(i).isDone())
                        done++;
                if (done == nThreads)
                    break;
            }
        }else
            for (int i = 0; i < quobjects.size(); i++)
                quobjects.get(i).paint(q, camera);
    }
}
