package com.shaksternano.imperishableitems.config;

import com.shaksternano.imperishableitems.ImperishableItems;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = ImperishableItems.MOD_ID)
@Config.Gui.Background(Config.Gui.Background.TRANSPARENT)
public class ModConfig implements ConfigData {

    public boolean imperishableIsTreasure = false;
    public boolean imperishableSoldByVillagers = true;
    public int imperishableMinPower = 15;

    public boolean imperishablePreventsDespawn = true;
    public boolean imperishableProtectsFromDamage = true;
    public boolean imperishableProtectsFromVoid = true;
    public boolean imperishablePreventsBreaking = true;
}