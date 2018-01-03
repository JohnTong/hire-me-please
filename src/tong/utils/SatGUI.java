package tong.utils;

import javax.swing.*;
import java.awt.*;

public class SatGUI extends JPanel {

    int[] variables;
    boolean[] evaluation;
    public SatGUI(int[] variables, boolean[] evaluation) {
        this.variables = new int[variables.length];
        for(int i = 0; i < this.variables.length; i++) {
            this.variables[i] = variables[i];
        }
        this.evaluation = new boolean[evaluation.length];
        for(int i = 0; i < this.evaluation.length; i++) {
            this.evaluation[i] = evaluation[i];
        }
        JTable table1 = makeVariableTable();
        JTabbedPane tabbedPane = new JTabbedPane();
        Component panel1 = makePanel(table1);
        tabbedPane.addTab("Variables", panel1);

        JTable table2 = makeClauseTable();
        Component panel4 = makePanel(table2);
        tabbedPane.addTab("Clauses", panel4);

        setLayout(new GridLayout(1,1));
        add(tabbedPane);

    }

    protected JTable makeClauseTable() {
        String[] columnNames = {"Clause", "Variables", "Evaluation"};

        Object[][] values = new Object[CnfUtil.getClauses().size()][columnNames.length];

        for(int i = 0; i < CnfUtil.getClauses().size(); i++) {
            StringBuilder sb = new StringBuilder();
            for(Integer in : CnfUtil.getClause(i).getVariables()) {
                sb.append(in);
                sb.append(" ");
            }

            values[i][0] = sb;
        }

        for(int i = 0; i < CnfUtil.getClauses().size(); i++) {
            StringBuilder sb = new StringBuilder();
            for(Integer in : CnfUtil.getClause(i).getVariables()) {
                int abs = Math.abs(in) - 1;
                sb.append(variables[abs]);
                sb.append(" ");
            }

            values[i][1] = sb;
        }

        for(int i = 0; i < CnfUtil.getClauses().size(); i++) {
            values[i][2] = String.valueOf(evaluation[i]);
        }

        return new JTable(values, columnNames);
    }

    protected JTable makeVariableTable() {
        String[] columnNames = {"Variable Number", "Value"};
        Object[][] values = new Object[variables.length][columnNames.length];

        for(int i = 0; i < variables.length; i++) {
            StringBuilder sb = new StringBuilder("P_");
            sb.append(i);
            values[i][0] = sb;
        }

        for(int i = 0; i < variables.length; i++) {
            values[i][1] = String.valueOf(variables[i]);
        }

        JTable table = new JTable(values, columnNames);

        return table;
    }

    protected Component makePanel(JTable table) {
        JPanel panel = new JPanel(false);
        //JEditorPane jEdit = new JEditorPane();
        JScrollPane jScrollPane1 = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        //jScrollPane1.getViewport().add(jEdit, null);
        panel.add(jScrollPane1, null);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(jScrollPane1);
        return panel;
    }
}
