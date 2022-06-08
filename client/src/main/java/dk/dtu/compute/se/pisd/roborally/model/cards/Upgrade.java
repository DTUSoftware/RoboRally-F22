package dk.dtu.compute.se.pisd.roborally.model.cards;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Upgrades.
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public enum Upgrade {
    /** Filler card */
    NONE("none");

    /** The displayName of a command */
    final public String displayName;

    /**
     * list of options
     */
    final private List<Upgrade> options;

    /**
     * Creates an upgrade
     * @param displayName displayname of card
     * @param options options of card
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    Upgrade(String displayName, Upgrade... options) {
        this.displayName = displayName;
        this.options = Collections.unmodifiableList(Arrays.asList(options));
    }
}
