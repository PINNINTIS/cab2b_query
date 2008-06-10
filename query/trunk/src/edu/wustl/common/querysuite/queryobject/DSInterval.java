package edu.wustl.common.querysuite.queryobject;

public enum DSInterval implements ITimeIntervalEnum {
    Minute, Hour, Day, Week;
    public static void main(String[] args) {
        System.out.println(Week.compareTo(Minute));
        for(DSInterval i : DSInterval.values()) {
            System.out.println(i);
        }
    }
}
