package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.Collectors;

public class FilterListener implements ActionListener {
    private int row;
    private StreamTableModel tableModel;
    private FilterWorker filterWorker;
    private JTable table;

    public FilterListener(int row, StreamTableModel tableModel, JTable table,  FilterWorker filterWorker){
        this.row = row;
        this.tableModel = tableModel;
        this.filterWorker = filterWorker;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JList<?> jList = new JList(tableModel.getAllData().stream()
                .map(o -> tableModel.getValueAtRow(o, row))
                .distinct()
                .toArray());
        JOptionPane.showMessageDialog(null, jList);
        if(jList.getSelectedValuesList().isEmpty()) return;
        filterWorker.parsePredicate(row, jList.getSelectedValuesList());
        tableModel.performFiltration();
        table.repaint();
    }
}
