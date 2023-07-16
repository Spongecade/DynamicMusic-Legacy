package net.ludocrypt.dynmus;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DynamicMusicSounds {

	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DynamicMusic.id("").getNamespace());
	public static final Map<ResourceLocation, SoundEvent> SOUND_EVENTS = new LinkedHashMap<>();

	public static final SoundEvent MUSIC_COLD = add("music.cold");
	public static final SoundEvent MUSIC_HOT = add("music.hot");
	public static final SoundEvent MUSIC_CAVE = add("music.cave");

	public static final SoundEvent MUSIC_NICE = add("music.nice");
	public static final SoundEvent MUSIC_DOWN = add("music.down");

	public static final SoundEvent MUSIC_COLD_CREATIVE = add("music.cold.creative");
	public static final SoundEvent MUSIC_HOT_CREATIVE = add("music.hot.creative");
	public static final SoundEvent MUSIC_CAVE_CREATIVE = add("music.cave.creative");

	public static final SoundEvent MUSIC_NICE_CREATIVE = add("music.nice.creative");
	public static final SoundEvent MUSIC_DOWN_CREATIVE = add("music.down.creative");

	public static final SoundEvent MUSIC_END_CREATIVE = add("music.end.creative");
	public static final SoundEvent MUSIC_END_BOSS = add("music.end.boss");
	
	private static SoundEvent add(String key) {
		ResourceLocation id = DynamicMusic.id(key);
		SoundEvent sve = new SoundEvent(id);
		SOUND_EVENTS.put(id, sve);
		return sve;
	}

	public static void init() {
		SOUND_EVENTS.forEach((id, sve) -> {
			SOUNDS.register(id.getPath(), () -> sve);
		});
		SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
