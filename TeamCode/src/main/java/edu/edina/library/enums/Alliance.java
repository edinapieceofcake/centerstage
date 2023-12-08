package edu.edina.library.enums;

public enum Alliance {
    Blue(1),
    Red(2);

    public final int value;

    private Alliance(int value) {
        this.value = value;
    }
}
