package dk.dtu.compute.se.pisd.roborally_server.model.cards;

public class UpgradeCard extends Card {
    private Upgrade upgrade;

    public UpgradeCard(Upgrade upgrade) {
        super();
        this.upgrade = upgrade;
    }

    public Upgrade getUpgrade() {
        return upgrade;
    }
}
