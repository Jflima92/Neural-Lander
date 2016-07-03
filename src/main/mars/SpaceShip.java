package main.mars;


import main.neural.NeuralNetwork;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jorgelima on 6/7/16.
 */
public class SpaceShip {
    private Player player;
    private NeuralNetwork brain;
    private double fitness;
    private double rotation;
    private double power;
    private ArrayList<Pair> path;
    private ArrayList<Pair> actions;
    private boolean valid;
    private boolean ended;

    public SpaceShip(){

    }

    public SpaceShip(Player player) {
        this.player = player;
        this.brain = new NeuralNetwork();
        this.fitness = 0;
        this.rotation = 0;
        this.power = 0;
        this.ended = false;
        this.path = new ArrayList<>();
        Pair m = new Pair(player.getCurrentState().getX(), player.getCurrentState().getY());
        path.add(m);
        this.actions = new ArrayList<>();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public NeuralNetwork getBrain() {
        return brain;
    }

    public void setBrain(NeuralNetwork brain) {
        this.brain = brain;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public boolean getEnded(){
        return ended;
    }
    public boolean hasEndedVerification(){
        if(hasFinished() || hasCrashed() || player.getCurrentState().isOutOfBounds() || player.getCurrentState().getFuel() <= 0){
            return true;
        }
        return false;
    }

    public boolean hasFinished(){
        State st = player.getCurrentState();
        if(st.getRotate() == 0 && st.gethSpeed() <= 20 && st.getvSpeed() <= 40 && hasLanded()){
            return true;
        }
        return false;
    }

    public boolean isLandingRotation(){
        State st = player.getCurrentState();
        if(st.getRotate() == 0){
            return true;
        }
        return false;
    }

    public boolean isLandingVSpeed(){
        State st = player.getCurrentState();
        if(Math.abs(st.getvSpeed()) <= 40){
            return true;
        }
        return false;
    }

    public boolean isLandingHSpeed(){
        State st = player.getCurrentState();
        if(Math.abs(st.gethSpeed()) <= 20){
            return true;
        }
        return false;
    }

    public boolean isinLandingZone(){
        State st = player.getCurrentState();
        if(hasLanded()){
            return true;
        }
        return false;
    }

    public boolean hasLanded(){
        State st = player.getCurrentState();

        ArrayList<Pair> landing = player.getLandingZone();
        if(st.getX() <= (int)landing.get(1).getKey() && st.getX() >= (int)landing.get(0).getKey()){
            if(st.getY() >= ((int) landing.get(0).getValue())-200 && st.getY() <= ((int) landing.get(0).getValue())+200) {
                return true;
            }
        }

        return false;
    }

    public int getNumberOfWeights() {
        return this.brain.getNumberOfWeights();
    }

    public void putWeights(ArrayList<Double> d) {
        this.brain.putWeights(d);
    }

    public boolean update(){
        State st = player.getCurrentState();
        ArrayList<Double> inputs = new ArrayList<>();

        inputs.add(normalizeDist(getXDist(), 0));
        inputs.add(normalizeDist(getYDist(), 1));

        inputs.add(st.getvSpeed());
        inputs.add(st.gethSpeed());

        inputs.add(st.getPower());
        inputs.add(st.getRotate());

        ArrayList<Double> output = brain.update(inputs);

        if(output.size() < 2 || output.size() > 2){
            return false;
        }

        //rotation = Math.toDegrees(output.get(0));

        rotation = Math.round(-180 * output.get(0) + 90);
        power = Math.round(output.get(1)/0.25);

        actions.add(new Pair(rotation, power));

        State prev = this.player.getCurrentState().clone();

        State next = this.player.getNextState(rotation, power);
//        if(!next.isOutOfBounds() || !hasCrashed()) {
        this.player.setCurrentState(next);

        if(hasEndedVerification()){
            this.player.setCurrentState(prev);

            ended = true;
        }else{

            Pair m = new Pair(this.player.getCurrentState().getX(), this.player.getCurrentState().getY());
            this.path.add(m);

        }

        return true;

    }

    public boolean hasCrashed(){
        //int y = y1 + ((y2 - y1)/(x2 - x1))*(x - x1);

        Pair point1 = null;
        Pair point2 = null;

        for(int i = 0; i < player.getPoints().size()-1; i++){
            if((int) player.getPoints().get(i).getKey() <= player.getCurrentState().getX()){

                if((int) player.getPoints().get(i+1).getKey() >= player.getCurrentState().getX()){
                    point1 = player.getPoints().get(i);
                    point2 = player.getPoints().get(i+1);
                }
            }
        }

        double ylimit = 0;

        if(point1 == null || point2 == null){
            return true;

        }

        double m = (Double.valueOf((int)point2.getValue()) - Double.valueOf((int)point1.getValue()))/(Double.valueOf((int)point2.getKey()) - Double.valueOf((int)point1.getKey()));
        //y = mx + b;

        double b = (int)point1.getValue() - m * (int)point1.getKey();

//        ylimit = (int)point1.getValue() + (((int)point2.getValue() - (int)point1.getValue())/((int)point2.getKey() - (int)point1.getKey())) * (player.getCurrentState().getX() - (int)point1.getKey());

        ylimit = m * Math.abs(player.getCurrentState().getX()) + b;

        if(Math.abs(player.getCurrentState().getY()) <= ylimit)
            return true;
        return false;
    }

    public Pair getRelativeDistance(){
        double x = 0;
        double y = 0;

        y =  Math.abs((int)player.getLandingZone().get(0).getValue() - player.getCurrentState().getY());
        Pair p = new Pair(0,y);
        return p;
    }

    public double getDistance(){
        int middle = ((int)player.getLandingZone().get(0).getKey() + (int)player.getLandingZone().get(1).getKey()) / 2;

        return Math.sqrt(Math.pow(Math.abs(player.getCurrentState().getX()-middle), 2) + Math.pow(Math.abs(player.getCurrentState().getY()-(int)player.getLandingZone().get(0).getValue()), 2));
    }

    public double getXDist(){
        int middle = ((int)player.getLandingZone().get(0).getKey() + (int)player.getLandingZone().get(1).getKey()) / 2;

        return this.player.getCurrentState().getX() - middle;
    }

    public double getYDist(){
        return (int)this.player.getCurrentState().getY() -  (int)player.getLandingZone().get(0).getValue();
    }

    public double normalizeDist(double dist, int target){
        double norm;
        if(target == 0)
            norm = dist/7000;
        else
            norm = dist/3000;

        return norm;
    }

    public double normalizeVel(double vel, int target){
        double norm = 0.0;
        if(target == 0)
            norm = vel/7000;
        else
            norm = vel/3000;

        return norm;
    }

    public ArrayList<Pair> getActions(){
        return actions;
    }
    public ArrayList<Pair> getPath(){
        return path;
    }

    public boolean isValid() {
        return valid;
    }

    public void reset(State st){
        player.setCurrentState(st);
        this.ended = false;
        this.actions = new ArrayList<>();
        this.path = new ArrayList<>();
        fitness = 0;
    }


}
