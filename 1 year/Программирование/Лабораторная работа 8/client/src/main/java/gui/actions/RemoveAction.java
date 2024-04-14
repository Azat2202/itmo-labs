package gui.actions;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import dtp.User;
import gui.GuiManager;
import models.StudyGroup;
import utility.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RemoveAction extends Action {

    public RemoveAction(User user, Client client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    private Integer getSelectedId() {
        Integer[] userOwnedIds = guiManager.getCollection().stream()
                .filter((s) -> s.getUserLogin().equals(user.name()))
                .map(StudyGroup::getId)
                .toArray(Integer[]::new);

        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);
        JLabel question = new JLabel(resourceBundle.getString("SelectIdForDelete"));
        JLabel idLabel = new JLabel(resourceBundle.getString("SelectId"));
        JComboBox idField = new JComboBox(userOwnedIds);

        layout.addLayoutComponent(question, BorderLayout.NORTH);
        layout.addLayoutComponent(idLabel, BorderLayout.WEST);
        layout.addLayoutComponent(idField, BorderLayout.EAST);

        JOptionPane.showMessageDialog(null,
                idField,
                resourceBundle.getString("Update"),
                JOptionPane.PLAIN_MESSAGE);
        return (Integer) idField.getSelectedItem();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Integer id = this.getSelectedId();
        if(id == null) JOptionPane.showMessageDialog(null, resourceBundle.getString("NoObjects"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        Response response = client.sendAndAskResponse(new Request("remove_by_id", id.toString(), user, GuiManager.getLocale()));
        if(response.getStatus() == ResponseStatus.OK) {
            JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectDeleted"), resourceBundle.getString("Ok"), JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectNotDeleted"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
