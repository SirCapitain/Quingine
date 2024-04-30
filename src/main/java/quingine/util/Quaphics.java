package quingine.util;

import quingine.sim.Math3D;
import quingine.sim.cam.Quamera;
import quingine.sim.pos.Quisition;
import quingine.util.win.Quicture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
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
    public static Color updateColor(Color color, double brightness){
        //Color
        int red = (int)Math.round(color.getRed()*(brightness));
        int green = (int)Math.round(color.getGreen()*(brightness));
        int blue = (int)Math.round(color.getBlue()*(brightness));
        red = red < minColor ? minColor : Math.min(red, maxColor);
        green = green < minColor ? minColor : Math.min(green, maxColor);
        blue = blue < minColor ? minColor : Math.min(blue, maxColor);
        return new Color(red, green, blue);
    }

    /**
     * Honestly. I gave up figuring this one out.
     * This method draws any polygon you put it. As long as it has points.
     * @param points list of Quetexes.
     * @param q the camera to draw on.
     */
    public static void drawPolygon(Quisition[] points, Quamera q, Color color, double brightness){
        Color colorN = updateColor(color, brightness);
        for (int i = 0; i < points.length; i++) {
            int j = i == points.length - 1 ? 0 : i + 1;
            if (Double.isNaN(points[i].z) || Double.isNaN(points[j].z))
                continue;
            int x1 = q.getScreenPosX(points[i].x);
            int x2 = q.getScreenPosX(points[j].x);
            int y1 = q.getScreenPosY(points[i].y);
            int y2 = q.getScreenPosY(points[j].y);
            int yInc = Integer.compare(y2, y1);
            int xInc = Integer.compare(x2, x1);
            double dx = x2 - x1;
            double dy = y2 - y1;
            double[] zEquation = Math3D.getPlane(points);
            if (dx == 0) {//Vertical
                if (x1 < 0 || x1 > q.getWidth())
                    continue;
                y1 = y1 < 0 ? 0 : Math.min(y1, q.getHeight());
                y2 = y2 < 0 ? 0 : Math.min(y2, q.getHeight());
                for (int y = y1; y != y2; y += yInc)
                    q.setPixel(x1, y, -1/zEquation[2]*(zEquation[0]*q.getMatrixPosX(x1)+zEquation[1]*q.getMatrixPosY(y)+zEquation[3]), colorN.getRGB());
                continue;
            }
            if (dy == 0) {//Horizontal
                if (y1 < 0 || y1 > q.getHeight())
                    continue;
                x1 = x1 < 0 ? 0 : Math.min(x1, q.getWidth());
                x2 = x2 < 0 ? 0 : Math.min(x2, q.getWidth());

                for (int x = x1; x != x2; x += xInc)
                    q.setPixel(x, y1, -1/zEquation[2]*(zEquation[0]*q.getMatrixPosX(x)+zEquation[1]*q.getMatrixPosY(y1)+zEquation[3]), colorN.getRGB());
                continue;
            }
            double slope = dy / dx;
            double b = y1 - slope * x1;
            if (Math.abs(slope) < 1) {//More Horizontal
                x1 = x1 < 0 ? 0 : Math.min(x1, q.getWidth());
                x2 = x2 < 0 ? 0 : Math.min(x2, q.getWidth());
                for (int x = x1; x != x2; x += xInc) {
                    double y = x * slope + b;
                    q.setPixel(x, (int) Math.round(y), -1/zEquation[2]*(zEquation[0]*q.getMatrixPosX(x)+zEquation[1]*q.getMatrixPosY(y)+zEquation[3]), colorN.getRGB());
                }
                continue;
            }
            if (Math.abs(slope) >= 1){//More Vertical
                y1 = y1 < 0 ? 0 : Math.min(y1, q.getHeight());
                y2 = y2 < 0 ? 0 : Math.min(y2, q.getHeight());
                for (int y = y1; y != y2; y+=yInc) {
                    double x = (y-b)/slope;
                    q.setPixel((int)Math.round(x), y, -1/zEquation[2]*(zEquation[0]*q.getMatrixPosX(x)+zEquation[1]*q.getMatrixPosY(y)+zEquation[3]), colorN.getRGB());
                }
            }
        }
    }

    /**
     * I did figure this one out.
     * This method fills any polygon you put in. This also
     * accounts for each individual z for each quixel.
     * @param points list of Quertexes to draw to.
     * @param camera the camera to draw on.
     */
    public static void fillPolygon(Quisition[] points, Quamera camera, Color color, double brightness) {
        Color colorN = updateColor(color, brightness);
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
            int x1 = camera.getScreenPosX(points[i].x);
            int x2 = camera.getScreenPosX(points[j].x);
            int y1 = camera.getScreenPosY(points[i].y);
            int y2 = camera.getScreenPosY(points[j].y);
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
        yMax = yMax < 0 ? 0 : Math.min(yMax, camera.getHeight());
        yMin = yMin < 0 ? 0 : Math.min(yMin, camera.getHeight());
        xMax = xMax < 0 ? 0 : Math.min(xMax, camera.getWidth());
        xMin = xMin < 0 ? 0 : Math.min(xMin, camera.getWidth());
        //Drawing every line
        for (int y = yMin; y <= yMax; y++) {
            //Finding each edge that will enclose the shape
            ArrayList<Integer> xPoints = new ArrayList<>();
            for (int i = 0; i < equations.length; i++) {
                int j = i == points.length - 1 ? 0 : i + 1;
                int x = (int) Math.round(y * equations[i][0] + equations[i][1]);
                if (!Double.isNaN(equations[i][0]) && !xPoints.contains(x) && ((y <= camera.getScreenPosY(points[i].y) && y >= camera.getScreenPosY((points[j].y))) || (y <= camera.getScreenPosY(points[j].y) && y >= camera.getScreenPosY(points[i].y))))
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
                    camera.setPixel(x, y, -1/zEquation[2]*(zEquation[0]*camera.getMatrixPosX(x)+zEquation[1]*camera.getMatrixPosY(y)+zEquation[3]), colorN.getRGB());
            }
        }
    }

    /**
     * A method that only fills in a triangle.
     * This does not take into account if the pixels extend beyond the screen
     * @param vertices points of the triangle
     * @param q the camera that the triangle will be drawn on
     */
    public static void fillTri(Quisition[] vertices, Quamera q, Color color, double brightness) {
        Color colorN = updateColor(color, brightness);
            //Draw from top to bottom
            Quisition[] points = new Quisition[]{vertices[0], vertices[1], vertices[2]};
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
                    for (int x = startX; x < endX; x++)
                        q.setPixel(x, y, Math3D.calcZ(vertices, q.getMatrixPosX(x), q.getMatrixPosY(y)), colorN.getRGB());
                }
            }
    }

    /**
     * A method draws an image in a triangle.
     * Does not factor in if the pixels are off screen
     * @param vertices points on the triangle
     * @param q the camera the draw image will be drawn on
     * @param image the image to be drawn
     */
    public static void drawImageTri(Quisition[] vertices, Quamera q, BufferedImage image){
        //Draw from top to bottom.
        Quisition[] points = new Quisition[]{vertices[0], vertices[1], vertices[2]};
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

        double u1 = points[0].u;
        double u2 = points[1].u;
        double u3 = points[2].u;

        double v1 = points[0].v;
        double v2=  points[1].v;
        double v3 = points[2].v;

        double w1 = points[0].w;
        double w2 = points[1].w;
        double w3 = points[2].w;

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
                    int texY = (int) Math.round(texV * image.getHeight());

                    texX = Math.min(texX, image.getWidth() - 1);
                    texY = Math.min(texY, image.getHeight() - 1);

                    texX = Math.max(texX, 0);
                    texY = Math.max(texY, 0);

                    q.setPixel(x, y, zz*(zEquation[0]*q.getMatrixPosX(x)+yz+zEquation[3]), imageData[texY*imageWidth + texX]);

                    t += texStep;
                }
            }
        }
    }
}
