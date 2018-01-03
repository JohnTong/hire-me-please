package tong.main;

import tong.objects.GeneticSAT;
import tong.objects.SATPop;
import tong.utils.CnfUtil;
import tong.utils.SatGUI;
import tong.workers.FileReader;
import tong.workers.GeneticWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;

public class NPSolver {

    public static void main(String[] args) {
        File file = new File(args[0]);

        try {
            FileReader fr = new FileReader();

            fr.satFileReader(file);
        } catch(FileNotFoundException fnfe) {
            System.out.println("Invalid File Name: " + fnfe);
        }
        GeneticWorker.setSelectionPool(10);
        GeneticWorker.initExpertPanel(3);
        GeneticWorker.initSatMatrix(100);
        GeneticWorker.setAgreementThreshold(.45);
        GeneticWorker.setMutationRate(.1);

        int GA = 0;
        int WOC = 0;
        int fail = 0;
        GeneticSAT wisdomSat = null;
        long time = System.currentTimeMillis();

            SATPop pop = new SATPop(10, 100, true);
            GeneticWorker.fillSatMatrix(pop);
            int i = 0;

            while (i < 10000 && !GeneticWorker.isSolved(pop) && (wisdomSat == null || wisdomSat.getFitness() != CnfUtil.getClauses().size())) {
                pop = GeneticWorker.evolve(pop);
                GeneticWorker.fillSatMatrix(pop);
                i++;
                System.out.println("Generation: " + i + " Fittest: " + pop.getFittest().getFitness());
                wisdomSat = GeneticWorker.wisdomSolution(i);
                if (wisdomSat != null) {
                    GeneticWorker.mutate(wisdomSat);
                    wisdomSat.optimize();
                    System.out.println("Wisdom fitness: " + wisdomSat.getFitness());
                }
            }
            SatGUI gui;
            if (i < 10000) {
                if (pop.getFittest().getFitness() == CnfUtil.getClauses().size()) {
                    GA++;
                    System.out.println("Solution found via GA.");
                    pop.getFittest().printVars();
                    //gui = new SatGUI(pop.getFittest().getVars(), pop.getFittest().getClauses());
                } else {
                    System.out.println("Solution found via Wisdom.");
                    wisdomSat.printVars();
                    //gui = new SatGUI(wisdomSat.getVars(), wisdomSat.getClauses());
                    WOC++;
                }
            } else {
                //System.out.println("Solution not found. Experiment terminated.");
                gui = null;
                fail++;
            }
        long finalTime = System.currentTimeMillis();
        System.out.println("Finished in: " + (finalTime - time) + "ms");
        /*
        if(gui != null) {
            JFrame frame = new JFrame("Demo");
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            frame.getContentPane().add(gui, BorderLayout.CENTER);
            frame.setSize(1000, 1000);
            frame.setVisible(true);
        }
        */
    }

}
