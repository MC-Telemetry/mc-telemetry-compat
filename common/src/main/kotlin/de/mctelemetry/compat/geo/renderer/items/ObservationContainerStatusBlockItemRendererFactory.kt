package de.mctelemetry.compat.geo.renderer.items

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import de.mctelemetry.compat.geo.items.ObservationContainerStatusBlockItem
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.model.GeoModel
import software.bernie.geckolib.renderer.GeoItemRenderer
import java.util.function.Consumer

object ObservationContainerStatusBlockItemRendererFactory {
        // createGeoRenderer in GeoItem explicitly requires anonymous classes for some reason
        fun createGeoRenderer(model: GeoModel<ObservationContainerStatusBlockItem>, consumer: Consumer<GeoRenderProvider>) {
            consumer.accept(object : GeoRenderProvider {
                private val renderer: GeoItemRenderer<ObservationContainerStatusBlockItem> by lazy {
                    (object : GeoItemRenderer<ObservationContainerStatusBlockItem>(model) {
                        override fun preRender(
                            poseStack: PoseStack,
                            animatable: ObservationContainerStatusBlockItem,
                            model: BakedGeoModel,
                            bufferSource: MultiBufferSource?,
                            buffer: VertexConsumer?,
                            isReRender: Boolean,
                            partialTick: Float,
                            packedLight: Int,
                            packedOverlay: Int,
                            colour: Int
                        ) {
                            super.preRender(
                                poseStack,
                                animatable,
                                model,
                                bufferSource,
                                buffer,
                                isReRender,
                                partialTick,
                                packedLight,
                                packedOverlay,
                                colour
                            )
                            if (!isReRender)
                                poseStack.translate(0f, -0.51f, 0f);
                        }
                    }).apply {
                    }
                }

                override fun getGeoItemRenderer(): BlockEntityWithoutLevelRenderer {
                    return renderer
                }
            })
        }
}
