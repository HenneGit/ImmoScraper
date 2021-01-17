package org.oszimt.fa83.definition;

/**
 * RoomSize enum for defining values for frontend choicebox.
 */
public enum RoomSize {

    NEVERMIND("egal", 0D), ONE("ab 1 Zimmer", 1D),ONE_HALF("ab 1,5 Zimmer ", 1.5), TWO("ab 2 Zimmer", 2D),
    TWO_HALF("ab 2,5 Zimmer", 2.5), THREE("ab drei Zimmer", 3D),
    THREE_HALF ("ab 3,5 Zimmer", 3.5), FOUR("ab 4 Zimmer", 4D), FIVE("ab 5 Zimmer", 5D);

    public String getDescription() {
        return description;
    }

    private String description;
    private Double size;

    RoomSize(String description, Double size) {
        this.description = description;
        this.size = size;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    /**
     * get the corresponding double value for description string.
     * @param description the description string of roomsize enum value.
     * @return the actual room size value.
     */
    public static Double getRoomSizeByDescription(String description){
        for (RoomSize roomSize : RoomSize.values()){
            if (roomSize.getDescription().equals(description)){
                return roomSize.getSize();
            }
        }
        return null;
    }

    /**
     * get roomsize description by double value.
     * @param value the double value to find the description for.
     * @return the description string.
     */
    public static String getRoomSizeString(Double value){
        for (RoomSize roomSize : RoomSize.values()){
            if (roomSize.getSize().equals(value)){
                return roomSize.getDescription();
            }
        }
        return null;

    }


}
