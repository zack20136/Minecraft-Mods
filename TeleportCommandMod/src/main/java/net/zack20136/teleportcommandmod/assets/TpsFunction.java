package net.zack20136.teleportcommandmod.assets;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class TpsFunction {
    public static IFormattableTextComponent tpsPosList(String player, String name, TpsPosData posData){
        BlockPos pos = posData.getPos();
        String dim = posData.getDimension();
        String desc = !posData.getDescription().isEmpty() ? " \"" + posData.getDescription() + "\"" : "";

        IFormattableTextComponent msg = new StringTextComponent(TextFormatting.BLUE + "  " + name + ": " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " " + desc + " " + dim);
        Style style = msg.getStyle()
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, getTeleportCommand(player, pos, dim)))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(TextFormatting.BLUE + name + ": " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " " + desc)));
        msg = msg.setStyle(style);
        return msg;
    }

    public static String tpsSetSuccess(String name, BlockPos pos, String desc){
        String msg = "Set " + name + " at " + "(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
        if(desc != ""){
            msg += " \"" + desc + "\"";
        }
        return CommonFunction.getModTitle() + TextFormatting.GREEN + msg;
    }

    public static String tpsRemoveSuccess(String name){
        String msg = "Remove " + name;
        return CommonFunction.getModTitle() + TextFormatting.GREEN + msg;
    }

    public static String getTpsTeleportCommand(String player, TpsPosData posData){
        BlockPos pos = posData.getPos();
        String dim = posData.getDimension();
        return getTeleportCommand(player, pos, dim);
    }

    public static String tpsTeleportFail(String name){
        return CommonFunction.getModTitle() + TextFormatting.RED + "Teleport to " + name + " failed";
    }

    private static String getTeleportCommand(String player, BlockPos pos, String dim){
        return "/execute in " + dim + " run tp " + player + " " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }
}
