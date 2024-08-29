import quingine.physics.entity.qysics.RigidQysic;
import quingine.physics.entity.qysics.link.cable.Rod;
import quingine.render.sim.cam.Quamera;
import quingine.render.sim.env.Quworld;
import quingine.physics.entity.qysics.particle.Quarticle;
import quingine.render.sim.env.light.LightSource;
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
        window.setTitle("Quingine 24.8.29");
        world.getPlayer().setGravity(new Quisition());

        int size = 2;

        //Board
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                Quarticle particle = new Quarticle();
                particle.setPos(i*2, -2, j*2+15);
                particle.setQuobject(new Quobject("sphere.obj", i, 0, i, 1));
                world.add(particle);
                particle.setMass(Math.random()*10);
                particle.setRestitution(Math.random());
//                particle.isLocked(true);
            }
        }

        Qube c1 = new Qube(2,0,0,15);
        world.add(c1);
        c1.setFullColor(Color.PINK.getRGB());

        //Light
        LightSource ls = new LightSource();
        ls.setPos(8,0,8);
        world.add(ls);

        LightSource ls2 = new LightSource();
        ls2.setPos(8,2,15);
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

        RigidQysic rq = new RigidQysic();
//        world.add(rq);

        AtomicBoolean down = new AtomicBoolean(false);

        double speed = .25;
        double rotSpeed = .02;

        world.startWorldThread();

        world.getPlayer().hasCollision(false);

        Quarticle p1 = new Quarticle();
        p1.setQuobject(new Quobject("sphere.obj",0,0,0,1));
        world.add(p1);

        Quarticle p2 = new Quarticle();
        p2.setQuobject(new Quobject("sphere.obj",0,0,0,1));
        world.add(p2);
        p2.setDrag(1);

        Quarticle p3 = new Quarticle();
        p3.setQuobject(new Quobject("sphereS.obj",0,0,0,1));
        world.add(p3);
        p3.setDrag(1);


        p1.isLocked(true);
        p1.setPos(0,10,15);
        p2.setPos(0,5,0);

        Rod r1 = new Rod(p1,p2);
        r1.setMaxLength(5);
        r1.setRestitution(1);
        world.add(r1);

        Rod r2 = new Rod(p2,p3);
        r2.setRestitution(1);
        r2.setMaxLength(5);
        world.add(r2);

        p3.setPos(3,10,0);

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
                ls.setPos(picture.getQuamera().getPos());
            }
            if (window.isKeyDown(KeyEvent.VK_C)) {
                boardCam.setPos(picture.getQuamera().getPos());
                boardCam.setRotation(picture.getQuamera().getYaw(), picture.getQuamera().getPitch(), picture.getQuamera().getRoll());
            }

            if (window.isKeyDown(KeyEvent.VK_I))
                c1.rotate(.1,.01,.041);

            if (window.isKeyDown(KeyEvent.VK_K))
                c1.setRotation(2,2,2);

            if (window.isKeyDown(KeyEvent.VK_L))
                c1.reload();

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
            } catch (Exception e4) {
            }
            picture.getQuamera().updateMovement(speed, rotSpeed, window);
            world.getPlayer().setPos(picture.getQuamera().getPos());
        }
    }
}
