package tuo.model;

public enum QuestionImage {
    STUDYING("Учене"),
    STUDYING_ALL_NIGHT("Учене цяла нощ"),
    WRITING("Писане"),
    PARTY("Парти"),
    CONCERT("Концерт"),
    LIBRARY("Библиотека"),
    PARK_WALK("Разходка в парка"),
    FOREST_WALK("Разходка в гората");

    private final String displayName;

    QuestionImage(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
