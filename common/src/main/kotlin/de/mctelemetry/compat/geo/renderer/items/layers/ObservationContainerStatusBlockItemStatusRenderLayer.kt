package de.mctelemetry.compat.geo.renderer.items.layers

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import de.mctelemetry.compat.geo.items.ObservationContainerStatusBlockItem
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

class ObservationContainerStatusBlockItemStatusRenderLayer(entityRendererIn: GeoRenderer<ObservationContainerStatusBlockItem>) :
    GeoRenderLayer<ObservationContainerStatusBlockItem>(entityRendererIn) {

    object StatusModel : DefaultedGeoModel<ObservationContainerStatusBlockItem>(
        ScraperBlockEntityStatusRenderLayer.StatusModel.assetSubPath
    ) {
        override fun subtype(): String = ScraperBlockEntityStatusRenderLayer.StatusModel.subtype()

        private fun getStatus(animatable: ObservationContainerStatusBlockItem): ObservationSourceErrorState.Type {
            return ObservationSourceErrorState.Type.Ok
        }

        @Deprecated("Deprecated")
        override fun getTextureResource(animatable: ObservationContainerStatusBlockItem): ResourceLocation {
            return ScraperBlockEntityStatusRenderLayer.getStatusTextureForStateType(
                getStatus(animatable)
            )
        }

        override fun getAnimationResource(animatable: ObservationContainerStatusBlockItem): ResourceLocation {
            return ScraperBlockEntityStatusRenderLayer.getStatusAnimationForStateType(
                getStatus(animatable)
            )
        }

        override fun getRenderType(animatable: ObservationContainerStatusBlockItem, texture: ResourceLocation): RenderType {
            return AutoGlowingTexture.getRenderType(texture)
        }
    }

    override fun render(
        poseStack: PoseStack,
        animatable: ObservationContainerStatusBlockItem,
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
