package de.mctelemetry.compat.appliedenergistics2.observations.network

import appeng.api.networking.IGridNode
import com.mojang.serialization.Codec
import de.mctelemetry.core.api.observations.IObservationSourceSingleton
import de.mctelemetry.core.api.observations.ObservationSourceBase
import de.mctelemetry.core.observations.model.ObservationAttributeMapping
import de.mctelemetry.core.persistence.DirectUnitCodec
import de.mctelemetry.core.utils.EmptyAutoCloseable
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

abstract class GridObservationSourceBase<
        I : IGridObservationSourceInstance<*, I>,
        > : ObservationSourceBase<IGridNode, I>(),
    IGridObservationSource<I> {

    final override val sourceOwnerType: Class<IGridNode> = IGridNode::class.java

    abstract class GridInstanceBase<OC: AutoCloseable, out I : GridInstanceBase<OC, I>>(
        override val source: GridObservationSourceBase<out I>
    ) : InstanceBase<IGridNode, OC, I>(source),
        IGridObservationSourceInstance<OC, I> {

        abstract class Simple<out I : Simple<I>>(source: GridObservationSourceBase<out I>) :
            GridInstanceBase<EmptyAutoCloseable, I>(source) {
            context(sourceOwner: IGridNode, mapping: ObservationAttributeMapping)
            final override fun createObservationContext(): EmptyAutoCloseable = EmptyAutoCloseable
        }
    }

    abstract class GridSingletonBase<OC: AutoCloseable, I : GridSingletonBase<OC, I>> :
        GridObservationSourceBase<I>(),
        IGridObservationSource<I>,
        IGridObservationSourceInstance<OC, I>,
        IObservationSourceSingleton<IGridNode, OC, I> {

        override val source: GridSingletonBase<OC, I>
            get() = this

        @Suppress("UNCHECKED_CAST")
        private val typedThis: I
            get() = this as I

        override val streamCodec: StreamCodec<in RegistryFriendlyByteBuf, I> = StreamCodec.unit(typedThis)
        override val codec: Codec<I> = DirectUnitCodec(typedThis)

        abstract class Simple<I : Simple<I>> :
            GridSingletonBase<EmptyAutoCloseable, I>() {
            context(sourceOwner: IGridNode, mapping: ObservationAttributeMapping)
            final override fun createObservationContext(): EmptyAutoCloseable = EmptyAutoCloseable
        }
    }
}
