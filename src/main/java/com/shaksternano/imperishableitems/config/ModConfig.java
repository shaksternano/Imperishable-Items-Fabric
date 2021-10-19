package com.shaksternano.imperishableitems.config;

import com.shaksternano.imperishableitems.ImperishableItems;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = ImperishableItems.MOD_ID)
@Config.Gui.Background(Config.Gui.Background.TRANSPARENT)
public class ModConfig implements ConfigData {

    @Comment("Should Imperishable be a treasure enchant? Default value is false.")
    public boolean imperishableIsTreasure = false;

    @Comment("Should villagers sell Imperishable enchanted books? Default value is true.")
    public boolean imperishableSoldByVillagers = true;

    @Comment("Minimum experience level required to get imperishable in an enchanting table. Maximum level is the mini. Default value is 15.")
    public int imperishableMinLevel = 15;

    @Comment("Maximum number of levels above the minimum level to get imperishable in an enchanting table. Default value is 50.")
    public int imperishableMaxLevelsAboveMin = 50;

    @Comment("Should Imperishable prevent items from despawning? Default value is true.")
    public boolean imperishablePreventsDespawn = true;

    @Comment("Should Imperishable prevent items from being destroyed? Default value is true.")
    public boolean imperishableProtectsFromDamage = true;

    @Comment("Should Imperishable prevent items from being destroyed in the void? Default value is true.")
    public boolean imperishableProtectsFromVoid = true;

    @Comment("Should Imperishable prevent items from breaking? If true, items don't break when they reach 0 durability but rather lose any special properties, such as increased mining speed on a pickaxe, until they are repaired. Default value is true.")
    public boolean imperishablePreventsBreaking = true;
}