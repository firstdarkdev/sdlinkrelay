package com.hypherionmc.sdlink.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hypherionmc.sdlink.Constants;
import io.javalin.Javalin;
import io.javalin.json.JavalinGson;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class WebSocketServer {

    private final Map<String, Set<WsContext>> sessions = new ConcurrentHashMap<>();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public WebSocketServer() throws IOException {
        File configFile = new File("sdlinkrelay.json");

        if (!configFile.exists()) {
            FileUtils.write(configFile, GSON.toJson(new ServerConfig()), StandardCharsets.UTF_8);
        }

        ServerConfig serverConfig = GSON.fromJson(FileUtils.readFileToString(configFile, StandardCharsets.UTF_8), ServerConfig.class);

        Javalin server = Javalin.create(config -> {
            config.showJavalinBanner = false;
            config.jsonMapper(new JavalinGson());
        });

        server.ws("/", ws -> {
            ws.onConnect(this::onClientConnect);
            ws.onMessage(this::onClientMessage);
            ws.onClose(this::onClientDisconnect);
            ws.onError((ctx) -> Constants.LOGGER.error("WebSocket Error", ctx.error()));
        });

        Constants.LOGGER.info("Starting SDLink Relay Server on port {}", serverConfig.port);
        server.start(serverConfig.port);
    }

    private void onClientDisconnect(WsCloseContext ctx) {
        String token = ctx.queryParam("identifier");
        String serverName = URLDecoder.decode(ctx.queryParam("serverName") == null ? "" : ctx.queryParam("serverName"), StandardCharsets.UTF_8);

        if (token != null && !token.isEmpty() && sessions.containsKey(token)) {
            sessions.get(token).remove(ctx);
            Constants.LOGGER.info("{} has disconnected", serverName);
        }
    }

    private void onClientMessage(WsMessageContext ctx) {
        if (ctx.message().equalsIgnoreCase("ping")) return;

        String token = ctx.queryParam("identifier");

        if (token == null || token.isEmpty()) return;

        for (WsContext context : sessions.get(token)) {
            if (context.session.isOpen() && !context.equals(ctx)) {
                context.send(ctx.message());
            }
        }
    }

    private void onClientConnect(WsConnectContext ctx) {
        String token = ctx.queryParam("identifier");
        String serverName = URLDecoder.decode(ctx.queryParam("serverName") == null ? "" : ctx.queryParam("serverName"), StandardCharsets.UTF_8);

        if (token == null || token.isEmpty()) {
            ctx.send("Missing Token or Server Name");
            ctx.session.close();
            return;
        }

        sessions.putIfAbsent(token, ConcurrentHashMap.newKeySet());
        sessions.get(token).add(ctx);

        Constants.LOGGER.info("{} has connected", serverName);
    }

}
