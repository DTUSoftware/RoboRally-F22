package dk.dtu.compute.se.pisd.roborally_server.model.cards;

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
}
