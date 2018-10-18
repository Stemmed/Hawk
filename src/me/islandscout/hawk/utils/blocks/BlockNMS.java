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

package me.islandscout.hawk.utils.blocks;

import me.islandscout.hawk.Hawk;
import me.islandscout.hawk.utils.AABB;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class BlockNMS {

    private final Block block;
    float strength;
    AABB hitbox;
    AABB[] collisionBoxes;
    boolean solid;
    float frictionFactor;

    BlockNMS(Block block) {
        this.block = block;
    }

    public abstract Object getNMS();

    public abstract void sendPacketToPlayer(Player p);

    public float getStrength() {
        return strength;
    }

    public Block getBukkitBlock() {
        return block;
    }

    public AABB getHitBox() {
        return hitbox;
    }

    public AABB[] getCollisionBoxes() {
        return collisionBoxes;
    }

    public boolean isColliding(AABB other) {
        for (AABB cBox : collisionBoxes) {
            if (cBox.isColliding(other))
                return true;
        }
        return false;
    }

    public static BlockNMS getBlockNMS(Block b) {
        if (Hawk.getServerVersion() == 8)
            return new BlockNMS8(b);
        else
            return new BlockNMS7(b);
    }

    public float getFrictionFactor() {
        return frictionFactor;
    }

    //Man, I hate having to do this. I don't know why Bukkit is confused over the definition of SOLID.
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isSolid() {
        return solid;
    }
}
