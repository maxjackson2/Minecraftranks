package com.minecraft.ranks.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import com.minecraft.ranks.App;
import com.minecraft.ranks.utils.StringUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class MinecraftRanksCommand implements CommandExecutor {

    private App plugin;

    public MinecraftRanksCommand(App plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        String usage = ChatColor.translateAlternateColorCodes('&', 
        "&6" + StringUtils.createSeparator(16) 
        + '\n' + "Here is the list of the commands:" 
        + '\n' + "&a/minecraftranks reload - Reloads all configs"
        + '\n' + "/minecraftranks version - Displays the current version of MinecraftRanks"
        + '\n' + "/minecraftranks createrank <name> <prefix> - Creates a rank"
        + '\n' + "/minecraftranks setprefix <rank> <prefix> - Sets a rank prefix"
        + '\n' + "/minecraftranks setsuffix <rank|none> [suffix] - Sets a rank suffix"
        + '\n' + "/minecraftranks setdisplayname <rank|none> [displayname] - Sets a rank display name"
        + '\n' + "/minecraftranks addPermission <rank> <permission> - Adds a permission to a rank"
        + '\n' + "/minecraftranks deleteRank <rank> - Deletes a rank"
        + '\n' + "/minecraftranks removePermission <rank> <permission> - Removes the permission from the rank"
        + '\n' + "/minecraftranks list - List's all ranks"
        + '\n' + "&6" + StringUtils.createSeparator(16));

        if (args.length == 0) {
            sender.sendMessage(usage);
            return false;
        }

        String cmd = args[0];

        if (cmd.equals("reload")) {
            this.plugin.reloadRanks(sender);
            this.plugin.getRanksConfig().reloadConfig();
            this.plugin.reloadConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReloaded all configs."));
        }
        if (cmd.equals("version")) {
            sender.sendMessage("The current version of MinecraftRanks is " + this.plugin.getDescription().getVersion());
        }
        if (cmd.equals("createrank")) {
            if (args.length <= 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /minecraftranks createrank <rank> <prefix>"));
                return false;
            }

            String rankname = args[1];
            String prefix = args[2];

            if (this.plugin.getRanksConfig().getConfig().contains(rankname)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere is a rank already named &6" + rankname));
                return false;
            }


            ConfigurationSection section = this.plugin.getRanksConfig().getConfig().createSection(rankname);
            section.set("prefix", prefix);
            section.set("permissions", new ArrayList<String>());

            this.plugin.getRanksConfig().saveConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCreated rank named: &6" + rankname));
        }

        if (cmd.equals("setprefix")) {
            if (args.length <= 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /minecraftranks setprefix <rank> <prefix>"));
                return false;
            }
            this.plugin.getRanksConfig().reloadConfig();

            String rankname = args[1];
            String prefix = args[2];

            if (!this.plugin.getRanksConfig().getConfig().contains(rankname)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere is no rank\'s named after &6" + rankname));
                return false;
            }


            ConfigurationSection section = this.plugin.getRanksConfig().getConfig().getConfigurationSection(rankname);
            section.set("prefix", prefix);

            this.plugin.getRanksConfig().getConfig().set(rankname, section);

            this.plugin.getRanksConfig().saveConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aUpdated rank named: &6" + rankname));

        }

        if (cmd.equals("setsuffix")) {
            if (args.length <= 1) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /minecraftranks setsuffix <rank> [suffix]"));
                return false;
            }
            this.plugin.getRanksConfig().reloadConfig();

            String rankname = args[1];
            String suffix = "";
            boolean removeSuffix = false;
            if (args.length <= 2) {
                suffix = "";
                removeSuffix = true;
            } else {
                suffix = args[2];
                
            }

            if (!this.plugin.getRanksConfig().getConfig().contains(rankname)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere is no rank\'s named after &6" + rankname));
                return false;
            }

            ConfigurationSection section = this.plugin.getRanksConfig().getConfig().getConfigurationSection(rankname);
            if (removeSuffix) {
                section.set("suffix", null);
            } else {
                section.set("suffix", suffix);
            }

            this.plugin.getRanksConfig().getConfig().set(rankname, section);

            this.plugin.getRanksConfig().saveConfig();
        
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aUpdated rank named: &6" + rankname));
            
        }

        if (cmd.equals("setdisplayname")) {
            if (args.length <= 1) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /minecraftranks setdisplayname <rank> [displayname]"));
                return false;
            }
            this.plugin.getRanksConfig().reloadConfig();

            String rankname = args[1];
            String suffix = "";
            boolean removeSuffix = false;
            if (args.length <= 2) {
                suffix = "";
                removeSuffix = true;
            } else {
                suffix = args[2];
            }

            if (!this.plugin.getRanksConfig().getConfig().contains(rankname)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere is no rank\'s named after &6" + rankname));
                return false;
            }

            ConfigurationSection section = this.plugin.getRanksConfig().getConfig().getConfigurationSection(rankname);
            if (removeSuffix) {
                section.set("displayName", null);
            } else {
                section.set("displayName", suffix);
            }

            this.plugin.getRanksConfig().getConfig().set(rankname, section);

            this.plugin.getRanksConfig().saveConfig();
        
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aUpdated rank named: &6" + rankname));
            
        }

        if (cmd.equals("list")) {
            String s = "";

            for (String keys : this.plugin.getRanksConfig().getConfig().getKeys(false)) {
                if (keys.equals("defaultRank")) {} else {
                    s += "&6" + keys + "\n";
                }
            }

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }

        if (cmd.equals("addPermission")) {
            if (args.length <= 1) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo Rank was given."));
                return false;
            }

            String rankName = args[1];
            if (args.length <= 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo Permission was given."));
                return false;
            }
            String permission = args[2];

            this.plugin.getRanksConfig().reloadConfig();
            if (!this.plugin.getRanksConfig().getConfig().contains(rankName)) return false;
            ConfigurationSection rankSection = this.plugin.getRanksConfig().getConfig().getConfigurationSection(rankName);
            List<String> permissions = rankSection.getStringList("permissions");
            permissions.add(permission);
            rankSection.set("permissions", permissions);

            this.plugin.getRanksConfig().saveConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aUpdated rank named: &6" + rankName));

        }

        if (cmd.equals("removePermission")) {
            if (args.length <= 1) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo Rank was given."));
                return false;
            }

            String rankName = args[1];
            if (args.length <= 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo Permission was given."));
                return false;
            }
            String permission = args[2];

            this.plugin.getRanksConfig().reloadConfig();
            if (!this.plugin.getRanksConfig().getConfig().contains(rankName)) return false;
            ConfigurationSection rankSection = this.plugin.getRanksConfig().getConfig().getConfigurationSection(rankName);
            List<String> permissions = rankSection.getStringList("permissions");
            permissions.remove(permission);
            rankSection.set("permissions", permissions);

            this.plugin.getRanksConfig().saveConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aUpdated rank named: &6" + rankName));

        }

        if (cmd.equals("deleteRank")) {
            if (args.length <= 1) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo Rank was given."));
                return false;
            }

            String rankName = args[1];

            this.plugin.getRanksConfig().reloadConfig();
            if (!this.plugin.getRanksConfig().getConfig().contains(rankName)) return false;
            this.plugin.getRanksConfig().getConfig().set(rankName, null);

            this.plugin.getRanksConfig().saveConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aUpdated rank named: &6" + rankName));

        }



        return true;
    }
    
}