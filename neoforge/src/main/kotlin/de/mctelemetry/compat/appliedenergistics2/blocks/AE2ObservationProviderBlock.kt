package de.mctelemetry.compat.appliedenergistics2.blocks

import com.mojang.serialization.MapCodec
import de.mctelemetry.compat.appliedenergistics2.AppliedEnergistics2ModRequired
import de.mctelemetry.compat.appliedenergistics2.blocks.entities.AE2ObservationProviderBlockEntity
import de.mctelemetry.core.blocks.ObservationSourceContainerBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState

@AppliedEnergistics2ModRequired
class AE2ObservationProviderBlock(properties: BlockBehaviour.Properties) : ObservationSourceContainerBlock(properties) {
    override fun codec(): MapCodec<AE2ObservationProviderBlock> {
        return CODEC
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): AE2ObservationProviderBlockEntity {
        return AE2ObservationProviderBlockEntity(
            blockPos, blockState,
        )
    }

    companion object {
        val CODEC: MapCodec<AE2ObservationProviderBlock> = simpleCodec(::AE2ObservationProviderBlock)
    }

}
