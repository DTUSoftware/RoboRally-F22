package dk.dtu.compute.se.pisd.roborally.model.cards;

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

    /**
     * Gets the displayName of the {@link Damage Damage} that's on the card.
     *
     * @return the displayName of the Command.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    @Override
    public String getName() {
        return damage.displayName;
    }
}
