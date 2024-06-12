package com.zack20136.teleportcommandmod.clients;

import com.zack20136.teleportcommandmod.assets.TpsFunction;
import com.zack20136.teleportcommandmod.assets.TpsTextFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraftforge.client.event.ClientChatEvent;
import com.zack20136.teleportcommandmod.assets.TpsPosData;
import com.zack20136.teleportcommandmod.assets.TpsPosDataFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpsClient {
    private Map<String, TpsPosData> tpsPosData = new HashMap<>();
    private ClientPlayerEntity playerEntity = Minecraft.getInstance().player;
    private UUID playerUUID = playerEntity.getUUID();

    public TpsClient(ClientChatEvent event) {
        String message = event.getMessage();
        if (!message.startsWith("#")) {
            return;
        }
        event.setCanceled(true);

        tpsPosData = TpsPosDataFunction.loadCoordinates(playerUUID);

        if (message.equals("#list")) {
            playerEntity.sendMessage(ITextComponent.nullToEmpty(TpsTextFunction.getModTitle()), playerUUID);
            for (Map.Entry<String, TpsPosData> entry : tpsPosData.entrySet()) {
                IFormattableTextComponent msg = TpsFunction.tpsPosList(playerEntity, entry.getKey(), entry.getValue());
                playerEntity.sendMessage(msg, playerUUID);
            }
        } else if (message.startsWith("#set")) {
            String[] split = message.split(" ", 4);
            if (split.length >= 2) {
                String name = split[1];
                switch(name){
                    case "list":
                    case "set":
                    case "rm":
                        return;
                }

                BlockPos pos = playerEntity.blockPosition();
                String dim = playerEntity.level.dimension().location().toString();
                String desc = split.length >= 3 ? split[2] : "";

                tpsPosData.put(name, new TpsPosData(pos, dim, desc));
                tpsPosData = TpsPosDataFunction.saveCoordinates(playerUUID, tpsPosData);
                playerEntity.sendMessage(ITextComponent.nullToEmpty(TpsTextFunction.tpsSetSuccess(name, pos, desc)), playerUUID);
            }
        } else if (message.startsWith("#rm")) {
            String[] split = message.split(" ");
            if (split.length == 2) {
                String name = split[1];
                if (tpsPosData.containsKey(name)) {
                    tpsPosData.remove(name);
                    tpsPosData = TpsPosDataFunction.saveCoordinates(playerUUID, tpsPosData);
                    playerEntity.sendMessage(ITextComponent.nullToEmpty(TpsTextFunction.tpsRemoveSuccess(name)), playerUUID);
                }
            }
        } else if (message.startsWith("#")) {
            String name = message.substring(1);
            if (tpsPosData.containsKey(name)) {
                String command = TpsFunction.getTeleportCommand(playerEntity ,tpsPosData.get(name), true);
                playerEntity.chat(command);
            } else {
                playerEntity.sendMessage(ITextComponent.nullToEmpty(TpsTextFunction.tpsTeleportFail(name)), playerUUID);
            }
        }
    }
}
