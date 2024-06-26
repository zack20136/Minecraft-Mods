package com.zack20136.teleportcommandmod.assets;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.Map;
import java.util.UUID;

public class TpsFunction {
    // tps list
    public static int showList(CommandSource source) throws CommandSyntaxException {
        PlayerEntity playerEntity = source.getPlayerOrException();
        Map<String, TpsPosData> tpsPosData = TpsPosDataFunction.loadCoordinates(playerEntity.getUUID());
        source.sendSuccess(new StringTextComponent(TpsTextFunction.getModTitle()), false);
        for (Map.Entry<String, TpsPosData> entry : tpsPosData.entrySet()) {
            if(entry.getKey().equals("back")){
                continue;
            }
            IFormattableTextComponent msg = TpsFunction.tpsPosList(playerEntity, entry.getKey(), entry.getValue());
            source.sendSuccess(msg, false);
        }
        return 1;
    }
    public static IFormattableTextComponent tpsPosList(PlayerEntity playerEntity, String name, TpsPosData posData){
        BlockPos pos = posData.getPos();
        String dim = posData.getDimension();
        String desc = !posData.getDescription().isEmpty() ? "\"" + posData.getDescription() + "\"" : "";

        IFormattableTextComponent msg = new StringTextComponent(TextFormatting.AQUA + "  " + name + " >> " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " " + desc);
        Style style = msg.getStyle()
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, CommonFunction.getTeleportPosCommand(playerEntity, posData)))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(TextFormatting.BLUE + name + " >> " + desc + " " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " " + dim)));
        msg = msg.setStyle(style);
        return msg;
    }

    // tps set
    public static int setPos(CommandSource source, String name, String desc) throws CommandSyntaxException {
        switch(name){
            case "list":
            case "set":
            case "rm":
            case "back":
                source.sendFailure(new StringTextComponent(TpsTextFunction.tpsSetFail()));
                return 0;
        }

        PlayerEntity playerEntity = source.getPlayerOrException();
        UUID playerUUID =  playerEntity.getUUID();

        Map<String, TpsPosData> tpsPosData = TpsPosDataFunction.loadCoordinates(playerUUID);
        BlockPos pos = playerEntity.blockPosition();
        String dim = playerEntity.level.dimension().location().toString();

        tpsPosData.put(name, new TpsPosData(pos, dim, desc));
        tpsPosData = TpsPosDataFunction.saveCoordinates(playerUUID, tpsPosData);
        source.sendSuccess(new StringTextComponent(TpsTextFunction.tpsSetSuccess(name, pos, desc)), false);
        return 1;
    }

    // tps remove
    public static int removePos(CommandSource source, String name) throws CommandSyntaxException {
        UUID playerUUID =  source.getPlayerOrException().getUUID();
        Map<String, TpsPosData> tpsPosData = TpsPosDataFunction.loadCoordinates(playerUUID);
        if (tpsPosData.containsKey(name)) {
            tpsPosData.remove(name);
            tpsPosData = TpsPosDataFunction.saveCoordinates(playerUUID, tpsPosData);
            source.sendSuccess(new StringTextComponent(TpsTextFunction.tpsRemoveSuccess(name)), false);
            return 1;
        } else {
            source.sendFailure(new StringTextComponent(TpsTextFunction.tpsRemoveFail()));
            return 0;
        }
    }

    // tps teleport
    public static int teleport(CommandSource source, String name) throws CommandSyntaxException {
        PlayerEntity playerEntity = source.getPlayerOrException();
        Map<String, TpsPosData> tpsPosData = TpsPosDataFunction.loadCoordinates(playerEntity.getUUID());
        if (tpsPosData.containsKey(name)) {
            // teleport
            MinecraftServer server = source.getServer();
            String command = CommonFunction.getTeleportPosCommand(playerEntity, tpsPosData.get(name));
            server.getCommands().performCommand(server.createCommandSourceStack(), command);
            return 1;
        } else {
            source.sendFailure(new StringTextComponent(TpsTextFunction.tpsTeleportFail(name)));
            return 0;
        }
    }
}
