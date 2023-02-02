package quinn.win;

import java.awt.*;

public class Quixel {
    public int x, y;
    public double z;
    private Color color = Color.white;
    public Quixel(int x, int y, double z, Color color){
        setPos(x,y,z);
        setColor(color);
    }
    public Quixel(int x, int y, double z){
        setPos(x,y,z);
    }
    public void setPos(int x, int y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void setColor(Color color){
        this.color = color;
    }
    public Color getColor(){
        return color;
    }
}
