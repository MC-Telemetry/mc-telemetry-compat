@file:Suppress("UnstableApiUsage", "FunctionName", "unused")

package de.mctelemetry.compat.neoforge

import dev.architectury.extensions.injected.InjectedItemPropertiesExtension
import dev.architectury.registry.registries.DeferredSupplier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item

fun Item.Properties.`arch$tab`(tab: CreativeModeTab): Item.Properties = (this as InjectedItemPropertiesExtension).`arch$tab`(tab)
fun Item.Properties.`arch$tab`(tab: DeferredSupplier<CreativeModeTab>): Item.Properties = (this as InjectedItemPropertiesExtension).`arch$tab`(tab)
fun Item.Properties.`arch$tab`(tab: ResourceKey<CreativeModeTab>): Item.Properties = (this as InjectedItemPropertiesExtension).`arch$tab`(tab)
