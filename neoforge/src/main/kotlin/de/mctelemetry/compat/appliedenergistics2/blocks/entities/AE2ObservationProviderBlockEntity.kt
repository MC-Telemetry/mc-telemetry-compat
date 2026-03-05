package de.mctelemetry.compat.appliedenergistics2.blocks.entities

import appeng.api.networking.IGrid
import de.mctelemetry.compat.appliedenergistics2.AppliedEnergistics2ModRequired
import de.mctelemetry.compat.appliedenergistics2.OTelCompatAppliedEnergistics2Content
import de.mctelemetry.core.blocks.entities.ObservationSourceContainerBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

@AppliedEnergistics2ModRequired
class AE2ObservationProviderBlockEntity(
    blockPos: BlockPos,
    blockState: BlockState
) : ObservationSourceContainerBlockEntity(
    OTelCompatAppliedEnergistics2Content.AE2_OBSERVATION_PROVIDER_BLOCK_ENTITY.get(),
    blockPos,
    blockState
) {
    @Suppress("UNCHECKED_CAST")
    override fun getType(): BlockEntityType<AE2ObservationProviderBlockEntity> = blockEntityType as BlockEntityType<AE2ObservationProviderBlockEntity>

    val grid: IGrid? = null
}
