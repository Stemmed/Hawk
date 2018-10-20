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

package me.islandscout.hawk.check.interaction;

import me.islandscout.hawk.HawkPlayer;
import me.islandscout.hawk.check.BlockPlacementCheck;
import me.islandscout.hawk.event.BlockPlaceEvent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlockInteractSpeed extends BlockPlacementCheck {

    private final Map<UUID, Long> lastPlaceTick;

    public BlockInteractSpeed() {
        super("blockplacespeed", "%player% failed block place speed. VL: %vl%");
        lastPlaceTick = new HashMap<>();
    }

    @Override
    protected void check(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        HawkPlayer pp = e.getHawkPlayer();
        if (pp.getCurrentTick() == lastPlaceTick.getOrDefault(p.getUniqueId(), 0L))
            punishAndTryCancelAndBlockDestroy(pp, e);
        else
            reward(pp);

        lastPlaceTick.put(p.getUniqueId(), pp.getCurrentTick());
    }

    @Override
    public void removeData(Player p) {
        lastPlaceTick.remove(p.getUniqueId());
    }
}