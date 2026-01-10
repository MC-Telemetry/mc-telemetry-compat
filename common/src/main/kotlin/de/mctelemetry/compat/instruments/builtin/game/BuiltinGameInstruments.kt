package de.mctelemetry.compat.instruments.builtin.game

import de.mctelemetry.core.api.instruments.manager.IGameInstrumentManager
import de.mctelemetry.core.instruments.builtin.game.GameInstrumentBase

object BuiltinGameInstruments {

    val gameInstruments: List<GameInstrumentBase<*>> = listOf(
    )

    fun register() {
        for (instrument in gameInstruments) {
            IGameInstrumentManager.Events.READY.register(instrument)
        }
    }
}
