package ru.paimonsoft.framecaserecode.other;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.paimonsoft.framecaserecode.FrameCasesRecode;

import java.util.List;

public class ConfigManager {
    public static String s(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    private static final FileConfiguration config = FrameCasesRecode.getInstance().getConfig();
    public static void  set(@NotNull String path, @Nullable Object value){
        config.set(path, value);
    }
    public static String getString(@NotNull String path){
        return config.getString(path);
    }
    public static int getInt(@NotNull String path){
        return config.getInt(path);
    }
    public static List<String> getStringList(@NotNull String path){
        return config.getStringList(path);
    }
    public static ConfigurationSection getConfigurationSection(@NotNull String path){
        return config.getConfigurationSection(path);
    }
    public static boolean getBoolean(@NotNull String path){
        return config.getBoolean(path);
    }
}
