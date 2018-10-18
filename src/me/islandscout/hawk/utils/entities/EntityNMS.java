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

package me.islandscout.hawk.utils.entities;

import me.islandscout.hawk.Hawk;
import me.islandscout.hawk.utils.AABB;
import org.bukkit.entity.Entity;

public abstract class EntityNMS {

    AABB collisionBox;
    protected int id;
    //hitbox appears to grow 0.1249 per side. verify?

    EntityNMS() {
    }

    public static EntityNMS getEntityNMS(Entity entity) {
        if (Hawk.getServerVersion() == 8)
            return new EntityNMS8(entity);
        else
            return new EntityNMS7(entity);
    }


    public AABB getCollisionBox() {
        return collisionBox;
    }
}
