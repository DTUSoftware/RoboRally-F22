package dk.dtu.compute.se.pisd.roborally_server.model.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Damage cards.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 * @author Oscar Maxwell
 * @author Nicolai Udbye
 */
public enum Damage {
    /**
     * Play top card of deck this register
     */
    SPAM("spam"),
    /**
     * Play 2 top cards of deck this register
     */
    TROJAN_HORSE("trojan_horse"),
    /**
     * Immediately reboot robot
     */
    WORM("worm"),
    /**
     * Robots within a certain radius are immediately rebooted,
     * and the top card of deck is played this register
     */
    VIRUS("virus");

    /**
     * The displayName of a command
     */
    final public String displayName;

    // XXX Assignment P3
    /**
     * list of options
     */
    final private List<Damage> options;

    /**
     * The Damage constructor.
     *
     * @param displayName the displayName of the Damage.
     * @param options     options
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    Damage(String displayName, Damage... options) {
        this.displayName = displayName;
        this.options = Collections.unmodifiableList(Arrays.asList(options));
    }

    /**
     * checks if it's interactive
     *
     * @return if its empty
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    @JsonIgnore
    public boolean isInteractive() {
        return !options.isEmpty();
    }

    /**
     * gets the options
     *
     * @return options
     * @author Ekkart Kindler, ekki@dtu.dk
     */
    @JsonIgnore
    public List<Damage> getOptions() {
        return options;
    }
}
