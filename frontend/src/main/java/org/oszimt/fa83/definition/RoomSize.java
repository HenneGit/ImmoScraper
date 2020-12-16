package org.oszimt.fa83.definition;

public enum RoomSize {

    ONE("1 Zimmer"),ONE_HALF(""), TWO("2 Zimmer"), TWO_HALF("2,5 Zimmer");

    private String description;

    RoomSize(String description) {
        this.description = description;
    }


}
