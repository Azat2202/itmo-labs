package gui.actions;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import dtp.User;
import gui.GuiManager;
import models.*;
import models.Color;
import utility.Client;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.Date;

import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

public class UpdateAction extends Action {
    public UpdateAction(User user, Client client, GuiManager guiManager) {
        super(user, client, guiManager);
    }

    private Integer getSelectedId() {
        Integer[] userOwnedIds = guiManager.getCollection().stream()
                .filter((s) -> s.getUserLogin().equals(user.name()))
                .map(StudyGroup::getId)
                .toArray(Integer[]::new);

        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);
        JLabel question = new JLabel(resourceBundle.getString("SelectId"));
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

    private StudyGroup getObject(Integer id) {
        return guiManager.getCollection().stream()
                .filter((s) -> s.getId().equals(id))
                .toList().get(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Integer id = this.getSelectedId();
        updateJOptionWorker(id);
    }

    public void updateJOptionWorker(Integer id) {
        if(id == null) JOptionPane.showMessageDialog(null, resourceBundle.getString("NoObjects"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);

        if(!guiManager.getCollection().stream()
                .filter((i) -> i.getId().equals(id))
                .toList()
                .get(0)
                .getUserLogin()
                .equals(user.name())){
            JOptionPane.showMessageDialog(null,
                    resourceBundle.getString("ObjectNotYour"),
                    resourceBundle.getString("Error"),
                    JOptionPane.WARNING_MESSAGE
                    );
            return;
        }

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);


        JLabel mainLabel = new JLabel(resourceBundle.getString("GroupCreation"));
        JLabel nameLabel = new JLabel(resourceBundle.getString("Name"));
        JLabel cordXLabel = new JLabel(resourceBundle.getString("CoordX"));
        JLabel cordYLabel = new JLabel(resourceBundle.getString("CoordY"));
        JLabel studentsCountLabel = new JLabel(resourceBundle.getString("StudentsCount"));
        JLabel expelledStudentsLabel = new JLabel(resourceBundle.getString("ExpelledStuds"));
        JLabel averageMarkLabel = new JLabel(resourceBundle.getString("AverageMark"));
        JLabel formOfEducationLabel = new JLabel(resourceBundle.getString("StudyType"));
        JLabel personLabel = new JLabel(resourceBundle.getString("AdminCreation"));
        JLabel personNameLabel = new JLabel(resourceBundle.getString("AdminName"));
        JLabel personWeightLabel = new JLabel(resourceBundle.getString("AdminWeight"));
        JLabel personEyeColorLabel = new JLabel(resourceBundle.getString("EyeColor"));
        JLabel personHairColorLabel = new JLabel(resourceBundle.getString("HairColor"));
        JLabel personNationalityLabel = new JLabel(resourceBundle.getString("Nationality"));
        JLabel personLocationXLabel = new JLabel(resourceBundle.getString("CoordX"));
        JLabel personLocationYLabel = new JLabel(resourceBundle.getString("CoordY"));
        JLabel personLocationNameLabel = new JLabel(resourceBundle.getString("LocationName"));
        JFormattedTextField nameField;
        JFormattedTextField cordXField;
        JFormattedTextField cordYField;
        JFormattedTextField studentsCountField;
        JFormattedTextField expelledStudentsField;
        JFormattedTextField averageMarkField;
        JComboBox formOfEducationField;
        JFormattedTextField personNameField;
        JFormattedTextField personWeightField;
        JComboBox personEyeColorField;
        JComboBox personHairColorField;
        JComboBox personNationalityField;
        JFormattedTextField personLocationCordXField;
        JFormattedTextField personLocationCordYField;
        JFormattedTextField personLocationNameField;
        // Action Listeners
        {
            nameField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    if (text.trim().isEmpty()) {
                        throw new ParseException(resourceBundle.getString("FiledNotEmpty"), 0);
                    }
                    return super.stringToValue(text);
                }
            });
            cordXField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Float num;
                    try {
                        num = Float.parseFloat(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + "float", 0);
                    }
                    if (num <= -206) throw new ParseException(resourceBundle.getString("NumberMustBe") + " " + resourceBundle.getString("More") + " -206", 0);
                    return num;
                }
            });
            cordYField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Double num;
                    try {
                        num = Double.parseDouble(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "double", 0);
                    }
                    if (num > 463) throw new ParseException(resourceBundle.getString("MaxNum") + " 463", 0);
                    return num;
                }
            });
            studentsCountField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Long num;
                    try {
                        num = Long.parseLong(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "long", 0);
                    }
                    if (num <= 0) throw new ParseException(resourceBundle.getString("NumberMustBe") + resourceBundle.getString("More") + " 0", 0);
                    return num;
                }
            });
            expelledStudentsField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Long num;
                    try {
                        num = Long.parseLong(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "long", 0);
                    }
                    if (num <= 0) throw new ParseException(resourceBundle.getString("NumberMustBe") + resourceBundle.getString("More") + " 0", 0);
                    return num;
                }
            });
            averageMarkField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Long num;
                    try {
                        num = Long.parseLong(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "long", 0);
                    }
                    if (num <= 0) throw new ParseException(resourceBundle.getString("NumberMustBe") + resourceBundle.getString("More") + " 0", 0);
                    return num;
                }
            });
            formOfEducationField = new JComboBox<>(FormOfEducation.values());
            personNameField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    if (text.trim().isEmpty()) {
                        throw new ParseException(resourceBundle.getString("FiledNotEmpty"), 0);
                    }
                    return super.stringToValue(text);
                }
            });
            personWeightField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Integer num;
                    try {
                        num = Integer.parseInt(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "int", 0);
                    }
                    if (num <= 0) throw new ParseException(resourceBundle.getString("NumberMustBe") + resourceBundle.getString("More") + " 0", 0);
                    return num;
                }
            });
            personEyeColorField = new JComboBox(Color.values());
            personHairColorField = new JComboBox(Color.values());
            personNationalityField = new JComboBox(Country.values());
            personLocationCordXField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Double num;
                    try {
                        num = Double.parseDouble(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "float", 0);
                    }
                    return num;
                }
            });
            personLocationCordYField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    Long num;
                    try {
                        num = Long.parseLong(text);
                    } catch (NumberFormatException e) {
                        throw new ParseException(resourceBundle.getString("NumberType") + " " + "double", 0);
                    }
                    return num;
                }
            });
            personLocationNameField = new JFormattedTextField(new DefaultFormatter() {
                @Override
                public Object stringToValue(String text) throws ParseException {
                    if (text.trim().isEmpty()) {
                        throw new ParseException(resourceBundle.getString("FieldNotEmpty"), 0);
                    }
                    return super.stringToValue(text);
                }
            });
        }
        // Default Values
        {
            StudyGroup studyGroup = this.getObject(id);
            nameField.setValue(studyGroup.getName());
            cordXField.setValue(studyGroup.getCoordinates().getX());
            cordYField.setValue(studyGroup.getCoordinates().getY());
            studentsCountField.setValue(studyGroup.getStudentsCount());
            expelledStudentsField.setValue(studyGroup.getExpelledStudents());
            averageMarkField.setValue(studyGroup.getAverageMark());
            personNameField.setValue(studyGroup.getGroupAdmin().getName());
            personWeightField.setValue(studyGroup.getGroupAdmin().getWeight());
            personLocationCordXField.setValue(studyGroup.getGroupAdmin().getLocation().getX());
            personLocationCordYField.setValue(studyGroup.getGroupAdmin().getLocation().getY());
            personLocationNameField.setValue(studyGroup.getGroupAdmin().getLocation().getName());
        }
        // Group Layout
        {
            layout.setVerticalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                            .addComponent(mainLabel))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(nameLabel)
                            .addComponent(nameField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(cordXLabel)
                            .addComponent(cordXField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(cordYLabel)
                            .addComponent(cordYField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(studentsCountLabel)
                            .addComponent(studentsCountField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(expelledStudentsLabel)
                            .addComponent(expelledStudentsField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(averageMarkLabel)
                            .addComponent(averageMarkField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(formOfEducationLabel)
                            .addComponent(formOfEducationField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(personLabel))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(personNameLabel)
                            .addComponent(personNameField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(personWeightLabel)
                            .addComponent(personWeightField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(personEyeColorLabel)
                            .addComponent(personEyeColorField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(personHairColorLabel)
                            .addComponent(personHairColorField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(personNationalityLabel)
                            .addComponent(personNationalityField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(personLocationXLabel)
                            .addComponent(personLocationCordXField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(personLocationYLabel)
                            .addComponent(personLocationCordYField))
                    .addGroup(layout.createParallelGroup()
                            .addComponent(personLocationNameLabel)
                            .addComponent(personLocationNameField))

            );
            layout.setHorizontalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                            .addComponent(mainLabel)
                            .addComponent(nameLabel)
                            .addComponent(cordXLabel)
                            .addComponent(cordYLabel)
                            .addComponent(studentsCountLabel)
                            .addComponent(expelledStudentsLabel)
                            .addComponent(averageMarkLabel)
                            .addComponent(formOfEducationLabel)
                            .addComponent(personLabel)
                            .addComponent(personNameLabel)
                            .addComponent(personWeightLabel)
                            .addComponent(personEyeColorLabel)
                            .addComponent(personHairColorLabel)
                            .addComponent(personNationalityLabel)
                            .addComponent(personLocationXLabel)
                            .addComponent(personLocationYLabel)
                            .addComponent(personLocationNameLabel)
                    )
                    .addGroup(layout.createParallelGroup()
                            .addComponent(nameField)
                            .addComponent(cordXField)
                            .addComponent(cordYField)
                            .addComponent(studentsCountField)
                            .addComponent(expelledStudentsField)
                            .addComponent(averageMarkField)
                            .addComponent(formOfEducationField)
                            .addComponent(personNameField)
                            .addComponent(personWeightField)
                            .addComponent(personEyeColorField)
                            .addComponent(personHairColorField)
                            .addComponent(personNationalityField)
                            .addComponent(personLocationCordXField)
                            .addComponent(personLocationCordYField)
                            .addComponent(personLocationNameField)
                    ));
        }
        int result = JOptionPane.showOptionDialog(null, panel, resourceBundle.getString("Update"), JOptionPane.YES_OPTION,
                QUESTION_MESSAGE, null, new String[]{resourceBundle.getString("Update")}, resourceBundle.getString("Update"));
        if(result == OK_OPTION){
            StudyGroup newStudyGroup = new StudyGroup(
                    nameField.getText(),
                    new Coordinates(
                            Float.parseFloat(cordXField.getText()),
                            Double.parseDouble(cordYField.getText())
                    ),
                    new Date(),
                    Long.parseLong(studentsCountField.getText()),
                    Long.parseLong(expelledStudentsField.getText()),
                    Long.parseLong(averageMarkField.getText()),
                    (FormOfEducation) formOfEducationField.getSelectedItem(),
                    new Person(
                            personNameField.getText(),
                            Integer.parseInt(personWeightField.getText()),
                            (Color) personEyeColorField.getSelectedItem(),
                            (Color) personHairColorField.getSelectedItem(),
                            (Country) personNationalityField.getSelectedItem(),
                            new Location(
                                    Double.parseDouble(personLocationCordXField.getText()),
                                    Long.parseLong(personLocationCordYField.getText()),
                                    personLocationNameField.getText()
                            )
                    ),
                    user.name()
            );
            if(!newStudyGroup.validate()) {
                JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectNotValid"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            Response response = client.sendAndAskResponse(new Request("update", id.toString(), user, newStudyGroup, GuiManager.getLocale()));
            if(response.getStatus() == ResponseStatus.OK) JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectUpdated"), resourceBundle.getString("Ok"), JOptionPane.PLAIN_MESSAGE);
            else JOptionPane.showMessageDialog(null, resourceBundle.getString("ObjectNotChanged"), resourceBundle.getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
