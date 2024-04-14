package gui;

import dtp.Request;
import dtp.Response;
import dtp.User;
import gui.actions.UpdateAction;
import models.Coordinates;
import models.StudyGroup;
import utility.Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.*;
import java.util.stream.Collectors;

class CartesianPanel extends JPanel implements ActionListener {
    private Client client;
    private User user;
    private GuiManager guiManager;
    private LinkedHashMap<Rectangle, Integer> rectangles = new LinkedHashMap<>();
    private Timer timer;
    private Map<String, Color> users;
    private int step;
    private Collection<StudyGroup> collection;

    private float maxCordX;
    private Double maxCordY;
    private BufferedImage img = null;
    private boolean isDragging = false;
    private boolean skip_animation = false;

    private Point mouseDragOldPoint; // Переменная для drag`n`drop
    private Rectangle mouseDragRectangle;
    private StudyGroup mouseDragObject;

    {
        try {
            this.img = ImageIO.read(getClass().getClassLoader().getResource("icons/tyler.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CartesianPanel(Client client, User user, GuiManager guiManager){
        super();
        this.client = client;
        this.user = user;
        this.guiManager = guiManager;
        this.step = 0;
        this.timer = new Timer(1, this);
        timer.start();
        updateUserColors();
        // Изменение по дабл клику
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() != 2) return;
                Rectangle toClick;
                try {
                    toClick = rectangles.keySet().stream()
                            .filter(r -> r.contains(e.getPoint()))
                            .sorted(Comparator.comparing(Rectangle::getX).reversed())
                            .toList().get(0);
                } catch (ArrayIndexOutOfBoundsException k) {
                    return;
                }
                Integer id = rectangles.get(toClick);
                new UpdateAction(user, client, guiManager).updateJOptionWorker(id);
            }
        });
        // Drag`n`drop объектов
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    mouseDragOldPoint = e.getPoint();
                    mouseDragRectangle = rectangles.keySet().stream()
                            .filter(r -> r.contains(mouseDragOldPoint))
                            .sorted(Comparator.comparing(Rectangle::getX).reversed())
                            .toList().get(0);
                    Integer id = rectangles.get(mouseDragRectangle);
                    mouseDragObject = collection.stream()
                            .filter((s) -> s.getId().equals(id))
                            .toList().get(0);
                    if(!mouseDragObject.getUserLogin().equals(user.name())) return;
                    isDragging = true;
                } catch (ArrayIndexOutOfBoundsException err) {return;}
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(!mouseDragObject.getUserLogin().equals(user.name())) return;
                super.mouseReleased(e);
                if(!isDragging) return;
                int width = getWidth();
                int halfWidth = width / 2;
                int height = getHeight();
                int halfHeight = height / 2;
                int elementWidth = 130;
                int elementHeight = 130;
//                System.out.print(mouseDragOldPoint.getX());
//                System.out.print("   ");
//                System.out.println(mouseDragOldPoint.getY());
                mouseDragObject.setCoordinates(new Coordinates(
                        (float) ((maxCordX / (halfWidth - elementWidth)) * (e.getX() - halfWidth)),
                        (maxCordY / (halfHeight - elementHeight)) * (e.getY() - halfHeight)));
                client.sendAndAskResponse(new Request("update", String.valueOf(mouseDragObject.getId()), user, mouseDragObject, GuiManager.getLocale()));
                guiManager.repaintNoAnimation();
                mouseDragOldPoint = e.getPoint();
                isDragging = false;
                skip_animation = true;
            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                return;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if(!mouseDragObject.getUserLogin().equals(user.name())) return;
                int width = getWidth();
                int halfWidth = width / 2;
                int height = getHeight();
                int halfHeight = height / 2;
                int elementWidth = 130;
                int elementHeight = 130;
                collection.stream()
                        .filter(s -> s.getId().equals(mouseDragObject.getId()))
                        .forEach(s -> s.setCoordinates(new Coordinates(
                                        (float) ((maxCordX / (halfWidth - elementWidth)) * (e.getX() - halfWidth)),
                                        (maxCordY / (halfHeight - elementHeight)) * (e.getY() - halfHeight))));
                CartesianPanel.this.repaint();
            }
        });
    }

    public void updateUserColors() {
        Random random = new Random();
        Response response = client.sendAndAskResponse(new Request("show", "", user, GuiManager.getLocale()));
        this.users = response.getCollection().stream()
                .map(StudyGroup::getUserLogin)
                .distinct()
                .collect(Collectors.toMap(
                        s -> s, s -> {
                            int red = random.nextInt(25) * 10;
                            int green = random.nextInt(25) * 10;
                            int blue = random.nextInt(25) * 10;
                            return new Color(red, green, blue);
                        }));
        /* ЭТО ОЧЕНЬ СТРАШНЫЙ МЕТОД
        ОН НУЖЕН ЧТОБЫ СОСЕДНИЕ КВАДРАТЫ СДВИГАЛИСЬ
        НО ОН РАБОТАЕТ ЗА О(n^2) а можно СПОКОЙНО написать за О(n)
        Н-О М-Н-Е Л-Е-Н-Ь
         */
        float delta = 0.2F;
        while(response.getCollection().stream().map(StudyGroup::getCoordinates).distinct().count() < response.getCollection().size()){
            for(StudyGroup studyGroup : response.getCollection()){
                if(response.getCollection().stream()
                        .anyMatch((i) -> i.getCoordinates().equals(studyGroup.getCoordinates())
                                && !i.getId().equals(studyGroup.getId()))){
                    studyGroup.getCoordinates().setX(studyGroup.getCoordinates().getX() + delta);
                    studyGroup.getCoordinates().setY(studyGroup.getCoordinates().getY() + delta);
                    break;
                }
            }
        }
        this.collection = response.getCollection();
        this.maxCordX = this.collection.stream()
                .map(StudyGroup::getCoordinates)
                .map(Coordinates::getX)
                .map(Math::abs)
                .max(Float::compareTo)
                .orElse(0F);
        this.maxCordY = this.collection.stream()
                .map(StudyGroup::getCoordinates)
                .map(Coordinates::getY)
                .map(Math::abs)
                .max(Double::compareTo)
                .orElse(0D);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setFont(new Font("Tahoma", Font.BOLD, 45));

        int width = getWidth();
        int height = getHeight();

        // Draw x-axis
        g2.drawLine(0, height / 2, width, height / 2);

        // Draw y-axis
        g2.drawLine(width / 2, 0, width / 2, height);

        // Draw arrows
        g2.drawLine(width - 10, height / 2 - 5, width, height / 2);
        g2.drawLine(width - 10, height / 2 + 5, width, height / 2);
        g2.drawLine(width / 2 - 5, 10, width / 2, 0);
        g2.drawLine(width / 2 + 5, 10, width / 2, 0);
        this.paintRectangles(g2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(step == 100) timer.stop();
        else{
            step += 2;
            step = Math.min(step, 100);
            repaint();
        }
    }

    private void paintRectangles(Graphics2D g2){
        int width = getWidth();
        int halfWidth = width / 2;
        int height = getHeight();
        int halfHeight = height / 2;
        int elementWidth = 130;
        int elementHeight = 130;
        if(skip_animation == true) {
            this.step = 100;
            this.skip_animation = false;
        }
        if(step == 100) this.rectangles = new LinkedHashMap<>();
        this.collection.stream().sorted(StudyGroup::compareTo).forEach(studyGroup -> {
            int dx1 = (int) ((halfWidth + (studyGroup.getCoordinates().getX() * step / 100 / maxCordX * (halfWidth - elementWidth))));
            int dx2 = (int) ((halfHeight + (studyGroup.getCoordinates().getY() * step / 100 / maxCordY * (halfHeight - elementHeight))));
            if(step == 100) {
                this.rectangles.put(new Rectangle(dx1 - elementWidth / 2 - 1,
                        dx2 - elementHeight / 2 - 1,
                        elementWidth + 2,
                        elementHeight + 2), studyGroup.getId());
            }
            //Image
            g2.drawImage(img,
                    dx1 - elementWidth / 2,
                    dx2 - elementHeight / 2,
                    dx1 + elementWidth / 2,
                    dx2 + elementHeight / 2,
                    0,
                    0,
                    img.getWidth(),
                    img.getHeight(),
                    null
            );
            //Border
            g2.setColor(users.get(studyGroup.getUserLogin()));
            g2.drawRect(dx1 - elementWidth / 2 - 1,
                    dx2 - elementHeight / 2 - 1,
                    elementWidth + 2,
                    elementHeight + 2);
            g2.setColor(Color.WHITE);
            //Numbers
            g2.drawString(studyGroup.getId().toString(),
                    dx1 - elementWidth / 4,
                    dx2 + elementHeight / 4
                    );
        });
    }

    public void reanimate(){
        this.step = 0;
        this.timer.start();
    }

    public void reanimate(int step){
        this.step = 100;
        repaint();
    }

}