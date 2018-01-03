package tong.utils;


import tong.objects.City;

public class DistanceUtil {

    private DistanceUtil(){}


    public static Double calculateDistance(City a, City b) {
        Double distance = Math.sqrt(Math.pow((b.getX() - a.getX()), 2) + Math.pow((b.getY() - a.getY()), 2));

        return distance;
    }
    /*
    @Param a, b are the two cities making the line.
    @Param c is the city being measured.
     */
    public static Double distanceFromLine(City a, City b, City c) {
        double numerator = Math.abs((b.getX() - a.getX()) * (a.getY() - c.getY()) - (a.getX() - c.getX() * (b.getY() - a.getY())));

        Double distance = numerator / Math.sqrt(Math.pow((b.getX() - a.getX()), 2) + Math.pow((b.getY() - a.getY()), 2));

        return distance;
    }
}
