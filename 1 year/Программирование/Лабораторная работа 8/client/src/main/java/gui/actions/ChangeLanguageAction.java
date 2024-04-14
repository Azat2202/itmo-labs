package gui.actions;

import dtp.User;
import gui.GuiManager;
import utility.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Locale;

public class ChangeLanguageAction extends Action{


    public ChangeLanguageAction(User user, Client client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox languages = new JComboBox(new Object[]{
                new Locale("ru", "RU"),
                Locale.GERMAN,
                new Locale("en", "AU"),
                new Locale("sv")
        });
        JOptionPane.showMessageDialog(null,
                languages,
                "Choose language",
                JOptionPane.INFORMATION_MESSAGE);
        guiManager.setLocale((Locale) languages.getSelectedItem());
    }
}
