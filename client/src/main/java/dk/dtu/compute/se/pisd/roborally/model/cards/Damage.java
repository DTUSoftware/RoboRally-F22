package dk.dtu.compute.se.pisd.roborally.model.cards;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Oscar Maxwell
 * @author Marcus
 * @author Nicolai
 */
public enum Damage {
    /** Play top card of deck this register */
    SPAM("spam"),
    /** Play 2 top cards of deck this register */
    TROJAN_HORSE("trojan_horse"),
    /** Immediately reboot robot  */
    WORM("worm"),
    /** Robots within a certain radius are immediately rebooted,
     *  and the top card of deck is played this register  */
    VIRUS("virus");

    /** The displayName of a command */
    final public String displayName;

    // XXX Assignment P3
    /**
     * The Damage constructor.
     *
     * @param displayName the displayName of the Damage.
     */
    // Command(String displayName) {
    //     this.displayName = displayName;
    // }
    // replaced by the code below:
    /**
     * list of options
     */
    final private List<Damage> options;

    Damage(String displayName, Damage... options) {
        this.displayName = displayName;
        this.options = Collections.unmodifiableList(Arrays.asList(options));
    }

    /**
     * checks if it's interactive
     * @return if its empty
     */
    public boolean isInteractive() {
        return !options.isEmpty();
    }

    /**
     * gets the options
     * @return options
     */
    public List<Damage> getOptions() {
        return options;
    }
}
