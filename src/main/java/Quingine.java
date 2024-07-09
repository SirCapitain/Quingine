import quingine.render.sim.cam.Quamera;
import quingine.render.sim.env.Quworld;
import quingine.physics.entity.qysics.particle.Quarticle;
import quingine.physics.entity.qysics.particle.spring.AnchoredSpring;
import quingine.render.sim.env.obj.Quable;
import quingine.render.sim.env.obj.Quobject;
import quingine.render.sim.env.obj.prism.Qube;
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
//        world.setMultiThread(5);

        picture.setDebugColor(Color.red);
        picture.getQuamera().setDebugColor(Color.red);
        picture.setBackgroundColor(Color.BLACK);
        window.setFps(0);

        picture.setPercentResolution(.4);

        window.setTitle("Quingine 24.7.8");

        world.load("15x15Checkers.quworld");

        picture.getQuamera().setPos(0,0,6.25);

        AtomicBoolean down = new AtomicBoolean(false);

        double speed = .25;
        double rotSpeed = .02;

        Quamera cam = new Quamera();
        cam.setResolution(344, 326);
        cam.setPos(1.762, 1, 1);
        cam.setRotation(.74,-.32,0);

//        world.addQuworldTickListener((tickSpeed, currentTick) -> {
//            if (!down.get() && window.isKeyDown(KeyEvent.VK_SPACE)) {
//                world.getPlayer().hit(10);
//                down.set(true);
//            }
//            if (!down.get() && window.isKeyDown(KeyEvent.VK_B)) {
//                world.getPlayer().hit(250);
//                down.set(true);
//            }
//            if (!window.isKeyDown(KeyEvent.VK_SPACE))
//                down.set(false);
//            if (!window.isKeyDown(KeyEvent.VK_B))
//                down.set(false);
//            if (window.isKeyDown(KeyEvent.VK_ALT))
//                world.getPlayer().hit(7);
//            if (window.isKeyDown(KeyEvent.VK_C)) {
//                cam.setPos(world.getPlayer().getQuamera().getPos());
//                cam.setRotation(world.getPlayer().getQuamera().getYaw(), world.getPlayer().getQuamera().getPitch(), world.getPlayer().getQuamera().getRoll());
//                File outputFile = new File("image.png");
//                try {
//                    ImageIO.write(cam.getNewPicture(picture), "png", outputFile);
//                } catch (IOException e) {
//                }
//            }
//        });

        while(true) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
            picture.getQuamera().updateMovement(speed, rotSpeed, window);
        }
    }
}
