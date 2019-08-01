package ladysnake.gaspunk.common.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GasUtil {

    private static final LoadingCache<CacheKey, Integer> distanceCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .build(CacheLoader.from(GasUtil::computeDistance));

    /**
     * Gets the minimum distance in air blocks between start and goal <br>
     * Information may be outdated by up to a second but gases don't update instantly irl either
     *
     * @param world                  the world in which the gas is
     * @param start                  the emission point of the gas
     * @param goal                   the location to check
     * @param maxPropagationDistance the maximum amount of blocks the gas can travel.
     *                               For an optimal use of the cache, this number should not vary between method calls with the same 3 other parameters
     * @return the minimum number of air blocks between start and goal, or -1 if no path is found
     */
    public static int getPropagationDistance(World world, BlockPos start, BlockPos goal, int maxPropagationDistance) {
        try {
            return distanceCache.get(new CacheKey(world, start, goal, maxPropagationDistance));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * A* implementation to know how dense the gas is at a certain location
     *
     * @param dataIn the relevant information to compute this distance
     * @return the minimum number of air blocks between start and goal, or -1 if no path is found
     */
    public static int computeDistance(@Nonnull CacheKey dataIn) {
        Map<BlockPos, Integer> cost = new HashMap<>();
        cost.put(dataIn.start, 0);
        Map<BlockPos, Integer> heuristic = new HashMap<>();
        heuristic.put(dataIn.start, (int) dataIn.start.getSquaredDistance(dataIn.goal));
        Set<BlockPos> closedSet = new HashSet<>();
        Queue<BlockPos> openSet = new PriorityQueue<>(Comparator.comparing(heuristic::get));
        openSet.add(dataIn.start);

        while (!openSet.isEmpty()) {
            BlockPos current = openSet.remove();
            if (current.equals(dataIn.goal))
                return cost.get(current);

            closedSet.add(current);

            for (Direction facing : Direction.values()) {
                BlockPos neighbour = current.offset(facing);
                if (/*!dataIn.world.isAirBlock(neighbour) || */dataIn.start.getSquaredDistance(neighbour) > dataIn.maxDistanceSq) {
                    continue; // gases can only propagate through air and up to a maximum distance from emission
                }
                Material mat = dataIn.world.getBlockState(neighbour).getMaterial();
                if (mat.isSolid() || mat.isLiquid() || mat.blocksMovement())
                    continue;
                if (closedSet.contains(neighbour))
                    continue; // ignore the neighbour which has already been evaluated

                int attemptCost = cost.get(current) + 1;
                if (openSet.contains(neighbour) && attemptCost >= cost.getOrDefault(neighbour, Integer.MAX_VALUE))
                    continue; // there's already a shorter way to this position
                cost.put(neighbour, attemptCost);
                // square everything for better performance
                heuristic.put(neighbour, attemptCost * attemptCost + (int) neighbour.getSquaredDistance(dataIn.goal));
                openSet.add(neighbour);
            }
        }
        return -1;
    }

    public static class CacheKey {
        final World world;
        final BlockPos start;
        final BlockPos goal;
        private int maxDistanceSq;

        public CacheKey(World world, BlockPos start, BlockPos goal, int maxDistance) {
            this.world = world;
            this.start = start;
            this.goal = goal;
            this.maxDistanceSq = maxDistance * maxDistance;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey cacheKey = (CacheKey) o;

            if (maxDistanceSq != cacheKey.maxDistanceSq) return false;
            if (!Objects.equals(world, cacheKey.world)) return false;
            if (!Objects.equals(start, cacheKey.start)) return false;
            return Objects.equals(goal, cacheKey.goal);
        }

        @Override
        public int hashCode() {
            int result = world != null ? world.hashCode() : 0;
            result = 31 * result + (start != null ? start.hashCode() : 0);
            result = 31 * result + (goal != null ? goal.hashCode() : 0);
            result = 31 * result + maxDistanceSq;
            return result;
        }
    }
}
