package net.ludocrypt.dynmus.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.dynmus.config.MusicConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
@Mixin(value = SimpleSound.class, priority = 80)
public class SimpleSoundMixin {

	@Inject(method = "music(Lnet/minecraft/util/SoundEvent;)Lnet/minecraft/client/audio/SimpleSound;", at = @At("RETURN"), cancellable = true)
	private static void DYNMUS_changePitch(SoundEvent music, CallbackInfoReturnable<SimpleSound> ci) {
		if (MusicConfig.dynamicPitch.get()) {
			Random random = new Random();
			Minecraft client = Minecraft.getInstance();
			if (client.world != null) {
				client.world.getDayTime();
				long absTime = Math.abs(client.world.getDayTime() - MusicConfig.dynamicPitchAnchor.get());
				double delta = absTime * (0.0001832172957);
				double chance = MathHelper.lerp(delta, 1, 0);
				if (random.nextDouble() < chance) {
					double minPitch = MathHelper.lerp(delta, -12, 0);
					double maxPitch = MathHelper.lerp(random.nextDouble(), minPitch / 3, random.nextDouble() * -1);
					double note = MathHelper.lerp(random.nextDouble(), MusicConfig.dynamicPitchFaster.get() ? -minPitch : minPitch, MusicConfig.dynamicPitchFaster.get() ? -maxPitch : maxPitch);
					double newPitch = Math.pow(2.0D, note / 12.0D);
					ci.setReturnValue(new SimpleSound(music.getName(), SoundCategory.MUSIC, 1.0F, (float) newPitch, false, 0, ISound.AttenuationType.NONE, 0.0D, 0.0D, 0.0D, true));
				}
			}
		}
	}

}
