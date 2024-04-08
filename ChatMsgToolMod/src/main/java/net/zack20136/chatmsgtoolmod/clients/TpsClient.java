package net.zack20136.chatmsgtoolmod.clients;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.zack20136.chatmsgtoolmod.ChatMsgToolMod;
import net.zack20136.chatmsgtoolmod.assets.CommonFunction;
import net.zack20136.chatmsgtoolmod.assets.TpsFunction;
import net.zack20136.chatmsgtoolmod.assets.TpsPosData;
import net.zack20136.chatmsgtoolmod.assets.TpsPosDataFunction;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ChatMsgToolMod.MOD_ID)
public class TpsClient {
    private Map<String, TpsPosData> tpsPosData = new HashMap<>();
    private ClientPlayerEntity player = Minecraft.getInstance().player;
    private UUID playerUUID = player.getUUID();

    public TpsClient(ClientChatEvent event) throws CommandSyntaxException {
        String message = event.getMessage();
        if (!message.startsWith("#")) {
            return;
        }
        event.setCanceled(true);

        File folder = new File(TpsPosDataFunction.getCoordinatesFolder());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        tpsPosData = TpsPosDataFunction.loadCoordinates(null);

        if (message.equals("#list")) {
            player.sendMessage(ITextComponent.nullToEmpty(CommonFunction.getModTitle()), playerUUID);
            for (Map.Entry<String, TpsPosData> entry : tpsPosData.entrySet()) {
                IFormattableTextComponent msg = TpsFunction.tpsPosList(player.getName().getString(), entry.getKey(), entry.getValue());
                player.sendMessage(msg, playerUUID);
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

                BlockPos pos = player.blockPosition();
                String dim = player.level.dimension().location().toString();
                String desc = split.length >= 3 ? split[2] : "";

                tpsPosData.put(name, new TpsPosData(pos, dim, desc));
                tpsPosData = TpsPosDataFunction.saveCoordinates(null, tpsPosData);
                player.sendMessage(ITextComponent.nullToEmpty(TpsFunction.tpsSetSuccess(name, pos, desc)), playerUUID);
            }
        } else if (message.startsWith("#rm")) {
            String[] split = message.split(" ");
            if (split.length == 2) {
                String name = split[1];
                if (tpsPosData.containsKey(name)) {
                    tpsPosData.remove(name);
                    tpsPosData = TpsPosDataFunction.saveCoordinates(null, tpsPosData);
                    player.sendMessage(ITextComponent.nullToEmpty(TpsFunction.tpsRemoveSuccess(name)), playerUUID);
                }
            }
        } else if (message.startsWith("#")) {
            String name = message.substring(1);
            if (tpsPosData.containsKey(name)) {
                String command = TpsFunction.getTpsTeleportCommand(player.getName().getString() ,tpsPosData.get(name));
                player.chat(command);
            } else {
                player.sendMessage(ITextComponent.nullToEmpty(TpsFunction.tpsTeleportFail(name)), playerUUID);
            }
        }
    }
}
