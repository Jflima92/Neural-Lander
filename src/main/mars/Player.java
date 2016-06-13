package main.mars;

import java.util.ArrayList;

/**
 * Created by jorgelima on 5/31/16.
 */
public class Player {

    private State currentState;
    private double  g = -3.711;
    private ArrayList<Pair> points;

    public Player(){

    }

    public Player(ArrayList<Pair> points){
        this.points = points;

    }

    public Player(State state){
        this.currentState = state;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getNextState(double angle, double power){
        State currentState = getCurrentState();
        State st = new State();

        double newAngle = getNewAngle(angle);
        double newPower = getNewPower(power);

        double newHspeed = getNewHS(newAngle, newPower);
        double newVspeed = getNewVS(newAngle, newPower);
        st.setRotate(newAngle);
        st.setPower(newPower);
        st.setX(getNewX(newAngle, newPower));
        st.setY(getNewY(newAngle, newPower));
        st.sethSpeed(newHspeed);
        st.setvSpeed(newVspeed);
        st.setFuel(getNewFuel(newPower));
        return st;
    }

    public ArrayList<Pair> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Pair> points) {
        this.points = points;
    }

    private double getNewAngle(double angle){
        double nang = currentState.getRotate();

        if(angle>currentState.getRotate()+15) {
            nang = currentState.getRotate() + 15;
        }
        else if(angle<currentState.getRotate()-15) {
            nang = currentState.getRotate() - 15;
        }
        else {
           nang += (angle-nang);
        }


        return nang;
    }

    private double getNewPower(double power){
        double npow = currentState.getPower();
        if(power>getCurrentState().getPower()+1) npow = currentState.getPower()+1;
        else if(power<getCurrentState().getPower()-1) npow = currentState.getPower()-1;
        else npow += (power-npow);


        return npow;
    }

    private int getNewFuel(double newPower){
        int f = currentState.getFuel() - (int)newPower;

        return f;
    }

    private double getNewHS(double rotate, double power) {

        double currentHS = currentState.gethSpeed();

        double toAddHs = power*Math.sin(Math.toRadians(-rotate));

        return currentHS + toAddHs;
    }

    private double getNewVS(double rotate, double power) {

        double currentVS = currentState.getvSpeed();

        double toAddVs = power*Math.cos(Math.toRadians(-rotate)) + g;


        return currentVS + toAddVs;
    }

    private int getNewX(double rotate, double power) {
        double a = power * Math.sin(Math.toRadians(-rotate));
        double currentHS = currentState.gethSpeed();

        double toAddX = currentState.getX() + currentHS + a * 0.5;

        return Integer.valueOf((int) Math.round(toAddX));
    }

    private int getNewY(double rotate, double power) {
        double a = power * Math.cos(Math.toRadians(-rotate));

        double currentVS = currentState.getvSpeed();

        double toAddY = currentState.getY() + currentVS  + ((g+a) * 0.5);

        return Integer.valueOf((int) Math.round(toAddY));
    }

    public ArrayList<Pair> getLandingZone(){
        ArrayList<Pair> landing = new ArrayList<>();
        for(int i = 0; i < points.size()-1; i++){
            if((int)points.get(i).getValue() == (int)points.get(i+1).getValue()){
                landing.add(points.get(i));
                landing.add(points.get(i+1));
            }
        }
        return landing;
    }
}
