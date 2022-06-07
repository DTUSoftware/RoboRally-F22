package dk.dtu.compute.se.pisd.roborally.model.cards;

/**
 * An upgradecard.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class UpgradeCard extends Card {
    private Upgrade upgrade;

    /**
     * Creates a new upgradecard
     *
     * @param upgrade the upgrade
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public UpgradeCard(Upgrade upgrade) {
        super(CardType.UPGRADE);
        this.upgrade = upgrade;
    }

    /**
     * Gets the upgrade
     *
     * @return the upgrade
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Upgrade getUpgrade() {
        return upgrade;
    }

    /**
     * Gets the displayName of the {@link Upgrade Upgrade} that's on the card.
     *
     * @return the displayName of the Command.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public String getName() {
        return upgrade.displayName;
    }
}
