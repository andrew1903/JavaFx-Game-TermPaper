package com.example.kursach.players;

import java.util.Arrays;
import java.util.Optional;

public enum Status {
    NEUTRAL("NEUTRAL"),
    RED("RED"),
    BLUE("BLUE");

    private String side;
    private int experience;

    Status(String envUrl) {
        this.side = envUrl;
        this.experience = 0;
    }

    public static Optional<Status> get(String side) {
        return Arrays.stream(Status.values())
                .filter(env -> env.side.equals(side))
                .findFirst();
    }

    public static String toString(Status status) {
        return status.name();
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return side;
    }
}
