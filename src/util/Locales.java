package util;

import java.util.ResourceBundle;
import java.util.Locale;

/**
 * This class provides a Resource Bundle for managing locales.
 */
public final class Locales {

    public static ResourceBundle messages = ResourceBundle.getBundle("resources.messages.messages", Locale.ENGLISH);

    /**
     * Don't let anyone instantiate this class
     */
    private Locales() {}
}
