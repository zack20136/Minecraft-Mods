package com.zack20136.teleportcommandmod.assets;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.CommandEvent;

import java.util.Map;
import java.util.UUID;

public class CommonFunction {

    public static String getTeleportPosCommand(PlayerEntity playerEntity, TpsPosData posData){
        String player = playerEntity.getStringUUID();
        String dim = posData.getDimension();
        BlockPos pos = posData.getPos();
        return "/execute in " + dim + " run tp " + player + " " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }

    // back
    public static int back(CommandSource source) throws CommandSyntaxException {
        PlayerEntity playerEntity = source.getPlayerOrException();
        Map<String, TpsPosData> posData = TpsPosDataFunction.loadCoordinates(playerEntity.getUUID());
        if (posData.containsKey("back")) {
            // teleport
            MinecraftServer server = source.getServer();
            String command = CommonFunction.getTeleportPosCommand(playerEntity, posData.get("back"));
            server.getCommands().performCommand(server.createCommandSourceStack(), command);
            // remove
            posData.remove("back");
            TpsPosDataFunction.saveCoordinates(playerEntity.getUUID(), posData);
            return 1;
        } else {
            source.sendFailure(new StringTextComponent(TpsTextFunction.backFail()));
            return 0;
        }
    }

    private static Map<String, TpsPosData> setBackPos(PlayerEntity playerEntity) {
        UUID playerUUID = playerEntity.getUUID();
        Map<String, TpsPosData> posData = TpsPosDataFunction.loadCoordinates(playerUUID);

        BlockPos pos = playerEntity.blockPosition();
        String dim = playerEntity.level.dimension().location().toString();
        posData.put("back", new TpsPosData(pos, dim, ""));
        return TpsPosDataFunction.saveCoordinates(playerUUID, posData);
    }

    public static void checkSetBackByCommandEvent(CommandEvent event) throws CommandSyntaxException {
        String command = event.getParseResults().getReader().getString();

        // tp
        if (command.startsWith("/tp ")) {
            String[] parts = command.split(" ");

            try{
                PlayerEntity playerEntity = event.getParseResults().getContext().getSource().getPlayerOrException();

                if (playerEntity != null) {
                    CommonFunction.setBackPos(playerEntity);
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }

        // tps
        if (command.startsWith("/execute in ") && command.contains(" run tp ")) {
            String[] parts = command.split(" ");

            try{
                UUID playerUUID = UUID.fromString(parts[5]);

                MinecraftServer server = event.getParseResults().getContext().getSource().getServer();
                PlayerEntity playerEntity = server.getPlayerList().getPlayer(playerUUID);

                if (playerEntity != null) {
                    CommonFunction.setBackPos(playerEntity);
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e);
            }
        }
    }
}
