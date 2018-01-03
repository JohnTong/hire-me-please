package tong.workers;

import tong.objects.City;
import tong.objects.IdealPath;
import tong.utils.DistanceUtil;
import tong.utils.PermutationUtil;

import java.util.ArrayList;
import java.util.Map;

public class BruteForceWorker {

    private static final String SPACE = " ";
    private static Map<Integer, City> cities;

    public BruteForceWorker(Map<Integer, City> cities) { this.cities = cities; }

    public void punySalesman() {

        ArrayList<ArrayList<Integer>> permutations;
        int[] array = populateIntList(cities.size());
        permutations = PermutationUtil.generateAllPermutations(array);

        double shortestDistance = -1;
        double workingDistance = 0;
        String route = null;


        for(ArrayList<Integer> il : permutations) {
            StringBuilder sb = new StringBuilder();
            workingDistance = calculateRoute(il, sb);

            if(shortestDistance < 0 || shortestDistance > workingDistance) {
                shortestDistance = workingDistance;
                route = sb.toString();
            }
        }

        IdealPath iP = new IdealPath(route, shortestDistance);
        System.out.println(iP.toString());
    }

    /*
    This calculates the route and tracks cities on the route in order.
     */
    private double calculateRoute(ArrayList<Integer> list, StringBuilder sb) {
        double distance = 0;
        int i;
        for(i = 0; i < list.size() - 1; i++) {
            sb.append(cities.get(list.get(i)).getCityNum());
            sb.append(SPACE);
            distance += DistanceUtil.calculateDistance(cities.get(list.get(i)), cities.get(list.get(i + 1)));
        }
        distance += DistanceUtil.calculateDistance(cities.get(list.get(list.size() - 1)), cities.get(list.get(0)));
        sb.append(cities.get(list.get(list.size() - 1)).getCityNum());
        return distance;
    }

    private int[] populateIntList(int size) {
        int[] array = new int[size];
        for(int i = 0; i < size; i++) {
            array[i] = i + 1;
        }

        return array;
    }
}