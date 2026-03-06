package de.mctelemetry.compat.appliedenergistics2.blocks.entities

import appeng.api.networking.GridFlags
import appeng.api.networking.GridHelper
import appeng.api.networking.IGridNode
import appeng.api.networking.IGridNodeListener
import appeng.api.networking.IInWorldGridNodeHost
import appeng.api.networking.IManagedGridNode
import appeng.api.stacks.AEItemKey
import de.mctelemetry.compat.appliedenergistics2.AppliedEnergistics2ModRequired
import de.mctelemetry.compat.appliedenergistics2.OTelCompatAppliedEnergistics2Content
import de.mctelemetry.core.blocks.entities.ObservationSourceContainerBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import java.util.EnumSet

@AppliedEnergistics2ModRequired
class AE2ObservationProviderBlockEntity(
    blockPos: BlockPos,
    blockState: BlockState
) : ObservationSourceContainerBlockEntity(
    OTelCompatAppliedEnergistics2Content.AE2_OBSERVATION_PROVIDER_BLOCK_ENTITY.get(),
    blockPos,
    blockState
), IInWorldGridNodeHost {
    @Suppress("UNCHECKED_CAST")
    override fun getType(): BlockEntityType<AE2ObservationProviderBlockEntity> =
        blockEntityType as BlockEntityType<AE2ObservationProviderBlockEntity>

    private val managedNode: IManagedGridNode = GridHelper.createManagedNode(this, GridNodeListener)
        .setFlags(GridFlags.REQUIRE_CHANNEL)
        .setInWorldNode(true)
        .setExposedOnSides(EnumSet.noneOf(Direction::class.java))
        .setVisualRepresentation(visualRepresentation)
        .setTagName("ae2proxy")

    val node: IGridNode?
        get() = managedNode.node

    val nodeIfActive: IGridNode?
        get() = node?.takeIf { it.isActive }

    override fun loadAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        super.loadAdditional(compoundTag, provider)
        managedNode.loadFromNBT(compoundTag)
    }

    override fun saveAdditional(compoundTag: CompoundTag, provider: HolderLookup.Provider) {
        super.saveAdditional(compoundTag, provider)
        managedNode.saveToNBT(compoundTag)
    }

    @Deprecated("Deprecated")
    override fun setBlockState(arg: BlockState) {
        @Suppress("DEPRECATION")
        super.setBlockState(arg)
        if (arg.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            val newDirection = arg.getValue(BlockStateProperties.HORIZONTAL_FACING)
            val existingBlockState = blockState
            if (!existingBlockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                managedNode.setExposedOnSides(EnumSet.of(newDirection))
            } else {
                val existingDirection = existingBlockState.getValue(BlockStateProperties.HORIZONTAL_FACING)
                if (existingDirection != newDirection) {
                    managedNode.setExposedOnSides(EnumSet.of(newDirection))
                }
            }
        }
    }


    override fun getGridNode(dir: Direction?): IGridNode? {
        if (dir != null && dir != blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            return null
        }
        return managedNode.node
    }

    private fun saveChanges() {
        val level = level
        if (level != null) {
            if (level.isClientSide) {
                setChanged()
            } else {
                level.blockEntityChanged(worldPosition)
                OTelCompatAppliedEnergistics2Content.addEndOfTickListener {
                    if (!this.isRemoved && this.hasLevel())
                        setChanged()
                }
            }
        }
    }

    override fun setRemoved() {
        super.setRemoved()
        managedNode.destroy()
    }

    override fun onChunkUnloaded() {
        super.onChunkUnloaded()
        managedNode.destroy()
    }

    override fun clearRemoved() {
        super.clearRemoved()
        GridHelper.onFirstTick(this) {
            val level = it.level ?: return@onFirstTick
            val state = it.blockState
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                it.managedNode.setExposedOnSides(EnumSet.of(it.blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)))
            }
            it.managedNode.create(level, it.blockPos)
        }
    }


    private object GridNodeListener : IGridNodeListener<AE2ObservationProviderBlockEntity> {
        override fun onSaveChanges(nodeOwner: AE2ObservationProviderBlockEntity, node: IGridNode?) {
            nodeOwner.saveChanges()
        }
    }

    companion object {
        private val visualRepresentation: AEItemKey =
            AEItemKey.of { OTelCompatAppliedEnergistics2Content.AE2_OBSERVATION_PROVIDER_ITEM.get() }
    }
}
