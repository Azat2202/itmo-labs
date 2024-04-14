package gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import dtp.User;
import gui.actions.*;
import models.StudyGroup;
import utility.Client;

import javax.swing.Timer;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.*;

import static javax.swing.JOptionPane.*;


/*
 */

public class GuiManager {
    private final Client client;
    private static Locale locale = new Locale("ru");
    private final ClassLoader classLoader = this.getClass().getClassLoader();
    private DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("GuiLabels", GuiManager.getLocale());
    private final JFrame frame;
    private Panel panel;
    private JTable table = null;
    private StreamTableModel tableModel = null;
    private CartesianPanel cartesianPanel = null;
    private ArrayList<StudyGroup> tableData = null;
    private ArrayList<StudyGroup> collection = null;
    private FilterWorker filterWorker = new FilterWorker();
    private Map<JButton, String> buttonsToChangeLocale = new LinkedHashMap<>();
    private User user;

    private final static Color RED_WARNING = Color.decode("#FF4040");
    private final static Color GREEN_OK = Color.decode("#00BD39");

    String[] columnNames = {"id",
            "group_name",
            "cord",
            "creation_date",
            "students_count",
            "expelled_students",
            "average_mark",
            "form_of_education",
            "person_name",
            "person_weight",
            "person_eye_color",
            "person_hair_color",
            "person_nationality",
            "person_location",
            "person_location_name",
            "owner_login"
    };

    public GuiManager(Client client) {
        this.client = client;

        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.frame = new JFrame(resourceBundle.getString("LabWork8"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(this::run);

    }

    public GuiManager(Client client, User user) {
        this.client = client;
        this.user = user;
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.frame = new JFrame(resourceBundle.getString("LabWork8"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(this::run);

    }

    public void run(){
        panel = new Panel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        if(user == null) this.loginAuth();
        this.tableData = this.getTableDataStudyGroup();
        this.tableModel = new StreamTableModel(columnNames, tableData.size(), filterWorker);
        this.tableModel.setDataVector(tableData, columnNames);
        this.table = new JTable(tableModel);
        frame.setJMenuBar(this.createMenuBar());

        JButton tableExecute = new JButton(resourceBundle.getString("Table"));
        JButton cartesianExecute = new JButton(resourceBundle.getString("Coordinates"));


        new Timer(500, (i) ->{
            this.timerTrigger();
        }).start();

        // Выбрать столбец для сортировки
        table.getTableHeader().setReorderingAllowed(false);
        table.setDragEnabled(false);
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                int column = table.columnAtPoint(point);
                tableModel.performSorting(column);
                table.repaint();
            }
        });
        // Выбрать строку для изменения
        this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Integer id = tableModel.getRow(table.getSelectedRow()).getId();
                new UpdateAction(user, client, GuiManager.this).updateJOptionWorker(id);
            }
        });



        JScrollPane tablePane = new JScrollPane(table);
        this.cartesianPanel = new CartesianPanel(client, user, this);
        JPanel cardPanel = new JPanel();
        ImageIcon userIcon = new ImageIcon(new ImageIcon(classLoader.getResource("icons/user.png"))
                .getImage()
                .getScaledInstance(25, 25, Image.SCALE_AREA_AVERAGING));
        JLabel userLabel = new JLabel(user.name());
        userLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        userLabel.setIcon(userIcon);
        CardLayout cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        cardPanel.add(tablePane, "Table");
        cardPanel.add(cartesianPanel, "Cartesian");

        tableExecute.addActionListener((actionEvent) -> {
            cardLayout.show(cardPanel, "Table");
        });
        cartesianExecute.addActionListener((actionEvent) -> {
            this.cartesianPanel.reanimate();
            cardLayout.show(cardPanel, "Cartesian");
        });

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(cardPanel)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(tableExecute)
                                .addComponent(cartesianExecute)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(userLabel)
                                .addGap(5))));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(cardPanel)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(tableExecute)
                        .addComponent(cartesianExecute)
                        .addComponent(userLabel)
                        .addGap(5)));
        frame.add(panel);
        frame.setVisible(true);
    }

    public ArrayList<StudyGroup> getTableDataStudyGroup(){
        Response response = client.sendAndAskResponse(new Request("show", "", user, GuiManager.getLocale()));
        if(response.getStatus() != ResponseStatus.OK) return null;
        this.collection = new ArrayList<>(response.getCollection());
        return new ArrayList<>(response.getCollection());
    }

    private JMenuBar createMenuBar(){
        int iconSize = 40;

        JMenuBar menuBar = new JMenuBar();
        JMenu actions = new JMenu(resourceBundle.getString("Actions"));
        JMenuItem add = new JMenuItem(resourceBundle.getString("Add"));
        JMenuItem addIfMax = new JMenuItem(resourceBundle.getString("AddIfMax"));
        JMenuItem clear = new JMenuItem(resourceBundle.getString("Clear"));
        JMenuItem countByAverageMark = new JMenuItem(resourceBundle.getString("CountByAverageMark"));
        JMenuItem countLessThanExpelled = new JMenuItem(resourceBundle.getString("CountLessThanExpelledStudents"));
        JMenuItem executeScript = new JMenuItem(resourceBundle.getString("executeScript"));
        JMenuItem exit = new JMenuItem(resourceBundle.getString("Exit"));
        JMenuItem info = new JMenuItem(resourceBundle.getString("Info"));
        JMenuItem removeAllByAverageMark = new JMenuItem(resourceBundle.getString("removeByAverageMark"));
        JMenuItem removeGreater = new JMenuItem(resourceBundle.getString("removeGreater"));
        JMenuItem update = new JMenuItem(resourceBundle.getString("Update"));
        JMenuItem remove = new JMenuItem(resourceBundle.getString("Remove"));
        JMenuItem language = new JMenuItem(resourceBundle.getString("Language"));

        add.addActionListener(new AddAction(user, client, this));
        update.addActionListener(new UpdateAction(user, client, this));
        remove.addActionListener(new RemoveAction(user, client, this));
        addIfMax.addActionListener(new AddIfMaxAction(user, client, this));
        clear.addActionListener(new ClearAction(user, client, this));
        countByAverageMark.addActionListener(new CountByAverageMarkAction(user, client, this));
        executeScript.addActionListener(new ExecuteScriptAction(user, client, this));
        countLessThanExpelled.addActionListener(new CountLessThanExpelledStudentsAction(user, client, this));
        exit.addActionListener(new ExitAction(user, client, this));
        info.addActionListener(new InfoAction(user, client, this));
        removeAllByAverageMark.addActionListener(new RemoveAllByAverageMarkAction(user, client, this));
        removeGreater.addActionListener(new RemoveGreaterAction(user, client, this));
        language.addActionListener(new ChangeLanguageAction(user, client, this));

        //I hate swing :)
        //Wtf I need two Image Constructor for each icon I HATE THAT
        add.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/add.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        addIfMax.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/add_if_max.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        update.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/update.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        remove.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/remove.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        clear.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/clear.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        countByAverageMark.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/count_by_average_mark.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        countLessThanExpelled.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/count_less_than_expelled.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        exit.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/exit.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        info.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/info.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        removeAllByAverageMark.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/remove_all_by_average_mark.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        removeGreater.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/remove_greater.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        language.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/language.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));
        executeScript.setIcon(new ImageIcon(new ImageIcon(classLoader.getResource("icons/execute.png"))
                .getImage()
                .getScaledInstance(iconSize, iconSize, Image.SCALE_AREA_AVERAGING)));


        actions.add(add);
        actions.add(addIfMax);
        actions.addSeparator();
        actions.add(update);
        actions.addSeparator();
        actions.add(remove);
        actions.add(removeGreater);
        actions.add(removeAllByAverageMark);
        actions.add(clear);
        actions.addSeparator();
        actions.add(countByAverageMark);
        actions.add(countLessThanExpelled);
        actions.add(info);
        actions.addSeparator();
        actions.add(language);
        actions.addSeparator();
        actions.add(executeScript);
        actions.add(exit);

        menuBar.add(actions);

        JMenuItem clearFilters = new JMenuItem(resourceBundle.getString("ClearFilter"));
        JMenuItem idFilter = new JMenuItem("id");
        JMenuItem groupNameFilter = new JMenuItem("group_name");
        JMenuItem cordFilter = new JMenuItem("cord");
        JMenuItem creationDateFilter = new JMenuItem("creation_date");
        JMenuItem studentsCountFilter = new JMenuItem("students_count");
        JMenuItem expelledStudentsFilter = new JMenuItem("students_count");
        JMenuItem averageMarkFilter = new JMenuItem("expelled_students");
        JMenuItem formOfEducationFilter = new JMenuItem("average_mark");
        JMenuItem personNameFilter = new JMenuItem("person_name");
        JMenuItem personWeightFilter = new JMenuItem("person_weight");
        JMenuItem personEyeColorFilter = new JMenuItem("person_eyeColor");
        JMenuItem personHairColorFilter = new JMenuItem("person_hairColor");
        JMenuItem personNationalityFilter = new JMenuItem("person_nationality");
        JMenuItem personLocationNameFilter = new JMenuItem("person_location_name");
        JMenuItem personLocationCordsFilter = new JMenuItem("person_location");
        JMenuItem ownerLoginFilter = new JMenuItem("owner_login");

        clearFilters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterWorker.clearPredicates();
                tableModel.performFiltration();
                table.repaint();
            }
        });
        idFilter.addActionListener(new FilterListener(0, tableModel, table, filterWorker));
        groupNameFilter.addActionListener(new FilterListener(1, tableModel, table, filterWorker));
        cordFilter.addActionListener(new FilterListener(2, tableModel, table, filterWorker));
        creationDateFilter.addActionListener(new FilterListener(3, tableModel, table, filterWorker));
        studentsCountFilter.addActionListener(new FilterListener(4, tableModel, table, filterWorker));
        expelledStudentsFilter.addActionListener(new FilterListener(5, tableModel, table, filterWorker));
        averageMarkFilter.addActionListener(new FilterListener(6, tableModel, table, filterWorker));
        formOfEducationFilter.addActionListener(new FilterListener(7, tableModel, table, filterWorker));
        personNameFilter.addActionListener(new FilterListener(8, tableModel, table, filterWorker));
        personWeightFilter.addActionListener(new FilterListener(9, tableModel, table, filterWorker));
        personEyeColorFilter.addActionListener(new FilterListener(10, tableModel, table, filterWorker));
        personHairColorFilter.addActionListener(new FilterListener(11, tableModel, table, filterWorker));
        personNationalityFilter.addActionListener(new FilterListener(12, tableModel, table, filterWorker));
        personLocationNameFilter.addActionListener(new FilterListener(13, tableModel, table, filterWorker));
        personLocationCordsFilter.addActionListener(new FilterListener(14, tableModel, table, filterWorker));
        ownerLoginFilter.addActionListener(new FilterListener(15, tableModel, table, filterWorker));

        JMenu filters = new JMenu(resourceBundle.getString("Filters"));

        filters.add(clearFilters);
        filters.add(idFilter);
        filters.add(groupNameFilter);
        filters.add(cordFilter);
        filters.add(creationDateFilter);
        filters.add(studentsCountFilter);
        filters.add(expelledStudentsFilter);
        filters.add(averageMarkFilter);
        filters.add(formOfEducationFilter);
        filters.add(personNameFilter);
        filters.add(personWeightFilter);
        filters.add(personEyeColorFilter);
        filters.add(personHairColorFilter);
        filters.add(personNationalityFilter);
        filters.add(personLocationNameFilter);
        filters.add(personLocationCordsFilter);
        filters.add(ownerLoginFilter);

        menuBar.add(filters);
        return menuBar;
    }

    public void loginAuth(){
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        JLabel loginTextLabel = new JLabel(resourceBundle.getString("WriteLogin"));
        JTextField loginField = new JTextField();
        JLabel passwordTextLabel = new JLabel(resourceBundle.getString("EnterPass"));
        JPasswordField passwordField = new JPasswordField();
        JLabel errorLabel = new JLabel("");
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(loginTextLabel)
                        .addComponent(passwordTextLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(loginField)
                        .addComponent(passwordField)
                        .addComponent(errorLabel)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(loginTextLabel)
                        .addComponent(loginField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordTextLabel)
                        .addComponent(passwordField))
                        .addComponent(errorLabel));
        while(true) {
            int result = JOptionPane.showOptionDialog(null, panel, resourceBundle.getString("Login"), JOptionPane.YES_NO_OPTION,
                    QUESTION_MESSAGE, null, new String[]{resourceBundle.getString("Login"), resourceBundle.getString("Register")}, resourceBundle.getString("Login"));
            if (result == OK_OPTION) {
                if (!checkFields(loginField, passwordField, errorLabel)) continue;
                Response response = client.sendAndAskResponse(
                        new Request(
                                "ping",
                                "",
                                new User(loginField.getText(), String.valueOf(passwordField.getPassword())),
                                GuiManager.getLocale()));
                if (response.getStatus() == ResponseStatus.OK) {
                    errorLabel.setText(resourceBundle.getString("LoginAcc"));
                    errorLabel.setForeground(GREEN_OK);
                    this.user = new User(loginField.getText(), String.valueOf(passwordField.getPassword()));
                    return;
                } else {
                    errorLabel.setText(resourceBundle.getString("LoginNotAcc"));
                    errorLabel.setForeground(RED_WARNING);
                }
            } else if (result == NO_OPTION){
                if (!checkFields(loginField, passwordField, errorLabel)) continue;
                Response response = client.sendAndAskResponse(
                        new Request(
                                "register",
                                "",
                                new User(loginField.getText(), String.valueOf(passwordField.getPassword())),
                                GuiManager.getLocale()));
                if (response.getStatus() == ResponseStatus.OK) {
                    errorLabel.setText(resourceBundle.getString("RegAcc"));
                    errorLabel.setForeground(GREEN_OK);
                    this.user = new User(loginField.getText(), String.valueOf(passwordField.getPassword()));
                    return;
                } else {
                    errorLabel.setText(resourceBundle.getString("RegNotAcc"));
                    errorLabel.setForeground(RED_WARNING);
                }
            } else if (result == CLOSED_OPTION) {
                System.exit(666);
            }
        }
    }

    private boolean checkFields(JTextField loginField, JPasswordField passwordField, JLabel errorLabel){
        if(loginField.getText().isEmpty()) {
            errorLabel.setText(resourceBundle.getString("LoginNotNull"));
            errorLabel.setForeground(RED_WARNING);
            return false;
        } else if(String.valueOf(passwordField.getPassword()).isEmpty()){
            errorLabel.setText(resourceBundle.getString("PassNotNull"));
            errorLabel.setForeground(RED_WARNING);
            return false;
        }
        return true;
    }

    public Collection<StudyGroup> getCollection() {
        return collection;
    }

    public static Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        GuiManager.locale = locale;
        Locale.setDefault(locale);
        ResourceBundle.clearCache();
        resourceBundle = ResourceBundle.getBundle("GuiLabels", locale);
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
        this.buttonsToChangeLocale.forEach((i, j) -> i.setText(resourceBundle.getString(j)));
        this.tableData = this.getTableDataStudyGroup();
        this.tableModel.setDataVector(this.tableData, columnNames);
        this.tableModel.fireTableDataChanged();
        this.frame.remove(panel);
        this.frame.setTitle(resourceBundle.getString("LabWork8"));
        this.run();
    }

    public void repaintNoAnimation(){
        ArrayList<StudyGroup> newTableData = this.getTableDataStudyGroup();
        this.tableData = newTableData;
        this.tableModel.setDataVector(this.tableData, columnNames);
        this.tableModel.performFiltration();
        this.table.repaint();
        this.tableModel.fireTableDataChanged();
//        this.cartesianPanel.updateUserColors();
        this.cartesianPanel.reanimate(100);
    }

    public void timerTrigger(){
        ArrayList<StudyGroup> newTableData = this.getTableDataStudyGroup();
        if(!(this.tableData.equals(newTableData))) {
            this.tableData = newTableData;
            this.tableModel.setDataVector(this.tableData, columnNames);
            this.tableModel.performFiltration();
            this.table.repaint();
            this.tableModel.fireTableDataChanged();
            this.cartesianPanel.updateUserColors();
            this.cartesianPanel.reanimate();
        }
    }
}
