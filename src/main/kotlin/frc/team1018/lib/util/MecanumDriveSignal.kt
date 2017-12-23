package frc.team1018.lib.util

data class MecanumDriveSignal(val x: Double, val y: Double, val z: Double) {
    
    companion object {
        val NEUTRAL = MecanumDriveSignal(0.0, 0.0, 0.0)
    }
}