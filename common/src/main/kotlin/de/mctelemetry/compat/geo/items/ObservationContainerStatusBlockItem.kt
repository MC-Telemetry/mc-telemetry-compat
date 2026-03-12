package de.mctelemetry.compat.geo.items

import de.mctelemetry.compat.geo.renderer.items.ObservationContainerStatusBlockItemRendererFactory
import de.mctelemetry.core.blocks.ObservationSourceContainerBlock
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

class ObservationContainerStatusBlockItem(block: ObservationSourceContainerBlock, properties: Properties) :
    BlockItem(block, properties), GeoItem {
    private val cache = GeckoLibUtil.createInstanceCache(this)

    override fun getBlock(): ObservationSourceContainerBlock {
        return super.getBlock() as ObservationSourceContainerBlock
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = cache

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
    }

    override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
        ObservationContainerStatusBlockItemRendererFactory.createGeoRenderer(
            DefaultedBlockGeoModel(
                block.`arch$holder`().unwrapKey().get().location()
            ), consumer
        )
    }
}
