package ru.paimonsoft.framecaserecode.gui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.paimonsoft.framecaserecode.FrameCasesRecode;
import ru.paimonsoft.framecaserecode.other.CasesContainer;
import ru.paimonsoft.framecaserecode.other.Coloriser;
import ru.paimonsoft.framecaserecode.other.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class GUI extends ConfigManager {
    public static String title = Coloriser.colorify("&7Кейсы");

    public static Inventory inventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, title);

        for (String items : getConfigurationSection("gui.items").getKeys(false)) {
            ConfigurationSection item = getConfigurationSection("gui.items." + items);
            List<String> lores = item.getStringList(".lore");
            String name = item.getString(".name");
            String material = item.getString(".material");
            int amount = item.getInt(".amount");
            int slot = item.getInt(".slot");
            byte data = (byte) item.getInt(".data");

            List<String> lore = new ArrayList<>();
            ItemStack itemStack = new ItemStack(Material.matchMaterial(material), amount, data);
            ItemMeta itemMeta = itemStack.getItemMeta();
            String case_ = item.getString(".case");
            for (String s : lores)
                lore.add(Coloriser.colorify(s).replace("%keys_" + case_ + "%", String.valueOf(CasesContainer.getKey(player, case_))));
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(Coloriser.colorify(name));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot, itemStack);
        }
        return inventory;
    }

}