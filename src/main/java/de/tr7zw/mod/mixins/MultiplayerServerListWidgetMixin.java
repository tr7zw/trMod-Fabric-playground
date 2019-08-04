package de.tr7zw.mod.mixins;

import java.util.concurrent.ThreadPoolExecutor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.world.level.storage.LevelStorageException;

@Mixin(MultiplayerServerListWidget.class)
public class MultiplayerServerListWidgetMixin {

	@Shadow
	private static ThreadPoolExecutor field_19105;

	@Inject(at = @At("RETURN"), method = "<init>")
	public void constructor(CallbackInfo info) {
		System.out.println("Increasing Thread amount!");
		field_19105.setCorePoolSize(20);
	}

}
