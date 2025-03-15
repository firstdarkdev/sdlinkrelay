package com.hypherionmc.sdlink;

import com.hypherionmc.sdlink.server.WebSocketServer;

import java.io.IOException;

public class SDLinkRelayServerStandalone {

    public static void main(String[] args) throws IOException {
        new WebSocketServer();
    }

}
