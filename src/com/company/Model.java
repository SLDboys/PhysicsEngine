package com.company;
import java.text.DecimalFormat;

import static java.lang.Math.min;

public class Model {
    final int AIR = 0,
            GROUND = 1,
            WATER = 2;
     float maxMass = 1f,//The normal, un-pressurized mass of a full water cell
            maxCompress = 0.25f,//How much excess water a cell can store, compared to the cell above it
            minMass = 0.0001f,//Ignore cells that are almost dry
            minFlow = 0.005f,
            maxSpeed = 4f, flowMult = 1f;
    private final int height = 250,
            width = 200;
    private long maxTime = 0;
    /* height & width are +2 of what's shown, because of border cells
     * EXAMPLE: 0 -- not shown on screen, 1 -- shown on screen
     * 00000
     * 01110
     * 01110
     * 01110
     * 00000
     * While calculating all border cells we can just use basic rule, because
     * all not-visible are just basic 'dead' cells
     * */

    private int[][] table;
    float[][] mass,
            newMass;

    //Just nullifying
    Model() {
        table = new int[width][height];
        mass = new float[width][height];
        newMass = new float[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                table[i][j] = AIR;
                mass[i][j] = 0;
                newMass[i][j] = 0;
            }
        }
    }

    public void setMaxMass(float maxMass) {
        this.maxMass = maxMass;
    }

    public void setMaxCompress(float maxCompress) {
        this.maxCompress = maxCompress;
    }

    public void setMinMass(float minMass) {
        this.minMass = minMass;
    }

    public void setMinFlow(float minFlow) {
        this.minFlow = minFlow;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    Model(int[][] table) {
        this.table = new int[width][height];
        mass = new float[width][height];
        newMass = new float[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (table[i][j] == AIR) {
                    this.table[i][j] = AIR;
                    mass[i][j] = 0;
                    newMass[i][j] = 0;
                } else if (table[i][j] == WATER) {
                    this.table[i][j] = WATER;
                    mass[i][j] = 1;
                    newMass[i][j] = 1;
                } else {
                    this.table[i][j] = GROUND;
                    mass[i][j] = 0;
                    newMass[i][j] = 0;
                }
            }
        }
    }

    int getHeight() {
        return height;
    }

    int getWidth() {
        return width;
    }

    void setCell(int val, int i, int j) throws IndexOutOfBoundsException {
        if (i >= width - 1 || i < 1)
            throw new IndexOutOfBoundsException("Wrong width. max is " + (width - 1) + ",min is 1; got " + i);
        if (j >= height - 1 || j < 1)
            throw new IndexOutOfBoundsException("Wrong height. max is " + (height - 1) + ",min is 1; got " + j);
        table[i][j] = val;
    }

    void setMass(float val, int i, int j) throws IndexOutOfBoundsException {
        if (i >= width - 1 || i < 1)
            throw new IndexOutOfBoundsException("Wrong width. max is " + (width - 1) + ",min is 1; got " + i);
        if (j >= height - 1 || j < 1)
            throw new IndexOutOfBoundsException("Wrong height. max is " + (height - 1) + ",min is 1; got " + j);
        mass[i][j] = val;
    }

    float getMass(int i, int j) throws IndexOutOfBoundsException {
        if (i >= width - 1 || i < 1)
            throw new IndexOutOfBoundsException("Wrong width. max is " + (width - 1) + ",min is 1; got " + i);
        if (j >= height - 1 || j < 1)
            throw new IndexOutOfBoundsException("Wrong height. max is " + (height - 1) + ",min is 1; got " + j);
        return mass[i][j];
    }

    void updateModel() {


        float Flow = 0;
        float remaining_mass;
        for (int y = 0; y < width; y++) {
            System.arraycopy(mass[y], 0, newMass[y], 0, height);
        }
        //Calculate and apply flow for each block
        for (int y = 1; y < width - 1; y++) {
            for (int x = 1; x < height - 1; x++) {

                //Skip inert ground table
                if (table[y][x] == GROUND) continue;

                //Custom push-only flow
                Flow = 0;
                remaining_mass = mass[y][x];
                if (remaining_mass <= 0) continue;

                //The block below this one
                if ((table[y + 1][x] != GROUND)) {
                    Flow = getStableState(remaining_mass + mass[y + 1][x]) - mass[y + 1][x];
                    if (Flow > minFlow) {
                        Flow *= flowMult; //leads to smoother flow
                    }
                    if (Flow < 0) Flow = 0;
                    else if (Flow > min(maxSpeed, remaining_mass)) Flow = min(maxSpeed, remaining_mass);
                    newMass[y][x] -= Flow;
                    newMass[y + 1][x] += Flow;
                    remaining_mass -= Flow;
                }

                if (remaining_mass <= 0) continue;

                //Left
                if (table[y][x - 1] != GROUND) {
                    //Equalize the amount of water in this block and it's neighbour
                    Flow = (mass[y][x] - mass[y][x - 1]) / 4;
                    if (Flow > minFlow) {
                        Flow *= flowMult;
                    }
                    if (Flow < 0) Flow = 0;
                    else if (Flow > remaining_mass) Flow = remaining_mass;
                    newMass[y][x] -= Flow;
                    newMass[y][x - 1] += Flow;
                    remaining_mass -= Flow;
                }

                if (remaining_mass <= 0) continue;

                //Right
                if (table[y][x + 1] != GROUND) {
                    //Equalize the amount of water in this block and it's neighbour
                    Flow = (mass[y][x] - mass[y][x + 1]) / 4;
                    if (Flow > minFlow) {
                        Flow *= flowMult;
                    }
                    if (Flow < 0) Flow = 0;
                    else if (Flow > remaining_mass) Flow = remaining_mass;
                    newMass[y][x] -= Flow;
                    newMass[y][x + 1] += Flow;
                    remaining_mass -= Flow;
                }

                if (remaining_mass <= 0) continue;

                //Up. Only compressed water flows upwards.
                if (table[y - 1][x] != GROUND) {
                    Flow = remaining_mass - getStableState(remaining_mass + mass[y - 1][x]);
                    if (Flow > minFlow) {
                        Flow *= flowMult;
                    }
                    if (Flow < 0) Flow = 0;
                    else if (Flow > min(maxSpeed, remaining_mass)) Flow = min(maxSpeed, remaining_mass);
                    newMass[y][x] -= Flow;
                    newMass[y - 1][x] += Flow;
                    remaining_mass -= Flow;
                }
            }
        }

        //Copy the new mass values to the mass array
        for (int y = 0; y < width; y++) {
            System.arraycopy(newMass[y], 0, mass[y], 0, height);
        }

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                //Skip ground table
                if (table[x][y] == GROUND) continue;
                //Flag/unflag water table
                if (mass[x][y] > minMass) {
                    table[x][y] = WATER;
                } else {
                    table[x][y] = AIR;
                }
            }
        }

        //Remove any water that has left the map
        for (int y = 0; y < width; y++) {
            mass[y][0] = 0;
            mass[y][height - 1] = 0;
        }
        for (int x = 1; x < height - 1; x++) {
            mass[0][x] = 0;
            mass[width - 1][x] = 0;
        }
    }

    float getStableState(float total_mass) {
        if (total_mass <= 1) {
            return 1;
        } else if (total_mass < 2 * maxMass + maxCompress) {
            return (maxMass * maxMass + total_mass * maxCompress) / (maxMass + maxCompress);
        } else {
            return (total_mass + maxCompress) / 2;
        }
    }

    int getCell(int i, int j) {
        return table[i][j];
    }
}
