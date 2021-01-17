package org.oszimt.fa83.definition;

/**
 * holds names of javafx fxml layout files.
 */
public enum Layout {


    QUERY("QuerySetupView.fxml"), ERROR("error.fxml");


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
