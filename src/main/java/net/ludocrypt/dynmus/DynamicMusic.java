package net.ludocrypt.dynmus;

import net.ludocrypt.dynmus.config.DynamicMusicConfig;
import net.ludocrypt.dynmus.config.MusicConfig;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("dynmus")
public class DynamicMusic {

	public DynamicMusic() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DynamicMusicConfig.client_config);
		DynamicMusicConfig.loadConfig(DynamicMusicConfig.client_config, FMLPaths.CONFIGDIR.get().resolve("dynmus-client.toml").toString());
		MinecraftForge.EVENT_BUS.register(this);
		DynamicMusicSounds.init();
	}

	public static ResourceLocation id(String id) {
		return new ResourceLocation("dynmus", id);
	}

	public static boolean isInCave(World world, BlockPos pos) {
		int searchRange = MusicConfig.searchRange.get();

		if (searchRange >= 1 && !world.canSeeSky(pos)) {
			int darkBlocks = 0;
			int stoneBlocks = 0;
			int airBlocks = 0;

			for (int x = -searchRange; x < searchRange; x++) {
				for (int y = -searchRange; y < searchRange; y++) {
					for (int z = -searchRange; z < searchRange; z++) {
						BlockPos offsetPos = pos.add(x, y, z);
						if (world.isAirBlock(offsetPos)) {
							airBlocks++;
							if (world.getLight(offsetPos) <= MusicConfig.darknessCap.get()) {
								darkBlocks++;
							}
						}
						if (world.getBlockState(offsetPos).getMaterial() == Material.LAVA) {
							darkBlocks++;
						}
						if (world.getBlockState(offsetPos).getMaterial() == Material.ROCK) {
							stoneBlocks++;
						}
					}
				}
			}

			double blockCount = Math.pow(searchRange * 2, 3);

			double stonePercentage = ((double) stoneBlocks) / ((double) blockCount);
			double darkPercentage = ((double) darkBlocks) / ((double) airBlocks);

			if (darkPercentage >= MusicConfig.darknessPercent.get()) {
				if (stonePercentage >= MusicConfig.stonePercent.get()) {
					return true;
				}
			}
		}
		return false;
	}

	public static double getAverageDarkness(World world, BlockPos pos) {
		int searchRange = MusicConfig.searchRange.get();

		if (searchRange >= 1) {
			int airBlocks = 0;
			int lightTogether = 0;

			for (int x = -searchRange; x < searchRange; x++) {
				for (int y = -searchRange; y < searchRange; y++) {
					for (int z = -searchRange; z < searchRange; z++) {
						BlockPos offsetPos = pos.add(x, y, z);
						if (world.isAirBlock(offsetPos)) {
							airBlocks++;
							lightTogether += world.getLight(offsetPos);
						}
					}
				}
			}

			return (((double) lightTogether) / ((double) airBlocks));

		}
		return 15;
	}

	public static boolean isInPseudoMineshaft(World world, BlockPos pos) {
		int searchRange = MusicConfig.pseudoMineshaftSearchRange.get();

		if (searchRange >= 1) {

			int pseudoMineshaftBlocks = 0;
			int airBlocks = 0;

			for (int x = -searchRange; x < searchRange; x++) {
				for (int y = -searchRange; y < searchRange; y++) {
					for (int z = -searchRange; z < searchRange; z++) {
						BlockPos offsetPos = pos.add(x, y, z);

						if (world.getBlockState(offsetPos).getMaterial() == Material.WOOD || world.getBlockState(offsetPos).getBlock() == Blocks.RAIL || world.getBlockState(offsetPos).getMaterial() == Material.WEB) {
							pseudoMineshaftBlocks++;
						}

						if (world.isAirBlock(offsetPos)) {
							airBlocks++;
						}

					}
				}
			}

			double mineshaftPercentage = ((double) pseudoMineshaftBlocks) / ((double) airBlocks);

			if (mineshaftPercentage >= MusicConfig.pseudoMineshaftPercent.get()) {
				return true;
			}

		}

		return false;
	}

}
