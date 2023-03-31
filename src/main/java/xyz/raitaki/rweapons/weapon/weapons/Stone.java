package xyz.raitaki.rweapons.weapon.weapons;

import org.bukkit.Material;
import org.joml.Vector3f;
import xyz.raitaki.rweapons.weapon.SkillClickType;
import xyz.raitaki.rweapons.weapon.Weapon;
import xyz.raitaki.rweapons.weapon.skills.ParticleSkill;
import xyz.raitaki.rweapons.weapon.skills.ThrowSkill;

import java.util.UUID;

public class Stone extends Weapon {

    public Stone(UUID owner) {
        super("Stone", owner, 1, Material.STONE, true);
        addSkill(new ThrowSkill(this, new Vector3f(1f,1f,1f)), SkillClickType.LEFT_CLICK);
        //addSkill(new ParticleSkill(this), SkillClickType.RIGHT_CLICK);
        createHeadItem();
    }
}
