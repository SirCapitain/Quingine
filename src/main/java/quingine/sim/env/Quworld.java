package quingine.sim.env;

import quingine.sim.Math3D;
import quingine.sim.cam.Quamera;
import quingine.sim.env.entity.Entity;
import quingine.sim.env.entity.Player;
import quingine.sim.env.entity.qysics.RigidQysic;
import quingine.sim.env.light.LightSource;
import quingine.sim.env.obj.Qulane;
import quingine.sim.env.obj.Quobject;
import quingine.sim.listener.QuworldTickListener;
import quingine.sim.pos.Quisition;
import quingine.util.win.Quicture;
import quingine.util.win.Quomponent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A Quworld holds quobjects light sources, and entities.
 */

public class Quworld{

    private ArrayList<LightSource> lightSources = new ArrayList<>();
    private ArrayList<Quobject> quobjects = new ArrayList<>();
    private ArrayList<Entity> entities = new ArrayList<>();

    private ArrayList<QuworldTickListener> tickListeners = new ArrayList<>();

    private int nThreads = 1;

    private int tickSpeed = 50;
    private int qysicSpeed = 50;

    private Player player;

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
        player = new Player(q.getQuamera());
        player.setQuworld(this);
    }

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
        if (quomponent instanceof Entity)
            if (!entities.contains(quomponent))
                return entities.add((Entity) quomponent);
        return false;
    }

    /**
     * Add a tick listener to the quworld
     * that runs once every tick
     * @param listener QuworldTickListener
     * @return true if successfully removed, false if nothing was removed.
     */
    public boolean addQuworldTickListener(QuworldTickListener listener){
        if (!tickListeners.contains(listener))
            return tickListeners.add(listener);
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
        if (quomponent instanceof Entity)
            return entities.remove(quomponent);
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

    /**
     * Get the currten list of entities the quworld currently has
     * @return an arraylist of entities currently in use.
     */
    public ArrayList<Entity> getEntities(){
        return entities;
    }

    /**
     * Set the current number of threads being used to render
     * quobjects. Threads do not speed up the render of one quobject
     * and its points but rather many quobjects equally split among other threads.
     * @param nThreads number of threads wanting to be used
     */
    public void setMultiThread(int nThreads){
        this.nThreads = Math.max(nThreads, 1);
    }

    /**
     * Get the current count of threads
     * being used to render quobjects
     * @return nThreads
     */
    public int getMultiThreadCount(){
        return nThreads;
    }

    /**
     * Save the current quworld as an untitled.quworld in resources/quworlds/output
     */
    public void save(){
        try {
            File file = new File(System.getProperty("user.dir") + "/src/main/resources/quworlds/output/untitled.quworld");
            FileWriter writer = new FileWriter(file);
            for (Quobject object : quobjects) {
                Quisition pos = object.getPos();
                writer.write(object.getObjectFile() + " " + pos.x + " " + pos.y + " " + pos.z + " " + object.getSize() + " " + object.getTextureFile() + "\n");
            }
            System.out.println("SAVED!");
            writer.close();
        }catch (IOException io){
            io.printStackTrace();
        }

    }

    /**
     * Load a quworld.
     * Previous quworld is unloaded.
     * File should be in the .quworld format in the quworlds folder
     * @param quworld
     */
    public void load(String quworld){
        quobjects = new ArrayList<>();
        File object =  new File(System.getProperty("user.dir") + "/src/main/resources/quworlds/" + quworld);
        try {
            Scanner reader = new Scanner(object);
            while (reader.hasNext()) {
                Quobject obj = new Quobject(reader.next(), Double.parseDouble(reader.next()), Double.parseDouble(reader.next()), Double.parseDouble(reader.next()), Double.parseDouble(reader.next()));
                obj.setTexture(reader.next());
                add(obj);
            }
        }catch (IOException io){
            io.printStackTrace();
        }
    }


    /**
     * Set the ticks per second the quworld will run at.
     * @param tickSpeed how many times per second the quworld will update
     */
    public void setTickSpeed(int tickSpeed){
        this.tickSpeed = tickSpeed;
    }

    /**
     * Get the current ticks per second of the quworld.
     * @return the current tick speed
     */
    public int getTickSpeed(){
        return tickSpeed;
    }

    /**
     * Set how many game ticks is considered
     * to be one second.
     * @param qysicSpeed how fast qysics are updated
     */
    public void setQysicSpeed(int qysicSpeed){
        this.qysicSpeed = qysicSpeed;
    }

    /**
     * Get the current tick speed of the qysics.
     * This is how many game ticks are considered
     * to be one seconds for physics.
     * @return how many qysics ticks per how many game ticks per second.
     */
    public int getQysicSpeed(){
        return qysicSpeed;
    }


    private Thread updateThread = new Thread(() -> {
        long start = System.nanoTime();
        while (true){
           if (System.nanoTime() - start >= 1000000000 / tickSpeed) {
               start = System.nanoTime();
               update();
           }
       }
    });

    /**
     * Start the update thread for the world
     */
    public void startWorldThread(){
        updateThread.start();
    }

    /**
     * Update the quworld
     */
    public void update(){
        for (QuworldTickListener listener : tickListeners)
            listener.tickUpdate(tickSpeed);
        for (Entity entity : entities)
            if (entity instanceof RigidQysic qys)
                qys.update(this);

    }

    /**
     * Set the current player to the quworld.
     * Used in interacting with the world.
     * @param player a player. I dunno how else to say this...
     */
    public void setPlayer(Player player){
        this.player = player;
    }

    /**
     * Get the player currently in use
     * @return player currently in use.
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Get a where the quamera is looking at based off of
     * all the quobjects in the world.
     * *NOTE* Does not fully work! I mean, it works... just not well with triangles... of course...
     * @return a new quisition on the face of the quobject it is looking at (point.u = index of quobject in quworld, point.v = index of plane on quobject)
     */
    public Quisition getQuameraLookingAt(){
        Quisition point = new Quisition(0,0, Integer.MAX_VALUE);
        for (int i = 0; i < entities.size(); i++) {
            Quisition testPoint = entities.get(i).getQuobject().getVectorIntersectionPoint(player.getQuamera().getPos(), player.getQuamera().getVector());
            if (testPoint.getDistance(player.getQuamera().getPos()) <= point.getDistance(player.getQuamera().getPos())) {
                point = new Quisition(testPoint);
                point.u = i;
            }
        }
        if (point.equals(new Quisition(0,0, Integer.MAX_VALUE)))
            return null;
        return point;
    }

    public void paint(Quicture pic, Quamera cam){
        int numThreads = nThreads;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        if (numThreads > 1) { //Initialize each new thread
            for (int thread = 0; thread < numThreads; thread++) {
                int threadN = thread;
                executor.execute(() -> {
                    int start = threadN * quobjects.size() / numThreads;
                    int end = (threadN + 1) * quobjects.size() / numThreads;
                    for (int i = start; i < end; i++) { //Draw only the quobjects assign to it.
                        quobjects.get(i).paint(pic, cam);
                    }
                });
            }
            executor.shutdown(); //Shutdown
            try {
                executor.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        } else //If nThreads = 1
            for (int i = 0; i < quobjects.size(); i++)
                quobjects.get(i).paint(pic, cam);
        for (LightSource ls : lightSources)
            ls.paint(pic, cam);
        //Paint entities outside of threads
        for (int i = 0; i < entities.size(); i++)
            entities.get(i).paint(pic, cam);
        }

    }