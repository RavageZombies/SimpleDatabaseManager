package fr.nathanael2611.customplayerdata.command;

import fr.nathanael2611.customplayerdata.core.Database;
import fr.nathanael2611.customplayerdata.core.Databases;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.List;

public class CommandCustomPlayerdData extends CommandBase {
    @Override
    public String getName() {
        return "customplayerdata";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length > 2){
            String playerStr = args[0];
            String actionType = args[1];
            EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(playerStr);
            if(player == null){
                sender.sendMessage(new TextComponentString("§cCannot resolve player \"" + playerStr + "\" ! Please specify a valid player."));
                return;
            }
            Database playerData = Databases.getPlayerData(player);


            if(actionType.startsWith("get")){
                Object data = null;
                if(actionType.equalsIgnoreCase("getString")){
                    data = playerData.getString(args[2]);
                }
                if(actionType.equalsIgnoreCase("getInteger")){
                    data = playerData.getInteger(args[2]);
                }
                sender.sendMessage(new TextComponentString(args[1].toUpperCase() + ":" + args[2] + " = " + data));
            }else if(actionType.startsWith("set")){
                if(args.length != 4){
                    sender.sendMessage(new TextComponentString("§cCorrect usage: " + args[0] + " " + args[1] + " " + args[2] + "<value>"));
                    return;
                }
                if(actionType.equalsIgnoreCase("setString")){
                    playerData.setString(args[2], args[3]);
                }
                sender.sendMessage(new TextComponentString("The player's " + args[0].substring(3) + "was set to " + args[3]));
            }
        }

    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 1)
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        if(args.length == 2)
            return getListOfStringsMatchingLastWord(
                    args,
                    "getString", "getInteger", "getDouble", "getFloat", "getBoolean",
                    "setString", "setInteger", "setDouble", "setFloat", "setBoolean"
            );
        if(args.length == 3){
            EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(args[0]);
            if(player != null){
                System.out.println(args[1]);
                if(args[1].contains("String")){
                    System.out.println("pas foufou x)");
                    return getListOfStringsMatchingLastWord(
                            args,
                            Databases.getPlayerData(player).getAllStringEntry()
                    );
                }
                if(args[1].contains("Integer")){
                    return getListOfStringsMatchingLastWord(
                            args,
                            Databases.getPlayerData(player).getAllIntegerEntry()
                    );
                }
                if(args[1].contains("Double")){
                    return getListOfStringsMatchingLastWord(
                            args,
                            Databases.getPlayerData(player).getAllDoubleEntry()
                    );
                }
                if(args[1].contains("Float")){
                    return getListOfStringsMatchingLastWord(
                            args,
                            Databases.getPlayerData(player).getAllFloatEntry()
                    );
                }
                if(args[1].contains("Boolean")){
                    return getListOfStringsMatchingLastWord(
                            args,
                            Databases.getPlayerData(player).getAllBooleanEntry()
                    );
                }

            }

        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
