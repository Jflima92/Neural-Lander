package main.mars;

import java.util.ArrayList;

/**
 * Created by jorgelima on 5/31/16.
 */
public class State {
    private int x;
    private int y;
    private double vSpeed;
    private double hSpeed;
    private int fuel;
    private double rotate;
    private double power;

    public State(){

    }

    public State(int x, int y, double vSpeed, double hSpeed, int fuel, double rotate, double power) {
        this.x = x;
        this.y = y;
        this.vSpeed = vSpeed;
        this.hSpeed = hSpeed;
        this.fuel = fuel;
        this.rotate = rotate;
        this.power = power;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getvSpeed() {
        return vSpeed;
    }

    public void setvSpeed(double vSpeed) {
        this.vSpeed = vSpeed;
    }

    public double gethSpeed() {
        return hSpeed;
    }

    public void sethSpeed(double hSpeed) {
        this.hSpeed = hSpeed;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public double getRotate() {
        return rotate;
    }

    public void setRotate(double rotate) {
        this.rotate = rotate;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public boolean isOutOfBounds(){
        boolean is = false;
        if(x >= 7000 || x < 0){
            is = true;
        }
        if(y >= 3000 || y < 0){
            is = true;
        }

        return is;
    }

    public ArrayList<Double> getAsList(){
        ArrayList<Double> arr = new ArrayList<>();
        arr.add((double)this.fuel);
        arr.add((double)this.x);
        arr.add((double)this.y);
        arr.add(this.power);
        arr.add(this.rotate);
        arr.add(this.vSpeed);
        arr.add(this.hSpeed);
        return arr;
    }

    public State clone(){
        State st = new State();
        st.setFuel(this.fuel);
        st.setX(this.x);
        st.setY(this.y);
        st.setvSpeed(this.vSpeed);
        st.sethSpeed(this.hSpeed);
        st.setPower(this.power);
        st.setRotate(this.rotate);
        return st;
    }

    @Override
    public String toString() {
        return "State{" +
                "x=" + x +
                ", y=" + y +
                ", vSpeed=" + vSpeed +
                ", hSpeed=" + hSpeed +
                ", fuel=" + fuel +
                ", rotate=" + rotate +
                ", power=" + power +
                '}';
    }
}

