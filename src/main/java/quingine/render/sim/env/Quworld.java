package quingine.render.sim.env;

import quingine.physics.entity.qysics.link.cable.Quable;
import quingine.physics.entity.qysics.link.spring.Spring;
import quingine.physics.entity.qysics.particle.Quarticle;
import quingine.render.sim.cam.Quamera;
import quingine.physics.entity.QollidableQuobject;
import quingine.physics.entity.Player;
import quingine.render.sim.env.light.LightSource;
import quingine.render.sim.env.obj.Quobject;
import quingine.render.sim.listener.QuworldTickListener;
import quingine.render.sim.pos.Quisition;
import quingine.render.util.win.Quicture;
import quingine.render.util.win.Quomponent;

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

    private volatile ArrayList<LightSource> lightSources = new ArrayList<>();
    private volatile ArrayList<Quobject> quobjects = new ArrayList<>();
    private volatile ArrayList<QollidableQuobject> qollidableQuobjects = new ArrayList<>();

    private ArrayList<QuworldTickListener> tickListeners = new ArrayList<>();

    private int nThreads = 1;

    private int tickSpeed = 40;
    private int qysicSpeed = 40;
    private int currentTick = 0;
    private int currentTickSpeed = 0;

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
        setPlayer(new Player(q.getQuamera()));
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
        if (quomponent instanceof QollidableQuobject)
            if (!qollidableQuobjects.contains(quomponent))
                return qollidableQuobjects.add((QollidableQuobject) quomponent);
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
        if (quomponent instanceof QollidableQuobject)
            return qollidableQuobjects.remove(quomponent);
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
    public ArrayList<QollidableQuobject> getQollidableQuobjects(){
        return qollidableQuobjects;
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
     * Save the current quworld as an test.quworld in resources/quworlds/output
     */
    public void save(){
        try {
            File file = new File(System.getProperty("user.dir") + "/src/main/resources/quworlds/output/untitled.quworld");
            FileWriter writer = new FileWriter(file);
            for (Quobject object : quobjects) {
                Quisition pos = object.getPos();
                Quisition rot = object.getRotation();
                writer.write("q " + object.getObjectFile() + " " + pos.x + " " + pos.y + " " + pos.z + " " + object.getSize() + " " + object.getTextureFile() + " " + rot.x + " " + rot.y + " " + rot.z + " " + rot.w + "\n");
            }
            for (QollidableQuobject object : qollidableQuobjects) {
                Quisition pos = object.getPos();
                String objectFile = "null";
                double size = 1;
                String textureFile = "null";
                if (object.getQuobject() != null) {
                    objectFile = object.getQuobject().getObjectFile();
                    size = object.getQuobject().getSize();
                    textureFile = object.getQuobject().getTextureFile();
                }
                if (object instanceof Quarticle particle)
                    writer.write("p " + objectFile + " " + pos.x + " " + pos.y + " " + pos.z + " " + size + " " + textureFile + " " + particle.getMass() + " " + particle.getRestitution() + " " + particle.getDrag() + "\n");
                else
                    writer.write("p " + objectFile + " " + pos.x + " " + pos.y + " " + pos.z + " " + size + " " + textureFile + " " + object.getMass() + "\n");
            }
            for (LightSource object : lightSources) {
                Quisition pos = object.getPos();
                writer.write("ls " + pos.x + " " + pos.y + " " + pos.z + " " + object.getPower() + "\n");
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
            String file;
            String str;
            while (reader.hasNext()) {
                str = reader.next();
                if (str.equals("q")){
                    file = reader.next();
                    if (file.equals("null"))
                        continue;
                    Quobject obj = new Quobject(file, Double.parseDouble(reader.next()), Double.parseDouble(reader.next()), Double.parseDouble(reader.next()), Double.parseDouble(reader.next()));
                    obj.setTexture(reader.next());
                    obj.alwaysLit(true);
                    obj.rotate(new Quisition(Double.parseDouble(reader.next()), Double.parseDouble(reader.next()), Double.parseDouble(reader.next()), Double.parseDouble(reader.next())));
                    add(obj);
                }
                else if (str.equals("p")){
                    file = reader.next();
                    if (file.equals("null"))
                        continue;
                    Quarticle particle = new Quarticle(file, Double.parseDouble(reader.next()), Double.parseDouble(reader.next()), Double.parseDouble(reader.next()), Double.parseDouble(reader.next()));
                    particle.getQuobject().setTexture(reader.next());
                    particle.setMass(Double.parseDouble(reader.next()));
                    particle.setRestitution(Double.parseDouble(reader.next()));
                    particle.setDrag(Double.parseDouble(reader.next()));
                    add(particle);
                }
                else if (str.equals("ls")){
                    LightSource ls = new LightSource(Double.parseDouble(reader.next()), Double.parseDouble(reader.next()), Double.parseDouble(reader.next()));
                    ls.setPower(Double.parseDouble(reader.next()));
                    add(ls);
                }
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
        long startSecond = System.nanoTime();
        while (true){
           if (System.nanoTime() - start >= 1000000000 / tickSpeed) {
               start = System.nanoTime();
               update();
               currentTick++;
               long wait = 1000/tickSpeed - (System.nanoTime() - start)/1000000;
               if (wait > 0)
                   try {
                       Thread.sleep(wait);
                   }catch (Exception e){e.printStackTrace();}
           }
            if (System.nanoTime() - startSecond >= 1000000000){
                startSecond = System.nanoTime();
                currentTickSpeed = currentTick;
                currentTick = 0;
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
        for (int i = 0; i < qollidableQuobjects.size(); i++)
            qollidableQuobjects.get(i).update(this);
        for (QuworldTickListener listener : tickListeners)
            listener.tickUpdate(tickSpeed, currentTick);

    }

    /**
     * Set the current player to the quworld.
     * Used in interacting with the world.
     * @param player a player. I dunno how else to say this...
     */
    public void setPlayer(Player player){
        this.player = player;
        if (!qollidableQuobjects.contains(player))
            qollidableQuobjects.add(player);
    }

    /**
     * Get the player currently in use
     * @return player currently in use.
     */
    public Player getPlayer(){
        return player;
    }

    public int getCurrentTickSpeed(){
        return currentTickSpeed;
    }

    /**
     * Get a where the quamera is looking at based off of
     * all the entities in the world.
     * @return a new quisition on the face of the quobject it is looking at (point.u = index of entity in quworld, point.v = index of plane on quobject)
     */
    public Quisition getQuameraLookingAt(){
        Quisition point = new Quisition(0,0, Integer.MAX_VALUE);
        for (int i = 0; i < qollidableQuobjects.size(); i++) {
            if (qollidableQuobjects.get(i).getQuobject() == null)
                continue;
            Quisition testPoint = qollidableQuobjects.get(i).getQuobject().getVectorIntersectionPoint(player.getQuamera().getPos(), player.getQuamera().getVector());
            if (testPoint != null && testPoint.getDistance(player.getQuamera().getPos()) <= point.getDistance(player.getQuamera().getPos())) {
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
                    int startO = threadN * quobjects.size() / numThreads;
                    int endO = (threadN + 1) * quobjects.size() / numThreads;
                    for (int i = startO; i < endO; i++) { //Draw only the quobjects of its cut.
                        quobjects.get(i).paint(pic, cam);
                    }
                    int startQ = threadN * qollidableQuobjects.size() / numThreads;
                    int endQ = (threadN + 1) * qollidableQuobjects.size() / numThreads;
                    for (int i = startQ; i < endQ; i++) {
                        qollidableQuobjects.get(i).paint(pic, cam);;
                    }
                });
            }
            executor.shutdown(); //Shutdown
            try {
                executor.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        } else { //If nThreads = 1
            for (int i = 0; i < quobjects.size(); i++)
                quobjects.get(i).paint(pic, cam);
            for (int i = 0; i < qollidableQuobjects.size(); i++)
                qollidableQuobjects.get(i).paint(pic, cam);
        }
        for (LightSource ls : lightSources)
            ls.paint(pic, cam);
        }

    }