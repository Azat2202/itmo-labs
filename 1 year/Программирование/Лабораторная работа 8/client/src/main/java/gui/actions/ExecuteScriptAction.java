package gui.actions;

import commandLine.Console;
import dtp.User;
import gui.GuiManager;
import utility.Client;
import utility.ExecuteFileManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.*;

public class ExecuteScriptAction extends Action{
    public ExecuteScriptAction(User user, Client client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        JLabel fileAsker = new JLabel(resourceBundle.getString("SelectFile"));
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(fileAsker)
                        .addComponent(fileChooser)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                    .addComponent(fileAsker)
                    .addComponent(fileChooser));

        int option = JOptionPane.showOptionDialog(null,
                panel,
                resourceBundle.getString("ScriptExecute"),
                JOptionPane.YES_NO_OPTION,
                QUESTION_MESSAGE,
                null,
                new String[]{resourceBundle.getString("Yes"), resourceBundle.getString("No")},
                resourceBundle.getString("Yes"));
        if(option == OK_OPTION) {
            try {
                Console.setFileMode(true);
                new ExecuteFileManager(new Console(), client, user).fileExecution(fileChooser.getSelectedFile().getAbsolutePath());
                Console.setFileMode(false);
            } catch (Exception ignored) {}
        }
    }
}
