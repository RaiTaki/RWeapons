package xyz.raitaki.rweapons.weapon;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Weapon {

    @Getter private static List<Weapon> weapons = new ArrayList<>();

    @Getter private final String name;
    @Getter private final UUID ownerUUID;
    @Getter private final int damage;
    @Getter private final Material material;
    @Getter private ItemDisplay display;
    @Getter private boolean isEquipped;
    @Getter private boolean isHeld;
    @Getter private SkillTable skills;
    @Getter private boolean glow;

    @Getter private ItemStack itemStack;

    protected Weapon(String name, UUID owner, int damage, Material material, boolean glow) {
        this.name = name;
        this.ownerUUID = owner;
        this.damage = damage;
        this.material = material;
        this.glow = glow;
        this.skills = new SkillTable();
        weapons.add(this);

        buildItemStack();
    }

    public Player getOwner(){
        return Bukkit.getPlayer(ownerUUID);
    }

    public void createDisplay(Vector3f scale){
        if(doesDisplayExists()){
            display = getOwner().getWorld().spawn(getOwner().getLocation(), ItemDisplay.class);
            display.setItemStack(getOwner().getInventory().getItemInMainHand());
            display.setInterpolationDelay(0);
            display.setTransformation(new Transformation(new Vector3f(0, 0, 0), new Quaternionf(0, 0, 0, 1),
                    scale, new Quaternionf(0, 0, 0, 1)));
        }
    }

    public boolean doesDisplayExists(){
        return display == null || display.isDead();
    }

    public void addSkill(Skill skill, SkillClickType clickType){
        skills.addSkill(clickType, skill);
    }

    private void buildItemStack(){
        itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        if(glow)
            meta.addEnchant(Enchantment.THORNS, 10, true);
        itemStack.setItemMeta(meta);
    }

    public void activateSkill(PlayerInteractEvent event){
        SkillClickType clickType = getClickType(event);
        if(clickType == null)
            return;

        Skill skill = skills.getSkills().get(clickType);
        if(skill == null)
            return;
        skill.activate();
    }

    public SkillClickType getClickType(PlayerInteractEvent event){
        event.setCancelled(true);
        switch (event.getAction()){
            case LEFT_CLICK_AIR:
                if(event.getPlayer().isSneaking())
                    return SkillClickType.SHIFT_LEFT_CLICK;
                return SkillClickType.LEFT_CLICK;
            case LEFT_CLICK_BLOCK:
                if(event.getPlayer().isSneaking())
                    return SkillClickType.SHIFT_LEFT_CLICK;
                return SkillClickType.LEFT_CLICK;
            case RIGHT_CLICK_AIR:
                if(event.getPlayer().isSneaking())
                    return SkillClickType.SHIFT_RIGHT_CLICK;
                return SkillClickType.RIGHT_CLICK;
            case RIGHT_CLICK_BLOCK:
                if(event.getPlayer().isSneaking())
                    return SkillClickType.SHIFT_RIGHT_CLICK;
                return SkillClickType.RIGHT_CLICK;
            default:
                return null;
        }
    }

    public void createHeadItem(){
        this.itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(ownerUUID));
        if(glow)
            meta.addEnchant(Enchantment.THORNS, 10, true);
        itemStack.setItemMeta(meta);
    }
}

