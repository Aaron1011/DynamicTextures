package com.me.tft_02.dynamictextures.runnables;

import com.me.tft_02.dynamictextures.DynamicTextures;
import com.me.tft_02.dynamictextures.util.Misc;
import com.me.tft_02.dynamictextures.util.RegionUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.resourcepack.ResourcePack;
import org.spongepowered.api.resourcepack.ResourcePackFactory;
import org.spongepowered.api.resourcepack.ResourcePacks;
import org.spongepowered.api.world.Location;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

public class RegionTimerTask implements Runnable {

    @Override
    public void run() {
        checkRegion();
    }

    public void checkRegion() {
        /*for (Player player : Sponge.getServer().getOnlinePlayers()) {
            Location location = player.getLocation();

            if (!RegionUtils.isTexturedRegion(location)) {
                continue;
            }

            String region = RegionUtils.getRegion(location);

            if (RegionUtils.getPreviousRegion(player).equals(region)) {
                continue;
            }

            String url = RegionUtils.getRegionTexturePackUrl(region);

            if (!Misc.isValidUrl(url)) {
                DynamicTextures.p.logger.warning("Url for region: " + region + " resource pack is invalid: " + url);
                continue;
            }

            try {
                player.sendResourcePack(ResourcePacks.fromUri(new URI(url)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            RegionUtils.setPreviousRegion(player, region);
        }*/
    }
}
