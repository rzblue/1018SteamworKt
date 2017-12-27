package frc.team1018.lib.util.drivers

import edu.wpi.first.wpilibj.I2C

/**
 * @author Ryan Blue
 */
class LidarLite(port: I2C.Port) {
    val kAddress = 0x62
    private val i2c = I2C(port, kAddress)

    private fun write(address: Char, value: Char) {
    }
}
