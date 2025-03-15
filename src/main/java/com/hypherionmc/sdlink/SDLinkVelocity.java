package com.hypherionmc.sdlink;

import com.hypherionmc.sdlink.server.WebSocketServer;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;

@Plugin(
        id = "sdlinkrelay",
        name = "SDLinkRelayServer",
        version = "1.0.0",
        description = "A Relay Server for Simple Discord Link",
        authors = {"HypherionSA"}
)
public class SDLinkVelocity {

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            new WebSocketServer();
        } catch (Exception e) {
            Constants.LOGGER.error("Failed to start SDLink Relay Server", e);
        }
    }
}
