package com.shaksternano.imperishableitems.common.config;

import com.shaksternano.imperishableitems.common.ImperishableItems;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.enchantment.Enchantment;

@Config(name = ImperishableItems.MOD_ID)
@Config.Gui.Background(Config.Gui.Background.TRANSPARENT)
public final class ModConfig implements ConfigData {

    @Comment("Rarity of the Imperishable enchantment. Minecraft must be restarted for a change of this to take effect. Default value is \"RARE\".")
    public Enchantment.Rarity imperishableRarity = Enchantment.Rarity.RARE;

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

    // Allows swapping between 0 and 1 durability on an item by crouching, and adding or removing the Imperishable enchantment by pressing the drop item key.
    @Comment("Don't change this unless you know what you're doing. Default value is false.")
    public boolean debugMode = false;
}
