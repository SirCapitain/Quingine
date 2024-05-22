import quingine.sim.cam.Quamera;
import quingine.sim.env.Quworld;
import quingine.sim.env.entity.qysics.*;
import quingine.sim.env.entity.qysics.particle.Quarticle;
import quingine.sim.env.entity.qysics.particle.liquid.Liquid;
import quingine.sim.env.entity.qysics.particle.spring.AnchoredBungee;
import quingine.sim.env.entity.qysics.particle.spring.AnchoredSpring;
import quingine.sim.env.entity.qysics.particle.spring.LinkedBungee;
import quingine.sim.env.entity.qysics.particle.spring.LinkedSpring;
import quingine.sim.env.obj.Quable;
import quingine.sim.env.obj.Quobject;
import quingine.sim.env.obj.prism.Qube;
import quingine.sim.pos.Quisition;
import quingine.util.win.Quicture;
import quingine.util.win.Quindow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;


public class Quingine {
    public static void main(String[] args) {
        Quindow window = new Quindow();
        Quicture picture = new Quicture(window);
        Quworld world = new Quworld(picture);
        window.setSize(800,500);
        picture.getQuamera().setzNear(.001);
        picture.getQuamera().setzFar(800);
        picture.getQuamera().setFov(Math.toRadians(60));
        window.setFps(0);
        picture.getQuamera().flashlightOn(true);
        picture.getQuamera().setLightPower(1);
        world.setMultiThread(5);

        picture.setDebugColor(Color.red);
        picture.getQuamera().setDebugColor(Color.red);
        picture.setBackgroundColor(Color.BLACK);


//        picture.setResolution(16,9);
//        picture.setMaxResolutionQuality(.25);
        picture.setPercentResolution(.4);

        window.setTitle("Quingine 24.5.21");

        world.load("50x50Checkers.quworld");


        Qube cube = new Qube(1, 0, 0 ,3);
        RigidQysic cubeE = new RigidQysic();
        cubeE.setPos(0, 0, 3);
        cubeE.setQuobject(cube);
//        world.add(cubeE);
        cube.alwaysLit(true);
        cube.debugColors();



        Quobject board = new Quobject("board.obj", 0,50,20,10);
//        world.add(board);
//        RigidQysic b = new RigidQysic();
//        b.setQuobject(board);
//        world.add(b);
        board.alwaysLit(true);
        board.rotate(Math.PI/2, 0, 0);

        Quobject sphere = new Quobject("sphere.obj", 0,0,0,1);
        Quarticle particle = new Quarticle();
        particle.setQuobject(sphere);
        particle.setPos(0,0,3);
        world.add(particle);
        particle.setMass(1);

        Quable cable = new Quable(.5);

        AnchoredSpring spring = new AnchoredSpring(5,10,particle);
        world.add(spring);
        spring.setQuobject(cable);
        spring.setPos(0,5,3);
        spring.setCable(cable);

        world.startWorldThread();

        AtomicBoolean down = new AtomicBoolean(false);

        double speed = .25;
        double rotSpeed = .02;

        Quamera cam = new Quamera();
        cam.setResolution(344, 326);
        cam.setPos(1.762, 1, 1);
        cam.setRotation(.74,-.32,0);


        world.addQuworldTickListener(tickSpeed -> {

            board.setTexture(cam.getNewPicture(picture));

            if (!down.get() && window.isKeyDown(KeyEvent.VK_SPACE)) {
                world.getPlayer().hit(25);
                down.set(true);
            }
            if (!down.get() && window.isKeyDown(KeyEvent.VK_B)) {
                world.getPlayer().hit(250);
                down.set(true);
            }
            if (!window.isKeyDown(KeyEvent.VK_SPACE))
                down.set(false);
            if (!window.isKeyDown(KeyEvent.VK_B))
                down.set(false);
            if (window.isKeyDown(KeyEvent.VK_ALT))
                world.getPlayer().hit(7);
            if (window.isKeyDown(KeyEvent.VK_C)) {
                cam.setPos(world.getPlayer().getQuamera().getPos());
                cam.setRotation(world.getPlayer().getQuamera().getYaw(), world.getPlayer().getQuamera().getPitch(), world.getPlayer().getQuamera().getRoll());
                File outputFile = new File("image.png");
                try {
                    ImageIO.write(cam.getNewPicture(picture), "png", outputFile);
                }catch (IOException e){}
            }
            if (window.isKeyDown(KeyEvent.VK_V))
                spring.setPos(world.getPlayer().getQuamera().getPos());

//            picture.getQuamera().updateMovement(speed, rotSpeed, window);

        });

        while(true) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
            picture.getQuamera().updateMovement(speed, rotSpeed, window);
        }
    }
}
