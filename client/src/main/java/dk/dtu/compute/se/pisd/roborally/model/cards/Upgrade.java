package dk.dtu.compute.se.pisd.roborally.model.cards;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Upgrade {
    NONE("none");

    /** The displayName of a command */
    final public String displayName;

    /**
     * list of options
     */
    final private List<Upgrade> options;

    Upgrade(String displayName, Upgrade... options) {
        this.displayName = displayName;
        this.options = Collections.unmodifiableList(Arrays.asList(options));
    }
}
