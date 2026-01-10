package de.mctelemetry.compat.instruments.builtin.world

import de.mctelemetry.core.api.instruments.manager.server.IServerWorldInstrumentManager
import de.mctelemetry.compat.instruments.builtin.world.level.BuiltinLevelInstruments
import de.mctelemetry.compat.instruments.builtin.world.player.BuiltinPlayerInstruments
import de.mctelemetry.core.instruments.builtin.world.WorldInstrumentBase

object BuiltinWorldInstruments {

    val worldInstruments: List<WorldInstrumentBase<*>> = listOf<WorldInstrumentBase<*>>(
    ) + BuiltinLevelInstruments.levelInstruments + BuiltinPlayerInstruments.playerInstruments

    fun register() {
        for (instrument in worldInstruments) {
            IServerWorldInstrumentManager.Events.LOADING.register(instrument)
        }
    }
}
