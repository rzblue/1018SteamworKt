package frc.team1018.steamworksKotlin.loops

interface Loop {
    fun onStart(timestamp: Double)

    fun onLoop(timestamp: Double)

    fun onStop(timestamp: Double)
}