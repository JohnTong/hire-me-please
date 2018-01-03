package tong.objects;

public class SATPop {
    GeneticSAT[] candidateSolutions;
    int initPop;
    int numVars;

    public SATPop(int initPop, int numVars, boolean initialize) {
        this.initPop = initPop;
        this.numVars = numVars;
        candidateSolutions = new GeneticSAT[initPop];

        if(initialize) {
            for(int i = 0; i < initPop; i++) {
                GeneticSAT newInd = new GeneticSAT(numVars);
                newInd.createIndividual();
                newInd.evaluateIndividual();
                candidateSolutions[i] = newInd;
            }
        }
    }

    public GeneticSAT getSolution(int pos) { return candidateSolutions[pos]; }

    public int popSize() { return candidateSolutions.length; }

    public void setCandidateSolution(int pos, GeneticSAT solution) { candidateSolutions[pos] = solution; }

    public GeneticSAT getFittest() {
        GeneticSAT fittest = null;
        double fitness = 0;
        double workingFitness;

        for(int i = 0; i < popSize(); i++) {
            workingFitness = candidateSolutions[i].getFitness();
            if(workingFitness > fitness) {
                fittest = candidateSolutions[i];
            }
        }
        return fittest;

    }

    public int getNumVars() { return numVars; }

    public void sortByFitness() {

        for(int i = 0; i < candidateSolutions.length - 1; i++) {
            for(int j = i; j < (candidateSolutions.length - i) - 1; j++) {
                if(candidateSolutions[j].getFitness() < candidateSolutions[j + 1].getFitness()) {
                    GeneticSAT tmp = candidateSolutions[j];
                    candidateSolutions[j] = candidateSolutions[j + 1];
                    candidateSolutions[j + 1] = tmp;
                }
            }
        }
    }
}
