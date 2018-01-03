package tong.objects;

import java.util.List;
import java.util.ArrayList;

public class City {
    private int cityNum;
    private double x;
    private double y;

    public City () {}

    public City (int cityNum, double x, double y) {
        this.x = x;
        this.y = y;
        this.cityNum = cityNum;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getCityNum() {
        return cityNum;
    }

    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }

    @Override
    public String toString() {
        return "City number: " + cityNum + " coordinates: " + x + " " + y;
    }
}
