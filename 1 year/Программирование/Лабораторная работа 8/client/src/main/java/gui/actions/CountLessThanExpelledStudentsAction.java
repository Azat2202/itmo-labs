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

public class CountLessThanExpelledStudentsAction extends Action{
    public CountLessThanExpelledStudentsAction(User user, Client client, GuiManager guiManager) {
        super(user, client, guiManager);
    }
    private String askExpelled(){
        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);
        JLabel question = new JLabel(resourceBundle.getString("EnterExpelled"));
        JLabel markLabel = new JLabel(resourceBundle.getString("ExpelledStuds"));
        JFormattedTextField markField = new JFormattedTextField(DecimalFormat.getInstance());

        layout.addLayoutComponent(question, BorderLayout.NORTH);
        layout.addLayoutComponent(markLabel, BorderLayout.WEST);
        layout.addLayoutComponent(markField, BorderLayout.EAST);

        JOptionPane.showMessageDialog(null,
                markField,
                "Count",
                JOptionPane.PLAIN_MESSAGE);
        return markField.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Response response = client.sendAndAskResponse(new Request("count_less_than_expelled_students", this.askExpelled(), user, GuiManager.getLocale()));
        if(response.getStatus() == ResponseStatus.OK) JOptionPane.showMessageDialog(null, response.getResponse(), resourceBundle.getString("Result"), JOptionPane.PLAIN_MESSAGE);
        else JOptionPane.showMessageDialog(null, resourceBundle.getString("NoResult"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);

    }
}
