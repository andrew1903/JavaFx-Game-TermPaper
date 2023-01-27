package com.example.kursach.players;

import java.util.Comparator;

public class FighterComparator {

    public static Comparator<FighterImpl> healthComparator = (o1, o2) -> {
        if ((o1 == null) && (o2 == null)) return 0;
        if (o1 == null) return -1;
        if (o2 == null) return 1;
        return Double.compare(o1.health, o2.health);
    };


//    public int compareTo(Fighter o) {
//        if (this.side == o.side && this.name.equals(o.name)) return 1;
//        return -1;
//    }

//    public static int myCompare(Fighter s1, Fighter s2) {
//        return s1.name.compareTo(s2.getName());
//    }
}
