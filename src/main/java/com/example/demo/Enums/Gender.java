package com.example.demo.Enums;

public enum Gender {

    MALE("MALE"),
    FEMALE ("FEMALE");

    private final String value;

    private Gender(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
