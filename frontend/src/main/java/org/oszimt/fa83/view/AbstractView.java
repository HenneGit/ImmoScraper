package org.oszimt.fa83.view;

import org.oszimt.fa83.StageController;

public class AbstractView {


    public static void callError(final Exception error){
        StageController.getInstance().callErrorLayout(error);
    }
}
