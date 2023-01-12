package quinn.world;


import quinn.win.Quicture;
import quinn.world.obj.Quisition;
import quinn.world.obj.Qulane;
import quinn.world.obj.Quobject;
import quinn.world.obj.Quobject3D;

import java.awt.*;
import java.util.ArrayList;

/**
 * I will show you the quworld!
 * A 3D world that is super cool.
 * @author Quinn Graham.
 */

public class Quworld extends Quomponent{
    public Quworld(Quicture quicture){
        quicture.add(this);
    }

    private ArrayList<Quomponent> quomponents = new ArrayList<>();
    public Quomponent add(Quomponent quomponent){
        if (!quomponents.contains(quomponent)) {
            quomponent.setQuicture(getQuicture());
            quomponents.add(quomponent);
        }
        return quomponent;
    }
    public Quobject remove(Quobject object){
        quomponents.remove(object);
        return object;
    }

    public ArrayList<Quomponent> getQuomponents(){
        return quomponents;
    }

    private Quisition camera = new Quisition(0,0,0);
    public void setCameraPos(double x, double y, double z){
        setCameraPos(new Quisition(x,y,z));
    }
    public void setCameraPos(Quisition quisition){
        camera = quisition;
    }
    public void changeCameraPos(double x, double y, double z){
        setCameraPos(camera.x + x, camera.y + y, camera.z + z);
    }
    public void changeCameraPos(Quisition quisition){
        setCameraPos(camera.x + quisition.x, camera.y + quisition.y, camera.z + quisition.z);
    }

    public void paint(Graphics g){
        for (int i = 0; i < quomponents.size(); i++)
            if (quomponents.get(i) instanceof Quobject3D){
                Quobject3D quobject = (Quobject3D) quomponents.get(i);
                for (int j = 0; j < quobject.getPlanes().length-1; j++) {
                    if (quobject.getPlanes()[j].getPos().z > quobject.getPlanes()[j+1].getPos().z) {
                        Qulane temp = quobject.getPlanes()[j];
                        quobject.getPlanes()[j] = quobject.getPlanes()[j+1];
                        quobject.getPlanes()[j+1] = temp;
                        i = 0;
                    }
                }
            }
        for (int i = 0; i < quomponents.size(); i++)
            quomponents.get(i).paint(g);
    }
}
