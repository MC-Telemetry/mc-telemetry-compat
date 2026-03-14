package de.mctelemetry.compat.appliedenergistics2.observations.network

import appeng.api.networking.IGridNode
import de.mctelemetry.core.api.attributes.AttributeDataSource
import de.mctelemetry.core.api.observations.IObservationSource
import net.minecraft.core.GlobalPos

interface IGridObservationSource<
        I : IGridObservationSourceInstance<*, I>
        > : IObservationSource<IGridNode, I> {

    override val sourceOwnerType: Class<IGridNode>
        get() = IGridNode::class.java
}
