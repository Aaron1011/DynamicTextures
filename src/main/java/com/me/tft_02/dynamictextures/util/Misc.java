package com.me.tft_02.dynamictextures.util;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import com.google.common.reflect.TypeToken;
import com.me.tft_02.dynamictextures.DynamicTextures;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.resourcepack.ResourcePack;
import org.spongepowered.api.resourcepack.ResourcePacks;
import org.spongepowered.api.world.Location;

public class Misc {

    public static void loadResourcePack(Player player) {
        if (!player.hasPermission("dynamictextures.resourcepack")) {
            return;
        }

        String url;

        Location location = player.getLocation();
        String world = player.getWorld().getName().toLowerCase();
        url = DynamicTextures.p.getConfig().getString("Worlds." + world);

        List<String> texturePermissions = null;
        try {
            texturePermissions = DynamicTextures.p.getConfig().getNode("Permissions").getList(TypeToken.of(String.class));
        } catch (ObjectMappingException e) {
            throw new RuntimeException(e);
        }

        for (String name : texturePermissions) {
            if (player.hasPermission("dynamictextures." + name)) {
                String permission_url = DynamicTextures.p.getConfig().getString("Permissions." + name);

                if (isValidUrl(permission_url)) {
                    url = permission_url;
                }
            }
        }

        if (DynamicTextures.p.getWorldGuardEnabled() && RegionUtils.isTexturedRegion(location)) {
            String region = RegionUtils.getRegion(location);

            if (!RegionUtils.getPreviousRegion(player).equals(region)) {
                RegionUtils.setPreviousRegion(player, region);
                url = RegionUtils.getRegionTexturePackUrl(region);
            }
        }

        if (isValidUrl(url)) {
            try {
                player.sendResourcePack(ResourcePacks.fromUri(new URI(url)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isValidUrl(String url) {
        return ((url != null) && (url.contains("http://") || url.contains("https://")) && url.contains(".zip") && !url.contains("url_to_the_resource_pack_here"));
    }
}
