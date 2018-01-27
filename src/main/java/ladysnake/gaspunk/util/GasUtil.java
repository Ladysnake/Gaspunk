package ladysnake.gaspunk.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.entity.EntityGasCloud;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.init.ModGases;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GasUtil {

    private static LoadingCache<CacheKey, Integer> distanceCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .build(CacheLoader.from(GasUtil::computeDistance));

    /**
     * Gets the minimum number of air blocks between start and goal
     * Information may be outdated by up to a second but gases don't update instantly irl either
     * @param world the world in which the gas is
     * @param start the emission point of the gas
     * @param goal the location to check
     * @return the minimum number of air blocks between start and goal, or -1 if no path is found
     */
    public static int getPropagationDistance(World world, BlockPos start, BlockPos goal) {
        try {
            return distanceCache.get(new CacheKey(world, start, goal));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * A* implementation to know how dense the gas is at a certain location
     * @param dataIn the relevant information to compute this distance
     * @return the minimum number of air blocks between start and goal, or -1 if no path is found
     */
    public static int computeDistance(@Nonnull CacheKey dataIn) {
        Map<BlockPos, Integer> cost = new HashMap<>();
        cost.put(dataIn.start, 0);
        Map<BlockPos, Integer> heuristic = new HashMap<>();
        heuristic.put(dataIn.start, (int)dataIn.start.distanceSq(dataIn.goal));
        Set<BlockPos> closedSet = new HashSet<>();
        Queue<BlockPos> openSet = new PriorityQueue<>(Comparator.comparing(heuristic::get));
        openSet.add(dataIn.start);

        while (!openSet.isEmpty()) {
            BlockPos current = openSet.remove();
            if (current.equals(dataIn.goal))
                return cost.get(current);

            closedSet.add(current);

            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos neighbour = current.offset(facing);
                if (/*!dataIn.world.isAirBlock(neighbour) || */dataIn.start.distanceSq(neighbour) > EntityGasCloud.MAX_PROPAGATION_DISTANCE_SQ)
                    continue; // gases can only propagate through air and up to a maximum distance from emission
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
                heuristic.put(neighbour, attemptCost * attemptCost + (int) neighbour.distanceSq(dataIn.goal));
                openSet.add(neighbour);
            }
        }
        return -1;
    }

    public static class CacheKey {
        World world;
        BlockPos start;
        BlockPos goal;

        public CacheKey(World world, BlockPos start, BlockPos goal) {
            this.world = world;
            this.start = start;
            this.goal = goal;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return Objects.equals(world, cacheKey.world) &&
                    Objects.equals(start, cacheKey.start) &&
                    Objects.equals(goal, cacheKey.goal);
        }

        @Override
        public int hashCode() {
            return Objects.hash(world, start, goal);
        }
    }
}
