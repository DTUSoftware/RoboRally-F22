package dk.dtu.compute.se.pisd.roborally_server.model.cards;

/**
 * A damagecard.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public class DamageCard extends Card {
    private Damage damage;

    /**
     * Creates a damagecard
     *
     * @param damage the damage the card has
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public DamageCard(Damage damage) {
        super(CardType.DAMAGE);
        this.damage = damage;
    }

    /**
     * Gets the damage
     *
     * @return the damage
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public Damage getDamage() {
        return damage;
    }
}
