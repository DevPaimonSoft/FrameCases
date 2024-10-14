package ru.paimonsoft.framecaserecode.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.paimonsoft.framecaserecode.FrameCasesRecode;
import ru.paimonsoft.framecaserecode.api.Holograms;
import ru.paimonsoft.framecaserecode.other.Coloriser;
import ru.paimonsoft.framecaserecode.other.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class SetCase extends ConfigManager implements Listener {

    private final FrameCasesRecode instance = FrameCasesRecode.getInstance();
    public static List<Player> setCaseList = new ArrayList<Player>();

    @EventHandler
    public void click(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (setCaseList.contains(player)
                && event.getClickedBlock().getType() != Material.AIR) {
            Location location = event.getClickedBlock().getLocation();

            set("location.world", location.getWorld().getName());
            set("location.x", location.getX());
            set("location.y", location.getY());
            set("location.z", location.getZ());
            set("location.pitch", location.getPitch());
            set("location.yaw", location.getYaw());

            instance.saveConfig();

            player.sendMessage(Coloriser.colorify(getString("messages.case-position-put")));
            event.setCancelled(true);

            Holograms.hologram(Holograms.locationCase());
            setCaseList.remove(player);
        }
    }
}