package io.github.shaksternano.imperishableitems.common.util;

import io.github.shaksternano.imperishableitems.common.ImperishableItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;

public final class ImperishableProtection {

    private ImperishableProtection() {}

    private static final Set<Item> globalBlacklist = new HashSet<>();
    private static final Map<ProtectionType, Set<Item>> blacklists = initBlacklistsMap();

    private static boolean blacklistsInitialized = false;

    // Adds a blacklist for every ProtectionType
    private static Map<ProtectionType, Set<Item>> initBlacklistsMap() {
        Map<ProtectionType, Set<Item>> blacklists = new HashMap<>();
        for (ProtectionType protectionType : ProtectionType.values()) {
            blacklists.put(protectionType, new HashSet<>());
        }
        return blacklists;
    }

    // Adds the item with the corresponding item ID to the set.
    private static void addItemToSet(String itemID, Set<Item> set) {
        Item item = Registry.ITEM.get(new Identifier(itemID));
        // If the item ID isn't valid then item will be air.
        if (!item.equals(Items.AIR)) {
            set.add(item);
        }
    }

    // Produces Item blacklists from the Item ID String blacklists in the config.
    public static void initBlacklists() {
        if (!blacklistsInitialized) {
            for (String itemId : ImperishableItems.getBlacklists().globalBlacklist) {
                addItemToSet(itemId, globalBlacklist);
            }

            for (Map.Entry<ProtectionType, Set<Item>> entry : blacklists.entrySet()) {
                ProtectionType protectionType = entry.getKey();
                Set<Item> blacklist = entry.getValue();

                if (protectionType.ITEM_ID_BLACKLIST != null) {
                    for (String itemId : protectionType.ITEM_ID_BLACKLIST) {
                        addItemToSet(itemId, blacklist);
                    }
                }
            }

            blacklistsInitialized = true;
        }
    }

    // Returns true if the item is on the global blacklist, otherwise, returns false.
    public static boolean isItemBlacklistedGlobally(Item item) {
        return globalBlacklist.contains(item);
    }

    public static boolean isItemBlacklistedGlobally(ItemStack stack) {
        return isItemBlacklistedGlobally(stack.getItem());
    }

    // Returns true if an item is on the global blacklist or the blacklist for the specified protection type. Otherwise, returns false.
    private static boolean isItemBlacklisted(Item item, ProtectionType protectionType) {
        if (isItemBlacklistedGlobally(item)) {
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
        return protectionType.PROTECTION_ENABLED && !isItemBlacklisted(item, protectionType);
    }

    public static boolean isItemProtected(ItemStack stack, ProtectionType protectionType) {
        return isItemProtected(stack.getItem(), protectionType);
    }

    // Protection types and their associated item ID blacklists from config and boolean enabled value from config.
    public enum ProtectionType {
        DESPAWN_PROTECTION(ImperishableItems.getBlacklists().despawnProtectionBlacklist, ImperishableItems.getConfig().imperishablePreventsDespawn),
        DAMAGE_PROTECTION(ImperishableItems.getBlacklists().damageProtectionBlacklist, ImperishableItems.getConfig().imperishableProtectsFromDamage),
        VOID_PROTECTION(ImperishableItems.getBlacklists().voidProtectionBlacklist, ImperishableItems.getConfig().imperishableProtectsFromVoid),
        BREAK_PROTECTION(ImperishableItems.getBlacklists().breakProtectionBlacklist, ImperishableItems.getConfig().imperishablePreventsBreaking);

        private final List<String> ITEM_ID_BLACKLIST;
        private final boolean PROTECTION_ENABLED;

        ProtectionType(List<String> itemIdBlacklist, boolean protectionEnabled) {
            ITEM_ID_BLACKLIST = itemIdBlacklist;
            PROTECTION_ENABLED = protectionEnabled;
        }
    }
}
