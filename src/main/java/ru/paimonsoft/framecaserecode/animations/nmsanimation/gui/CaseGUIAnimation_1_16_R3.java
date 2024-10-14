package ru.paimonsoft.framecaserecode.animations.nmsanimation.gui;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import ru.paimonsoft.framecaserecode.FrameCasesRecode;
import ru.paimonsoft.framecaserecode.api.Actions;
import ru.paimonsoft.framecaserecode.other.Coloriser;
import ru.paimonsoft.framecaserecode.other.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaseGUIAnimation_1_16_R3 extends ConfigManager implements Listener {

    private final FrameCasesRecode instance = FrameCasesRecode.getInstance();
    public boolean animationRunning = false;
    private final List<ItemStack> animatedItems = new ArrayList<>();
    private final int[] itemSlots = {20, 21, 22, 23, 24}; // Слоты от 20 до 24


    String title = Coloriser.colorify("&7Открытие..");
    public void openAnimation(Player player) {
        FrameCasesRecode.isOpen = true;
        Inventory animationMenu = Bukkit.createInventory(null, 45, title);

        // Заполняем инвентарь заглушками (заглушками может быть, например, стеки воздуха)
        for (int i = 0; i < 45; i++) 
            animationMenu.setItem(i, new ItemStack(Material.AIR));
        

        player.openInventory(animationMenu);

        //FileConfiguration config = instance.getConfig();
        for (String items : getConfigurationSection("animation-gui.items").getKeys(false)) {
            ConfigurationSection item = getConfigurationSection("animation-gui.items." + items);
            List<String> lores = item.getStringList(".lore");
            String name = item.getString(".name");
            String material = item.getString(".material");
            int amount = item.getInt(".amount");
            int slot = item.getInt(".slot");

            List<String> lore = new ArrayList<>();
            ItemStack itemStack = new ItemStack(Material.matchMaterial(material), amount);
            ItemMeta itemMeta = itemStack.getItemMeta();
            for (String s : lores)
                lore.add(Coloriser.colorify(s));
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(Coloriser.colorify(name));
            itemStack.setItemMeta(itemMeta);
            animationMenu.setItem(slot, itemStack);
        }

        animationRunning = true;
        animatedItems.clear();

        // Заполняем animatedItems рандомными предметами (5 штук)
        for (int i = 0; i < 5; i++) {
            ItemStack item = getRandomItem();
            ItemMeta meta = item.getItemMeta();
            for (String st : getConfigurationSection("cases." + FrameCasesRecode.openCaseName).getKeys(false)) {
                String path = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".material");
                Material material = Material.valueOf(getString(path));
                if (item.getType().equals(material)) {
                    String path_ = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".name");
                    meta.setDisplayName(Coloriser.colorify(getString(path_)));
                    item.setItemMeta(meta);
                }
            }
            animatedItems.add(item);
        }

        new BukkitRunnable() {
            int ticks = 0;
            int maxTicks = getInt("animation-gui-time") * 2; // 10 секунд (20 тиков = 1 секунда)

            @Override
            public void run() {
                // Остановить анимацию через 10 секунд
                if (ticks >= maxTicks) {
                    animationRunning = false;
                    ItemStack itemToGive = animationMenu.getItem(22); // Получаем предмет из 22 слота

                    for (String st : getConfigurationSection("cases." + FrameCasesRecode.openCaseName).getKeys(false)) {
                        String path = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".material");
                        Material material = Material.valueOf(getString(path));
                        if (itemToGive.getType().equals(material)) {
                            String path_ = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".commands");
                            List<String> commands = getStringList(path_);
                            Actions.use(commands, player);
                        }
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.closeInventory(); // Закрыть инвентарь анимации
                    FrameCasesRecode.isOpen = false;
                    FrameCasesRecode.items.clear();
                    this.cancel();
                    return;
                }

                // Очищаем текущие слоты
                for (int slot : itemSlots) 
                    animationMenu.setItem(slot, new ItemStack(Material.AIR));

                // Устанавливаем следующие предметы на новых слотах
                for (int i = 0; i < 5; i++) 
                    animationMenu.setItem(itemSlots[i], animatedItems.get((ticks + i) % 5));

                player.updateInventory();
                ticks++;
            }
        }.runTaskTimer(instance, 0L, 10L); // Период анимации (в тиках)
    }

    private ItemStack getRandomItem() {
        return FrameCasesRecode.items.get(new Random().nextInt(FrameCasesRecode.items.size()));
    }

    @EventHandler
    public void guiClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player 
                && event.getView().getTitle().equals(title)) {
            event.setCancelled(true);
        }
    }
}