package tong.objects;

import tong.workers.GeneticWorker;

import java.util.ArrayList;

public class Population {

    GeneticTour[] tours;
    int initialPop;

    public Population(int initialPop, boolean initialize) {

        tours = new GeneticTour[initialPop];
        this.initialPop = initialPop;
        if(initialize) {
            for (int i = 0; i < initialPop; i++) {
                GeneticTour tour = new GeneticTour();
                tour.createIndividual();
                tours[i] = tour;
            }
        }
    }

    public Population(Population clone) {
        tours = new GeneticTour[clone.populationSize()];
        initialPop = clone.initialPop;
        for(int i = 0; i < clone.tours.length;  i++) {
            GeneticTour tour = new GeneticTour();
            GeneticTour cloneTour = clone.getTour(i);
            for(int j = 0; j < cloneTour.getTourSize(); j++) {
                tour.setCity(j, cloneTour.getCity(j));
            }
            tours[i] = tour;
        }
    }

    public GeneticTour getTour(int pos) { return tours[pos]; }

    public void setTour(int pos, GeneticTour tour) { tours[pos] = tour; }

    public int populationSize() { return tours.length; }

    public GeneticTour getFittest() {
        GeneticTour fittestTour = null;
        double fittest = -1;
        double workingFitness;
        for(int i = 0; i < initialPop; i++) {
            workingFitness = tours[i].getTotalDistance();
            if(fittest < 0 || workingFitness < fittest) {
                fittest = workingFitness;
                fittestTour = tours[i];
            }
        }

        return fittestTour;
    }

    public void sortByFitness() {
        int n = tours.length;
        for( int i = 0; i < n - 1; i++) {
            for( int j = 0; j < n - i - 1; j++) {
                if(tours[j].getTotalDistance() > tours[j + 1].getTotalDistance()) {
                    GeneticTour temp = tours[j];
                    tours[j] = tours[j + 1];
                    tours[j + 1] = temp;
                }
            }
        }
    }
}
