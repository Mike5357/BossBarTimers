package thenodemc.bossbartimers.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import thenodemc.bossbartimers.BossBar;
import thenodemc.bossbartimers.BossBarTimers;

import java.util.Arrays;
import java.util.List;

public class BBTCommand extends CommandBase {

    @Override
    public String getName() {
        return "bbt";
    }

    public List<String> getAliases() {
        return Arrays.asList("bossbartimers","bbtimers","bosstimer");
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "\u00a7c/bbt <start|colors|list|remove|removecontaining>";
    }

    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        BossInfo.Color color;
        Integer duration;
        String title = "";

        if (args.length == 0) {
            sender.sendMessage(new TextComponentString(this.getUsage(sender)));
        } else {
            switch (args[0].toLowerCase()) {
                case "start": {
                    if (args.length<4) {
                        sender.sendMessage(new TextComponentString("\u00a7cNot enough arguments!\n\u00a7cUsage: /bbt start <color> <duration(in s)|-1> <title[$time]>\n\u00a7cEx.: /bbt start blue 10 &aThere is $time remaining!"));
                    } else {
                        //args 1 - color
                        switch (args[1].toLowerCase()) {  //return ends method execution there returning whatever you tell it to, while break breaks out of the switch case
                            case "blue": {
                                color = BossInfo.Color.BLUE;
                                break;
                            }
                            case "green": {
                                color = BossInfo.Color.GREEN;
                                break;
                            }
                            case "pink": {
                                color = BossInfo.Color.PINK;
                                break;
                            }
                            case "purple": {
                                color = BossInfo.Color.PURPLE;
                                break;
                            }
                            case "red": {
                                color = BossInfo.Color.RED;
                                break;
                            }
                            case "white": {
                                color = BossInfo.Color.WHITE;
                                break;
                            }
                            case "yellow": {
                                color = BossInfo.Color.YELLOW;
                                break;
                            }
                            default: {
                                sender.sendMessage(new TextComponentString("\u00a7cInvalid color. Use \u00a7e/bbt colors \u00a7cto see all valid boss bar colors."));
                                return;
                            }
                        }

                        //args 2 - duration in s
                        try { //try to set duration to args[2], if its not actually an int it should fail
                            duration = Integer.parseInt(args[2]);
                        } catch (NumberFormatException ex) {
                            sender.sendMessage(new TextComponentString("\u00a7cInvalid duration: Must be an integer."));
                            return;
                        }

                        //args 3 - title
                        for (int i=3;i< args.length;i++) { //this should loop through all arguments past 3 and append them as a single string
                            title = title.concat(args[i])+" ";
                        }
                        title = title.replace("&", "\u00a7");

                        //Create the boss bar
                        BossBarTimers.bossbars.add(new BossBar(color, duration, title));
                        for (EntityPlayerMP playerMP:server.getPlayerList().getPlayers()) {
                            for (BossBar bossbar : BossBarTimers.bossbars) {
                                bossbar.addPlayer(playerMP);
                            }
                        }

                    }
                    break;
                }
                case "colors": {
                    //show color names
                    sender.sendMessage(new TextComponentString("\u00a76Color options: \u00a7bblue\u00a77, \u00a7agreen\u00a77, \u00a7dpink\u00a77, \u00a75purple\u00a77, \u00a7cred\u00a77, \u00a7fwhite\u00a77, \u00a7eyellow"));
                    break;
                }
                case "list": {
                    //list active boss bar timers
                    if (BossBarTimers.bossbars.size()>0) {
                        sender.sendMessage(new TextComponentString("\u00a76Current active boss bar timers:"));
                        for (int i = 0; i < BossBarTimers.bossbars.size(); i++) {
                            sender.sendMessage(new TextComponentString("\u00a77ID: \u00a7f" + i + "  |  \u00a77Name: \u00a7f" + BossBarTimers.bossbars.get(i).getName().getFormattedText()));
                        }
                    } else {
                        sender.sendMessage(new TextComponentString("\u00a77There are no active boss bar timers."));
                    }
                    break;
                }
                case "kill":
                case "remove": {
                    //kill an active boss bar by ID
                    if (args.length>1) {
                        try { //if its not actually an int it should fail
                            if (BossBarTimers.bossbars.contains(BossBarTimers.bossbars.get(Integer.parseInt(args[1])))) {
                                for (EntityPlayerMP playerMP:server.getPlayerList().getPlayers()) {
                                    BossBarTimers.bossbars.get(Integer.parseInt(args[1])).removePlayer(playerMP);
                                }
                                BossBarTimers.bossbars.remove(Integer.parseInt(args[1]));
                            } else {
                                sender.sendMessage(new TextComponentString("\u00a7cInvalid ID. Use \u00a7e/bbt list \u00a7cto find a valid active timer ID."));
                            }
                        } catch (NumberFormatException ex) {
                            sender.sendMessage(new TextComponentString("\u00a7cInvalid ID. Use \u00a7e/bbt list \u00a7cto find a valid active timer ID."));
                            return;
                        }
                    } else {
                        sender.sendMessage(new TextComponentString("\u00a7cNot enough arguments! /bbt remove <id>\n\u00a7cUse \\u00a7e/bbt list \\u00a7cto find a valid active timer ID.\""));
                    }
                    break;
                }
                case "killcontaining":
                case "removecontaining": {
                    //kill boss bar with specific contents
                    if (args.length>1) {
                        if (BossBarTimers.bossbars.size()>0) {
                            sender.sendMessage(new TextComponentString("\u00a76Boss Bars Removed:"));
                            String concattitle = "";
                            for (int i = 1; i < args.length; i++) { //this should loop through all arguments past 1 and append them as a single string
                                concattitle = concattitle.concat(args[i]) + " ";
                            }
                            for (BossBar bar : BossBarTimers.bossbars) {
                                if (bar.title.contains(concattitle)) {
                                    sender.sendMessage(new TextComponentString("\u00a77Name: \u00a7f" + bar.getName().getFormattedText()));
                                    BossBarTimers.removeBar(bar.title, bar.duration, bar.remaining);
                                }
                            }
                        } else {
                            sender.sendMessage(new TextComponentString("\u00a77There are no active boss bar timers to remove."));
                        }
                    } else {
                        sender.sendMessage(new TextComponentString("\u00a77No arguments specified. What would you like to remove?"));
                    }
                    break;
                }
                default: {
                    sender.sendMessage(new TextComponentString(this.getUsage(sender)));
                    break;
                }
            }

        }
    }


}
