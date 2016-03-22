package com.me.tft_02.dynamictextures.listeners;

import com.me.tft_02.dynamictextures.DynamicTextures;
import com.me.tft_02.dynamictextures.runnables.LoadResourceTask;
import com.me.tft_02.dynamictextures.util.Misc;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Tristate;

public class PlayerListener {

    @Listener(order = Order.POST)
    @IsCancelled(Tristate.UNDEFINED)
    public void onPlayerJoin(final ClientConnectionEvent.Join event) {
        Task.builder().delayTicks(20).execute(new LoadResourceTask(event.getTargetEntity())).submit(DynamicTextures.p);
    }

    @Listener(order = Order.POST)
    @IsCancelled(Tristate.UNDEFINED)
    public void onPlayerChangedWorldEvent(DisplaceEntityEvent.Teleport.TargetPlayer event) {
        Misc.loadResourcePack(event.getTargetEntity());
    }
}
