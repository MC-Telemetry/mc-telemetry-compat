package de.mctelemetry.compat.appliedenergistics2.geo.renderer.blocks.layers

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import de.mctelemetry.compat.appliedenergistics2.AppliedEnergistics2ModRequired
import de.mctelemetry.compat.appliedenergistics2.blocks.entities.AE2ConnectorBlockEntity
import de.mctelemetry.core.blocks.ObservationSourceContainerBlock
import de.mctelemetry.core.geo.renderer.blocks.entities.layers.ScraperBlockEntityStatusRenderLayer
import de.mctelemetry.core.observations.model.ObservationSourceErrorState
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.cache.texture.AutoGlowingTexture
import software.bernie.geckolib.model.DefaultedGeoModel
import software.bernie.geckolib.renderer.GeoRenderer
import software.bernie.geckolib.renderer.layer.GeoRenderLayer

@AppliedEnergistics2ModRequired
class AE2ConnectorBlockEntityStatusRenderLayer(entityRendererIn: GeoRenderer<AE2ConnectorBlockEntity>) :
    GeoRenderLayer<AE2ConnectorBlockEntity>(entityRendererIn) {

    object StatusModel : DefaultedGeoModel<AE2ConnectorBlockEntity>(
        ScraperBlockEntityStatusRenderLayer.StatusModel.assetSubPath
    ) {
        public override fun subtype(): String = ScraperBlockEntityStatusRenderLayer.StatusModel.subtype()

        private fun getStatus(animatable: AE2ConnectorBlockEntity): ObservationSourceErrorState.Type {
            return animatable.blockState.getValue(ObservationSourceContainerBlock.ERROR)
        }

        @Deprecated("Deprecated")
        override fun getTextureResource(animatable: AE2ConnectorBlockEntity): ResourceLocation {
            return ScraperBlockEntityStatusRenderLayer.getStatusTextureForStateType(
                getStatus(animatable)
            )
        }

        override fun getAnimationResource(animatable: AE2ConnectorBlockEntity): ResourceLocation {
            return ScraperBlockEntityStatusRenderLayer.getStatusAnimationForStateType(
                getStatus(animatable)
            )
        }

        override fun getRenderType(animatable: AE2ConnectorBlockEntity, texture: ResourceLocation): RenderType? {
            if (animatable.blockState.getValue(ObservationSourceContainerBlock.ERROR) == ObservationSourceErrorState.Type.NotConfigured)
                return super.getRenderType(animatable, texture)
            return AutoGlowingTexture.getRenderType(texture)
        }
    }

    override fun render(
        poseStack: PoseStack,
        animatable: AE2ConnectorBlockEntity,
        bakedModel: BakedGeoModel,
        renderType: RenderType?,
        bufferSource: MultiBufferSource,
        buffer: VertexConsumer?,
        partialTick: Float,
        packedLight: Int,
        packedOverlay: Int
    ) {
        super.render(
            poseStack,
            animatable,
            bakedModel,
            renderType,
            bufferSource,
            buffer,
            partialTick,
            packedLight,
            packedOverlay
        )
        if (buffer == null) return
        val renderer = renderer
        val statusModel = StatusModel.getBakedModel(StatusModel.getModelResource(animatable, renderer))
        val statusTexture = StatusModel.getTextureResource(animatable, renderer)
        val statusRenderType = StatusModel.getRenderType(animatable, statusTexture) ?: return
        renderer.reRender(
            statusModel,
            poseStack,
            bufferSource,
            animatable,
            statusRenderType,
            bufferSource.getBuffer(statusRenderType),
            partialTick,
            packedLight,
            packedOverlay,
            renderer.getRenderColor(animatable, partialTick, packedLight).argbInt,
        )
    }
}
