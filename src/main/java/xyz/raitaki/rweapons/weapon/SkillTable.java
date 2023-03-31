package xyz.raitaki.rweapons.weapon;

import lombok.Getter;

import java.util.HashMap;

public class SkillTable {

    @Getter private final HashMap<SkillClickType, Skill> skills;

    public SkillTable() {
        this.skills = new HashMap<>();
    }

    public void addSkill(SkillClickType clickType, Skill skill) {
        skills.put(clickType, skill);
    }
}
