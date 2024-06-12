package com.zack20136.teleportcommandmod.assets;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class TpsTextFunction {
    // tps mod title
    public static String getModTitle(){
        return TextFormatting.GREEN + "[TPS]  ";
    }

    // tps set
    public static String tpsSetSuccess(String name, BlockPos pos, String desc){
        String msg = "Set " + name + " at " + "(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
        if(desc != ""){
            msg += " \"" + desc + "\"";
        }
        return TpsTextFunction.getModTitle() + TextFormatting.GREEN + msg;
    }
    public static String tpsSetFail(){
        return TpsTextFunction.getModTitle() + TextFormatting.RED + "The names 'list', 'set', 'rm', 'back' are reserved and cannot be used";
    }

    // tps remove
    public static String tpsRemoveSuccess(String name){
        String msg = "Remove " + name;
        return TpsTextFunction.getModTitle() + TextFormatting.GREEN + msg;
    }
    public static String tpsRemoveFail(){
        return TpsTextFunction.getModTitle() + TextFormatting.RED + "Remove failed.";
    }

    // tps teleport failed
    public static String tpsTeleportFail(String name){
        return TpsTextFunction.getModTitle() + TextFormatting.RED + "Teleport to " + name + " failed";
    }
    public static String backFail(){
        return TpsTextFunction.getModTitle() + TextFormatting.RED + "No position can go back";
    }
}
