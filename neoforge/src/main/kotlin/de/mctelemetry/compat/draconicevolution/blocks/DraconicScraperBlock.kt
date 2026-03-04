package de.mctelemetry.compat.draconicevolution.blocks

import com.mojang.serialization.MapCodec
import de.mctelemetry.compat.draconicevolution.DraconicEvolutionModRequired
import de.mctelemetry.core.blocks.ContainerScraperBlock
import de.mctelemetry.core.blocks.ObservationSourceContainerBlock
import de.mctelemetry.core.blocks.ScraperBlock
import net.minecraft.world.level.block.BaseEntityBlock

@DraconicEvolutionModRequired
class DraconicScraperBlock(properties: Properties): ScraperBlock(properties) {

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC

    companion object {
        val CODEC: MapCodec<ObservationSourceContainerBlock> = simpleCodec(::ContainerScraperBlock)
    }
}
