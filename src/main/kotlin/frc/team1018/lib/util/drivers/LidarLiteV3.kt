package frc.team1018.lib.util.drivers

import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.Timer
import java.nio.ByteBuffer
import kotlin.concurrent.thread

/**
 * @author Ryan Blue
 */
class LidarLiteV3(port: I2C.Port, address: Int) {

    private companion object {
        const val LIDAR_BUSY_MASK = 0x01
        const val LIDAR_COMMAND_ACQUIRE_WITHOUT_CORRECTION = 0x03
        const val LIDAR_COMMAND_ACQUIRE_WITH_CORRECTION = 0x04
        const val LIDAR_CONFIG_REGISTER = 0x00
        const val LIDAR_STATUS_REGISTER = 0x01
        const val LIDAR_SIG_COUNT = 0x02
        const val LIDAR_ACQ_CONFIG = 0x04
        const val LIDAR_THRESHOLD_BYPASS = 0x1c
        const val LIDAR_DISTANCE_REGISTER = 0x8f

        const val UPDATE_PERIOD = 20 //ms
        const val RETRY_COUNT = 50
    }

    constructor(port: I2C.Port) : this(port, 0x62)

    var distanceCm = 0
        @Synchronized private set
        @Synchronized get

    val distanceMm
        get() = distanceCm * 10

    val distanceIn
        get() = distanceCm / 2.54 //*dab**dab*
    private var measurementCount = 0

    private val buffer = ByteBuffer.allocateDirect(3)


    private val i2c = I2C(port, address)

    private val updateLoop = Runnable {
        distanceCm = updateDistance()
    }

    private val updater = Notifier(updateLoop)

    init {
        thread {
            synchronized(this) {
                i2c.write(LIDAR_SIG_COUNT, 0x80)
                Timer.delay(0.001)
                i2c.write(LIDAR_ACQ_CONFIG, 0x08)
                Timer.delay(0.001)
                i2c.write(LIDAR_THRESHOLD_BYPASS, 0x00)
                Timer.delay(0.001)
            }
        }
        synchronized(this) {
            updater.startPeriodic(1.0 / UPDATE_PERIOD)
        }
    }

    private fun updateDistance(): Int {
        val command = if(measurementCount % 100 == 0) LIDAR_COMMAND_ACQUIRE_WITH_CORRECTION else LIDAR_COMMAND_ACQUIRE_WITHOUT_CORRECTION
        i2c.write(LIDAR_CONFIG_REGISTER, command)
        measurementCount++
        var busyCount = 0
        do {
            Timer.delay(0.001)
            val status = readByte(LIDAR_STATUS_REGISTER)
            val busy = (status and LIDAR_BUSY_MASK) == LIDAR_BUSY_MASK
            if(busy) {
                busyCount++
            } else {
                return readShort(LIDAR_DISTANCE_REGISTER)
            }
        } while(busyCount < RETRY_COUNT)
        return distanceCm
    }

    private fun readByte(register: Int): Int {
        buffer.put(0, register.toByte())
        i2c.writeBulk(buffer, 1)
        i2c.readOnly(buffer, 1)
        return buffer.get(0).toInt() and 0xFF
    }

    private fun readShort(register: Int): Int {
        buffer.put(0, register.toByte())
        i2c.writeBulk(buffer, 1)
        i2c.readOnly(buffer, 2)
        return buffer.getShort(0).toInt() and 0xFFFF
    }

}
