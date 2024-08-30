package dev.lunisity.novaitembuilder.builder.enums;

import dev.lunisity.novaitembuilder.builder.ItemBuilder;
import dev.lunisity.novaitembuilder.interfaces.TriConsumer;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum ItemBuilderAction {

    MATERIAL((bukkitConfig, string, itemBuilder) -> {

        final Material material = Material.getMaterial(bukkitConfig.getString(string + ".Material"));
        itemBuilder.setMaterial(material == null ? Material.BARRIER : material);
    }, 1, "Material"),

    AMOUNT((bukkitConfig, string, itemBuilder) -> {

        final int amount = bukkitConfig.getInt(string + ".Amount");
        itemBuilder.setAmount(amount);
    }, "Amount"),

    NAME((bukkitConfig, string, itemBuilder) -> {

        final String name = bukkitConfig.getString(string + ".Name");
        itemBuilder.setName(name);
    }, "Name"),

    LORE((bukkitConfig, string, itemBuilder) -> {

        final List<String> lore = bukkitConfig.getStringList(string + ".Lore");
        itemBuilder.setLore(lore);
    }, "Lore"),

    GLOW((bukkitConfig, string, itemBuilder) -> {

        final boolean glow = bukkitConfig.getBoolean(string + ".Glow");
        itemBuilder.setGlow(glow);
    }, "Glow"),

    ITEM_FLAGS((bukkitConfig, string, itemBuilder) -> {

        final List<String> itemFlags = bukkitConfig.getStringList(string + ".Item-Flags");
        final ItemFlag[] flags = new ItemFlag[itemFlags.size()];
        int index = 0;
        for (final String itemFlag : itemFlags) {
            flags[index++] = ItemFlag.valueOf(itemFlag);
        }
        itemBuilder.addFlags(flags);
    }, "Item-Flags"),

    ENCHANTS((bukkitConfig, string, itemBuilder) -> {

        final Map<Enchantment, Integer> mappedEnchants = new HashMap<>();

        final List<String> enchantments = bukkitConfig.getStringList(string + ".Enchants");

        for (final String enchantmentData : enchantments) {

            final String[] splitData = enchantmentData.split(";");

            final Enchantment enchantment = Enchantment.getByName(splitData[0]);
            final int enchantmentLevel = Integer.parseInt(splitData[1]);

            mappedEnchants.put(enchantment, enchantmentLevel);
        }
        itemBuilder.addEnchants(mappedEnchants);
    }, "Enchants"),

    UNBREAKABLE((bukkitConfig, string, itemBuilder) -> {

        final boolean unbreakable = bukkitConfig.getBoolean(string + ".Unbreakable");
        itemBuilder.setUnbreakable(unbreakable);
    }, "Unbreakable"),

    SLOT((bukkitConfig, string, itemBuilder) -> {
        final int slot = bukkitConfig.getInt(string + ".Slot");
        itemBuilder.setSlot(slot);
    }, "Slot");

    private final TriConsumer<FileConfiguration, String, ItemBuilder> consumer;

    private final int priority;
    private final String key;

    ItemBuilderAction(final TriConsumer<FileConfiguration, String, ItemBuilder> consumer, final int priority, final String key) {
        this.consumer = consumer;
        this.priority = priority;
        this.key = key;
    }

    ItemBuilderAction(final TriConsumer<FileConfiguration, String, ItemBuilder> consumer, final String key) {
        this(consumer, 0, key);
    }

    public void apply(final ItemBuilder itemBuilder, final FileConfiguration config, final String path) {
        this.consumer.apply(config, path, itemBuilder);
    }

    private static final Map<String, ItemBuilderAction> MAPPED_TYPES = Arrays.stream(ItemBuilderAction.values())
            .collect(Collectors.toMap(actionType -> actionType.key.toLowerCase(), actionType -> actionType));

    public static boolean isType(final String key) {
        return ItemBuilderAction.MAPPED_TYPES.containsKey(key.toLowerCase());
    }

    public static ItemBuilderAction from(final String key) {
        return ItemBuilderAction.MAPPED_TYPES.get(key.toLowerCase());
    }
}