package xyz.raitaki.rweapons.weapon.weapons;

import org.bukkit.Material;
import org.joml.Vector3f;
import xyz.raitaki.rweapons.weapon.SkillClickType;
import xyz.raitaki.rweapons.weapon.Weapon;
import xyz.raitaki.rweapons.weapon.skills.ThrowSkill;

import java.util.UUID;

public class NanoItem extends Weapon {

    public NanoItem(UUID owner) {
        super("NanoItem", owner, 1, Material.STONE, true);
        createHeadItem();
        addSkill(new ThrowSkill(this, new Vector3f(1,1,1)), SkillClickType.LEFT_CLICK);
    }
}
