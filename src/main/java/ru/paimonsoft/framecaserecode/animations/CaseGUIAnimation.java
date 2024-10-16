package ru.paimonsoft.framecaserecode.animations;

import org.bukkit.*;
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
import ru.paimonsoft.framecaserecode.other.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaseGUIAnimation extends ConfigManager implements Listener {

    public boolean animationRunning = false;
    private List<ItemStack> animatedItems = new ArrayList<>();
    private int currentItemIndex = 0;
    private int[] itemSlots = {20, 21, 22, 23, 24}; // Слоты от 20 до 24



    String title = s("&7Открытие..");
    public Inventory openAnimation(Player player) {
        FrameCasesRecode.isOpen = true;
        Inventory animationMenu = Bukkit.createInventory(null, 45, title);

        // Заполняем инвентарь заглушками (заглушками может быть, например, стеки воздуха)
        for (int i = 0; i < 45; i++)
            animationMenu.setItem(i, new ItemStack(Material.AIR));

        player.openInventory(animationMenu);

        for (String items : getConfigurationSection("animation-gui.items").getKeys(false)) {
            try {
                List<String> lores = getStringList("animation-gui.items." + items + ".lore");
                String name = getString("animation-gui.items." + items + ".name");
                String material = getString("animation-gui.items." + items + ".material");
                int amount = getInt("animation-gui.items." + items + ".amount");
                int slot = getInt("animation-gui.items." + items + ".slot");
                byte data = (byte) getInt("animation-gui.items." + items + ".data");

                List<String> lore = new ArrayList<>();
                ItemStack itemStack = new ItemStack(Material.matchMaterial(material), amount, data);
                ItemMeta itemMeta = itemStack.getItemMeta();
                for (String varLore : lores)
                    lore.add(s(varLore));
                itemMeta.setLore(lore);
                itemMeta.setDisplayName(s(name));
                itemStack.setItemMeta(itemMeta);
                animationMenu.setItem(slot, itemStack);
            } catch (NullPointerException ignored) {

            }
        }

        animationRunning = true;
        animatedItems.clear();

        // Заполняем animatedItems рандомными предметами (5 штук)
        for (int i = 0; i < 5; i++) {
            ItemStack item = getRandomItem();
            ItemMeta meta = item.getItemMeta();
            for (String st : getConfigurationSection("cases." + FrameCasesRecode.openCaseName).getKeys(false)) {
                String path = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".material");
                Material material = Material.valueOf(FrameCasesRecode.getInstance().getConfig().getString(path));
                String pathData = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".data");
                byte data = (byte)getInt(pathData);
                if (item.getType().equals(material) && item.getData().getData() == data) {
                    String path_ = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".name");
                    meta.setDisplayName(s(FrameCasesRecode.getInstance().getConfig().getString(path_)));
                    item.setItemMeta(meta);
                }
            }
            animatedItems.add(item);
        }

        new BukkitRunnable() {
            int ticks = 0;
            final int maxTicks = getInt("animation-gui-time") * 2; // 10 секунд (20 тиков = 1 секунда)

            @Override
            public void run() {
                // Остановить анимацию через 10 секунд
                if (ticks >= maxTicks) {
                    animationRunning = false;
                    ItemStack itemToGive = animationMenu.getItem(22); // Получаем предмет из 22 слота

                    for (String st : getConfigurationSection("cases." + FrameCasesRecode.openCaseName).getKeys(false)) {
                        try {
                            String path = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".material");
                            Material material = Material.valueOf(getString(path));
                            String pathData = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".data");
                            byte data = (byte) getInt(pathData);
                            if (itemToGive.getType().equals(material) && itemToGive.getData().getData() == data) {
                                String path_ = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".commands");
                                List<String> commands = getStringList(path_);
                                Actions.use(commands, player);
                            }
                        } catch (NullPointerException exception) {

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
                for (int slot : itemSlots) {
                    animationMenu.setItem(slot, new ItemStack(Material.AIR));
                }

                // Устанавливаем следующие предметы на новых слотах
                for (int i = 0; i < 5; i++) {
                    animationMenu.setItem(itemSlots[i], animatedItems.get((ticks + i) % 5));
                }

                player.updateInventory();

                ticks++;
            }
        }.runTaskTimer(FrameCasesRecode.getInstance(), 0L, 10L); // Период анимации (в тиках)
        return animationMenu;
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