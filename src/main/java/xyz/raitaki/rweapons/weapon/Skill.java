package xyz.raitaki.rweapons.weapon;

import lombok.Getter;

public abstract class Skill {

    @Getter private final String name;
    @Getter private final int level;
    @Getter private final int cost;
    @Getter private final int cooldown;
    @Getter private final int duration;
    @Getter private final Weapon weapon;

    public Skill(String name, int level, int cost, int cooldown, int duration, Weapon weapon) {
        this.name = name;
        this.level = level;
        this.cost = cost;
        this.cooldown = cooldown;
        this.duration = duration;
        this.weapon = weapon;
    }

    public abstract void activate();
}
