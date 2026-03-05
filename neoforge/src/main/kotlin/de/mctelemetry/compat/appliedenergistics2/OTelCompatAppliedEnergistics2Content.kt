package de.mctelemetry.compat.appliedenergistics2

import de.mctelemetry.compat.OTelCompatMod
import de.mctelemetry.compat.appliedenergistics2.blocks.AE2ObservationProviderBlock
import de.mctelemetry.compat.appliedenergistics2.blocks.entities.AE2ObservationProviderBlockEntity
import de.mctelemetry.compat.neoforge.`arch$tab`
import de.mctelemetry.core.OTelCoreMod
import de.mctelemetry.core.api.OTelCoreModAPI
import de.mctelemetry.core.api.observations.IObservationSource
import de.mctelemetry.core.component.OTelCoreModComponents
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

@AppliedEnergistics2ModRequired
object OTelCompatAppliedEnergistics2Content {


    val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(Registries.BLOCK, OTelCompatMod.MOD_ID)

    val ITEMS: DeferredRegister<Item> = DeferredRegister.create(Registries.ITEM, OTelCompatMod.MOD_ID)

    val OBSERVATION_SOURCES: DeferredRegister<IObservationSource<*, *>> = DeferredRegister.create(
        OTelCoreModAPI.ObservationSources,
        OTelCompatMod.MOD_ID
    )

    val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, OTelCompatMod.MOD_ID)

    val AE2_OBSERVATION_PROVIDER_BLOCK = BLOCKS.register("ae2_observation_provider") { ->
        AE2ObservationProviderBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(3.5f))
    }

    val AE2_OBSERVATION_PROVIDER_BLOCK_ENTITY = BLOCK_ENTITIES.register("ae2_observation_provider") { ->
        BlockEntityType(
            ::AE2ObservationProviderBlockEntity,
            setOf(AE2_OBSERVATION_PROVIDER_BLOCK.get()),
            null
        )
    }


    val AE2_OBSERVATION_PROVIDER_ITEM = ITEMS.register("ae2_observation_provider") { ->
        BlockItem(
            AE2_OBSERVATION_PROVIDER_BLOCK.get(),
            Item.Properties()
                .`arch$tab`(OTelCoreMod.OTEL_TAB)
                .component(OTelCoreModComponents.GENERATE_SINGLETON_STATES.get(), true)
        )
    }

    private fun <T : IObservationSource<*, *>> registerObservationSource(source: T): DeferredHolder<IObservationSource<*, *>, T> {
        return OBSERVATION_SOURCES.register(source.id.location().path) { -> source }
    }

    fun register(bus: IEventBus) {
        BLOCKS.register(bus)
        BLOCK_ENTITIES.register(bus)
        ITEMS.register(bus)
        OBSERVATION_SOURCES.register(bus)
    }
}
