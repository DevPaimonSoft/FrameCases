package ru.paimonsoft.framecaserecode.api;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import ru.paimonsoft.framecaserecode.FrameCasesRecode;
import ru.paimonsoft.framecaserecode.other.Coloriser;
import ru.paimonsoft.framecaserecode.other.ConfigManager;

public class Holograms extends ConfigManager {

    private static final FrameCasesRecode instance = FrameCasesRecode.getInstance();
    public static HolographicDisplaysAPI api = HolographicDisplaysAPI.get(instance);
    public static void hologram(Location location) {
        api.deleteHolograms();
        Hologram hologram = api.createHologram(location.add(0.45, 1.9, 0.45));
        for (String s : getStringList("holograms")) {
            TextHologramLine textHologramLine = hologram.getLines().appendText(Coloriser.colorify(s));
        }
    }

    public static Location locationCase() {
        ConfigurationSection location = getConfigurationSection("location");
        String w = location.getString("world");
        World world = Bukkit.getWorld(w);
        double x = location.getDouble("x");
        double y = location.getDouble("y");
        double z = location.getDouble("z");
        float pitch = (float) location.getDouble("pitch");
        float yaw = (float) location.getDouble("yaw");
        return new Location(world, x, y, z, pitch, yaw);
    }

    public static void removeHD() {
        api.deleteHolograms();
    }
}
