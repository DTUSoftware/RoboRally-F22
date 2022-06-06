package dk.dtu.compute.se.pisd.roborally.model.cards;

public class DamageCard extends Card {
    private Damage damage;

    public DamageCard(Damage damage) {
        super(CardType.DAMAGE);
        this.damage = damage;
    }

    public Damage getDamage() {
        return damage;
    }

    @Override
    public String getName() {
        return damage.displayName;
    }
}
