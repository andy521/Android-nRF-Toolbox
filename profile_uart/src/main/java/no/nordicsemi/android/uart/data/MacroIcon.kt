package no.nordicsemi.android.uart.data

enum class MacroIcon(public val index: Int) {
    LEFT(0),
    UP(1),
    RIGHT(2),
    DOWN(3),
    SETTINGS(4),
    REW(5),
    PLAY(6),
    PAUSE(7),
    STOP(8),
    FWD(9),
    INFO(10),
    NUMBER_1(11),
    NUMBER_2(12),
    NUMBER_3(13),
    NUMBER_4(14),
    NUMBER_5(15),
    NUMBER_6(16),
    NUMBER_7(17),
    NUMBER_8(18),
    NUMBER_9(19);

    companion object {
        fun create(index: Int): MacroIcon {
            return values().firstOrNull { it.index == index }
                ?: throw IllegalArgumentException("Cannot create MacroIcon for index: $index")
        }
    }
}
