package data

import statemachine.StateMachine

enum class Needs {
    Fuel,
    Rest,
    Means,
    Belonging
}

enum class NeedStatus {
    Great,
    Adequate,
    Low,
    Desperate
}

enum class Traits {
    Selfish,
    Open
}

enum class Moods {
    Happy,
    Content,
    Angry
}

enum class NeedLevel {
    Below,
    Normal,
    Above
}

data class Need(val need:Needs, val needLevel: NeedLevel, var needValue: Int = 11) {
    val needStatus : NeedStatus get() {
        return when (needLevel) {
            NeedLevel.Below -> when(needValue) {
                in Int.MIN_VALUE..1 -> NeedStatus.Desperate
                in 2..3 -> NeedStatus.Low
                in 4..7 -> NeedStatus.Adequate
                in 8..Int.MAX_VALUE -> NeedStatus.Great
                else -> NeedStatus.Great
            }
            NeedLevel.Normal -> when(needValue) {
                in Int.MIN_VALUE..2 -> NeedStatus.Desperate
                in 3..5 -> NeedStatus.Low
                in 6..9 -> NeedStatus.Adequate
                in 10..Int.MAX_VALUE -> NeedStatus.Great
                else -> NeedStatus.Great
            }
            NeedLevel.Above -> when(needValue) {
                in Int.MIN_VALUE..3 -> NeedStatus.Desperate
                in 4..7 -> NeedStatus.Low
                in 5..9 -> NeedStatus.Adequate
                in 10..Int.MAX_VALUE -> NeedStatus.Great
                else -> NeedStatus.Great
            }
        }
    }
}