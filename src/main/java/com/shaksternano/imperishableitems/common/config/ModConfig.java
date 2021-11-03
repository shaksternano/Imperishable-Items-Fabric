package com.shaksternano.imperishableitems.common.config;

import com.shaksternano.imperishableitems.common.ImperishableItems;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.enchantment.Enchantment;

@SuppressWarnings("CanBeFinal")
@Config(name = ImperishableItems.MOD_ID)
@Config.Gui.Background(Config.Gui.Background.TRANSPARENT)
public final class ModConfig implements ConfigData {

    @ConfigEntry.Category("obtainingImperishable")
    @Comment("Rarity of the Imperishable enchantment.\nMinecraft must be restarted for a\nchange of this option to take effect.\n\nOptions:\n  \"COMMON\"\n  \"UNCOMMON\"\n  \"RARE\"\n  \"VERY_RARE\"\n\nDefault value is \"RARE\".")
    public Enchantment.Rarity imperishableRarity = Enchantment.Rarity.RARE;

    @ConfigEntry.Category("obtainingImperishable")
    @Comment("Should Imperishable be a treasure enchant?\n\nDefault value is false.")
    public boolean imperishableIsTreasure = false;

    @ConfigEntry.Category("obtainingImperishable")
    @Comment("Should villagers sell Imperishable enchanted books?\n\nDefault value is true.")
    public boolean imperishableSoldByVillagers = true;

    @ConfigEntry.Category("obtainingImperishable")
    @Comment("Minimum experience level required to get\nImperishable in an enchanting table.\n\nDefault value is 15.")
    public int imperishableMinLevel = 15;

    @ConfigEntry.Category("obtainingImperishable")
    @Comment("Maximum number of levels above the minimum level\nto get Imperishable in an enchanting table.\n\nDefault value is 50.")
    public int imperishableMaxLevelsAboveMin = 50;

    @ConfigEntry.Category("imperishableProtection")
    @Comment("Should Imperishable prevent items from despawning?\n\nDefault value is true.")
    public boolean imperishablePreventsDespawn = true;

    @ConfigEntry.Category("imperishableProtection")
    @Comment("Should Imperishable prevent items from being destroyed?\n\nDefault value is true.")
    public boolean imperishableProtectsFromDamage = true;

    @ConfigEntry.Category("imperishableProtection")
    @Comment("Should Imperishable prevent items from\nbeing destroyed in the void?\n\nDefault value is true.")
    public boolean imperishableProtectsFromVoid = true;

    @ConfigEntry.Category("imperishableProtection")
    @Comment("Should Imperishable prevent items from breaking?\nIf true, items don't break when they reach\n0 durability, but rather lose any special\nproperties, such as increased mining speed\non a pickaxe, until they are repaired.\n\nDefault value is true.")
    public boolean imperishablePreventsBreaking = true;

    @ConfigEntry.Gui.Excluded
    @ConfigEntry.Category("obtainingImperishable")
    @Comment("Debug mode.\nAllows swapping between 0 and 1 durability on an\nitem by crouching, and adding or removing\nthe Imperishable enchantment by pressing the drop item key.\nDon't change this unless you're testing this mod.\n\nDefault value is false.")
    public boolean debugMode = false;
}
