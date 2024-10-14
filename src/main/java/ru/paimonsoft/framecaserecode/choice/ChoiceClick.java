package ru.paimonsoft.framecaserecode.choice;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.paimonsoft.framecaserecode.api.CheckChoiceAnimation;
import ru.paimonsoft.framecaserecode.gui.GUI;
import ru.paimonsoft.framecaserecode.other.Coloriser;
import ru.paimonsoft.framecaserecode.other.ConfigManager;

public class ChoiceClick extends ConfigManager implements Listener {


    @EventHandler
    public void click(InventoryClickEvent event) {
        if (!event.getView().getTitle().equalsIgnoreCase(ChoiceAnimation.title)) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getWhoClicked();

        int[] animationSlots = {
                getInt("choice.items.ANIMATION_1.slot"),
                getInt("choice.items.ANIMATION_2.slot"),
                getInt("choice.items.ANIMATION_3.slot"),
                getInt("choice.items.ANIMATION_4.slot"),
                getInt("choice.items.ANIMATION_5.slot")
        };

        for (int i = 0; i < animationSlots.length; i++) {
            if (event.getSlot() == animationSlots[i]) {
                String itemConfigKey = "choice.items.ANIMATION_" + (i + 1);
                String nameAnimation = "ANIMATION_" + (i + 1);

                if (!player.hasPermission(getString(itemConfigKey + ".perms"))) {
                    for (String s : getStringList(itemConfigKey + ".no-perms")) {
                        player.sendMessage(Coloriser.colorify(s));
                    }
                    player.closeInventory();
                    return;
                }

                player.sendMessage(Coloriser.colorify(getString("messages.set-animation")));
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                CheckChoiceAnimation.setAnimation(player, nameAnimation);
                player.closeInventory();
                return;
            }
        }

        if (event.getSlot() == getInt("choice.items.BACK.slot")) {
            player.openInventory(GUI.inventory(player));
        }
    }
}
