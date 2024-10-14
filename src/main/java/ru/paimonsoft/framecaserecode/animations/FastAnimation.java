package ru.paimonsoft.framecaserecode.animations;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.paimonsoft.framecaserecode.FrameCasesRecode;
import ru.paimonsoft.framecaserecode.api.Actions;
import ru.paimonsoft.framecaserecode.other.ConfigManager;

import java.util.List;
import java.util.Random;

public class FastAnimation extends ConfigManager {

    public void openCase(Player player) {
        ItemStack drop = getRandomItem();
        for (String st : getConfigurationSection("cases." + FrameCasesRecode.openCaseName).getKeys(false)) {
            String path = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".material");
            Material material = Material.valueOf(getString(path));
            String pathData = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".data");
            byte data = (byte) getInt(pathData);
            if (drop.getType().equals(material) && drop.getData().getData() == data) {
                String path_ = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".commands");
                List<String> commands = getStringList(path_);
                Actions.use(commands, player);
            }
        }

        FrameCasesRecode.items.clear();
        FrameCasesRecode.isOpen = false;
    }

    private ItemStack getRandomItem() {
        return FrameCasesRecode.items.get(new Random().nextInt(FrameCasesRecode.items.size()));
    }
}