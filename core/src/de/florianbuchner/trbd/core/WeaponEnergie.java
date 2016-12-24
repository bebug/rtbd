package de.florianbuchner.trbd.core;

public class WeaponEnergie {
    private float energy = 0f;

    public float add(final float amount){
        this.energy += amount;
        return this.energy;
    }

    public void reset() {
        this.energy = 0f;
    }

    public float getEnergy() {
        return energy;
    }
}
