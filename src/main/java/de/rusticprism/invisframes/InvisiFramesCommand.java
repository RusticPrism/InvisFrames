package de.rusticprism.invisframes;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvisiFramesCommand implements CommandExecutor, TabCompleter {
    private final SurvivalInvisiFrames survivalInvisiframes;

    public InvisiFramesCommand(SurvivalInvisiFrames survivalInvisiframes) {
        this.survivalInvisiframes = survivalInvisiframes;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("get")) {
            giveItem(sender);
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("survivalinvisiframes.reload")) {
                sendNoPermissionMessage(sender);
                return true;
            }
            survivalInvisiframes.reload();
            sender.sendMessage(NamedTextColor.GREEN + "Reloaded!");
            return true;
        } else if (args[0].equalsIgnoreCase("force-recheck")) {
            if (!sender.hasPermission("survivalinvisiframes.forcerecheck")) {
                sendNoPermissionMessage(sender);
                return true;
            }
            survivalInvisiframes.forceRecheck();
            sender.sendMessage(NamedTextColor.GREEN + "Rechecked invisible item frames");
            return true;
        } else if (args[0].equalsIgnoreCase("setitem")) {
            if (!sender.hasPermission("survivalinvisiframes.setitem")) {
                sendNoPermissionMessage(sender);
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(NamedTextColor.RED + "Sorry, you must be a player to use this command!");
                return true;
            }
            ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
            survivalInvisiframes.setRecipeItem(item);
            sender.sendMessage(NamedTextColor.GREEN + "Recipe item updated!");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        List<String> options = new ArrayList<>();
        if (sender.hasPermission("survivalinvisiframes.get")) {
            options.add("get");
        }
        if (sender.hasPermission("survivalinvisiframes.reload")) {
            options.add("reload");
        }
        if (sender.hasPermission("survivalinvisiframes.forcerecheck")) {
            options.add("force-recheck");
        }
        if (sender.hasPermission("survivalinvisiframes.setitem")) {
            options.add("setitem");
        }
        List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], options, completions);
        Collections.sort(completions);
        return completions;
    }

    private void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(NamedTextColor.RED + "Sorry, you don't have permission to run this command");
    }

    private void giveItem(CommandSender sender) {
        if (!sender.hasPermission("survivalinvisiframes.get")) {
            sendNoPermissionMessage(sender);
            return;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(NamedTextColor.RED + "Sorry, you must be a player to use this command!");
            return;
        }

        player.getInventory().addItem(SurvivalInvisiFrames.generateInvisibleItemFrame());
        player.sendMessage(NamedTextColor.GREEN + "Added an invisible item frame to your inventory");
    }
}
