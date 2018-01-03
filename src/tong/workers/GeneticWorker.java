package tong.workers;

import tong.objects.*;
import tong.utils.CnfUtil;
import tong.utils.DistanceUtil;
import tong.utils.TourUtil;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GeneticWorker {

    private static int initialParentSelectionPool; //That is a mouthful.
    private static double mutationRate;
    private static double agreementThreshold;

    private static int[][] agreeMatrix;
    private static int[][] satMatrix;
    private static int numExperts = 0;

    private static int[] crossoverMap;

    //Set the number of potential candidates to duke it out for procreation rights.
    public static void setSelectionPool(int pool) { initialParentSelectionPool = pool; }

    public static void initAgreementMatrix(int size) {
        agreeMatrix = new int[size][size];
    }

    public static void initSatMatrix(int size) { satMatrix = new int[2][size]; }
    public static void initExpertPanel(int size) { numExperts = size; }
    public static void setAgreementThreshold(double threshold) { agreementThreshold = threshold; }
    public static void setMutationRate(double rate) { mutationRate = rate; }

    public static Population evolve(Population pop, String crossover, boolean withWisdom, int gen) {
        Population newPop = new Population(pop.populationSize(), false);

        for(int i = 0; i < newPop.populationSize(); i++) {
            GeneticTour parentA = selectParent(pop);
            GeneticTour parentB = selectParent(pop);
            GeneticTour child;
            switch (crossover) {
                case "random":
                    child = randomCrossover(parentA, parentB);
                    mutate(child);
                    newPop.setTour(i, child);
                    break;
                case "heuristic":
                    child = heuristicCrossover(parentA, parentB);
                    mutate(child);
                    newPop.setTour(i, child);
                    break;
            }
        }

        if(withWisdom) {
            fillAgreementMatrix(newPop, gen);
        }

        return newPop;
    }

    public static SATPop evolve(SATPop pop) {
        SATPop newPop = new SATPop(pop.popSize(), pop.getNumVars(), false);

        //The two best solutions from the previous generation will always go to the next generation.
        pop.sortByFitness();

        newPop.setCandidateSolution(0, pop.getSolution(0));
        newPop.setCandidateSolution(1, pop.getSolution(1));

        GeneticSAT parentA = null;
        GeneticSAT parentB = null;
        GeneticSAT child;

        for(int i = 2; i < newPop.popSize(); i++) {

            if(i % 2 == 0) {
                parentA = selectParent(pop);
                parentB = selectParent(pop);
                child = crossover(parentA, parentB);
            } else {
                child = crossover(parentA, parentB);
            }
            mutate(child);
            child.evaluateIndividual();
            child.optimize();
            newPop.setCandidateSolution(i, child);

        }

        return  newPop;
    }

    public static void fillSatMatrix(SATPop pop) {
        pop.sortByFitness();

        for(int i = 0; i < numExperts; i++) {
            for(int k = 0; k < satMatrix[0].length; k++) {
                if(pop.getSolution(i).getVar(k) == 0) {
                    satMatrix[0][k]++;
                } else {
                    satMatrix[1][k]++;
                }
            }
        }
    }
    public static GeneticSAT wisdomSolution(int gen) {
        double totalExperts = numExperts * gen;
        int[] solutionSpace = new int[satMatrix[0].length];

        for(int i = 0; i < solutionSpace.length; i++) {
            solutionSpace[i] = -1;
        }

        boolean filled = true;

        for(int i = 0; i < satMatrix.length; i++) {
            for(int k = 0; k < solutionSpace.length; k++) {
                if(((double)satMatrix[i][k] / totalExperts) > agreementThreshold ) {
                    solutionSpace[k] = i;
                } else if(solutionSpace[k] == -1 && i == 1) {
                    filled = false;
                }
            }
        }

        if(!filled) {
            return null;
        } else {
            return new GeneticSAT(solutionSpace);
        }
    }

    public static boolean isSolved(SATPop pop) {

        if(pop.getFittest().getFitness() == CnfUtil.getClauses().size()) {
            return true;
        } else {
            return false;
        }
    }

    private static void initCrossoverMap(int size) {
        Random rand = new Random();
        crossoverMap = new int[size];
        for(int i = 0; i < crossoverMap.length; i++) {
            crossoverMap[i] = rand.nextInt(2);
        }
    }

    private static GeneticSAT crossover(GeneticSAT parentA, GeneticSAT parentB) {
        int[] newSolution = new int[parentA.getSize()];
        double uniformSelection;

        for(int i = 0; i < newSolution.length; i++) {
            uniformSelection = Math.random();

            if(uniformSelection > 0.5) {
                newSolution[i] = parentB.getVar(i);
            } else {
                newSolution[i] = parentA.getVar(i);
            }
        }

        GeneticSAT child = new GeneticSAT(newSolution);

        return child;
    }


    private static GeneticTour randomCrossover(GeneticTour parentA, GeneticTour parentB) {

        GeneticTour child = new GeneticTour();

        int start = (int) Math.random() * parentA.getTourSize();
        int end = (int) (Math.random() * (parentA.getTourSize() - start) + start);

        for(int i = 0; i < end; i++) {
            if(i >= start && i < end) {
                child.setCity(i, parentA.getCity(i));
            }
        }

        for(int i = 0; i < parentB.getTourSize(); i++) {

            if(!child.contains(parentB.getCity(i))) {

                for (int j = 0; j < child.getTourSize(); j++) {
                    if (child.getCity(j) == null) {
                        child.setCity(j, parentB.getCity(i));
                        break;
                    }
                }
            }
        }

        return child;
    }

    /*
    This crossover selects a random city for the child starting point and then finds the closest city to it from its parents.
    If the closest city is already present in the child's tour, a random, unvisited city is selected.
     */
    private static GeneticTour heuristicCrossover(GeneticTour parentA, GeneticTour parentB) {

        GeneticTour child = new GeneticTour();
        ArrayList<City> unvisitedCities = new ArrayList<>();


        for(int i = 0; i < TourUtil.numberOfCities(); i++) {
            unvisitedCities.add(TourUtil.getCity(i));
        }

        int randomCity = (int) (Math.random() * (TourUtil.numberOfCities() - 1));
        City nextCityParentA;
        double aDistance;
        City nextCityParentB;
        double bDistance;
        City nextCity;

        child.setCity(0, TourUtil.getCity(randomCity));
        unvisitedCities.remove(TourUtil.getCity(randomCity));

        for(int i = 1; i < child.getTourSize(); i++) {
            if(parentA.getCityPosition(child.getCity(i - 1)) + 1  != parentA.getTourSize()) {
                nextCityParentA = parentA.getCity(parentA.getCityPosition(child.getCity(i - 1)) + 1);
            } else {
                nextCityParentA = parentA.getCity(0);
            }
            aDistance = DistanceUtil.calculateDistance(child.getCity(i - 1), nextCityParentA);
            if(parentB.getCityPosition(child.getCity(i - 1)) + 1 != parentB.getTourSize()) {
                nextCityParentB = parentB.getCity(parentB.getCityPosition(child.getCity(i - 1)) + 1);
            } else {
                nextCityParentB = parentB.getCity(0);
            }
            bDistance = DistanceUtil.calculateDistance(child.getCity(i - 1), nextCityParentB);

            nextCity = aDistance < bDistance ? nextCityParentA : nextCityParentB;

            if(!child.contains(nextCity)) {
                child.setCity(i, nextCity);
                unvisitedCities.remove(nextCity);
            } else {
                int random = (int) (Math.random() * (unvisitedCities.size() - 1));
                child.setCity(i, unvisitedCities.get(random));
                unvisitedCities.remove(random);

            }
        }

        return child;
    }

    /*
    Increments a cell in the matrix when an expert agrees that city i and j connect. Later generations experts "opinions"
    are weighed higher than earlier generations.
     */
    private static void fillAgreementMatrix(Population pop, int gen) {
        ArrayList<GeneticTour> experts = selectExperts(pop);
        int i, j;
        for(GeneticTour t : experts) {
            for(int k = 0; k < t.getTourSize() - 1; k++) {
                i = t.getCity(k).getCityNum() - 1;
                j = t.getCity(k + 1).getCityNum() - 1;
                agreeMatrix[i][j]++;
            }
            i = t.getCity(t.getTourSize() - 1).getCityNum() - 1;
            j = t.getCity(0).getCityNum() - 1;
            agreeMatrix[i][j] = agreeMatrix[i][j] + ((1 * gen) / 100);
        }
    }
    /*
    Divides each cell of the matrix by the distance between i and j. The idea being that fewer votes, but a short distance
    will be weighed similarly to many votes with a longer distance.
     */
    public static void transformMatrix(ArrayList<City> cityList) {
        for(int i = 0; i < cityList.size(); i++) {
            for(int j = 0; j < cityList.size(); j++) {
                agreeMatrix[i][j] /= DistanceUtil.calculateDistance(cityList.get(i), cityList.get(j));
            }
        }
    }

    public static GeneticTour wisdomTour() {
        int startingCity = 0;
        int cost = -1;
        GeneticTour tourByWisdom = new GeneticTour();
        for(int i = 0; i < agreeMatrix.length; i++) {
            for(int j = 0; j < agreeMatrix.length; j++) {
                if(cost <= 0 || agreeMatrix[i][j] > cost) {
                    cost = agreeMatrix[i][j];
                    startingCity = i;
                }
            }
        }

        tourByWisdom.setCity(0, TourUtil.getCity(startingCity));

        int prevCity = startingCity;

        for(int i = 1; i < agreeMatrix.length; i++) {
            int nextCity = 0;
            cost = -1;
            for(int j = 0; j < agreeMatrix.length; j++) {
                if((cost <= 0 || agreeMatrix[prevCity][j] > cost) && !tourByWisdom.contains(TourUtil.getCity(j))) {
                    cost = agreeMatrix[prevCity][j];
                    nextCity = j;
                }
            }

            tourByWisdom.setCity(i, TourUtil.getCity(nextCity));
            prevCity = nextCity;
        }

        return tourByWisdom;
    }

    private static GeneticTour fixIntersections(GeneticTour tour) {
        ArrayList<City> array = tour.getTour();
        boolean done;

        do {
            done = true;

            for(int i = 0; i < array.size() - 1; i++) {

                if(!done) {
                    break;
                }
                double x1 = array.get(i).getX();
                double y1 = array.get(i).getY();
                double x2 = array.get(i + 1).getX();
                double y2 = array.get(i + 1).getY();

                for(int j = i + 2; j < array.size() - 1; j++) {

                    double x3 = array.get(j).getX();
                    double y3 = array.get(j).getY();
                    double x4 = array.get(j + 1).getX();
                    double y4 = array.get(j + 1).getY();

                    if(Line2D.linesIntersect(x1, y1, x2, y2, x3, y3, x4, y4)) {
                        done = false;
                        City tmp = array.get(i + 1);
                        array.set(i +1, array.get(j));
                        array.set(j, tmp);
                        break;
                    }

                }
            }
        } while(!done);

        return new GeneticTour(array);
    }

    public static void printMatrix() {
        for(int i = 0; i < agreeMatrix.length; i++) {
            for(int j = 0; j < agreeMatrix.length; j++) {
                System.out.print(agreeMatrix[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    private static ArrayList<GeneticTour> selectExperts(Population pop) {
        ArrayList<GeneticTour> experts = new ArrayList<>(numExperts);
        pop.sortByFitness();

        for(int i = 0; i < numExperts; i++) {
            experts.add(pop.getTour(i));
        }

        return experts;
    }

    /*
    We will randomly select n parents from the population pool and then get the fittest from that to use for evolution
    purposes.
     */
    private static GeneticTour selectParent(Population initialPop) {
        Population parentPop = new Population(initialParentSelectionPool, false);

        for(int i = 0; i < initialParentSelectionPool; i++) {
            int randPos = (int) (Math.random() * initialPop.populationSize());
            parentPop.setTour(i, initialPop.getTour(randPos));
        }

        GeneticTour fittestTour = parentPop.getFittest();

        return fittestTour;
    }

    private static GeneticSAT selectParent(SATPop pop) {
        SATPop potentialParents = new SATPop(initialParentSelectionPool, pop.getNumVars(), false);

        for(int i = 0; i < initialParentSelectionPool; i++) {
            int randPos = (int)(Math.random() * potentialParents.popSize());
            potentialParents.setCandidateSolution(i, pop.getSolution(randPos));
        }

        GeneticSAT fittestSolution = potentialParents.getFittest();

        return fittestSolution;
    }

    public static void mutate(GeneticSAT solution) {
        for(int i = 0; i < solution.getSize(); i++) {
            if(Math.random() < mutationRate) {
                solution.invertVariable(i);
            }
        }
    }

    /*
    Check each position to see if it should mutate. To prevent duplication of cities, in this case, mutation is random swapping of
    elements.
     */
    private static void mutate(GeneticTour tour) {

        for(int pos1 = 0; pos1 < tour.getTourSize(); pos1 ++) {
            if(Math.random() < mutationRate ) {

                int pos2 = (int)(tour.getTourSize() * Math.random());

                City city1 = tour.getCity(pos1);
                City city2 = tour.getCity(pos2);

                tour.setCity(pos1, city2);
                tour.setCity(pos2, city1);
            }

        }
    }
}
