package de.mctelemetry.compat.appliedenergistics2.blocks

import com.mojang.serialization.MapCodec
import de.mctelemetry.compat.appliedenergistics2.AppliedEnergistics2ModRequired
import de.mctelemetry.compat.appliedenergistics2.blocks.entities.AE2ObservationProviderBlockEntity
import de.mctelemetry.core.blocks.ObservationSourceContainerBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition

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

    override fun getRenderShape(arg: BlockState): RenderShape {
        return RenderShape.MODEL
    }


    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(FACING, context.horizontalDirection)
    }

    protected override fun rotate(state: BlockState, rotation: Rotation): BlockState {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)))
    }

    protected override fun mirror(state: BlockState, mirror: Mirror): BlockState {
        return rotate(state, mirror.getRotation(state.getValue(FACING)))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(FACING)
    }

    init {
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH))
    }
}
