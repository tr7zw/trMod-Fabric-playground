package de.tr7zw.mod.mixins;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(Block.class)
public class BlockMixin {

	@Inject(at = @At("HEAD"), method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", cancellable = true)
	private static List<ItemStack> getDroppedStacks(BlockState blockState_1, ServerWorld serverWorld_1, BlockPos blockPos_1, BlockEntity blockEntity_1, Entity entity_1, ItemStack itemStack_1, CallbackInfoReturnable<List<ItemStack>> info) {
		if(itemStack_1.getItem() == Items.SPONGE) {
			List<ItemStack> drops = new ArrayList<>();
			drops.add(new ItemStack(Items.STICK));
			info.setReturnValue(drops);
			return drops;
		}
		return null;
	}

}
