package com.toasterfritzi.nonether;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PortalRuleCommand implements CommandExecutor, TabCompleter {

    private final NoNether plugin;
    private final List<String> validRules = Arrays.asList("allow-nether", "allow-end", "show-portal-disabled-message");

    public PortalRuleCommand(NoNether plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("nonether.portalrule")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /portalrule <rule> [value]");
            sender.sendMessage(ChatColor.GRAY + "Valid rules: " + String.join(", ", validRules));
            return true;
        }

        String rule = args[0].toLowerCase();
        if (!validRules.contains(rule)) {
            sender.sendMessage(ChatColor.RED + "Unknown rule: " + rule);
            return true;
        }

        if (args.length == 1) {
            boolean currentValue = plugin.getConfig().getBoolean(rule);
            sender.sendMessage(ChatColor.GOLD + "Rule " + ChatColor.WHITE + rule + ChatColor.GOLD + " is currently set to: " + ChatColor.WHITE + currentValue);
            return true;
        }

        if (args.length == 2) {
            String valueStr = args[1].toLowerCase();
            if (!valueStr.equals("true") && !valueStr.equals("false")) {
                sender.sendMessage(ChatColor.RED + "Value must be true or false.");
                return true;
            }

            boolean newValue = Boolean.parseBoolean(valueStr);
            plugin.getConfig().set(rule, newValue);
            plugin.saveConfig();
            
            sender.sendMessage(ChatColor.GREEN + "Rule " + ChatColor.WHITE + rule + ChatColor.GREEN + " has been updated to: " + ChatColor.WHITE + newValue);
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Usage: /portalrule <rule> [value]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (!sender.hasPermission("nonether.portalrule")) {
            return completions;
        }

        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            completions.addAll(validRules.stream()
                    .filter(r -> r.startsWith(partial))
                    .collect(Collectors.toList()));
        } else if (args.length == 2 && validRules.contains(args[0].toLowerCase())) {
            String partial = args[1].toLowerCase();
            if ("true".startsWith(partial)) completions.add("true");
            if ("false".startsWith(partial)) completions.add("false");
        }

        return completions;
    }
}
