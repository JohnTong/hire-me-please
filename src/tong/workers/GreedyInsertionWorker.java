package tong.workers;

import tong.objects.City;
import tong.utils.DistanceUtil;
import tong.utils.TourPlottingUtil;

import java.util.LinkedList;

public class GreedyInsertionWorker {

    LinkedList<City> cities;
    LinkedList<City> idealPath = new LinkedList<>();

    public GreedyInsertionWorker(LinkedList<City> cities) {
        this.cities = cities;
    }


    public void createTour() {
        findInitialTour();

        findRemainingTour();

        double distance = 0;
        for(City c : idealPath) {
            System.out.print(c.getCityNum() + " ");
            if(!c.equals(idealPath.getLast())) {
                distance += DistanceUtil.calculateDistance(c, idealPath.get(idealPath.indexOf(c) + 1));
            } else {
                distance += DistanceUtil.calculateDistance(c, idealPath.get(0));
            }
        }

        System.out.println("Distance = " + distance);


        TourPlottingUtil draw = new TourPlottingUtil(idealPath);
        draw.drawPath();

    }

    /*
    Calculates each cities distance from every other city, then selects the two with the shortest distance between them.
     */
    private void findInitialTour() {
        double minDistance = -1;
        InitialCities initCities = new InitialCities();

        for(City a : cities) {
            for (int i = cities.indexOf(a); i < cities.size(); i++) {
                City b = cities.get(i);

                if(!a.equals(b)) {
                    Double calcDistance = DistanceUtil.calculateDistance(a,b);
                    if(minDistance < 0 || minDistance > calcDistance) {
                        minDistance = calcDistance;
                        initCities.setA(a);
                        initCities.setB(b);
                    }
                }
            }
        }

        cities.remove(initCities.getA());
        cities.remove(initCities.getB());
        idealPath.add(initCities.getA());
        idealPath.add(initCities.getB());
    }
    /*
    Iterates through cities not yet in the path, finds the city which is closest to an edge in the path, then adds it to the path.
    Repeats until all cities are in the path.
     */
    private void findRemainingTour() {
        City next = null;
        int insertionPoint = -1;
        double minDistance = -1;
        double workingDistance;
        for(City c : cities) {
            for (City a : idealPath) {
                if(!a.equals(idealPath.getLast())) {
                    workingDistance = DistanceUtil.distanceFromLine(a, idealPath.get(idealPath.indexOf(a) + 1), c);
                } else {
                    workingDistance = Double.MAX_VALUE;
                }

                if(minDistance < 0 || workingDistance < minDistance) {
                    minDistance = workingDistance;
                    insertionPoint = idealPath.indexOf(a);
                    next = c;
                }
            }
        }

        cities.remove(next);

        idealPath.add(insertionPoint + 1, next);

        if(!cities.isEmpty()) {
            findRemainingTour();
        }
    }
    static class InitialCities {
        private City a;
        private City b;

        InitialCities() {}

        void setA(City a) { this.a = a; }

        void setB(City b) { this.b = b; }

        City getA() { return a; }

        City getB() { return b; }

        @Override
        public String toString() {
            return "Initial tour of: " + a.getCityNum() + ", " + b.getCityNum();
        }
    }
}
