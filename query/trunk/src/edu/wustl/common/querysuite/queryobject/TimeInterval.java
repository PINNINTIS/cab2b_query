package edu.wustl.common.querysuite.queryobject;

public enum TimeInterval {
    Minute, Hour, Day, Week, Month, Quarter, Year;
    public static void main(String[] args) {
        System.out.println(Minute.compareTo(Day));
        System.out.println(Week.compareTo(Week));
    }
}
