package de.mctelemetry.compat.appliedenergistics2.geo.renderer.blocks

import de.mctelemetry.compat.appliedenergistics2.AppliedEnergistics2ModRequired
import de.mctelemetry.compat.appliedenergistics2.OTelCompatAppliedEnergistics2Content
import de.mctelemetry.compat.appliedenergistics2.blocks.entities.AE2ConnectorBlockEntity
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer

@AppliedEnergistics2ModRequired
class AE2ConnectorBlockEntityRenderer(context: BlockEntityRendererProvider.Context) : GeoBlockRenderer<AE2ConnectorBlockEntity>(
    DefaultedBlockGeoModel(OTelCompatAppliedEnergistics2Content.AE2_CONNECTOR_BLOCK.id)
){

}
