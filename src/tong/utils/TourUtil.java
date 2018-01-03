package tong.utils;

import tong.objects.City;

import java.util.ArrayList;

public class TourUtil {

    private static ArrayList<City> cities = new ArrayList<>();

    public static void addCity(City city) { cities.add(city); }

    public static City getCity(int pos) { return cities.get(pos); }

    public static City getCitybyNumber(int num) {
        City c = null;

        for(City ci : cities) {
            if(ci.getCityNum() == num) {
                c = ci;
            }
        }

        return c;
    }

    public static int numberOfCities() { return cities.size(); }

}
