import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.Graphics.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
public class SquareGUI {
    private int x;
    private int y;
    private boolean w;
    public SquareGUI(int x, int y, boolean wall) {
        this.x = x;
        this.y = y;
        this.w = wall;
    }
    public void draw(Graphics g) {
        if(w) {
            g.setColor(new Color(0,0,0));
        } else {
            g.setColor(new Color(150, 150, 150));
        }
        g.fillRect(x*Viewer2.size+Viewer2.ox, y*Viewer2.size+Viewer2.oy, Viewer2.size, Viewer2.size);
    }
    public int getRow() {
        return x;
    }
    public int getCol() {
        return y;
    }
}