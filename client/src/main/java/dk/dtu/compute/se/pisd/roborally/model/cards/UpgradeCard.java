package dk.dtu.compute.se.pisd.roborally.model.cards;

public class UpgradeCard extends Card {
    private Upgrade upgrade;

    public UpgradeCard(Upgrade upgrade) {
        super(CardType.UPGRADE);
        this.upgrade = upgrade;
    }

    public Upgrade getUpgrade() {
        return upgrade;
    }

    @Override
    public String getName() {
        return upgrade.displayName;
    }
}
