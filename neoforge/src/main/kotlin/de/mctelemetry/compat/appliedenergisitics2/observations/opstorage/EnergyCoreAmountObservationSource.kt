package de.mctelemetry.compat.appliedenergisitics2.observations.opstorage

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyCore
import de.mctelemetry.compat.OTelCompatMod
import de.mctelemetry.compat.appliedenergisitics2.AppliedEnergistics2ModRequired
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

@AppliedEnergistics2ModRequired
object AppliedEnergistics2AmountObservationSource : PositionObservationSourceBase.PositionSingletonBase<AppliedEnergistics2AmountObservationSource>() {

    override val id: ResourceKey<IObservationSource<*, *>>
        get() = ResourceKey.create(
            OTelCoreModAPI.ObservationSources, ResourceLocation.fromNamespaceAndPath(
                OTelCompatMod.MOD_ID, "draconic_scraper.energy_core.amount"
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
//        val core: TileEnergyCore = level.getDraconicEvolutionTileEnergyCore(position) ?: return
//        observedPosition.withValue(core.globalPosOrThrow) {
//            if (!core.energy.isUnlimited || !recorder.supportsFloating) {
//                recorder.observe(core.energy.uncappedStored)
//                return
//            }
//            recorder.observe(core.energy.storedBig.toDouble())
//        }
    }
}
