package com.shaksternano.imperishableitems.common.api;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.shaksternano.imperishableitems.common.ImperishableItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;

public class ImperishableProtection {

    private static final Set<Item> globalBlacklist = Sets.newHashSet();
    private static final EnumSet<ProtectionType> PROTECTION_TYPES = EnumSet.allOf(ProtectionType.class);
    private static final Map<ProtectionType, Set<Item>> blacklists = initBlacklistsMap();

    // Adds a blacklist for every value in ProtectionType
    private static Map<ProtectionType, Set<Item>> initBlacklistsMap() {
        Map<ProtectionType, Set<Item>> blacklists = Maps.newHashMap();
        for (ProtectionType protectionType : PROTECTION_TYPES) {
            blacklists.put(protectionType, Sets.newHashSet());
        }
        return blacklists;
    }

    // Produces Item blacklists from the Item ID String blacklists in the config.
    public static void initBlacklists() {
        for (String stringId : ImperishableItems.getBlacklists().globalBlacklist) {
            Identifier itemId = new Identifier(stringId);
            Item item = Registry.ITEM.get(itemId);
            if (!item.equals(Items.AIR)) {
                globalBlacklist.add(item);
            }
        }

        for (Map.Entry<ProtectionType, Set<Item>> entry : blacklists.entrySet()) {
            ProtectionType protectionType = entry.getKey();
            Set<Item> blacklist = entry.getValue();

            List<String> stringIds;
            switch (protectionType) {
                case DESPAWN_PROTECTION -> stringIds = ImperishableItems.getBlacklists().despawnProtectionBlacklist;
                case DAMAGE_PROTECTION -> stringIds = ImperishableItems.getBlacklists().damageProtectionBlacklist;
                case VOID_PROTECTION -> stringIds = ImperishableItems.getBlacklists().voidProtectionBlacklist;
                case BREAK_PROTECTION -> stringIds = ImperishableItems.getBlacklists().breakProtectionBlacklist;
                default -> stringIds = Collections.emptyList();
            }

            for (String stringId : stringIds) {
                Identifier itemId = new Identifier(stringId);
                Item item = Registry.ITEM.get(itemId);
                if (!item.equals(Items.AIR)) {
                    blacklist.add(item);
                }
            }
        }
    }

    // Returns true if an item is on the global blacklist or the blacklist for the specified protection type. Otherwise, returns false.
    private static boolean isItemBlacklisted(Item item, ProtectionType protectionType) {
        if (globalBlacklist.contains(item)) {
            return true;
        } else {
            Set<Item> blacklist = blacklists.get(protectionType);
            if (blacklist != null) {
                return blacklist.contains(item);
            }

            return false;
        }
    }

    // Returns true if the specified item is not blacklisted and Imperishable is set to protect from the specified protection type. Otherwise, returns false.
    public static boolean isItemProtected(Item item, ProtectionType protectionType) {
        boolean protectionEnabled;
        switch (protectionType) {
            case DESPAWN_PROTECTION -> protectionEnabled = ImperishableItems.getConfig().imperishablePreventsDespawn;
            case DAMAGE_PROTECTION -> protectionEnabled = ImperishableItems.getConfig().imperishableProtectsFromDamage;
            case VOID_PROTECTION -> protectionEnabled = ImperishableItems.getConfig().imperishableProtectsFromVoid;
            case BREAK_PROTECTION -> protectionEnabled = ImperishableItems.getConfig().imperishablePreventsBreaking;
            default -> protectionEnabled = true;
        }

        return protectionEnabled && !isItemBlacklisted(item, protectionType);
    }

    public static boolean isItemProtected(ItemStack stack, ProtectionType protectionType) {
        return isItemProtected(stack.getItem(), protectionType);
    }

    public enum ProtectionType {
        DESPAWN_PROTECTION,
        DAMAGE_PROTECTION,
        VOID_PROTECTION,
        BREAK_PROTECTION
    }
}
