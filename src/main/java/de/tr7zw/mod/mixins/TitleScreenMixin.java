package de.tr7zw.mod.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.SystemUtil;
import net.minecraft.world.level.storage.LevelStorage;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

	@Shadow
	private boolean doBackgroundFade;
	@Shadow
	private long backgroundFadeStart;
	
	@Inject(at = @At("RETURN"), method = "render")
	public void render(int int_1, int int_2, float float_1, CallbackInfo info) {
		doBackgroundFade = false;
		if(MinecraftClient.getInstance().getOverlay() instanceof SplashScreen) {
			MinecraftClient.getInstance().setOverlay(null);
		}
	}

}
