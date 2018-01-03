package tong.utils;

import tong.objects.Clause;

import java.util.ArrayList;

public class CnfUtil {

    private static ArrayList<Clause> clauses = new ArrayList<>();

    public static void addClause(Clause clause) { clauses.add(clause); }

    public static ArrayList<Clause> getClauses() { return clauses; }

    public static Clause getClause(int clauseNum) { return clauses.get(clauseNum); }

    public static void printClauses() {
        for(Clause c : clauses) {
            for(Integer i : c.getVariables()) {
                System.out.print(i + " ");
            }
            System.out.print("\n");
        }
    }

    private static int invert(int i) {
        i = i < 1 ? 1 : 0;

        return i;
    }
}

