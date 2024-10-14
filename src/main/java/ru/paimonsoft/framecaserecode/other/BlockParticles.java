package ru.paimonsoft.framecaserecode.other;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import ru.paimonsoft.framecaserecode.FrameCasesRecode;
import ru.paimonsoft.framecaserecode.api.Holograms;

public class BlockParticles extends ConfigManager {

    private static final boolean isPower = getBoolean("block-particles.power");

    public static void startStarfallAnimation() {
        if (isPower) {
            new BukkitRunnable() {
                public void run() {
                    Location centerLocation = Holograms.locationCase().add(0.45, 0.3, 0.45);
                    for (int i = 0; i < 30; i++) {
                        double offsetX = Math.random() * 4.0D - 2.0D;
                        double offsetY = Math.random() * 4.0D - 2.0D;
                        double offsetZ = Math.random() * 4.0D - 2.0D;
                        Location particleLocation = centerLocation.clone().add(offsetX, offsetY, offsetZ);
                        centerLocation.getWorld().spawnParticle(Particle.valueOf(getString("block-particles.particle")), particleLocation, 1, 0.0D, 0.0D, 0.0D, 0.1D);
                    }
                }
            }.runTaskTimer(FrameCasesRecode.getInstance(), 0L, 20L);
        }
    }
}