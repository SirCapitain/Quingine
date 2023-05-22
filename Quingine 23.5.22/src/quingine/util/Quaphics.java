package quingine.util;

import quingine.sim.Math3D;
import quingine.sim.Quisition;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Quaphics are the new, next gen, graphics. Get ready to be blown
 * out of the water!
 * This class draws shapes for you in 3D space! Oooohhhh Aaaahhhhh
 *
 * @author mostly Quinn Graham
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
        for (int i = 0; i < points.length; i++) {
            int j = i == points.length - 1 ? 0 : i + 1;
            if (Double.isNaN(points[i].z) || Double.isNaN(points[j].z))
                continue;
            int x1 = quicture.getScreenPosX(points[i].x);
            int x2 = quicture.getScreenPosX(points[j].x);
            int y1 = quicture.getScreenPosY(points[i].y);
            int y2 = quicture.getScreenPosY(points[j].y);
            int yInc = Integer.compare(y2, y1);
            int xInc = Integer.compare(x2, x1);
            double dx = x2 - x1;
            double dy = y2 - y1;
            double[] zEquation = Math3D.getPlane(points);
            if (dx == 0) {//Vertical
                if (x1 < 0 || x1 > quicture.getWidth())
                    continue;
                y1 = y1 < 0 ? 0 : Math.min(y1, quicture.getHeight());
                y2 = y2 < 0 ? 0 : Math.min(y2, quicture.getHeight());
                for (int y = y1; y != y2; y += yInc)
                    quicture.setPixel(x1, y, -1/zEquation[2]*(zEquation[0]*quicture.getMatrixPosX(x1)+zEquation[1]*quicture.getMatrixPosY(y)+zEquation[3]), color);
                continue;
            }
            if (dy == 0) {//Horizontal
                if (y1 < 0 || y1 > quicture.getHeight())
                    continue;
                x1 = x1 < 0 ? 0 : Math.min(x1, quicture.getWidth());
                x2 = x2 < 0 ? 0 : Math.min(x2, quicture.getWidth());

                for (int x = x1; x != x2; x += xInc)
                    quicture.setPixel(x, y1, -1/zEquation[2]*(zEquation[0]*quicture.getMatrixPosX(x)+zEquation[1]*quicture.getMatrixPosY(y1)+zEquation[3]), color);
                continue;
            }
            double slope = dy / dx;
            double b = y1 - slope * x1;
            if (Math.abs(slope) < 1) {//More Horizontal
                x1 = x1 < 0 ? 0 : Math.min(x1, quicture.getWidth());
                x2 = x2 < 0 ? 0 : Math.min(x2, quicture.getWidth());
                for (int x = x1; x != x2; x += xInc) {
                    double y = x * slope + b;
                    quicture.setPixel(x, (int) Math.round(y), -1/zEquation[2]*(zEquation[0]*quicture.getMatrixPosX(x)+zEquation[1]*quicture.getMatrixPosY(y)+zEquation[3]), color);
                }
                continue;
            }
            if (Math.abs(slope) >= 1){//More Vertical
                y1 = y1 < 0 ? 0 : Math.min(y1, quicture.getHeight());
                y2 = y2 < 0 ? 0 : Math.min(y2, quicture.getHeight());
                for (int y = y1; y != y2; y+=yInc) {
                    double x = (y-b)/slope;
                    quicture.setPixel((int)Math.round(x), y, -1/zEquation[2]*(zEquation[0]*quicture.getMatrixPosX(x)+zEquation[1]*quicture.getMatrixPosY(y)+zEquation[3]), color);
                }
            }
        }
    }

    /**
     * I did figure this one out.
     * This method fills any polygon you put in. This also
     * accounts for each individual z for each quixel.
     * @param points list of Quisitions to draw to.
     * @param quicture the quicture to draw on.
     */
    public static void fillPolygon(Quisition[] points, Quicture quicture) {
        //Determining the Slope
        double[][] equations = new double[points.length][2];
        int yMax =Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE;
        int xMax =Integer.MIN_VALUE;
        int xMin = Integer.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            int j = i == points.length-1 ? 0 : i + 1;
            if (Double.isNaN(points[i].z) || Double.isNaN(points[j].z))
                continue;
            int x1 = quicture.getScreenPosX(points[i].x);
            int x2 = quicture.getScreenPosX(points[j].x);
            int y1 = quicture.getScreenPosY(points[i].y);
            int y2 = quicture.getScreenPosY(points[j].y);
            yMax = Math.max(y1, yMax);
            yMin = Math.min(y1, yMin);
            xMax = Math.max(x1, xMax);
            xMin = Math.min(x1, xMin);
            int dx = x2-x1;
            int dy = y2-y1;
            double slope = dy != 0 ? (double)dx/dy : Double.NaN;
            equations[i][0] = slope;
            equations[i][1] = x1 - (slope*y1);
        }
        yMax = yMax < 0 ? 0 : Math.min(yMax, quicture.getHeight());
        yMin = yMin < 0 ? 0 : Math.min(yMin, quicture.getHeight());
        xMax = xMax < 0 ? 0 : Math.min(xMax, quicture.getWidth());
        xMin = xMin < 0 ? 0 : Math.min(xMin, quicture.getWidth());
        //Drawing every line
        for (int y = yMin-1; y <= yMax+1; y++) {
            //Finding each edge that will enclose the shape
            ArrayList<Integer> xPoints = new ArrayList<>();
            for (int i = 0; i < equations.length; i++) {
                int j = i == points.length - 1 ? 0 : i + 1;
                int x = (int) Math.round(y * equations[i][0] + equations[i][1]);
                if (!Double.isNaN(equations[i][0]) && !xPoints.contains(x) && ((y <= quicture.getScreenPosY(points[i].y) && y >= quicture.getScreenPosY((points[j].y))) || (y <= quicture.getScreenPosY(points[j].y) && y >= quicture.getScreenPosY(points[i].y))))
                    xPoints.add(x);
            }
            //Order
            Collections.sort(xPoints);
            double[] zEquation = Math3D.getPlane(points);
            //Draw each pixel in between the pixels.
            if (xPoints.size() > 1) {
                xPoints.set(0, xPoints.get(0) < xMin ? xMin : xPoints.get(0));
                xPoints.set(1, xPoints.get(1) > xMax ? xMax : xPoints.get(1));
                for (int x = xPoints.get(0); x <= xPoints.get(1); x++)
                    quicture.setPixel(x, y, -1/zEquation[2]*(zEquation[0]*quicture.getMatrixPosX(x)+zEquation[1]*quicture.getMatrixPosY(y)+zEquation[3]), color);
            }
        }
    }
}
