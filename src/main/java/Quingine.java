import quingine.physics.entity.qysics.link.cable.Quable;
import quingine.physics.entity.qysics.link.cable.Rod;
import quingine.render.sim.cam.Quamera;
import quingine.render.sim.env.Quworld;
import quingine.physics.entity.qysics.particle.Quarticle;
import quingine.render.sim.env.light.LightSource;
import quingine.render.sim.env.obj.ExtendableQuism;
import quingine.render.sim.env.obj.Quobject;
import quingine.render.sim.env.obj.prism.Qube;
import quingine.render.sim.pos.Quisition;
import quingine.render.util.win.Quicture;
import quingine.render.util.win.Quindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;


public class Quingine {
    public static void main(String[] args) {
        Quindow window = new Quindow();
        Quicture picture = new Quicture(window);
        Quworld world = new Quworld(picture);
        window.setSize(800,500);
        world.setMultiThread(4);

        picture.setDebugColor(Color.red);
        picture.getQuamera().setDebugColor(Color.red);
        picture.setBackgroundColor(Color.BLACK);
        window.setFps(0);
        picture.setPercentResolution(.4);
        window.setTitle("Quingine 24.8.19");
        world.getPlayer().setGravity(new Quisition());

//        world.load("15x15Checkers.quworld");


        int size = 10;

        //Board
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                Quarticle particle = new Quarticle();
                particle.setPos(i*2, -2, j*2);
                particle.setQuobject(new Quobject("sphere.obj", i, 0, i, 1));
                world.add(particle);
                particle.setMass(Math.random()*10);
                particle.setRestitution(Math.random());
//                particle.isLocked(true);
            }
        }

        //Light
        LightSource ls = new LightSource();
        ls.setPos(8,0,8);
        world.add(ls);

        LightSource ls2 = new LightSource();
        ls2.setPos(0,-2,0);
        world.add(ls2);

        Qube cube = new Qube(1,0,0,0);
        world.add(cube);
        cube.alwaysLit(true);

        Qube cube2 = new Qube(1,0,0,0);
        world.add(cube2);
        cube2.alwaysLit(true);

        //GRASS
        Quobject grass = new Quobject("board.obj",0,-4,0,10);
        grass.rotate(0,0,Math.PI/2);
        grass.setTexture("grass.png");
        grass.alwaysLit(true);
        world.add(grass);

        //Fun Board
        Quobject board = new Quobject("board.obj",0,45,50,5);
        board.rotate(Math.PI/2,0,0);
        board.alwaysLit(true);
        world.add(board);

        //Camera
        Quamera boardCam = new Quamera();

        //Particles
        Quarticle particle = new Quarticle();
        particle.setPos(8, 0, 8);
        particle.setQuobject(new Quobject("sphere.obj", 0, 3, 0, 1));
        world.add(particle);
        particle.setMass(10);

        //Cable
        Quarticle particleA = new Quarticle();
        particleA.setPos(5, 0, 8);
        particleA.setQuobject(new Quobject("sphere.obj", 0, 0, 0, 1));
        world.add(particleA);
        particleA.setMass(10);

        Quarticle particleB = new Quarticle();
        particleB.setPos(5, 0, 0.9);
        particleB.setQuobject(new Quobject("sphere.obj", 0, 0, 0, 1));
        world.add(particleB);
        particleB.getQuobject().setFullColor(Color.red.getRGB());
        particleB.setMass(10);

        ExtendableQuism eq = new ExtendableQuism(1);
        eq.setFullColor(Color.blue.getRGB());
        eq.outline(true);
        eq.alwaysLit(true);

        Rod cable = new Rod(particleA, particleB);
        cable.setQuobject(eq);
        world.add(cable);

        AtomicBoolean down = new AtomicBoolean(false);

        double speed = .25;
        double rotSpeed = .02;

        world.startWorldThread();

        world.getPlayer().hasCollision(false);

        world.addQuworldTickListener((tickSpeed, currentTick) -> {
            cube.setPos(ls.getPos());
            cube2.setPos(ls2.getPos());
            if (currentTick % 7 == 0)
                board.setTexture(boardCam.getNewPicture(picture));
            if (!down.get() && window.isKeyDown(KeyEvent.VK_SPACE)) {
                world.getPlayer().hit(10);
                down.set(true);
            }
            if (!down.get() && window.isKeyDown(KeyEvent.VK_B)) {
                world.getPlayer().hit(250);
                down.set(true);
            }
            if (window.isKeyDown(KeyEvent.VK_R)) {
                particle.setPos(picture.getQuamera().getPos());
                ls.setPos(picture.getQuamera().getPos());
                particle.setVelocity(new Quisition());
            }
            if (window.isKeyDown(KeyEvent.VK_C)) {
                boardCam.setPos(picture.getQuamera().getPos());
                boardCam.setRotation(picture.getQuamera().getYaw(),picture.getQuamera().getPitch(),picture.getQuamera().getRoll());
            }

            if (!window.isKeyDown(KeyEvent.VK_SPACE))
                down.set(false);
            if (!window.isKeyDown(KeyEvent.VK_B))
                down.set(false);
            if (window.isKeyDown(KeyEvent.VK_ALT))
                world.getPlayer().hit(7);
        });

        world.getPlayer().setMass(1000);

        while(true) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
            picture.getQuamera().updateMovement(speed, rotSpeed, window);
            world.getPlayer().setPos(picture.getQuamera().getPos());
        }
    }
}
