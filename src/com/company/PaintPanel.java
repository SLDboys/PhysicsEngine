package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaintPanel extends JPanel implements ActionListener{
    int[][] table =
            {
                    {0,0,0,0,0,0,0},
                    {0,1,2,1,0,1,0},
                    {0,1,2,1,0,1,0},
                    {0,1,2,1,0,1,0},
                    {0,1,2,2,2,1,0},
                    {0,1,1,1,1,1,0},
                    {0,0,0,0,0,0,0}
            };
    long maxTimeCalc = 0, maxTimeDraw = 0;
    Model model = new Model();
    Timer tm = new Timer(500, this);
    private int cellSize,
    mouseX, mouseY, brushSize;
    PaintPanel(int cellSize) {
        super();
        this.cellSize = cellSize;
        brushSize = cellSize;
        setSize((model.getWidth()-2)*cellSize, (model.getHeight()-2)*cellSize);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i=1; i < model.getHeight()-1; i++) {
            for(int j=1; j < model.getWidth() -1; j++) {
                if(model.getCell(j, i) > 0 ){
                    if(model.getCell(j, i) == 1) g.setColor(new Color(212, 155, 0));
                    else {
                        float mass = model.getMass(j, i);
                        //l   d
                        //150 23 r
                        //185 45 b
                        //255 95 g
                        //0.1 1
                        //145-23=122
                        //180-45=135
                        //250-95=155
                        //50  0
                       if(mass <= 1) g.setColor(new Color((int)(5*(1-mass)) + 142, (int)(5*(1-mass)) + 173 ,(int)(5*(1-mass)) + 247));
                       else if (mass <= 60) g.setColor(new Color((int)(122*(1-(mass/60)))+ 20,(int)(135*(1-(mass/60)))+ 42,(int)(155*(1-(mass/60)))+ 92));
                       else g.setColor(new Color(23,45,95));
                    }
                    g.fillRect(cellSize*(i), cellSize*(j), cellSize, cellSize);
                }
            }
        }
        g.setColor(Color.GREEN);
        g.drawRect(mouseX-brushSize/2, mouseY-brushSize/2, brushSize, brushSize);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.updateModel();
        repaint();
    }

    public void pause() {
        tm.stop();
    }

    public void start() {
        tm.start();
    }

    public void setSpeed(int speed) {
        tm.setDelay(speed);
        tm.setInitialDelay(0);
    }

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
    }

    public int getBrushSize() {
        return brushSize;
    }

    public void setCell(int val, int i, int j) {
        try {
            model.setCell(val, i, j);
            if(val == 2) model.setMass(model.maxMass, i, j);
        }
        catch (IndexOutOfBoundsException err) {
//            System.out.println(err.getMessage());
        }
    }

    public void updateMousePos(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
//        System.out.println(model.mass[mouseY/cellSize][mouseX/cellSize]);
        repaint();
    }

}
