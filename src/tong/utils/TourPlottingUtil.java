package tong.utils;

import tong.objects.City;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class TourPlottingUtil {

    static LinkedList<City> path;
    String title;

    public TourPlottingUtil(LinkedList<City> path) {
        this.path = path;
    }

    public TourPlottingUtil(ArrayList<City> arrayPath) {
        path = new LinkedList<>();

        path.addAll(arrayPath);
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void drawPath() {
        JFrame frame = new JFrame();
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Hamiltonian Path: " + title);

        //add the custom JPanel that will display the points and edges.
        frame.add(new MyPanel());
        frame.setVisible(true);
    }

    //mypanel class will display the points on a jframe
    public static class MyPanel extends JPanel{
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;

            for( int i = 0; i< path.size(); i++ ){
                if( i + 1 >= path.size() ){
                    //draw the point and the line
                    g2.drawOval((int)path.getLast().getX() * 4, (int)path.getLast().getY() * 4, 5, 5);
                    g2.drawString(String.valueOf(path.getLast().getCityNum()), (int)path.getLast().getX() * 4 + 2, (int)path.getLast().getY() * 4);
                    g2.drawLine( (int)path.getFirst().getX() * 4, (int)path.getFirst().getY() * 4,(int)path.getLast().getX() * 4,(int)path.getLast().getY() * 4);
                } else {
                    //draw the point and the line
                    g2.drawOval((int)path.get(i).getX() * 4, (int)path.get(i).getY() * 4, 5, 5);
                    g2.drawString(String.valueOf(path.get(i).getCityNum()), (int)path.get(i).getX() * 4 + 2, (int)path.get(i).getY() * 4);
                    g2.drawLine( (int)path.get(i).getX() * 4, (int)path.get(i).getY() * 4,(int)path.get(i + 1).getX() * 4,(int)path.get(i + 1).getY() * 4);
                }
            }
        }
    }
}
