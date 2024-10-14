package ru.paimonsoft.framecaserecode.events;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.paimonsoft.framecaserecode.FrameCasesRecode;
import ru.paimonsoft.framecaserecode.gui.GUI;
import ru.paimonsoft.framecaserecode.other.Coloriser;
import ru.paimonsoft.framecaserecode.other.ConfigManager;

public class ClickBlock extends ConfigManager implements Listener {

    @EventHandler
    public void click(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ConfigurationSection location = getConfigurationSection("location");
        String w = location.getString("world");
        World world = Bukkit.getWorld(w);
        double x = location.getDouble("x");
        double y = location.getDouble("y");
        double z = location.getDouble("z");
        float pitch = (float) location.getDouble("pitch");
        float yaw = (float) location.getDouble("yaw");

        Location locationCase = new Location(world, x, y, z, pitch, yaw);

        if (event.getClickedBlock().getLocation().equals(locationCase)
                && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (FrameCasesRecode.isOpen) {
                player.sendMessage(Coloriser.colorify(getString("messages.case-is-opened")));
                event.setCancelled(true);
                return;
            }
            event.setCancelled(true);
            player.openInventory(GUI.inventory(player));
        }
    }

    public static void setChestOpened(Block block, boolean opened) {
        try {
            PacketContainer libPacket = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);
            libPacket.getBlockPositionModifier().write(0, new BlockPosition(block.getX(), block.getY(), block.getZ()));
            libPacket.getIntegers().write(0, 1);
            libPacket.getIntegers().write(1, opened ? 1 : 0);
            libPacket.getBlocks().write(0, block.getType());
            Location loc = block.getLocation();
            ProtocolManager manager = ProtocolLibrary.getProtocolManager();
            for (Player player : block.getWorld().getPlayers()) {
                if (player.getLocation().distanceSquared(loc) < (64 * 64)) {
                    manager.sendServerPacket(player, libPacket);
                }
            }
        } catch (Exception ex) {
            Bukkit.getLogger().severe("Невозможно отправить пакет на открытие сундука, проверьте, установлен ли ProtocolLib");
        }
    }
}