package io.github.skylarkblue1.events;

import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;

import io.github.skylarkblue1.commands.APConnect;

public class APChatListener {

    private final APConnect connectCommand;

    public APChatListener(APConnect connectCommand) {
        this.connectCommand = connectCommand;
    }

    public void onPlayerChat(PlayerChatEvent event) {
        if (connectCommand.apClient == null || !connectCommand.apClient.isConnected()) return;

        // Send the Hytale player's chat to the archipelago server
        String message = event.getContent();
        connectCommand.apClient.sendChat(message);
    }
}