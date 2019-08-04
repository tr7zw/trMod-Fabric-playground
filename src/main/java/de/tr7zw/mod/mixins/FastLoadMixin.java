package de.tr7zw.mod.mixins;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;

import net.minecraft.text.TranslatableText;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelSummary;

@Mixin(LevelStorage.class)
public abstract class FastLoadMixin {

	@Shadow
	private Path savesDirectory;

	private List<LevelSummary> cache = null;
	private String[] lastFiles = new String[0];

	@Inject(at = @At("RETURN"), method = "<init>")
	public void constructor(CallbackInfo info) {
		new Thread(() -> {
			try {
				System.out.println("Precaching worlds");
				getLevelList();
			} catch (LevelStorageException e) {
				e.printStackTrace();
			}
		}).run();
	}

	@Inject(at = @At("HEAD"), method = "getLevelList", cancellable = true)
	public List<LevelSummary> getLevelList(CallbackInfoReturnable<List<LevelSummary>> info) throws LevelStorageException {
		if (!Files.isDirectory(this.savesDirectory, new LinkOption[0])) {
			throw new LevelStorageException((new TranslatableText("selectWorld.load_folder_access", new Object[0])).getString());
		} else {
			if(cache != null && Arrays.equals(this.savesDirectory.toFile().list(), lastFiles)) {
				info.setReturnValue(cache);
				return cache;
			}
			List<LevelSummary> list_1 = Lists.newArrayList();
			File[] files_1 = this.savesDirectory.toFile().listFiles();
			File[] var3 = files_1;
			int var4 = files_1.length;

			for(int var5 = 0; var5 < var4; ++var5) {
				File file_1 = var3[var5];
				if (file_1.isDirectory()) {
					String string_1 = file_1.getName();
					LevelProperties levelProperties_1 = this.getLevelProperties(string_1);
					if (levelProperties_1 != null && (levelProperties_1.getVersion() == 19132 || levelProperties_1.getVersion() == 19133)) {
						boolean boolean_1 = levelProperties_1.getVersion() != this.getCurrentVersion();
						String string_2 = levelProperties_1.getLevelName();
						if (StringUtils.isEmpty(string_2)) {
							string_2 = string_1;
						}

						long long_1 = 0L;
						list_1.add(new LevelSummary(levelProperties_1, string_1, string_2, 0L, boolean_1));
					}
				}
			}
			lastFiles = this.savesDirectory.toFile().list();
			cache = list_1;
			info.setReturnValue(list_1);
			return list_1;
		}
	}

	@Shadow
	abstract List<LevelSummary> getLevelList() throws LevelStorageException;
	@Shadow
	abstract int getCurrentVersion();
	@Shadow
	abstract LevelProperties getLevelProperties(String string_1);

}
