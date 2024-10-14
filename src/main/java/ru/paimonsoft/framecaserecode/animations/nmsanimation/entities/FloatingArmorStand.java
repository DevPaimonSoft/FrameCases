package ru.paimonsoft.framecaserecode.animations.nmsanimation.entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import ru.paimonsoft.framecaserecode.FrameCasesRecode;
import ru.paimonsoft.framecaserecode.other.Coloriser;

import java.util.Random;

public class FloatingArmorStand {

    private final FrameCasesRecode instance = FrameCasesRecode.getInstance();
    AnimationArmorStand anim;
    ArmorStand ast;
    double angleRads;

    public FloatingArmorStand(AnimationArmorStand anim, double angleRads) {
        this.anim = anim;
        this.angleRads = angleRads;
    }

    public void rotate(Location center, double distance, double rads, boolean counterClockwise) {
        angleRads += counterClockwise ? rads : -rads;
        double offsetX = Math.cos(angleRads) * distance;
        double offsetY = Math.sin(angleRads) * distance;

        Location astLoc = anim.getPlain().transform(center.clone(), offsetX, offsetY);

        if (ast == null) {

            ast = center.getWorld().spawn(astLoc, ArmorStand.class);
            ast.setGravity(false);
            ast.setVisible(false);
            ast.getEquipment().setHelmet(new ItemStack(getRandomItem()));
            for (String st : instance.getConfig().getConfigurationSection("cases." + FrameCasesRecode.openCaseName).getKeys(false)) {
                String path = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".material");
                Material material = Material.valueOf(instance.getConfig().getString(path));
                String pathData = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".data");
                String hdPath = String.join(".", "cases." + FrameCasesRecode.openCaseName + "." + st + ".name");
                String hd = Coloriser.colorify(instance.getConfig().getString(hdPath));
                byte data = (byte) instance.getConfig().getInt(pathData);
                if (ast.getEquipment().getHelmet().getType().equals(material) && ast.getEquipment().getHelmet().getData().getData() == data) {
                    ast.setCustomName(hd);
                    ast.setCustomNameVisible(true);
                }
            }
            ast.setMetadata("case_", new FixedMetadataValue(FrameCasesRecode.getInstance(), true));
        } else {
            if (ast.getEquipment().getHelmet().getType().isBlock()) {
                ast.teleport(astLoc);
                return;
            }
            ast.teleport(astLoc.add(0, -0.5, 0));
        }
    }

    public ArmorStand getArmorStand() {
        return ast;
    }

    public double getY() {
        return ast.getLocation().getY();
    }

    public void removeArmorStand() {
        ast.remove();
    }

    private ItemStack getRandomItem() {
        return FrameCasesRecode.items.get(new Random().nextInt(FrameCasesRecode.items.size()));
    }
}
