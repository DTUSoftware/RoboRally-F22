package dk.dtu.compute.se.pisd.roborally_server.model.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Damage {
    /** Giver player one energi cube */
    POWER_UP("power_up"),
    /** Plays the last card in register */
    AGAIN("again"),
    /** Play top card of deck this register */
    SPAM("spam"),
    /** Play 2 top cards of deck this register */
    TROJAN_HORSE("trojan_horse"),
    /** Immediately reboot robot  */
    WORM("worm"),
    /** Robots within a certain radius are immediately rebooted,
     *  and the top card of deck is played this register  */
    VIRUS("virus"),
    /** Discard 1 spam card from discard pile  */
    SPAM_FOLDER("spam_routine"),
    /** Repeat previous register  */
    REPEAT_ROUTINE("repeat_routine");

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
    @JsonIgnore
    public boolean isInteractive() {
        return !options.isEmpty();
    }

    /**
     * gets the options
     * @return options
     */
    @JsonIgnore
    public List<Damage> getOptions() {
        return options;
    }
}
