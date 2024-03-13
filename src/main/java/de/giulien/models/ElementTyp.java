package de.giulien.models;

public enum ElementTyp {
    CLASS(1),
    TEACHER(2),
    SUBJECT(3),
    ROOM(4),
    PERSON(5),
    OTHER(6);

    public final int Type;

    private ElementTyp(int type) {
        Type = type;
    }


    @Override
    public String toString() {
        return String.valueOf(Type);
    }
}
