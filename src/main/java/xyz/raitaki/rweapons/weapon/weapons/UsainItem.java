package xyz.raitaki.rweapons.weapon.weapons;

import org.bukkit.Material;
import org.joml.Vector3f;
import xyz.raitaki.rweapons.weapon.SkillClickType;
import xyz.raitaki.rweapons.weapon.Weapon;
import xyz.raitaki.rweapons.weapon.skills.ThrowSkill;

import java.util.UUID;

public class UsainItem extends Weapon {

    public UsainItem(UUID owner) {
        super("UsainItem", owner, 1, Material.SPRUCE_STAIRS, true);
        addSkill(new ThrowSkill(this, new Vector3f(0.5f,0.5f,0.5f), 0.12), SkillClickType.LEFT_CLICK);

    }
}
