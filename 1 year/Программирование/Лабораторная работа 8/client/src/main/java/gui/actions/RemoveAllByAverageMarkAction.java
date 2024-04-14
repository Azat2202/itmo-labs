package gui.actions;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import dtp.User;
import gui.GuiManager;
import utility.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

public class RemoveAllByAverageMarkAction extends Action{
    public RemoveAllByAverageMarkAction(User user, Client client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    private String askAverageMark(){
        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);
        JLabel question = new JLabel(resourceBundle.getString("EnterAvgMark"));
        JLabel markLabel = new JLabel(resourceBundle.getString("AverageMark"));
        JFormattedTextField markField = new JFormattedTextField(DecimalFormat.getInstance());

        layout.addLayoutComponent(question, BorderLayout.NORTH);
        layout.addLayoutComponent(markLabel, BorderLayout.WEST);
        layout.addLayoutComponent(markField, BorderLayout.EAST);
        markField.setValue(5);
        JOptionPane.showMessageDialog(null,
                markField,
                "Remove",
                JOptionPane.PLAIN_MESSAGE);
        return markField.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Response response = client.sendAndAskResponse(new Request("remove_all_by_average_mark", this.askAverageMark(), user, GuiManager.getLocale()));
        if(response.getStatus() == ResponseStatus.OK) JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectDeleted"), resourceBundle.getString("Result"), JOptionPane.PLAIN_MESSAGE);
        else JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectNotDeleted"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
    }
}
