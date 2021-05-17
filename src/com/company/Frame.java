package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

//import java.awt.event.MouseListener;
/*TODO
* [x]Выставление начальных условий по мышке
* [x]Выбор скорости апдейта
* []Выбор размера поля
* []Пошаговые действия
* []Заменить на другую тему
*
* */
public class Frame extends JFrame {

    private JButton pauseButton;
    private JButton startButton;

    private JTextField maxMass,
                        maxCompress,
            minMass,
            minFlow,
            maxSpeed;
    private JButton setButton;

    private JToolBar toolBar;
    private JSlider speedSlider;
    private JSlider brushSizeSlider;
    private boolean LMDragged = false;
    private boolean RMDragged = false;
    int cellSize;
    public Frame() {
        super("Game of life");
        setLayout(new BorderLayout());
        cellSize = 4;

        pauseButton = new JButton(new ImageIcon("./img/pause.png"));
        startButton = new JButton(new ImageIcon("./img/start.png"));

        pauseButton.setBorderPainted(false);
        startButton.setBorderPainted(false);

        speedSlider = new JSlider(1, 1000);
        speedSlider.setOrientation(JSlider.HORIZONTAL);
        speedSlider.setInverted(true);
        speedSlider.setPaintTicks(true);
        Hashtable<Integer,JLabel> speedLabels = new Hashtable<Integer,JLabel>();
        speedLabels.put(1, new JLabel("Fast"));
        speedLabels.put(1000, new JLabel("Slow"));
        speedSlider.setLabelTable(speedLabels);
        speedSlider.setPaintLabels(true);
        speedSlider.setPreferredSize(new Dimension(100, 45));

        brushSizeSlider = new JSlider(cellSize, cellSize*10);
        brushSizeSlider.setOrientation(JSlider.HORIZONTAL);
        brushSizeSlider.setPaintTicks(true);
        Hashtable<Integer,JLabel> brushSizeLabels = new Hashtable<Integer,JLabel>();
        brushSizeLabels.put(cellSize, new JLabel(cellSize + " pix"));
        brushSizeLabels.put(cellSize*10, new JLabel(cellSize*10 + " pix"));
        brushSizeSlider.setLabelTable(brushSizeLabels);
        brushSizeSlider.setPaintLabels(true);
        brushSizeSlider.setPreferredSize(new Dimension(100, 45));

        maxMass = new JTextField("1");
        maxCompress = new JTextField("0.25");
        minMass = new JTextField("0.0001");
        minFlow = new JTextField("0.005");
        maxSpeed = new JTextField("4");
        setButton = new JButton("set val");

        toolBar = new JToolBar("ToolBar");
        toolBar.add(pauseButton);
        toolBar.add(startButton);
        toolBar.add(speedSlider);
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        toolBar.add(brushSizeSlider);
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));

        toolBar.add(maxMass);
        toolBar.add(maxCompress);
        toolBar.add(minMass);
        toolBar.add(minFlow);
        toolBar.add(maxSpeed);
        toolBar.add(setButton);

        toolBar.setFloatable(false);
        getContentPane().add(toolBar, BorderLayout.NORTH);

        PaintPanel panel = new PaintPanel(cellSize);
        getContentPane().add(panel, BorderLayout.CENTER);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 900);

        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.model.setMaxMass(Float.parseFloat(maxMass.getText()));
                panel.model.setMaxCompress(Float.parseFloat(maxCompress.getText()));
                panel.model.setMinMass(Float.parseFloat(minMass.getText()));
                panel.model.setMinFlow(Float.parseFloat(minFlow.getText()));
                panel.model.setMaxSpeed(Float.parseFloat(maxSpeed.getText()));
            }
        });


        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                panel.setSpeed(speedSlider.getValue());
            }
        });

        brushSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                panel.setBrushSize(brushSizeSlider.getValue());
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.pause();
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.start();
            }
        });

        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    int brushSize = panel.getBrushSize()/2;
                    for (int i = (e.getY()-brushSize)/cellSize; i < (e.getY()+brushSize)/cellSize; i++) {
                        for (int j = (e.getX()-brushSize)/cellSize; j < (e.getX()+brushSize)/cellSize; j++) {
                            panel.setCell(2, i, j);
                        }
                    }
                    panel.repaint();

                }
                else if(e.getButton() == MouseEvent.BUTTON3) {
                    int brushSize = panel.getBrushSize()/2;
                    for (int i = (e.getY()-brushSize)/cellSize; i < (e.getY()+brushSize)/cellSize; i++) {
                        for (int j = (e.getX()-brushSize)/cellSize; j < (e.getX()+brushSize)/cellSize; j++) {
                            panel.setCell(1, i, j);
                        }
                    }
                    panel.repaint();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) LMDragged = true;
                else if (e.getButton() == MouseEvent.BUTTON3) RMDragged = true;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) LMDragged = false;
                else if (e.getButton() == MouseEvent.BUTTON3) RMDragged = false;
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(LMDragged) {
                    int brushSize = panel.getBrushSize()/2;
                    for (int i = (e.getY()-brushSize)/cellSize; i < (e.getY()+brushSize)/cellSize; i++) {
                        for (int j = (e.getX()-brushSize)/cellSize; j < (e.getX()+brushSize)/cellSize; j++) {
                            panel.setCell(2, i, j);
                        }
                    }

                }
                else if (RMDragged){
                    int brushSize = panel.getBrushSize()/2;
                    for (int i = (e.getY()-brushSize)/cellSize; i < (e.getY()+brushSize)/cellSize; i++) {
                        for (int j = (e.getX()-brushSize)/cellSize; j < (e.getX()+brushSize)/cellSize; j++) {
                            panel.setCell(1, i, j);
                        }
                    }
                }
                panel.updateMousePos(e.getX(), e.getY());
                panel.repaint();
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                panel.updateMousePos(e.getX(), e.getY());
            }
        });
    }
}
