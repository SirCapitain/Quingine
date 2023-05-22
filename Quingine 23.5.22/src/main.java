import quingine.sim.Math3D;
import quingine.sim.Quisition;
import quingine.sim.obj.BindingPlane;
import quingine.sim.obj.Quobject;
import quingine.sim.obj.prism.Qube;
import quingine.sim.obj.prism.QuectangularQuism;
import quingine.util.Quicture;
import quingine.util.Quindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;


public class main {
    public static void main(String[] args) {
        Quindow window = new Quindow();
        Quicture quicture = new Quicture(window);
        window.setSize(600,400);
        quicture.getQuamera().setzNear(.001);
        quicture.getQuamera().setzFar(400);
        window.setFps(30);
        quicture.getQuamera().setFov(60);
        quicture.getQuamera().flashlightOn(true);

        Quobject man = new Quobject("man.obj",5,-5,10,.5);
        quicture.add(man);

        Quobject gem = new Quobject("gem.obj",10,-5,10,.25);
        quicture.add(gem);

        QuectangularQuism big = new QuectangularQuism(1,1,10, 0,-1,7);
        quicture.add(big);

        Quobject staff = new Quobject("staff.obj", -10,-10,7,.1);
        quicture.add(staff);

        double speed = .01;
        double rotSpeed = .1;

        while(true) {
            try{
                Thread.sleep(1);
            }
            catch (Exception e){}
            if (window.isKeyDown(KeyEvent.VK_D))
                quicture.getQuamera().changePosBy(speed * Math.cos(Math.toRadians(quicture.getQuamera().getYaw())), 0, speed * Math.sin(Math.toRadians(quicture.getQuamera().getYaw())));
            if (window.isKeyDown(KeyEvent.VK_W))
                quicture.getQuamera().changePosBy(-speed * Math.sin(Math.toRadians(quicture.getQuamera().getYaw())), 0, speed * Math.cos(Math.toRadians(quicture.getQuamera().getYaw())));
            if (window.isKeyDown(KeyEvent.VK_A))
                quicture.getQuamera().changePosBy(-speed * Math.cos(Math.toRadians(quicture.getQuamera().getYaw())), 0, -speed * Math.sin(Math.toRadians(quicture.getQuamera().getYaw())));
            if (window.isKeyDown(KeyEvent.VK_S))
                quicture.getQuamera().changePosBy(speed * Math.sin(Math.toRadians(quicture.getQuamera().getYaw())), 0, -speed * Math.cos(Math.toRadians(quicture.getQuamera().getYaw())));
            if (window.isKeyDown(KeyEvent.VK_I))
                quicture.getQuamera().changePosBy(0, 0, speed);
            if (window.isKeyDown(KeyEvent.VK_K))
                quicture.getQuamera().changePosBy(0, 0, -speed);
            if (window.isKeyDown(KeyEvent.VK_Q))
                quicture.getQuamera().changePosBy(0, -speed, 0);
            if (window.isKeyDown(KeyEvent.VK_E))
                quicture.getQuamera().changePosBy(0, speed, 0);
            if (window.isKeyDown(KeyEvent.VK_UP))
                quicture.getQuamera().changeRotationBy(0, rotSpeed, 0);
            if (window.isKeyDown(KeyEvent.VK_DOWN))
                quicture.getQuamera().changeRotationBy(0, -rotSpeed, 0);
            if (window.isKeyDown(KeyEvent.VK_RIGHT))
                quicture.getQuamera().changeRotationBy(-rotSpeed, 0, 0);
            if (window.isKeyDown(KeyEvent.VK_LEFT))
                quicture.getQuamera().changeRotationBy(rotSpeed, 0, 0);
            if (window.isKeyDown(KeyEvent.VK_PAGE_UP))
                quicture.getQuamera().changeRotationBy(0, 0, rotSpeed);
            if (window.isKeyDown(KeyEvent.VK_PAGE_DOWN))
                quicture.getQuamera().changeRotationBy(0, 0, -rotSpeed);
        }
    }
}
