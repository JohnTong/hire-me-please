package tong.objects;

import tong.utils.DistanceUtil;
import tong.utils.TourUtil;

import java.util.ArrayList;
import java.util.Collections;

public class GeneticTour {

    private ArrayList<City> tour;
    private double totalDistance = 0;

    public GeneticTour() {
        tour = new ArrayList<>();

        for(int i = 0; i < TourUtil.numberOfCities(); i++) {
            tour.add(null);
        }
    }

    public GeneticTour(ArrayList<City> tour) {
        this.tour = tour;
    }

    public void createIndividual() {
        for(int i = 0; i < TourUtil.numberOfCities(); i++) {
            tour.set(i, TourUtil.getCity(i));
        }
        Collections.shuffle(tour);
    }

    private void findDistance() {
        totalDistance = 0;
        for(int i = 0; i < tour.size() - 1; i++) {
            totalDistance += DistanceUtil.calculateDistance(tour.get(i), tour.get(i + 1));
        }
        totalDistance += DistanceUtil.calculateDistance(tour.get(tour.size() - 1), tour.get(0));
    }

    public double getTotalDistance() {
        findDistance();
        return totalDistance;
    }

    public int getTourSize() { return tour.size(); }

    public City getCity(int pos) { return tour.get(pos); }

    public int getCityPosition(City city) { return tour.indexOf(city); }

    public ArrayList<City> getTour() {
        return tour;
    }

    public void setCity(int pos, City city) {
        tour.set(pos, city);
        totalDistance = 0;
    }

    public void setCity(City city) {
        tour.add(city);
        totalDistance = 0;
    }
    public boolean contains(City city) {
        return tour.contains(city);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(City c : tour) {
            sb.append(c.getCityNum());
            sb.append(" ");
        }

        return sb.toString().trim();
    }
}
