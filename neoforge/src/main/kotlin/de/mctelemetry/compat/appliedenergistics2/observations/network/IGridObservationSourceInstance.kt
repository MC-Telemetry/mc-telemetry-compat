package de.mctelemetry.compat.appliedenergistics2.observations.network

import appeng.api.networking.IGridNode
import de.mctelemetry.core.api.attributes.IAttributeValueStore
import de.mctelemetry.core.api.observations.IObservationSourceInstance

interface IGridObservationSourceInstance<
        AS : IAttributeValueStore.Mutable,
        out I : IGridObservationSourceInstance<AS, I>,
        > : IObservationSourceInstance<IGridNode, AS, I> {

    override val source: IGridObservationSource<out I>

}
