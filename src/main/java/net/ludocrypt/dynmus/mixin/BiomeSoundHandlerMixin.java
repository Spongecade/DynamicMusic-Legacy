package net.ludocrypt.dynmus.mixin;

import java.util.Optional;
import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ludocrypt.dynmus.DynamicMusic;
import net.minecraft.client.audio.BiomeSoundHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
@Mixin(BiomeSoundHandler.class)
public class BiomeSoundHandlerMixin {

	@Shadow
	private float darknessAmbienceChance;

	@Shadow
	@Final
	private ClientPlayerEntity player;

	@Shadow
	private Optional<MoodSoundAmbience> currentAmbientMoodSound;

	@Shadow
	@Final
	private Random random;

	@Inject(method = "tick", at = @At("HEAD"))
	private void DYNMUSIC_tick(CallbackInfo ci) {
		this.currentAmbientMoodSound.ifPresent((biomeMoodSound) -> {
			World world = this.player.world;
			if (DynamicMusic.isInCave(world, player.getPosition()) && DynamicMusic.isInPseudoMineshaft(world, player.getPosition())) {
				this.darknessAmbienceChance += (float) ((15 - DynamicMusic.getAverageDarkness(world, player.getPosition())) / (float) biomeMoodSound.getTickDelay());
			}
		});
	}
}
