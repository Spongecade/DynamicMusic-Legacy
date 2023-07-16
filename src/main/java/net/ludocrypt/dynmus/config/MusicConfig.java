package net.ludocrypt.dynmus.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MusicConfig {

	public static ForgeConfigSpec.IntValue searchRange;
	public static ForgeConfigSpec.IntValue darknessCap;
	public static ForgeConfigSpec.DoubleValue darknessPercent;
	public static ForgeConfigSpec.DoubleValue stonePercent;
	public static ForgeConfigSpec.IntValue pseudoMineshaftSearchRange;
	public static ForgeConfigSpec.DoubleValue pseudoMineshaftPercent;
	public static ForgeConfigSpec.BooleanValue coldMusic;
	public static ForgeConfigSpec.BooleanValue hotMusic;
	public static ForgeConfigSpec.BooleanValue niceMusic;
	public static ForgeConfigSpec.BooleanValue downMusic;
	public static ForgeConfigSpec.BooleanValue dynamicPitch;
	public static ForgeConfigSpec.LongValue dynamicPitchAnchor;
	public static ForgeConfigSpec.BooleanValue dynamicPitchFaster;

	public static void init(ForgeConfigSpec.Builder client) {
		client.comment("Music Duration Reducer Config");
		searchRange = client.comment("Cave detection search range").defineInRange("music.searchRange", 5, Integer.MIN_VALUE, Integer.MAX_VALUE);
		darknessCap = client.comment("Cave detection darkness cap").defineInRange("music.darknessCap", 8, Integer.MIN_VALUE, Integer.MAX_VALUE);
		darknessPercent = client.comment("Cave detection darkness percent").defineInRange("music.darknessPercent", 0.3, Double.MIN_VALUE, Double.MAX_VALUE);
		stonePercent = client.comment("Cave detection stone percent").defineInRange("music.stonePercent", 0.15, Double.MIN_VALUE, Double.MAX_VALUE);
		pseudoMineshaftSearchRange = client.comment("Mineshaft Detection range").defineInRange("music.pseudoMineshaftSearchRange", 2, Integer.MIN_VALUE, Integer.MAX_VALUE);
		pseudoMineshaftPercent = client.comment("Mineshaft Detection percent").defineInRange("music.pseudoMineshaftPercent", 0.1, Double.MIN_VALUE, Double.MAX_VALUE);
		coldMusic = client.comment("Play cold music?").define("music.coldMusic", true);
		hotMusic = client.comment("Play hot music?").define("music.hotMusic", true);
		niceMusic = client.comment("Play daytime music?").define("music.niceMusic", true);
		downMusic = client.comment("Play nighttime music?").define("music.downMusic", true);
		dynamicPitch = client.comment("Dynamic pitch?").define("music.dynamicPitch", true);
		dynamicPitchAnchor = client.comment("Divide delays of length instead?").defineInRange("music.dynamicPitchAnchor", 18000L, Long.MIN_VALUE, Long.MAX_VALUE);
		dynamicPitchFaster = client.comment("Dynamic pitch faster?").define("music.dynamicPitchFaster", false);
	}
}
