package com.example.kursach.players;

public class Fist {

    private double damageGive;      // health -= damageGive
    private int distanceToAttack;

    protected Fist() {
    }

    public double getDamageGive() {
        return damageGive;
    }

    public void setDamageGive(double damageGive) {
        this.damageGive = damageGive;
    }

    public int getDistanceToAttack() {
        return distanceToAttack;
    }

    public void setDistanceToAttack(int distanceToAttack) {
        this.distanceToAttack = distanceToAttack;
    }
}
