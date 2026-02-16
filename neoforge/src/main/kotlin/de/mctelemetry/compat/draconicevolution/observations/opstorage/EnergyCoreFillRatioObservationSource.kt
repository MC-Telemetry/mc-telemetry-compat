package de.mctelemetry.compat.draconicevolution.observations.opstorage

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyCore
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyCoreStabilizer
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyPylon
import de.mctelemetry.compat.OTelCompatMod
import de.mctelemetry.compat.draconicevolution.OTelCompatDraconicEvolutionContent.getDraconicEvolutionTileEnergyCore
import de.mctelemetry.core.api.OTelCoreModAPI
import de.mctelemetry.core.api.attributes.AttributeDataSource
import de.mctelemetry.core.api.attributes.IAttributeValueStore
import de.mctelemetry.core.api.observations.IObservationRecorder
import de.mctelemetry.core.api.observations.IObservationSource
import de.mctelemetry.core.api.observations.position.PositionObservationSourceBase
import de.mctelemetry.core.utils.globalPosOrThrow
import de.mctelemetry.core.utils.observe
import de.mctelemetry.core.utils.withValue
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntity

object EnergyCoreFillRatioObservationSource : PositionObservationSourceBase.PositionSingletonBase<EnergyCoreFillRatioObservationSource>() {

    override val id: ResourceKey<IObservationSource<*, *>>
        get() = ResourceKey.create(
            OTelCoreModAPI.ObservationSources, ResourceLocation.fromNamespaceAndPath(
                OTelCompatMod.MOD_ID, "draconic_scraper.energy_core.fill_ratio"
            )
        )

    context(sourceContext: BlockEntity, attributeStore: IAttributeValueStore.MapAttributeStore)
    override fun observePosition(
        recorder: IObservationRecorder.Unresolved,
        level: ServerLevel,
        position: BlockPos,
        facing: Direction?,
        unusedAttributes: Set<AttributeDataSource<*>>,
    ) {
        val core: TileEnergyCore = level.getDraconicEvolutionTileEnergyCore(position) ?: return
        observedPosition.withValue(core.globalPosOrThrow) {
            recorder.observe(core.fillPercent.get().toDouble())
        }
    }
}
