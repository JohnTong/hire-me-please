package tong.workers;

import tong.objects.City;
import tong.utils.DistanceUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DFSWorker {
    private HashMap<Integer, City> cities;

    private int navigableCities[][] = {
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,1,1,1,0,0,0,0,0,0,0},
            {0,0,0,1,0,0,0,0,0,0,0,0},
            {0,0,0,0,1,1,0,0,0,0,0,0},
            {0,0,0,0,0,1,1,1,0,0,0,0},
            {0,0,0,0,0,0,0,1,1,0,0,0},
            {0,0,0,0,0,0,0,0,1,0,0,0},
            {0,0,0,0,0,0,0,0,0,1,1,0},
            {0,0,0,0,0,0,0,0,0,1,1,1},
            {0,0,0,0,0,0,0,0,0,0,0,1},
            {0,0,0,0,0,0,0,0,0,0,0,1},
            {0,0,0,0,0,0,0,0,0,0,0,0}
    };
    private boolean visited[] = new boolean[navigableCities.length];

    private Map<Integer, Integer> dfsPath = new HashMap<>();

    public DFSWorker() {}

    public DFSWorker(HashMap<Integer, City> cities) {
        this.cities = cities;
    }


    public void printDFSPath() {
        LinkedList<Integer> path = new LinkedList<>();
        int i = 11;
        double distance = 0;
        path.addFirst(i);
        while( i != 1) {
            i = dfsPath.get(i); //starting at 11, we work backwards, getting all parent nodes, until we get the path to root
            path.addFirst(i);
        }
        System.out.print("Found best path using DFS: ");
        for(int in = 0; in < path.size() - 1; in++) {
            System.out.print(path.get(in) + " ");
            distance += DistanceUtil.calculateDistance(cities.get(path.get(in)), cities.get(path.get(in + 1)));
        }
        System.out.print(path.getLast() + ". ");
        System.out.println("With total distance of : " + distance);
    }

    public void dfs(int i) {
        int j;

        visited[i] = true;  // Mark node as "visited"

        for ( j = 0; j < 12; j++ ) {
            if (navigableCities[i][j] > 0 && !visited[j]) {
                dfsPath.put(j, i); // puts parent and child into a map.
                dfs(j);       // Visit node
            }
        }
    }
}
