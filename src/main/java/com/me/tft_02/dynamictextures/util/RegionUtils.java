package com.me.tft_02.dynamictextures.util;

import com.google.common.reflect.TypeToken;
import com.me.tft_02.dynamictextures.DynamicTextures;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RegionUtils {
    private final static HashMap<String, String> regionData = new HashMap<String, String>();

    public static boolean isTexturedRegion(Location location) {

        List<String> worldGuardRegions = null;
        try {
            worldGuardRegions = DynamicTextures.p.getConfig().getNode("WorldGuard_Regions").getList(TypeToken.of(String.class));
        } catch (ObjectMappingException e) {
            throw new RuntimeException(e);
        }

        for (String name : worldGuardRegions) {
            if (getRegion(location).equalsIgnoreCase("[" + name + "]")) {
                return true;
            }
        }

        return false;
    }

    public static String getRegionTexturePackUrl(String region) {
        region = region.substring(1, region.length() - 1);
        String url = DynamicTextures.p.getConfig().getString("WorldGuard_Regions." + region);

        if (Misc.isValidUrl(url)) {
            return url;
        }

        return null;
    }

    public static String getRegion(Location location) {
        /*RegionManager regionManager = DynamicTextures.p.getWorldGuard().getRegionManager(location.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(location);
        LinkedList<String> parentNames = new LinkedList<String>();
        LinkedList<String> regions = new LinkedList<String>();

        for (ProtectedRegion region : set) {
            String id = region.getId();
            regions.add(id);
            ProtectedRegion parent = region.getParent();
            while (parent != null) {
                parentNames.add(parent.getId());
                parent = parent.getParent();
            }
        }

        for (String name : parentNames) {
            regions.remove(name);
        }

        return regions.toString();*/
        return null;
    }

    public static void setPreviousRegion(Player player, String region) {
        regionData.put(player.getName(), region);
    }

    public static String getPreviousRegion(Player player) {
        String playerName = player.getName();
        String region = "null";

        if (regionData.containsKey(playerName)) {
            region = regionData.get(playerName);
        }

        return region;
    }
}
