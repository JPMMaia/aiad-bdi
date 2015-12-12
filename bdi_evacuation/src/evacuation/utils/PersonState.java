package evacuation.utils;

import java.util.Random;

public class PersonState {

    //influenced by a lot of variables // normalized

    private int age;

    private int gender;
    private double impulsivity;
    private double trustAuthorities;

    //state
    private double stateAnxiety;
    private double stateFear;
    private double stateRage;
    private double stateControlLocus;
    private double statePatience;

    //trait
    private double traitAnxiety;
    private double traitFear;
    private double traitRage;
    private double traitControlLocus;
    private double traitPatience;

    public int patience;

    Random r;

    public PersonState(){

        r = new Random();

        this.gender = r.nextInt(2);
        this.impulsivity = r.nextGaussian();
        this.trustAuthorities = r.nextGaussian();
        //state
        this.stateAnxiety = r.nextGaussian();
        this.stateFear = r.nextGaussian();
        this.stateRage = r.nextGaussian();
        this.stateControlLocus = r.nextGaussian();
        this.statePatience = r.nextGaussian();

        //trait
        this.traitAnxiety = r.nextGaussian();
        this.traitFear = r.nextGaussian();
        this.traitRage = r.nextGaussian();
        this.traitControlLocus = r.nextGaussian();
        this.traitPatience = r.nextGaussian();

        this.patience = r.nextInt(2);
    }

    public boolean getPatience() {
        if(patience == 0)
            return false;

        return true;
    }

    public int getRiskPerception(int valueForRiskPerception) {

        double temp = gender + impulsivity - trustAuthorities;
        temp += - stateAnxiety + stateFear - stateRage - stateControlLocus;
        temp += - traitAnxiety + traitFear - traitRage - traitControlLocus;
        return valueForRiskPerception + (int) temp;
    }
}
