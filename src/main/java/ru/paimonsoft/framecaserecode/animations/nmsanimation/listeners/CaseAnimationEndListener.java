package ru.paimonsoft.framecaserecode.animations.nmsanimation.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.paimonsoft.framecaserecode.FrameCasesRecode;
import ru.paimonsoft.framecaserecode.animations.nmsanimation.events.CaseAnimationEndEvent;
import ru.paimonsoft.framecaserecode.api.Holograms;


public class CaseAnimationEndListener implements Listener {

    @EventHandler
    private void onCaseAnimationEnd(CaseAnimationEndEvent event) {
        FrameCasesRecode.isOpen = false;
        FrameCasesRecode.items.clear();
        Holograms.hologram(Holograms.locationCase());
    }
}
