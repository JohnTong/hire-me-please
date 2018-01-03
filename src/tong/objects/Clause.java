package tong.objects;

import java.util.ArrayList;

public class Clause {
    ArrayList<Integer> variables = new ArrayList<>();

    public Clause(Integer varA, Integer varB, Integer varC) {
        variables.add(varA);
        variables.add(varB);
        variables.add(varC);
    }

    public ArrayList<Integer> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<Integer> vars) {
        for(Integer i : vars) {
            variables.add(i);
        }
    }

    public Integer getVariable(int pos) { return variables.get(pos); }
}
