package net.ludocrypt.dynmus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.dynmus.DynamicMusic;
import net.ludocrypt.dynmus.DynamicMusicSounds;
import net.ludocrypt.dynmus.config.MusicConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.BackgroundMusicTracks;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow
	public ClientPlayerEntity player;
	@Shadow
	public ClientWorld world;

	@Inject(method = "getBackgroundMusicSelector", at = @At("RETURN"), cancellable = true)
	private void DYNMUSIC_getBackgroundMusicSelector(CallbackInfoReturnable<BackgroundMusicSelector> ci) {
		if (ci.getReturnValue() == BackgroundMusicTracks.WORLD_MUSIC || ci.getReturnValue() == BackgroundMusicTracks.CREATIVE_MODE_MUSIC) {
			if (this.world != null) {
				if (DynamicMusic.isInCave(world, player.getPosition())) {
					DYNMUS_setReturnType(ci, DynamicMusicSounds.MUSIC_CAVE);
				} else if ((world.getBiomeManager().getBiome(this.player.getPosition()).getTemperature() < 0.15F) || (world.isRaining()) && MusicConfig.coldMusic.get()) {
					DYNMUS_setReturnType(ci, DynamicMusicSounds.MUSIC_COLD);
				} else if ((world.getBiomeManager().getBiome(this.player.getPosition()).getTemperature() > 0.95F) && (!world.isRaining()) && MusicConfig.hotMusic.get()) {
					DYNMUS_setReturnType(ci, DynamicMusicSounds.MUSIC_HOT);
				} else if (world.getDayTime() <= 12500 && MusicConfig.niceMusic.get()) {
					DYNMUS_setReturnType(ci, DynamicMusicSounds.MUSIC_NICE);
				} else if (world.getDayTime() > 12500 && MusicConfig.downMusic.get()) {
					DYNMUS_setReturnType(ci, DynamicMusicSounds.MUSIC_DOWN);
				}
			}
		} else if (ci.getReturnValue() == BackgroundMusicTracks.DRAGON_FIGHT_MUSIC) {
			ci.setReturnValue(BackgroundMusicTracks.getDefaultBackgroundMusicSelector(DynamicMusicSounds.MUSIC_END_BOSS));
		} else if (ci.getReturnValue() == BackgroundMusicTracks.END_MUSIC) {
			if (this.player.abilities.isCreativeMode && this.player.abilities.allowFlying) {
				ci.setReturnValue(BackgroundMusicTracks.getDefaultBackgroundMusicSelector(DynamicMusicSounds.MUSIC_END_CREATIVE));
			}
		}
	}
	
	@Unique
	private void DYNMUS_setReturnType(CallbackInfoReturnable<BackgroundMusicSelector> ci, SoundEvent e) {
		ci.setReturnValue(BackgroundMusicTracks.getDefaultBackgroundMusicSelector(new SoundEvent(ci.getReturnValue() == BackgroundMusicTracks.CREATIVE_MODE_MUSIC ? e.getName() : new ResourceLocation(String.join("", e.getName().toString(), ".creative")))));
	}
}
