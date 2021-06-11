package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import static java.lang.Float.parseFloat;


enum StartPause{
    PAUSE,
    START
}

public class Frame extends JFrame{
    private JButton startPauseButton;
    private JMenuBar menu;
    private JMenu fileMenu;
    private JMenuItem maxMassMenu, maxCompressMenu, minMassMenu, minFlowMenu, maxSpeedMenu, flowMultMenu, helpMenu;
    private JButton clearScreenButton, returnToStandConstButton;
    private StartPause startPause = StartPause.PAUSE;
    private JToolBar toolBar;
    private JSlider speedSlider;
    private JSlider brushSizeSlider;
    private ImageIcon pause, start;
    private boolean LMDragged = false;
    private boolean RMDragged = false;
    int cellSize;
    public Frame() {
        super("WaterSim");
        setLayout(new BorderLayout());
        cellSize = 4;
        start = new ImageIcon(getClass().getResource("/start.png"));
        pause = new ImageIcon(getClass().getResource("/pause.png"));
        startPauseButton = new JButton(start);
        clearScreenButton = new JButton("Clear Screen");
        returnToStandConstButton = new JButton("Undo constant changes");
        startPauseButton.setBorderPainted(false);

        speedSlider = new JSlider(1, 1000);
        speedSlider.setOrientation(JSlider.HORIZONTAL);
        speedSlider.setInverted(true);
        speedSlider.setPaintTicks(true);
        Hashtable<Integer,JLabel> speedLabels = new Hashtable<Integer,JLabel>();
        speedLabels.put(1, new JLabel("Fast"));
        speedLabels.put(1000, new JLabel("Slow"));
        speedSlider.setLabelTable(speedLabels);
        speedSlider.setPaintLabels(true);
        speedSlider.setValue(1);
        speedSlider.setPreferredSize(new Dimension(100, 45));

        brushSizeSlider = new JSlider(cellSize, cellSize*10);
        brushSizeSlider.setOrientation(JSlider.HORIZONTAL);
        brushSizeSlider.setPaintTicks(true);
        Hashtable<Integer,JLabel> brushSizeLabels = new Hashtable<Integer,JLabel>();
        brushSizeLabels.put(cellSize, new JLabel(cellSize + " pix"));
        brushSizeLabels.put(cellSize*10, new JLabel(cellSize*10 + " pix"));
        brushSizeSlider.setLabelTable(brushSizeLabels);
        brushSizeSlider.setPaintLabels(true);
        brushSizeSlider.setValue(cellSize);
        brushSizeSlider.setPreferredSize(new Dimension(100, 45));

        menu = new JMenuBar();
        fileMenu = new JMenu("Advanced");
        maxMassMenu = new JMenuItem("Set Standard Mass");
        maxCompressMenu = new JMenuItem("Set Compress Rate");
        minMassMenu = new JMenuItem("Set Min Showable Mass");
        minFlowMenu = new JMenuItem("Change Min Flow");
        maxSpeedMenu = new JMenuItem("Change Max Speed");
        flowMultMenu = new JMenuItem("Change Smoothness of sim");
        helpMenu = new JMenuItem("Help");

        fileMenu.add(maxMassMenu);
        fileMenu.add(maxCompressMenu);
        fileMenu.add(minMassMenu);
        fileMenu.add(minFlowMenu);
        fileMenu.add(maxSpeedMenu);
        fileMenu.add(flowMultMenu);
        fileMenu.addSeparator();
        fileMenu.add(helpMenu);
        menu.add(fileMenu);
        menu.add(Box.createHorizontalGlue());
        setJMenuBar(menu);

        toolBar = new JToolBar("ToolBar");
        toolBar.add(startPauseButton);
        toolBar.add(speedSlider);
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        toolBar.add(brushSizeSlider);
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        toolBar.add(clearScreenButton);
        toolBar.add(returnToStandConstButton);
        setJMenuBar(menu);

        toolBar.setFloatable(false);
        getContentPane().add(toolBar, BorderLayout.NORTH);

        PaintPanel panel = new PaintPanel(cellSize);
        getContentPane().add(panel, BorderLayout.CENTER);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 900);
        setResizable(false);
        this.setFocusable(true);
        this.requestFocusInWindow();

        maxMassMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Set Standard Mass\nCurrent value: " + panel.model.getMaxMass());
                if(input.equals("")||input == null) {
                    return;
                }
                float newVal = 0;
                try {
                    newVal = parseFloat(input);
                    panel.model.setMaxMass(newVal);
                }
                catch(NumberFormatException err) {
                }
                catch(IllegalArgumentException err) {
                    JOptionPane.showMessageDialog(null, "Illegal argument:" + err.getMessage());
                }
            }
        });

        maxCompressMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Set Compress Rate\nCurrent value: " + panel.model.getMaxCompress());
                if(input.equals("")||input == null) {
                    return;
                }
                float newVal = 0;
                try {
                    newVal = parseFloat(input);
                    panel.model.setMaxCompress(newVal);
                }
                catch(NumberFormatException err) {
                }
                catch(IllegalArgumentException err) {
                    JOptionPane.showMessageDialog(null, "Illegal argument:" + err.getMessage());
                }
            }
        });

        minMassMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Set Min Showable Mass\nCurrent value: " + panel.model.getMinMass());
                if(input.equals("")||input == null) {
                    return;
                }
                float newVal = 0;
                try {
                    newVal = parseFloat(input);
                    panel.model.setMinMass(newVal);
                }
                catch(NumberFormatException err) {
                }
                catch(IllegalArgumentException err) {
                    JOptionPane.showMessageDialog(null, "Illegal argument:" + err.getMessage());
                }
            }
        });

        minFlowMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Set Min Flow\nCurrent value: " + panel.model.getMinFlow());
                if(input.equals("")||input == null) {
                    return;
                }
                float newVal = 0;
                try {
                    newVal = parseFloat(input);
                    panel.model.setMinFlow(newVal);
                }
                catch(NumberFormatException err) {
                }
                catch(IllegalArgumentException err) {
                    JOptionPane.showMessageDialog(null, "Illegal argument:" + err.getMessage());
                }
            }
        });

        maxSpeedMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Set Max Speed\nCurrent value: " + panel.model.getMaxSpeed());
                if(input.equals("")||input == null) {
                    return;
                }
                float newVal = 0;
                try {
                    newVal = parseFloat(input);
                    panel.model.setMaxSpeed(newVal);
                }
                catch(NumberFormatException err) {
                }
                catch(IllegalArgumentException err) {
                    JOptionPane.showMessageDialog(null, "Illegal argument:" + err.getMessage());
                }
            }
        });

        flowMultMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Set flow smoothness\nCurrent value: "+ panel.model.getFlowMult());
                if(input.equals("")||input == null) {
                    return;
                }
                float newVal = 0;
                try {
                    newVal = parseFloat(input);
                    panel.model.setFlowMult(newVal);
                }
                catch(NumberFormatException err) {
                }
                catch(IllegalArgumentException err) {
                    JOptionPane.showMessageDialog(null, "Illegal argument:" + err.getMessage());
                }
            }
        });

        helpMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "\tThis program simulates water flow\n" +
                        "In \"WATER\" mode press/drag LMB to spawn water\n" +
                        "In \"GROUND\" mode press/drag LMB to spawn ground\n"+
                        "To change mod, press button on the top-right side\n" +
                        "To erase blocks press/drag RMB\n" +
                        "To change speed of the simulation drag the left slider\n"+
                        "To change size of the brush drag the right slider\n" +
                        "To stop/start simulation press top-left button\n" +
                        "To change constant press appropriate menu item\n"+
                        "Constants: \n"+
                        "Standard Mass - The mass of water in a single cell when it is created\n"+
                        "Compress Rate - Maximum difference in water mass between the upper and lower cells\n"+
                        "Min Showable Mass - Minimum mass of water that can be contained in the cell\n"+
                        "Min Flow - Threshold of the flow for which additional multiplication is applied\n"+
                        "Max Speed - Maximum water flow rate\n"+
                        "Smoothness of sim - Changing the smoothness of the water animation\n"+
                        "Authors: Vadim Parmuzin, Ilia Mushkin, Dmitri Levitsky"
                );
            }
        });

        clearScreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.clearModel();
            }
        });

        returnToStandConstButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.model.setConstStand();
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

        startPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(startPause == StartPause.PAUSE) {
                    startPause = StartPause.START;
                    startPauseButton.setIcon(pause);
                    panel.start();
                }
                else {
                    startPause = StartPause.PAUSE;
                    startPauseButton.setIcon(start);
                    panel.pause();
                }
            }
        });

        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    int brushSize = panel.getBrushSize()/2;
                    for (int i = (e.getY()-brushSize)/cellSize; i < (e.getY()+brushSize)/cellSize; i++) {
                        for (int j = (e.getX()-brushSize)/cellSize; j < (e.getX()+brushSize)/cellSize; j++) {
                            if(e.isAltDown()) {
                                panel.setCell(1, i, j);
                            }
                            else {
                                panel.setCell(2, i, j);
                            }
                        }
                    }
                    panel.repaint();

                }
                else if(e.getButton() == MouseEvent.BUTTON3) {
                    int brushSize = panel.getBrushSize()/2;
                    for (int i = (e.getY()-brushSize)/cellSize; i < (e.getY()+brushSize)/cellSize; i++) {
                        for (int j = (e.getX()-brushSize)/cellSize; j < (e.getX()+brushSize)/cellSize; j++) {
                            panel.setCell(0, i, j);
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
                            if(e.isAltDown()) {
                                panel.setCell(1, i, j);
                            }
                            else {
                                panel.setCell(2, i, j);
                            }
                        }
                    }

                }
                else if (RMDragged){
                    int brushSize = panel.getBrushSize()/2;
                    for (int i = (e.getY()-brushSize)/cellSize; i < (e.getY()+brushSize)/cellSize; i++) {
                        for (int j = (e.getX()-brushSize)/cellSize; j < (e.getX()+brushSize)/cellSize; j++) {
                            panel.setCell(0, i, j);
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
