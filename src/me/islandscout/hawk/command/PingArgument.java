/*
 * This file is part of Hawk Anticheat.
 *
 * Hawk Anticheat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hawk Anticheat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hawk Anticheat.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.islandscout.hawk.command;

import me.islandscout.hawk.HawkPlayer;
import me.islandscout.hawk.utils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingArgument extends Argument {

    PingArgument() {
        super("ping", "[player]", "Displays ping of target player.");
    }

    @Override
    public boolean process(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 1) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Unknown player \"" + args[1] + "\"");
                return true;
            }
            HawkPlayer pp = hawk.getHawkPlayer(target);
            sender.sendMessage(ChatColor.GOLD + target.getName() + "'s ping: " + ServerUtils.getPing(target) + "ms");
            sender.sendMessage(ChatColor.GOLD + target.getName() + "'s jitter: " + pp.getPingJitter() + "ms");
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(HawkCommand.PLAYER_ONLY);
                return true;
            }
            HawkPlayer pp = hawk.getHawkPlayer((Player) sender);
            sender.sendMessage(ChatColor.GOLD + "Your ping: " + ServerUtils.getPing((Player) sender) + "ms");
            sender.sendMessage(ChatColor.GOLD + "Your jitter: " + pp.getPingJitter() + "ms");
        }
        return true;
    }
}
