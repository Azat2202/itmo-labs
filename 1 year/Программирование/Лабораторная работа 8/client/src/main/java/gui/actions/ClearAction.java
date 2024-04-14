package gui.actions;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import dtp.User;
import gui.GuiManager;
import utility.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

public class ClearAction extends Action{
    public ClearAction(User user, Client client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.showOptionDialog(null,
                resourceBundle.getString("AreYouSure"),
                resourceBundle.getString("Confirmation"),
                JOptionPane.YES_NO_OPTION,
                QUESTION_MESSAGE,
                null,
                new String[]{resourceBundle.getString("Yes"), resourceBundle.getString("No")},
                resourceBundle.getString("No"));
        if(result == OK_OPTION){
            Response response = client.sendAndAskResponse(new Request("clear", "", user, GuiManager.getLocale()));
            if(response.getStatus() == ResponseStatus.OK) JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectsDeleted"), resourceBundle.getString("Result"), JOptionPane.PLAIN_MESSAGE);
            else JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectNotValid"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
