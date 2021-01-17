package org.oszimt.fa83.view;

import org.oszimt.fa83.StageController;

/**
 * absctract class for all view holding function to call an error.
 */
public abstract class AbstractView {

    /**
     * call error popup.
     * @param error the exception to display in popup.
     */
    public static void callError(final Exception error){
        StageController.getInstance().callErrorLayout(error);
    }
}
