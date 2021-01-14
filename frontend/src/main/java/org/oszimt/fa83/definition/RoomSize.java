package org.oszimt.fa83.definition;

public enum RoomSize {

    NEVERMIND("egal"), ONE("ab 1 Zimmer"),ONE_HALF("ab 1,5 Zimmer "), TWO("ab 2 Zimmer"), TWO_HALF("ab 2,5 Zimmer"), THREE("ab drei Zimmer"),
    THREE_HALF ("ab 3,5 Zimmer"), FOUR("ab 4 Zimmer"), FIVE("ab 5 Zimmer");

    public String getDescription() {
        return description;
    }

    private String description;

    RoomSize(String description) {
        this.description = description;
    }

    public RoomSize getRoomSizeByDescription(String description){
        for (RoomSize roomSize : RoomSize.values()){
            if (roomSize.getDescription().equals(description)){
                return roomSize;
            }
        }
        return null;
    }
}
