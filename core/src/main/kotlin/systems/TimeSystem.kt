package systems

import com.badlogic.ashley.systems.IntervalSystem
import ktx.log.info
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/*
Keeps track of Time of day and date and stuff because fun fun!
 */
class TimeSystem(startTime: Int = 6) : IntervalSystem(5f) {

    init {
        currentDateTime = LocalDateTime.of(2020, 1,1, startTime,0)
    }

    override fun updateInterval() {
        currentDateTime =  currentDateTime.plusHours(1)
        info { currentDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)) }
    }
    companion object {
        var currentDateTime: LocalDateTime = LocalDateTime.of(2020, 1,1, 1,0)
    }

}