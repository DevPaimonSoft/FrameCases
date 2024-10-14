package ru.paimonsoft.framecaserecode.gui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import ru.paimonsoft.framecaserecode.FrameCasesRecode;
import ru.paimonsoft.framecaserecode.animations.CaseAnimation;
import ru.paimonsoft.framecaserecode.animations.FastAnimation;
import ru.paimonsoft.framecaserecode.animations.nmsanimation.entities.AnimationArmorStand;
import ru.paimonsoft.framecaserecode.animations.nmsanimation.entities.RotationPlain;
import ru.paimonsoft.framecaserecode.animations.nmsanimation.gui.CaseGUIAnimation_1_12_R2;
import ru.paimonsoft.framecaserecode.animations.nmsanimation.gui.CaseGUIAnimation_1_16_R3;
import ru.paimonsoft.framecaserecode.api.CheckChoiceAnimation;
import ru.paimonsoft.framecaserecode.api.Holograms;
import ru.paimonsoft.framecaserecode.other.*;
import ru.paimonsoft.framecaserecode.choice.*;

public class ClickGUI extends ConfigManager implements Listener {

    private final FrameCasesRecode instance = FrameCasesRecode.getInstance();
    CaseAnimation animation = new CaseAnimation();
    CaseGUIAnimation_1_12_R2 guiAnimation_1_12_r2 = new CaseGUIAnimation_1_12_R2();
    CaseGUIAnimation_1_16_R3 guiAnimation_1_16_r3 = new CaseGUIAnimation_1_16_R3();

    @EventHandler
    public void clickMenu(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(GUI.title)) {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                if (event.getSlot() == 49) {
                    player.openInventory(ChoiceAnimation.choice());
                }
                for (String s : getConfigurationSection("cases").getKeys(false)) {
                    if (getInt("cases." + s + ".slot") == event.getSlot()) {
                        if (!CasesContainer.containsKey(player, s)) {
                            player.sendMessage(Coloriser.colorify(getString("messages.error-no-case")));
                            player.playSound(Holograms.locationCase(), Sound.valueOf(getString("settings.no-open-sound")), 1f, 1f); //settings.no-open-sound
                            player.closeInventory();
                            return;
                        }
                        for (String st : getConfigurationSection("cases." + s).getKeys(false)) {
                            String path = String.join(".", "cases." + s + "." + st + ".material");
                            Material material = Material.valueOf(getString(path));
                            String pathData = String.join(".", "cases." + s + "." + st + ".data");
                            byte data = (byte) getInt(pathData);
                            ItemStack item = new ItemStack(material, 1, data);
                            FrameCasesRecode.items.add(item);
                        }


                        if (!CheckChoiceAnimation.contains(player)) {
                            player.sendMessage(Coloriser.colorify("&cОшибка, выберите анимацию, чтобы открыть кейс!"));
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                            player.closeInventory();
                            return;
                        }

                        FrameCasesRecode.openCaseName = s;
                        CasesContainer.takeKey(player.getName(), FrameCasesRecode.openCaseName, 1);

                        switch (CheckChoiceAnimation.getAnimation(player)){
                            case "ANIMATION_1": {
                                animation.startCaseAnimation(Holograms.locationCase(), player);
                                player.closeInventory();
                                break;
                            }
                            case "ANIMATION_2": {
                                if (instance.getServer().getVersion().contains("1.16") ||
                                        instance.getServer().getVersion().contains("1.17") ||
                                        instance.getServer().getVersion().contains("1.18")) {
                                    if (!guiAnimation_1_16_r3.animationRunning) {
                                        guiAnimation_1_16_r3.openAnimation(player);
                                        return;
                                    }
                                } else {
                                    if (!guiAnimation_1_12_r2.animationRunning) {
                                        guiAnimation_1_12_r2.openAnimation(player);
                                        return;
                                    }
                                    return;
                                }
                                return;
                            }
                            case "ANIMATION_3": {
                                AnimationArmorStand anim = new AnimationArmorStand(RotationPlain.Z);
                                anim.play(player, instance, Holograms.locationCase(), 6, 1.5, 5, 1, 238, true);
                                player.closeInventory();
                                break;
                            }
                            case "ANIMATION_4": {
                                AnimationArmorStand anim = new AnimationArmorStand(RotationPlain.Y);
                                anim.play(player, instance, Holograms.locationCase(), 6, 1.5, 5, 1, 238, false);
                                player.closeInventory();
                                break;
                            }
                            case "ANIMATION_5": {
                                FrameCasesRecode.isOpen = true;
                                new FastAnimation().openCase(player);
                                player.closeInventory();
                                break;
                            }
                        }
                        return;
                    }
                }
            }
        }
    }
}