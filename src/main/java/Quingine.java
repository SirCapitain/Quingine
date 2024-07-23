import quingine.render.sim.cam.Quamera;
import quingine.render.sim.env.Quworld;
import quingine.physics.entity.qysics.particle.Quarticle;
import quingine.physics.entity.qysics.particle.spring.AnchoredSpring;
import quingine.render.sim.env.light.LightSource;
import quingine.render.sim.env.obj.Quable;
import quingine.render.sim.env.obj.Quobject;
import quingine.render.sim.env.obj.prism.Qube;
import quingine.render.sim.pos.Quisition;
import quingine.render.util.win.Quicture;
import quingine.render.util.win.Quindow;

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
        world.setMultiThread(1);

        picture.setDebugColor(Color.red);
        picture.getQuamera().setDebugColor(Color.red);
        picture.setBackgroundColor(Color.BLACK);
        window.setFps(0);
        picture.setPercentResolution(.4);
        window.setTitle("Quingine 24.7.23");
        world.getPlayer().getQuamera().flashlightOn(false);
        world.getPlayer().setGravity(new Quisition());

//        world.load("15x15Checkers.quworld");

        int size = 10;

        //Board
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                Quarticle particle = new Quarticle();
                particle.setPos(i*2, -2, j*2);
                particle.setQuobject(new Quobject("sphere.obj", i, -2, i, 1));
                world.add(particle);
                particle.setGravity(new Quisition());
                particle.setMass(1000);
            }
        }

        //Light
        LightSource ls = new LightSource();
        ls.setPos(8,8,8);
        world.add(ls);

        //Particles
        Quarticle particle = new Quarticle();
        particle.setPos(8, 8, 8);
        particle.setQuobject(new Quobject("sphere.obj", 0, 0, 0, 1));
        world.add(particle);
        particle.setVelocity(new Quisition(0,0,1));
//        particle.setGravity(new Quisition());

        AtomicBoolean down = new AtomicBoolean(false);

        double speed = .25;
        double rotSpeed = .02;

        world.startWorldThread();

        world.addQuworldTickListener((tickSpeed, currentTick) -> {
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
