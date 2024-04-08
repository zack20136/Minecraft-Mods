package net.zack20136.chatmsgtoolmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.zack20136.chatmsgtoolmod.assets.CommonFunction;
import net.zack20136.chatmsgtoolmod.assets.TpsFunction;
import net.zack20136.chatmsgtoolmod.assets.TpsPosData;
import net.zack20136.chatmsgtoolmod.assets.TpsPosDataFunction;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TpsCommand {
    private Map<String, TpsPosData> tpsPosData = new HashMap<>();

    public TpsCommand(CommandDispatcher<CommandSource> dispatcher) {
        File folder = new File(TpsPosDataFunction.getCoordinatesFolder());
        if (!folder.exists()) {
            folder.mkdirs();
        }

        LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("tps");

        // list
        literalargumentbuilder.then(Commands.literal("list").executes((command) -> {
            return showList(command.getSource());
        }));

        // set
        literalargumentbuilder.then(Commands.literal("set").then(Commands.argument("name", StringArgumentType.string()).executes((command) -> {
            return setPos(command.getSource(), StringArgumentType.getString(command, "name"), "");
        }).then(Commands.argument("desc", StringArgumentType.greedyString()).executes((command) -> {
            return setPos(command.getSource(), StringArgumentType.getString(command, "name"), StringArgumentType.getString(command, "desc"));
        }))));

        // remove
        literalargumentbuilder.then(Commands.literal("rm").then(Commands.argument("name", StringArgumentType.string())
            .suggests((context, builder) -> {
                tpsPosData = TpsPosDataFunction.loadCoordinates(context.getSource());
                for (String name : tpsPosData.keySet()) {
                    if (name.startsWith(builder.getRemaining().toLowerCase())) {
                        builder.suggest(name);
                    }
                }
                return builder.buildFuture();
            })
            .executes((command) -> {
                return removePos(command.getSource(), StringArgumentType.getString(command, "name"));
            })));

        // teleport
        literalargumentbuilder.then(Commands.argument("name", StringArgumentType.string())
            .suggests((context, builder) -> {
                tpsPosData = TpsPosDataFunction.loadCoordinates(context.getSource());
                for (String name : tpsPosData.keySet()) {
                    if (name.startsWith(builder.getRemaining().toLowerCase())) {
                        builder.suggest(name);
                    }
                }
                return builder.buildFuture();
            })
            .executes((command) -> {
                return teleport(command.getSource(), StringArgumentType.getString(command, "name"));
            }));

        dispatcher.register(literalargumentbuilder);
    }

    private int showList(CommandSource source) throws CommandSyntaxException {
        String player = source.getPlayerOrException().getName().getString();
        tpsPosData = TpsPosDataFunction.loadCoordinates(source);
        source.sendSuccess(new StringTextComponent(CommonFunction.getModTitle()), false);
        for (Map.Entry<String, TpsPosData> entry : tpsPosData.entrySet()) {
            IFormattableTextComponent msg = TpsFunction.tpsPosList(player, entry.getKey(), entry.getValue());
            source.sendSuccess(msg, false);
        }
        return 1;
    }

    private int setPos(CommandSource source, String name, String desc) throws CommandSyntaxException {
        switch(name){
            case "list":
            case "set":
            case "rm":
                return 0;
        }

        tpsPosData = TpsPosDataFunction.loadCoordinates(source);
        ServerPlayerEntity player = source.getPlayerOrException();
        BlockPos pos = player.blockPosition();
        String dim = player.level.dimension().location().toString();

        tpsPosData.put(name, new TpsPosData(pos, dim, desc));
        tpsPosData = TpsPosDataFunction.saveCoordinates(source, tpsPosData);
        source.sendSuccess(new StringTextComponent(TpsFunction.tpsSetSuccess(name, pos, desc)), false);
        return 1;
    }

    private int removePos(CommandSource source, String name) throws CommandSyntaxException {
        tpsPosData = TpsPosDataFunction.loadCoordinates(source);
        if (tpsPosData.containsKey(name)) {
            tpsPosData.remove(name);
            tpsPosData = TpsPosDataFunction.saveCoordinates(source, tpsPosData);
            source.sendSuccess(new StringTextComponent(TpsFunction.tpsRemoveSuccess(name)), false);
            return 1;
        } else {
            return 0;
        }
    }

    private int teleport(CommandSource source, String name) throws CommandSyntaxException {
        tpsPosData = TpsPosDataFunction.loadCoordinates(source);
        if (tpsPosData.containsKey(name)) {
            String player = source.getPlayerOrException().getName().getString();
            MinecraftServer server = source.getServer();
            String command = TpsFunction.getTpsTeleportCommand(player, tpsPosData.get(name));
            server.getCommands().performCommand(server.createCommandSourceStack(), command);
            return 1;
        } else {
            source.sendFailure(new StringTextComponent(TpsFunction.tpsTeleportFail(name)));
            return 0;
        }
    }
}
