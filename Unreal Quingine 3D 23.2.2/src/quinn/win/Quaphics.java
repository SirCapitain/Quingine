package quinn.win;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import quinn.world.obj.Quisition;
import quinn.world.obj.Qulane;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Quaphics are the new, next gen, graphics. Get ready to be blown
 * out of the water!
 *
 * @author mostlyQuinn
 */

public class Quaphics {
    private static Color color = Color.black;
    public static void setColor(Color color) {
        Quaphics.color = color;
    }
    public static Color getColor(){
        return color;
    }

    /**
     * Honestly. I gave up figuring this one out.
     * This method draws any polygon you put it. As long as it has points.
     * @param points list of Quisitions.
     * @param quicture the quicture to draw on.
     */
    public static void drawPolygon(Quisition[] points, Quicture quicture){
        for (int i = 0; i < points.length; i++){
            int j = i == points.length-1 ? 0 : i + 1;
            int x1 = quicture.getScreenPosX(points[i].x);
            int x2 = quicture.getScreenPosX(points[j].x);
            int y1 = quicture.getScreenPosY(points[i].y);
            int y2 = quicture.getScreenPosY(points[j].y);
            int dx = x2-x1;
            int dy = y2-y1;
            int incX = Integer.compare(dx, 0);
            int incY = Integer.compare(dy, 0);
            dx = Math.abs(dx);
            dy = Math.abs(dy);
            if (dy == 0)//Horizontal
                for (int x = x1; x != x2 + incX; x += incX) {
                    quicture.setQuixel(x, y1, Qulane.calcZ(points, quicture.getMatrixPosX(x), quicture.getMatrixPosY(y1)), color);
                }
            else if (dx == 0)//Vertical
                for (int y = y1; y != y2 + incY; y += incY)
                    quicture.setQuixel(x1, y ,Qulane.calcZ(points, quicture.getMatrixPosX(x1), quicture.getMatrixPosY(y)), color);
            else if (dx >= dy){//More horizontal
                int slope = 2 * dy;
                int error = -dx;
                int errorInc = -2 * dx;
                int y = y1;
                for (int x = x1; x != x2 + incX; x += incX){
                    quicture.setQuixel(x, y, Qulane.calcZ(points, quicture.getMatrixPosX(x), quicture.getMatrixPosY(y)), color);
                    error += slope;
                    if (error >= 0){
                        y += incY;
                        error += errorInc;
                    }
                }
            }else{//More vertical
                int slope = 2 * dx;
                int error = -dy;
                int errorInc = -2 * dy;
                int x = x1;
                for (int y = y1; y != y2 + incY; y += incY){
                    quicture.setQuixel(x, y, Qulane.calcZ(points, quicture.getMatrixPosX(x), quicture.getMatrixPosY(y)), color);
                    error += slope;
                    if (error >= 0){
                        x += incX;
                        error += errorInc;
                    }
                }
            }
        }
    }

    /**
     * I did figure this one out.
     * This method fills any polygon you put in. This also
     * accounts for each individual z for each quixel.
     * @param points list of Quisistions to draw to.
     * @param quicture the quicture to draw on.
     */
    public static void fillPolygon(Quisition[] points, Quicture quicture) {

        //Determining the Slope
        double[][] equations = new double[points.length][2];
        int yMax =Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            int j = i == points.length-1 ? 0 : i + 1;
            int x1 = quicture.getScreenPosX(points[i].x);
            int x2 = quicture.getScreenPosX(points[j].x);
            int y1 = quicture.getScreenPosY(points[i].y);
            int y2 = quicture.getScreenPosY(points[j].y);
            yMax = y1 > yMax ? y1 : yMax;
            yMin = y1 < yMin ? y1 : yMin;
            int dx = x2-x1;
            int dy = y2-y1;
            double slope = dy != 0 ? (double)dx/dy : Double.NaN;
            equations[i][0] = slope;
            equations[i][1] = x1 - (slope*y1);
        }


        //Drawing every line
        for (int y = yMin; y <= yMax; y++){
            //Finding each edge that will enclose the shape
            ArrayList<Integer> xPoints = new ArrayList<>();
            for (int i = 0; i < equations.length; i++){
                int j = i == points.length-1 ? 0 : i + 1;
                int x = (int)Math.round(y*equations[i][0] + equations[i][1]);
                if (!Double.isNaN(equations[i][0]) && !xPoints.contains(x) && (y <= quicture.getScreenPosY(points[i].y) && y >= quicture.getScreenPosY((points[j].y)) || y <= quicture.getScreenPosY(points[j].y) && y >= quicture.getScreenPosY(points[i].y))) {
                    xPoints.add(x);
                }
            }
            //Order
            Collections.sort(xPoints);


            //Draw each pixel in between the pixels.
            if (xPoints.size() > 1)
                for (int x = xPoints.get(0); x <= xPoints.get(1); x++)
                    quicture.setQuixel(x, y, Qulane.calcZ(points, quicture.getMatrixPosX(x), quicture.getMatrixPosY(y)), color);
        }
    }
}
