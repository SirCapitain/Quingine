package quingine.render.util;

import quingine.render.sim.Math3D;
import quingine.render.sim.cam.Quamera;
import quingine.render.sim.pos.Quisition;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Quaphics are the new, next gen, graphics. Get ready to be blown
 * out of the water!
 * This class helps draw shapes for you in 3D space! (in a way) Oooohhhh Aaaahhhhh
 */

public class Quaphics {

    private static int maxColor = 255;
    private static int minColor = 20;

    /**
     * Set the minimum and maximum a color can be.
     * from 0 - 255. default: 20 - 255
     * @param min if color is below this number it will be set to this number
     * @param max if color is above this number it will be set to this number
     */
    public static void setMinMaxColor(int min, int max){
        minColor = min;
        maxColor = max;
    }

    /**
     * Updates the color to match the current color and brightness
     */
    private static int updateColor(int color, double brightness){
        //Color
        int blue = (int)((color & 0xff) * brightness);
        int green = (int)(((color & 0xff00) >> 8) * brightness);
        int red = (int)(((color & 0xff0000) >> 16) * brightness);
        int alpha = (color & 0xff000000) >> 24;
        red = red < minColor ? minColor : Math.min(red, maxColor);
        green = green < minColor ? minColor : Math.min(green, maxColor);
        blue = blue < minColor ? minColor : Math.min(blue, maxColor);
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * A method the draw a triangle.
     * NO POLYGONS WITH MORE THAN 3 SIDES!
     * I SAID NO!
     * @param p1 A Quisition to be a vertex
     * @param p2 A Quisition to be a vertex
     * @param p3 A Quisition to be a vertex
     * @param q the camera to draw on.
     * @param color the color of the triangle
     * @param zOffset the offset of the z to allow it to draw over some other things
     */
    public static void drawPolygon(Quisition p1, Quisition p2, Quisition p3, Quamera q, int color, double zOffset){
        Quisition[] points = new Quisition[]{p1, p2, p3};
        if (points[1].y > points[0].y)
            points = new Quisition[]{points[1], points[0], points[2]};
        if (points[2].y > points[0].y)
            points = new Quisition[]{points[2], points[0], points[1]};
        if (points[2].y > points[1].y)
            points = new Quisition[]{points[0], points[2], points[1]};
        int x1 = q.getScreenPosX(points[0].x);
        int x2 = q.getScreenPosX(points[1].x);
        int x3 = q.getScreenPosX(points[2].x);
        int x4 = x2;

        int y1 = q.getScreenPosY(points[0].y);
        int y2 = q.getScreenPosY(points[1].y);
        int y3 = q.getScreenPosY(points[2].y);
        int y4 = y2;

        for (int i = 0; i < 3; i++) {
            if (i == 1){
                x2 = x3;
                y2 = y3;
            }
            else if(i == 2){
                x1 = x4;
                y1 = y4;
            }
            int yInc = Integer.compare(y2, y1);
            int xInc = Integer.compare(x2, x1);
            double dx = x2 - x1;
            double dy = y2 - y1;
            double[] zEquation = Math3D.getPlane(points);
            double zRep = -1 / zEquation[2];
            if (dx == 0) {//Vertical
                if (x1 < 0 || x1 > q.getWidth())
                    continue;
                y1 = y1 < 0 ? 0 : Math.min(y1, q.getHeight());
                y2 = y2 < 0 ? 0 : Math.min(y2, q.getHeight());
                for (int y = y1; y != y2; y += yInc)
                    q.setPixel(x1, y,  zRep * (zEquation[0] * q.getMatrixPosX(x1) + zEquation[1] * q.getMatrixPosY(y) + zEquation[3]) + zOffset, color);
                continue;
            }
            if (dy == 0) {//Horizontal
                if (y1 < 0 || y1 > q.getHeight())
                    continue;
                x1 = x1 < 0 ? 0 : Math.min(x1, q.getWidth());
                x2 = x2 < 0 ? 0 : Math.min(x2, q.getWidth());

                for (int x = x1; x != x2; x += xInc)
                    q.setPixel(x, y1, zRep * (zEquation[0] * q.getMatrixPosX(x) + zEquation[1] * q.getMatrixPosY(y1) + zEquation[3]) + zOffset, color);
                continue;
            }
            double slope = dy / dx;
            double b = y1 - slope * x1;
            if (Math.abs(slope) < 1) {//More Horizontal
                x1 = x1 < 0 ? 0 : Math.min(x1, q.getWidth());
                x2 = x2 < 0 ? 0 : Math.min(x2, q.getWidth());
                for (int x = x1; x != x2; x += xInc) {
                    double y = x * slope + b;
                    q.setPixel(x, (int) Math.round(y), zRep * (zEquation[0] * q.getMatrixPosX(x) + zEquation[1] * q.getMatrixPosY(y) + zEquation[3]) + zOffset, color);
                }
                continue;
            }
            if (Math.abs(slope) >= 1) {//More Vertical
                y1 = y1 < 0 ? 0 : Math.min(y1, q.getHeight());
                y2 = y2 < 0 ? 0 : Math.min(y2, q.getHeight());
                for (int y = y1; y != y2; y += yInc) {
                    double x = (y - b) / slope;
                    q.setPixel((int) Math.round(x), y, zRep * (zEquation[0] * q.getMatrixPosX(x) + zEquation[1] * q.getMatrixPosY(y) + zEquation[3]) + zOffset, color);
                }
            }
        }
    }

    /**
     * A method that only fills in a triangle.
     * This does not take into account if the pixels extend beyond the screen
     * @param p1 A Quisition to be a vertex
     * @param p2 A Quisition to be a vertex
     * @param p3 A Quisition to be a vertex
     * @param q the camera that the triangle will be drawn on
     * @param color the color of the filling. YUM!
     */
    public static void fillTri(Quisition p1, Quisition p2, Quisition p3, Quamera q, int color) {
            //Draw from top to bottom
            Quisition[] nv = new Quisition[]{new Quisition(q.getScreenPosX(p1.x), q.getScreenPosY(p1.y), p1.data[3]),new Quisition(q.getScreenPosX(p2.x), q.getScreenPosY(p2.y), p2.data[3]),new Quisition(q.getScreenPosX(p3.x), q.getScreenPosY(p3.y), p3.data[3])};
            double[] lightMap = Math3D.getPlane(nv);
            Quisition[] points = new Quisition[]{p1, p2, p3};
            if (points[1].y > points[0].y)
                points = new Quisition[]{points[1], points[0], points[2]};
            if (points[2].y > points[0].y)
                points = new Quisition[]{points[2], points[0], points[1]};
            if (points[2].y > points[1].y)
                points = new Quisition[]{points[0], points[2], points[1]};
            int x1 = q.getScreenPosX(points[0].x);
            int x2 = q.getScreenPosX(points[1].x);
            int x3 = q.getScreenPosX(points[2].x);

            int y1 = q.getScreenPosY(points[0].y);
            int y2 = q.getScreenPosY(points[1].y);
            int y3 = q.getScreenPosY(points[2].y);

            double dy1 = y2 - y1;
            double dy2 = y3 - y1;
            if (dy2 == 0)
                return;
            double dy3 = y3 - y2;

            double dx1Inc = (x2 - x1)/Math.abs(dy1);
            double dx2Inc = (x3 - x1)/Math.abs(dy2);
            double dx3Inc = (x3 - x2)/Math.abs(dy3);

            for (int i = 0; i < 2; i++) {
                int sx = x1;
                int sy = y1;
                int ey = y2;
                double sXInc = dx1Inc;
                if (i == 1) {
                    sx = x2;
                    sy = y2;
                    ey = y3;
                    sXInc = dx3Inc;
                }
                for (int y = sy; y < ey; y++) {
                    int startX = (int) (.5 + sx + (y - sy) * sXInc);
                    int endX = (int) (.5 + x1 + (y - y1) * dx2Inc);

                    //sort
                    if (startX > endX) {
                        int temp = startX;
                        startX = endX;
                        endX = temp;
                    }
                    //draw
                    for (int x = startX; x < endX; x++) {
                        double lv = -1/lightMap[2]*(lightMap[0]*x+lightMap[1]*y+lightMap[3]);
                        int colorN = updateColor(color, lv);
                        q.setPixel(x, y, Math3D.calcZ(p1, p2, p3, q.getMatrixPosX(x), q.getMatrixPosY(y)), colorN);
                    }
                }
            }
    }

    /**
     * A method draws an image in a triangle.
     * Does not factor in if the pixels are off screen
     * @param p1 point on the triangle
     * @param p2 point on the triangle
     * @param p3 point on the triangle
     * @param q the camera the draw image will be drawn on
     * @param image the image to be drawn
     */
    public static void drawImageTri(Quisition p1, Quisition p2, Quisition p3, Quamera q, BufferedImage image){
        Quisition[] nv = new Quisition[]{new Quisition(q.getScreenPosX(p1.x), q.getScreenPosY(p1.y), p1.data[3]),new Quisition(q.getScreenPosX(p2.x), q.getScreenPosY(p2.y), p2.data[3]),new Quisition(q.getScreenPosX(p3.x), q.getScreenPosY(p3.y), p3.data[3])};
        double[] lightMap = Math3D.getPlane(nv);
        //Draw from top to bottom.
        Quisition[] points = new Quisition[]{p1, p2, p3};
        if (points[1].y > points[0].y)
            points = new Quisition[]{points[1], points[0], points[2]};
        if (points[2].y > points[0].y)
            points = new Quisition[]{points[2], points[0], points[1]};
        if (points[2].y > points[1].y)
            points = new Quisition[]{points[0], points[2], points[1]};

        int x1 = q.getScreenPosX(points[0].x);
        int x2 = q.getScreenPosX(points[1].x);
        int x3 = q.getScreenPosX(points[2].x);

        int y1 = q.getScreenPosY(points[0].y);
        int y2 = q.getScreenPosY(points[1].y);
        int y3 = q.getScreenPosY(points[2].y);

        double w1 = points[0].data[0];
        double w2 = points[1].data[0];
        double w3 = points[2].data[0];

        double u1 = points[0].data[1];
        double u2 = points[1].data[1];
        double u3 = points[2].data[1];

        double v1 = points[0].data[2];
        double v2 = points[1].data[2];
        double v3 = points[2].data[2];

        double dy1 = Math.abs(y2 - y1);
        double dy2 = Math.abs(y3 - y1);
        if (dy2 == 0)
            return;
        double dy3 = Math.abs(y3 - y2);

        double texU, texV, texW;

        double[] zEquation = Math3D.getPlane(points);
        double zz = -1/zEquation[2];

        int imageWidth = image.getWidth();
        int[] imageData = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < 2; i++) {
            int sx = x1;
            int sy = y1;
            int ey = y2;
            double su = u1;
            double sv = v1;
            double sw = w1;
            double sXInc = (x2 - x1)/dy1;
            double sUInc = (u2 - u1)/dy1;
            double sVInc = (v2 - v1)/dy1;
            double sWInc = (w2 - w1)/dy1;
            if (i == 1) {
                sx = x2;
                sy = y2;
                ey = y3;
                su = u2;
                sv = v2;
                sw = w2;
                sXInc = (x3 - x2)/dy3;
                sUInc = (u3 - u2)/dy3;
                sVInc = (v3 - v2)/dy3;
                sWInc = (w3 - w2)/dy3;
            }
            for (int y = sy; y < ey; y++) {

                int startX = (int) (.5 + sx + (y - sy) * sXInc);
                double startU = su + (y - sy) * sUInc;
                double startV = sv + (y - sy) * sVInc;
                double startW = sw + (y - sy) * sWInc;

                int endX = (int) (.5 + x1 + (y - y1) * (x3 - x1)/dy2);
                double endU = u1 + (y - y1) * (u3 - u1)/dy2;
                double endV = v1 + (y - y1) * (v3 - v1)/dy2;
                double endW = w1 + (y - y1) * (w3 - w1)/dy2;

                //sort
                if (startX > endX) {
                    int temp = startX;
                    startX = endX;
                    endX = temp;
                    double tempU = startU;
                    startU = endU;
                    endU = tempU;
                    double tempV = startV;
                    startV = endV;
                    endV = tempV;
                    double tempW = startW;
                    startW = endW;
                    endW = tempW;
                }
                double texStep = 1 / ((double) (endX - startX));
                double t = 0;

                double yz = zEquation[1]*q.getMatrixPosY(y);

                //draw
                for (int x = startX; x < endX; x++) {

                    texU = (1 - t) * startU + t * endU;
                    texV = (1 - t) * startV + t * endV;
                    texW = (1 - t) * startW + t * endW;

                    texU /= texW;
                    texV /= texW;

                    int texX = (int) Math.round(texU * image.getWidth());
                    int texY = (int) (image.getHeight() - Math.round(texV * image.getHeight()));

                    texX = Math.min(texX, image.getWidth() - 1);
                    texY = Math.min(texY, image.getHeight() - 1);

                    texX = Math.max(texX, 0);
                    texY = Math.max(texY, 0);

                    double lv = -1/lightMap[2]*(lightMap[0]*x+lightMap[1]*y+lightMap[3]);
                    int colorN = updateColor(imageData[texY*imageWidth + texX], lv);

                    q.setPixel(x, y, zz*(zEquation[0]*q.getMatrixPosX(x)+yz+zEquation[3]), colorN);

                    t += texStep;
                }
            }
        }
    }
}