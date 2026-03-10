package io.github.skylarkblue1.events;

import io.github.archipelagomw.events.ArchipelagoEventListener;
import io.github.archipelagomw.events.PrintJSONEvent;

import io.github.skylarkblue1.archipelagoclient.APClient;
import io.github.skylarkblue1.commands.APConnect;

public class APEventListener {

    private final APClient apClient;

    public APEventListener(APClient apClient) {
        this.apClient = apClient;
    }

    @ArchipelagoEventListener
    public void onPrintJSON(PrintJSONEvent event) {
        String message = event.apPrint.getPlainText();

        // Hopefully this'll stop the echo? Without this, any message from the Hytale player is printed a second time as it comes back through the archipelago stream. Idk why. This seems to stop it relatively cleanly but I don't know how to check only the start of the string.
        String returnString = (APConnect.slot + ": ");
        if (message.contains(returnString)) return;

        apClient.broadcastToSubscribers(message);
    }
}