package gui.actions;

import dtp.User;
import gui.GuiManager;
import models.StudyGroup;
import utility.Client;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class Action extends AbstractAction {
    protected ResourceBundle resourceBundle;

    protected User user;
    protected Client client;
    protected GuiManager guiManager;

    public Action(User user, Client client, GuiManager guiManager) {
        this.user = user;
        this.client = client;
        this.guiManager = guiManager;
        this.resourceBundle = ResourceBundle.getBundle("GuiLabels", guiManager.getLocale());
    }
}
