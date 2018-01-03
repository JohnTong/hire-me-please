package tong.workers;

import tong.objects.City;
import tong.utils.DistanceUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BFSWorker {

    private HashMap<Integer, City> cities;
    private LinkedList<LinkedList<Integer>> paths = new LinkedList<>();
    private LinkedList<Integer> bfsPath = new LinkedList<>();
    private int root = 1;
    private int navigableCities[][] = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    private int numCities = navigableCities.length;
    boolean[] visited = new boolean[numCities];

    public BFSWorker() {
    }

    public BFSWorker(HashMap<Integer, City> cities) {
        this.cities = cities;
    }

    private void printPath() {
        double distance = 0;
        System.out.print("Found best path using BFS: ");
        for(int i = 0; i < bfsPath.size() - 1; i++) {
            System.out.print(bfsPath.get(i) + " ");
            distance = DistanceUtil.calculateDistance(cities.get(bfsPath.get(i)), cities.get(bfsPath.get(i + 1)));
        }
        System.out.print(bfsPath.getLast() + ". ");
        System.out.println("With a total distance of: " + distance);
    }


    public void bfs() {
        // BFS uses Queue data structure

        Queue<Integer> q = new LinkedList<>();
        HashMap<Integer, LinkedList<Integer>> paths = new HashMap<>();

        q.add(root);

        visited[root] = true;
        //This adds each node into the queue, finds its children, adds them to the queue, then unqueues the node.
        while (!q.isEmpty()) {
            int n, child;

            n = (q.peek()).intValue();

            child = getUnvisitedChildCity(n);

            if (child != -1) {
                visited[child] = true;

                q.add(child);
            } else {
                q.remove();
            }
        }

        clearVisited();      //Clear visited property of nodes
        printPath();
    }

    //Finds unvisited child nodes.
    int getUnvisitedChildCity(int n) {
        int j;
        for (j = 0; j < numCities; j++) {
            if (navigableCities[n][j] > 0) {
                if (!visited[j]) {
                    addToPath(n, j);
                    return (j);
                }
            }
        }

        return (-1);
    }

    /* addToPath and findPath will keep/add to various paths, and at the end we can pull out the appropriate path that
       leads from the goal state to root.
     */
    void addToPath(int parent, int child) {
        if(parent == 1) {
            LinkedList<Integer> i = new LinkedList<>();
            i.add(parent);
            i.add(child);
            paths.add(i);
        } else {
            LinkedList<Integer> l = findPath(parent);
            l.add(child);

            if(child == 11) {
                bfsPath = l;
            }
        }
    }

    LinkedList<Integer> findPath(int parent) {
        for(LinkedList<Integer> l : paths) {
            for(Integer i : l) {
                if(i.equals(parent))
                    return l;
            }
        }

        return new LinkedList<>();
    }

    void clearVisited() {
        int i;

        for (i = 0; i < visited.length; i++)
            visited[i] = false;
    }
}
