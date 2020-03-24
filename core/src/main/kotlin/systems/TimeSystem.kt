package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.ashley.systems.IntervalSystem
import components.NpcComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.log.info
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/*
Keeps track of Time of day and date and stuff because fun fun!
 */


class TimeSystem(startTime: Int = 6, interval: Float = 5f, private val minutesPerTick: Long = 60) : IntervalIteratingSystem(
        allOf(NpcComponent::class).get(),
        interval) {

    val mapper = mapperFor<NpcComponent>()

    init {
        currentDateTime = LocalDateTime.of(2020, 1,1, startTime,0)
    }

    override fun processEntity(entity: Entity) {
        mapper.get(entity).npc.expendEnergyAndStuff()
    }

    override fun updateInterval() {
        currentDateTime =  currentDateTime.plusMinutes(minutesPerTick)
        info { currentDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)) }
        super.updateInterval()
    }
    companion object {
        var currentDateTime: LocalDateTime = LocalDateTime.of(2020, 1,1, 1,0)
    }

}