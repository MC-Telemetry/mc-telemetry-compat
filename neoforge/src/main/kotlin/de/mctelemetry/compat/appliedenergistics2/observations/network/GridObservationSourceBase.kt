package de.mctelemetry.compat.appliedenergistics2.observations.network

import appeng.api.networking.IGridNode
import com.mojang.serialization.Codec
import de.mctelemetry.core.api.attributes.AttributeDataSource
import de.mctelemetry.core.api.attributes.BuiltinAttributeKeyTypes
import de.mctelemetry.core.api.attributes.IAttributeValueStore
import de.mctelemetry.core.api.observations.IObservationSourceSingleton
import de.mctelemetry.core.api.observations.ObservationSourceBase
import de.mctelemetry.core.persistence.DirectUnitCodec
import net.minecraft.core.GlobalPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

abstract class GridObservationSourceBase<
        I : IGridObservationSourceInstance<IAttributeValueStore.MapAttributeStore, I>
        > : ObservationSourceBase<IGridNode, I>(),
    IGridObservationSource<I> {

    final override val sourceContextType: Class<IGridNode> = IGridNode::class.java

    abstract class GridInstanceBase<out I : GridInstanceBase<I>>(
        override val source: GridObservationSourceBase<out I>
    ) : InstanceBase<IGridNode, I>(source),
        IGridObservationSourceInstance<IAttributeValueStore.MapAttributeStore, I> {

        context(sourceContext: IGridNode)
        override fun createAttributeStore(parent: IAttributeValueStore): IAttributeValueStore.MapAttributeStore {
            return IAttributeValueStore.MapAttributeStore(attributes.references, parent)
        }
    }

    abstract class GridSingletonBase<I : GridSingletonBase<I>> :
        GridObservationSourceBase<I>(),
        IGridObservationSource<I>,
        IGridObservationSourceInstance<IAttributeValueStore.MapAttributeStore, I>,
        IObservationSourceSingleton<IGridNode, IAttributeValueStore.MapAttributeStore, I> {

        override val source: GridSingletonBase<I>
            get() = this

        @Suppress("UNCHECKED_CAST")
        private val typedThis: I
            get() = this as I

        override val streamCodec: StreamCodec<in RegistryFriendlyByteBuf, I> = StreamCodec.unit(typedThis)
        override val codec: Codec<I> = DirectUnitCodec(typedThis)

        context(sourceContext: IGridNode)
        override fun createAttributeStore(parent: IAttributeValueStore): IAttributeValueStore.MapAttributeStore {
            return IAttributeValueStore.MapAttributeStore(attributes.references, parent)
        }
    }
}
