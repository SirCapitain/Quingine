import quingine.sim.cam.Quamera;
import quingine.sim.env.Quworld;
import quingine.sim.env.entity.qysics.*;
import quingine.sim.env.entity.qysics.particle.Quarticle;
import quingine.sim.env.entity.qysics.particle.spring.AnchoredSpring;
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

//        world.load("15x15Checkers.quworld");


        Qube cube = new Qube(1, 0, 0 ,3);
//        RigidQysic cubeE = new RigidQysic();
//        cubeE.setPos(0, 2, 3);
//        cubeE.setQuobject(cube);
//        world.add(cubeE);
        world.add(cube);
        cube.alwaysLit(true);
        cube.debugColors();

        Qube cube2 = new Qube(3, -5, 0 ,3);
//        world.add(cube2);
        cube2.alwaysLit(true);
        cube2.debugColors();



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
        particle.setPos(0,0,8);
//        world.add(particle);
        particle.setMass(5);

        Quobject s1 = new Quobject("sphere.obj", 0,10,0,1);
        Quarticle p1 = new Quarticle();
        p1.setQuobject(s1);
        p1.setPos(-5,3,3);
//        world.add(p1);
        p1.setMass(5);

        Quobject s2 = new Quobject("sphere.obj", 0,1,0,1);
        Quarticle p2 = new Quarticle();
        p2.setQuobject(s2);
        p2.setPos(0,5,3);
        world.add(p2);
        p2.setMass(10);
//        world.setQysicSpeed(1);

        Quable cable = new Quable(.5);

        AnchoredSpring spring = new AnchoredSpring(2,15,particle);
//        world.add(spring);
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

        world.addQuworldTickListener((tickSpeed, currentTick) -> {
//            System.out.println(1.5-p2.getPos().y);
            board.setTexture(cam.getNewPicture(picture));

            if (!down.get() && window.isKeyDown(KeyEvent.VK_SPACE)) {
                world.getPlayer().hit(10);
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
                } catch (IOException e) {
                }
            }
            if (window.isKeyDown(KeyEvent.VK_V))
                spring.setPos(world.getPlayer().getQuamera().getPos());
            if (window.isKeyDown(KeyEvent.VK_R))
                p2.setPos(world.getPlayer().getQuamera().getPos());
            if (window.isKeyDown(KeyEvent.VK_I))
                p2.changePosBy(0,0.1,0);
            if (window.isKeyDown(KeyEvent.VK_K))
                p2.setPos(0,1.5,3);

//            picture.getQuamera().updateMovement(speed, rotSpeed, window);
        });

        while(true) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
            picture.getQuamera().updateMovement(speed, rotSpeed, window);
            cube2.rotate(.01,.01,.01);
        }
    }
}
