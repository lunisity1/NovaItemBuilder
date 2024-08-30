package dev.lunisity.novaitembuilder.builder;

import com.google.common.base.Preconditions;
import dev.lunisity.novaitembuilder.api.color.ColorAPI;
import dev.lunisity.novaitembuilder.api.color.TextUtils;
import dev.lunisity.novaitembuilder.api.text.TextReplacer;
import dev.lunisity.novaitembuilder.builder.enums.ItemBuilderAction;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Getter
public class ItemBuilder {

    private static final Comparator<ItemBuilderAction> COMPARATOR = Comparator.comparingInt(ItemBuilderAction::getPriority).reversed();

    private static final Predicate<String> PREDICATE = ItemBuilderAction::isType;

    private final ItemStack itemStack;

    private int slot;

    public static ItemBuilder from(final ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static ItemBuilder empty() {
        return ItemBuilder.of(Material.AIR, 1);
    }

    public static ItemBuilder from(final FileConfiguration config, final String path) {
        Preconditions.checkNotNull(config, "Failed to find configuration file");

        final ItemBuilder itemBuilder = ItemBuilder.empty();
        final Set<String> keys = Objects.requireNonNull(config.getConfigurationSection(path)).getKeys(false);

        keys.stream().filter(ItemBuilder.PREDICATE)
                .map(ItemBuilderAction::from)
                .filter(Objects::nonNull)
                .forEach(itemBuilderAction -> {
                    itemBuilderAction.apply(itemBuilder, config, path);
                });
        return itemBuilder;
    }

    public static ItemBuilder of(final Material material, final int amount) {
        return new ItemBuilder(material, amount);
    }

    public static ItemBuilder of(final Material material) {
        return ItemBuilder.of(material, 1);
    }

    public ItemBuilder(final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder(final Material material) {
        this(material, 1);
    }

    public ItemBuilder(final Material material, final int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    @Override
    public ItemBuilder clone() {
        return new ItemBuilder(this.itemStack.clone());
    }

    public ItemBuilder setMaterial(final Material material) {
        return transformStack(stack -> stack.setType(material));
    }

    public ItemBuilder setAmount(final int amount) {
        return transformStack(stack -> stack.setAmount(amount));
    }

    public ItemBuilder setName(final String name) {
        return transformMeta(itemMeta -> {
            itemMeta.setDisplayName(ColorAPI.apply(name));
        });
    }

    public ItemBuilder setLore(final List<String> lore) {
        return transformMeta(itemMeta -> {
            itemMeta.setLore(ColorAPI.apply(lore));
        });
    }

    public ItemBuilder setGlow(final boolean glow) {
        return transformMeta(itemMeta -> {

            if (glow) {
                itemMeta.addEnchant(Enchantment.LURE, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                itemMeta.removeEnchant(Enchantment.LURE);
                itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
        });
    }

    public ItemBuilder addEnchant(final Enchantment enchantment, final int level) {
        return transformMeta(itemMeta -> {
            itemMeta.addEnchant(enchantment, level, true);
        });
    }

    public ItemBuilder addEnchants(final Map<Enchantment, Integer> mappedEnchants) {
        mappedEnchants.forEach(this::addEnchant);
        return this;
    }

    public ItemBuilder addFlags(final ItemFlag... flags) {
        return transformMeta(itemMeta -> {
            itemMeta.addItemFlags(flags);
        });
    }

    public ItemBuilder atIndex(final String original, final List<String> replacement) {
        return transformMeta(meta -> {

            if (!meta.hasLore()) {
                return;
            }

            final List<String> list = meta.getLore();

            int index = list.indexOf(original);

            if (index == -1) {
                return;
            }

            list.remove(index);
            for (final String string : replacement) {
                list.add(index, ColorAPI.apply(string));
                index++;
            }
            meta.setLore(list);
        });
    }

    public ItemBuilder atIndex(final String original, final String replacement) {
        return transformMeta(itemMeta -> {

            if (!itemMeta.hasLore()) {
                return;
            }

            final List<String> lore = itemMeta.getLore();


            final int index = lore.indexOf(original);

            if (index == -1) {
                return;
            }

            lore.set(index, replacement);
            itemMeta.setLore(lore);
        });
    }

    public ItemBuilder apply(final TextReplacer replacer) {
        return transformMeta(itemMeta -> {

            if (itemMeta.hasDisplayName()) {
                itemMeta.setDisplayName(replacer.apply(itemMeta.getDisplayName()));
            }
            if (itemMeta.hasLore()) {
                final List<String> lore = itemMeta.getLore();
                itemMeta.setLore(ColorAPI.apply(replacer.apply(lore)));
            }
        });
    }

    public ItemBuilder apply(final Supplier<TextReplacer> supplier) {
        return transformMeta(itemMeta -> {

            final TextReplacer replacer = supplier.get();

            if (itemMeta.hasDisplayName()) {
                itemMeta.setDisplayName(replacer.apply(itemMeta.getDisplayName()));
            }

            if (itemMeta.hasLore()) {
                itemMeta.setLore(replacer.apply(itemMeta.getLore()));
            }
        });
    }

    public ItemBuilder setUnbreakable(final boolean unbreakable) {
        return transformMeta(itemMeta -> itemMeta.setUnbreakable(unbreakable));
    }

    public void setSlot(final int value) {
        slot = value;
    }

    public ItemBuilder transformStack(Consumer<ItemStack> consumer) {
        consumer.accept(this.itemStack);
        return this;
    }

    public ItemBuilder transformMeta(Consumer<ItemMeta> consumer) {

        final ItemMeta itemMeta;

        if (this.itemStack.hasItemMeta()) {
            itemMeta = this.itemStack.getItemMeta();
        } else {
            itemMeta = Bukkit.getItemFactory().getItemMeta(this.itemStack.getType());
        }

        consumer.accept(itemMeta);
        this.itemStack.setItemMeta(itemMeta);

        return this;
    }
}
