package de.mctelemetry.compat.draconicevolution

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyCore
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyCoreStabilizer
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyPylon
import de.mctelemetry.compat.OTelCompatMod
import de.mctelemetry.compat.draconicevolution.blocks.DraconicScraperBlock
import de.mctelemetry.compat.draconicevolution.observations.opstorage.EnergyCoreAmountObservationSource
import de.mctelemetry.compat.draconicevolution.observations.opstorage.EnergyCoreCapacityObservationSource
import de.mctelemetry.compat.draconicevolution.observations.opstorage.EnergyCoreFillRatioObservationSource
import de.mctelemetry.core.api.OTelCoreModAPI
import de.mctelemetry.core.api.observations.IObservationSource
import de.mctelemetry.core.component.OTelCoreModComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

@DraconicEvolutionModRequired
object OTelCompatDraconicEvolutionContent {

    val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(Registries.BLOCK, OTelCompatMod.MOD_ID)

    val ITEMS: DeferredRegister<Item> = DeferredRegister.create(Registries.ITEM, OTelCompatMod.MOD_ID)

    val OBSERVATION_SOURCES: DeferredRegister<IObservationSource<*, *>> = DeferredRegister.create(
        OTelCoreModAPI.ObservationSources,
        OTelCompatMod.MOD_ID
    )

    val DRACONIC_SCRAPER_BLOCK = BLOCKS.register("draconic_scraper") { ->
        DraconicScraperBlock(BlockBehaviour.Properties.of())
    }

    val DRACONIC_SCRAPER_ITEM = ITEMS.register("draconic_scraper") { ->
        BlockItem(
            DRACONIC_SCRAPER_BLOCK.get(),
            Item.Properties()
                .component(OTelCoreModComponents.GENERATE_SINGLETON_STATES.get(), true)
        )
    }

    val DRACONIC_ENERGY_CORE_AMOUNT_OBSERVATIONSOURCE = registerObservationSource(
        EnergyCoreAmountObservationSource
    )

    val DRACONIC_ENERGY_CORE_CAPACITY_OBSERVATIONSOURCE = registerObservationSource(
        EnergyCoreCapacityObservationSource
    )

    val DRACONIC_ENERGY_CORE_FILL_RATIO_OBSERVATIONSOURCE = registerObservationSource(
        EnergyCoreFillRatioObservationSource
    )

    internal fun ServerLevel.getDraconicEvolutionTileEnergyCore(pos: BlockPos): TileEnergyCore? {
        var block: BlockEntity? = null
        level.server.executeBlocking {
            block = level.getBlockEntity(pos)
        }
        return when (
            // recapture value as testBlock to enable smart cast in when
            // (`block` cannot be smart cast because it is mutated in `executeBlocking` capture)
            val testBlock = block
        ) {
            is TileEnergyPylon -> testBlock.core
            is TileEnergyCoreStabilizer -> testBlock.core
            is TileEnergyCore -> testBlock
            else -> null
        }
    }

    private fun <T : IObservationSource<*, *>> registerObservationSource(source: T): DeferredHolder<IObservationSource<*, *>, T> {
        return OBSERVATION_SOURCES.register(source.id.location().path) { -> source }
    }

    fun register(bus: IEventBus) {
        BLOCKS.register(bus)
        ITEMS.register(bus)
        OBSERVATION_SOURCES.register(bus)
    }
}
