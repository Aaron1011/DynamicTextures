package com.me.tft_02.dynamictextures.runnables;


import com.me.tft_02.dynamictextures.util.Misc;
import org.spongepowered.api.entity.living.player.Player;

public class LoadResourceTask implements Runnable {
    private Player player;

    public LoadResourceTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        Misc.loadResourcePack(player);
    }
}
