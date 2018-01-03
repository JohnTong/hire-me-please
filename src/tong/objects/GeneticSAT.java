package tong.objects;

import tong.utils.CnfUtil;

import java.util.Random;

public class GeneticSAT {

    int[] vars;
    boolean[] clauses;
    int fitness = 0;

    public GeneticSAT(int numVars) {
        vars = new int[numVars];
        clauses = new boolean[CnfUtil.getClauses().size()];
    }

    public GeneticSAT(int[] vars) {
        this.vars = new int[vars.length];
        clauses = new boolean[CnfUtil.getClauses().size()];
        for(int i = 0; i < vars.length; i++) {
            this.vars[i] = vars[i];
        }
    }
    public void createIndividual() {
        Random r = new Random();
        for(int i = 0; i < vars.length; i++) {
            vars[i] = r.nextInt(2);
        }
        evaluateIndividual();
        optimize();
    }

    public void optimize() {
        int improved = 1;

        while(improved > 0) {
            improved = 0;

            for(int i = 0; i < vars.length; i++) {
                int prevFitness = fitness;
                vars[i] = invertBit(vars[i]);
                evaluateIndividual();

                if(prevFitness >= fitness) {
                    vars[i] = invertBit(vars[i]);
                    fitness = prevFitness;
                } else {
                    improved++;
                }
            }
        }
    }

    private int invertBit(int bit) {
        int invertedBit = bit == 0 ? 1 : 0;

        return invertedBit;
    }

    public void invertVariable(int pos) {
        vars[pos] = vars[pos] == 1 ? 0 : 1;
    }

    public void setVar(int pos, int var) { vars[pos] = var; }

    public int getVar(int pos) { return vars[pos]; }

    public boolean[] getClauses() {
        return clauses;
    }

    public void evaluateIndividual() {
        Clause clause;
        int eval;
        int trueClauses = 0;
        int absVal;
        for(int i = 0; i < CnfUtil.getClauses().size(); i++) {
            eval = 0;
            clause = CnfUtil.getClause(i);
            for(Integer varNum : clause.getVariables()) {
                if(varNum < 0) {
                    absVal = Math.abs(varNum);
                    eval += invertBit(vars[absVal - 1]);
                } else {
                    eval += vars[varNum - 1];
                }
            }

            if(eval > 0) {
                trueClauses++;
                clauses[i] = true;
            }
        }
        fitness = trueClauses;
    }

    public boolean isClauseTrue(int pos) { return clauses[pos]; }

    public double getFitness() { return fitness; }

    public int getSize() {return vars.length; }

    public int[] getVars() { return vars; }

    public void printVars() {
        for(int i = 0; i < vars.length; i++) {
            System.out.print(vars[i] + " ");
        }

        System.out.print("\n" + fitness + " of " + CnfUtil.getClauses().size() + " clauses evaluated true.");
    }
}
