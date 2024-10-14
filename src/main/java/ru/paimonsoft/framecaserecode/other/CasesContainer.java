package ru.paimonsoft.framecaserecode.other;
import org.bukkit.entity.Player;
import ru.paimonsoft.framecaserecode.FrameCasesRecode;

import java.util.ArrayList;
import java.util.List;

public class CasesContainer extends ConfigManager {

    private static final FrameCasesRecode instance = FrameCasesRecode.getInstance();

    public static void giveKey(String name, String case_, int amount) {
        String s = String.join(".", "players." + name + "." + case_);
        int i = getInt(s) + amount;
        setAndSave(s, i);
    }

    public static boolean takeKey(String name, String case_, int amount) {
        String s = String.join(".", "players." + name + "." + case_);
        int i = getInt(s);
        if (i >= amount) {
            i -= amount;
            setAndSave(s, i);
            return true;
        }
        return false;
    }

    public static void setKey(String name, String case_, int amount) {
        String s = String.join(".", "players." + name + "." + case_);
        setAndSave(s, amount);
    }

    public static int getKey(Player player, String case_) {
        String s = String.join(".", "players." + player.getName() + "." + case_);
        return Math.max(getInt(s), 0);
    }

    public static boolean containsKey(Player player, String case_) {
        String s = String.join(".", "players." + player.getName() + "." + case_);
        return (getInt(s) > 0);
    }

    public static boolean isValidateCase(String case_) {
        String s = String.join(".", "cases." + case_);
        return instance.getConfig().contains(s);
    }

    public static List<String> cases() {
        return new ArrayList<>(instance.getConfig().getConfigurationSection("cases").getKeys(false));
    }
    private static void setAndSave(String var0, Object var1){
        instance.getConfig().set(var0, var1);
        instance.saveConfig();
    }
}
