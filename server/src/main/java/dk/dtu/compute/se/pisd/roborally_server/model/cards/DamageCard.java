package dk.dtu.compute.se.pisd.roborally_server.model.cards;

public class DamageCard extends Card {
    private Damage damage;

    public DamageCard(Damage damage) {
        super();
        this.damage = damage;
    }

    public Damage getDamage() {
        return damage;
    }
}
