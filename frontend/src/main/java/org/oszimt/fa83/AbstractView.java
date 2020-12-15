package org.oszimt.fa83;

public class AbstractView {


    public static void callError(final Exception error){
        StageController.getInstance().callErrorLayout(error);
    }
}
