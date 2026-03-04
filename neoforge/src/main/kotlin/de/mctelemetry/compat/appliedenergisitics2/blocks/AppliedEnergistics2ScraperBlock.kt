package de.mctelemetry.compat.appliedenergisitics2.blocks

import com.mojang.serialization.MapCodec
import de.mctelemetry.compat.appliedenergisitics2.AppliedEnergistics2ModRequired
import de.mctelemetry.core.blocks.ObservationSourceContainerBlock
import de.mctelemetry.core.blocks.ScraperBlock
import net.minecraft.world.level.block.BaseEntityBlock

@AppliedEnergistics2ModRequired
class AppliedEnergistics2ScraperBlock(properties: Properties): ScraperBlock(properties.noOcclusion()) {

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC

    companion object {
        val CODEC: MapCodec<ObservationSourceContainerBlock> = simpleCodec(::AppliedEnergistics2ScraperBlock)
    }
}
