package de.mctelemetry.compat.appliedenergistics2.observations.network

import appeng.api.networking.IGridNode
import de.mctelemetry.core.api.attributes.IAttributeValueStore
import de.mctelemetry.core.api.observations.IObservationSourceInstance

interface IGridObservationSourceInstance<
        OC : AutoCloseable,
        out I : IGridObservationSourceInstance<OC, I>,
        > : IObservationSourceInstance<IGridNode, OC, I> {

    override val source: IGridObservationSource<out I>

}
