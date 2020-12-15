package org.oszimt.fa83;

public enum Layout {


    MAIN("scene2.fxml"), QUERY(""), ERROR("error.fxml");


    private String fileName;

    Layout(String fileName) {
        this.fileName = fileName;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
