package dev.lockpickgames.feelybots.utils;

import dev.lockpickgames.feelybots.FeelyBots;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemBuilder {
    private FeelyBots plugin;
    private ItemStack itemStack;
    private ItemMeta meta;

    public ItemBuilder(FeelyBots plugin) {
        this.plugin = plugin;
    }

    public ItemBuilder(){}

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.meta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.meta = this.itemStack.getItemMeta();
    }

    public ItemBuilder item(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.meta = this.itemStack.getItemMeta();
        return this;
    }

    public ItemBuilder material(Material material) {
        this.itemStack = new ItemStack(material);
        this.meta = this.itemStack.getItemMeta();
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name) {
        meta.setDisplayName(StringUtil.translate(name));
        return this;
    }

    public ItemBuilder lore(String...lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder addLore(String...lore) {
        List<String> list = new ArrayList<>();
        if(meta.hasLore()) {
            list.addAll(meta.getLore());
        }
        list.addAll(Arrays.asList(lore));
        return lore(list);
    }

    public ItemBuilder addItemFlags(ItemFlag...itemFlags) {
        meta.addItemFlags(itemFlags);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        meta.setLore(lore.stream().map(StringUtil::translate).collect(Collectors.toList()));
        return this;
    }

    public ItemBuilder skin(String url) {
        this.itemStack.setItemMeta(meta);
        this.itemStack = HeadUtil.getHeadFromUrl(url);
        this.meta = this.itemStack.getItemMeta();
        return this;
    }

    public ItemBuilder skin(UUID uuid) {
        this.itemStack.setItemMeta(meta);
        this.itemStack = HeadUtil.getHead(uuid);
        this.meta = this.itemStack.getItemMeta();
        return this;
    }

    public ItemBuilder enchant(List<String> list) {
        for(String string : list) {
            String[] data = string.split(";");
            Enchantment enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(data[0].toUpperCase()));
            this.meta.addEnchant(enchantment, Integer.parseInt(data[1]), true);
        }
        return this;
    }

    public ItemBuilder enchant(String string) {
        String[] data = string.split(";");
        Enchantment enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(data[0].toUpperCase()));
        return enchant(enchantment, Integer.parseInt(data[1]));
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder glow(boolean glow) {
        return glow ? glow() : this;
    }

    public ItemBuilder glow() {
        return enchant(Enchantment.UNBREAKING, 1);
    }

    // String persistent data
    public ItemBuilder data(String key, String value) {
        meta.getPersistentDataContainer().set(createKey(key), PersistentDataType.STRING, value);
        return this;
    }

    // Integer persistent data
    public ItemBuilder data(String key, int value) {
        meta.getPersistentDataContainer().set(createKey(key), PersistentDataType.INTEGER, value);
        return this;
    }

    // Double persistent data
    public ItemBuilder data(String key, double value) {
        meta.getPersistentDataContainer().set(createKey(key), PersistentDataType.DOUBLE, value);
        return this;
    }

    // Boolean persistent data
    public ItemBuilder data(String key, boolean value) {
        meta.getPersistentDataContainer().set(createKey(key), PersistentDataType.BOOLEAN, value);
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(meta);
        return this.itemStack;
    }

    private NamespacedKey createKey(String key) {
        return new NamespacedKey(plugin, key);
    }
}
