package org.oszimt.fa83.definition;

public enum Layout {


    MAIN("MainView.fxml"), QUERY("QuerySetupView.fxml"), ERROR("error.fxml");


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
