package io.github.thebusybiscuit.slimefun4.storage.data;

import com.google.common.annotations.Beta;

import io.github.thebusybiscuit.slimefun4.api.gps.Waypoint;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;

/**
 * The data which backs {@link io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile}
 *
 * <b>This API is still experimental, it may change without notice.</b>
 */
// TODO: Should we keep this in PlayerProfile?
@Beta
public class PlayerData {

    private final Set<Research> researches = new HashSet<>();
    private final Set<Waypoint> waypoints = new HashSet<>();

    public PlayerData(Set<Research> researches, Set<Waypoint> waypoints) {
        this.researches.addAll(researches);
    }

    public Set<Research> getResearches() {
        return researches;
    }

    public void addResearch(@Nonnull Research research) {
        Validate.notNull(research, "Cannot add a 'null' research!");
        researches.add(research);
    }

    public void removeResearch(@Nonnull Research research) {
        Validate.notNull(research, "Cannot remove a 'null' research!");
        researches.remove(research);
    }

    public Set<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void addWaypoint(@Nonnull Waypoint waypoint) {
        Validate.notNull(waypoint, "Cannot add a 'null' waypoint!");

        for (Waypoint wp : waypoints) {
            if (wp.getId().equals(waypoint.getId())) {
                throw new IllegalArgumentException("A Waypoint with that id already exists for this Player");
            }
        }

        // TODO: Figure out why we limit this to 21, it's a weird number        
        if (waypoints.size() >= 21) {
            return; // Also, not sure why this doesn't throw but the one above does...
        }

        waypoints.add(waypoint);
    }

    public void removeWaypoint(@Nonnull Waypoint waypoint) {
        Validate.notNull(waypoint, "Cannot remove a 'null' waypoint!");
        waypoints.remove(waypoint);
    }
}
