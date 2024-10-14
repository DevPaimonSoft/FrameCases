package ru.paimonsoft.framecaserecode.other;
import org.bukkit.ChatColor;

public class Coloriser {

    public static String colorify(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
