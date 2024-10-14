package ru.paimonsoft.framecaserecode;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import ru.paimonsoft.framecaserecode.animations.nmsanimation.gui.CaseGUIAnimation_1_12_R2;
import ru.paimonsoft.framecaserecode.api.Holograms;
import ru.paimonsoft.framecaserecode.choice.ChoiceClick;
import ru.paimonsoft.framecaserecode.commands.*;
import ru.paimonsoft.framecaserecode.other.*;
import ru.paimonsoft.framecaserecode.gui.*;
import ru.paimonsoft.framecaserecode.events.*;

import java.util.ArrayList;
import java.util.List;

public class FrameCasesRecode extends JavaPlugin {
    public static List<ItemStack> items = new ArrayList<>(); // Список предметов для анимации
    private static FrameCasesRecode instance;
    public static boolean isOpen = false;
    public static String openCaseName;
    public ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();
        if (pluginManager.getPlugin("HolographicDisplays") == null || pluginManager.getPlugin("ProtocolLib") == null) {
            getLogger().warning("No install HolographicDisplays and ProtocolLib plugins.");
            pluginManager.disablePlugin(this);
        }

        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        saveDefaultConfig();

        pluginManager.registerEvents(new ClickBlock(), this);
        pluginManager.registerEvents(new ClickGUI(), this);
        pluginManager.registerEvents(new SetCase(), this);
        pluginManager.registerEvents(new ChoiceClick(), this);
        pluginManager.registerEvents(new EquItems(), this);
        pluginManager.registerEvents(new CaseGUIAnimation_1_12_R2(), this);
        getCommand("framecases").setExecutor(new CommandsManager());
        getCommand("framecases").setTabCompleter(new TabCommands());
        Holograms.hologram(Holograms.locationCase());
        BlockParticles.startStarfallAnimation();
        runMetrics();
    }

    public void runMetrics() {
        Metrics metrics = new Metrics(this, 19894);
        metrics.addCustomChart(new Metrics.SimplePie("holograms", () -> String.valueOf(this.getConfig().getStringList("holograms"))));
    }

    public static FrameCasesRecode getInstance() {
        return instance;
    }
}
