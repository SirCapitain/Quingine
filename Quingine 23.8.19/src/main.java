import quingine.sim.obj.Quobject;
import quingine.util.Quicture;
import quingine.util.Quindow;

import java.awt.*;
import java.awt.event.KeyEvent;


public class main {
    public static void main(String[] args) {
        Quindow window = new Quindow();
        Quicture quicture = new Quicture(window);
        window.setSize(1000,600);
        quicture.getQuamera().setzNear(.001);
        quicture.getQuamera().setzFar(400);
        window.setFps(30);
        quicture.getQuamera().setFov(60);
        quicture.getQuamera().flashlightOn(true);

        quicture.setDebugColor(Color.red);

        window.setTitle("Quingine 23.8.19");

        Quobject room = new Quobject("room.obj",0,-7,0,5);
        quicture.add(room);
        room.rotate(0,1,0,180);

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
