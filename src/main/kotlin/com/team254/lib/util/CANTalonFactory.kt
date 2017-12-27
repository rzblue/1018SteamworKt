package com.team254.lib.util

import com.ctre.CANTalon
import com.ctre.CANTalon.TalonControlMode
import edu.wpi.first.wpilibj.MotorSafety

/**
 * Creates CANTalon objects and configures all the parameters we care about to factory defaults. Closed-loop and sensor
 * parameters are not set, as these are expected to be set by the application.
 */
object CANTalonFactory {

    private val kDefaultConfiguration = Configuration()
    private val kSlaveConfiguration = Configuration()

    class Configuration {
        var LIMIT_SWITCH_NORMALLY_OPEN = true
        var MAX_OUTPUT_VOLTAGE = 12.0
        var NOMINAL_VOLTAGE = 0.0
        var PEAK_VOLTAGE = 12.0
        var ENABLE_BRAKE = false
        var ENABLE_CURRENT_LIMIT = false
        var ENABLE_SOFT_LIMIT = false
        var ENABLE_LIMIT_SWITCH = false
        var CURRENT_LIMIT = 0
        var EXPIRATION_TIMEOUT_SECONDS = MotorSafety.DEFAULT_SAFETY_EXPIRATION
        var FORWARD_SOFT_LIMIT = 0.0
        var INVERTED = false
        var NOMINAL_CLOSED_LOOP_VOLTAGE = 12.0
        var REVERSE_SOFT_LIMIT = 0.0
        var SAFETY_ENABLED = false

        var CONTROL_FRAME_PERIOD_MS = 5
        var MOTION_CONTROL_FRAME_PERIOD_MS = 100
        var GENERAL_STATUS_FRAME_RATE_MS = 5
        var FEEDBACK_STATUS_FRAME_RATE_MS = 100
        var QUAD_ENCODER_STATUS_FRAME_RATE_MS = 100
        var ANALOG_TEMP_VBAT_STATUS_FRAME_RATE_MS = 100
        var PULSE_WIDTH_STATUS_FRAME_RATE_MS = 100

        var VELOCITY_MEASUREMENT_PERIOD: CANTalon.VelocityMeasurementPeriod = CANTalon.VelocityMeasurementPeriod.Period_100Ms
        var VELOCITY_MEASUREMENT_ROLLING_AVERAGE_WINDOW = 64

        var VOLTAGE_COMPENSATION_RAMP_RATE = 0.0
        var VOLTAGE_RAMP_RATE = 0.0
    }

    init {
        kSlaveConfiguration.CONTROL_FRAME_PERIOD_MS = 1000
        kSlaveConfiguration.MOTION_CONTROL_FRAME_PERIOD_MS = 1000
        kSlaveConfiguration.GENERAL_STATUS_FRAME_RATE_MS = 1000
        kSlaveConfiguration.FEEDBACK_STATUS_FRAME_RATE_MS = 1000
        kSlaveConfiguration.QUAD_ENCODER_STATUS_FRAME_RATE_MS = 1000
        kSlaveConfiguration.ANALOG_TEMP_VBAT_STATUS_FRAME_RATE_MS = 1000
        kSlaveConfiguration.PULSE_WIDTH_STATUS_FRAME_RATE_MS = 1000
    }

    // Create a CANTalon with the default (out of the box) configuration.
    fun createDefaultTalon(id: Int): CANTalon {
        return createTalon(id, kDefaultConfiguration)
    }

    fun createPermanentSlaveTalon(id: Int, master_id: Int): CANTalon {
        return createTalon(id, kSlaveConfiguration).apply {
            changeControlMode(TalonControlMode.Follower)
            set(master_id.toDouble())
        }
    }

    fun createTalon(id: Int, config: Configuration): CANTalon {
        return LazyCANTalon(id, config.CONTROL_FRAME_PERIOD_MS).apply {
            changeControlMode(TalonControlMode.Voltage)
            changeMotionControlFramePeriod(config.MOTION_CONTROL_FRAME_PERIOD_MS)
            clearIAccum()
            ClearIaccum()
            clearMotionProfileHasUnderrun()
            clearMotionProfileTrajectories()
            clearStickyFaults()
            ConfigFwdLimitSwitchNormallyOpen(config.LIMIT_SWITCH_NORMALLY_OPEN)
            configMaxOutputVoltage(config.MAX_OUTPUT_VOLTAGE)
            configNominalOutputVoltage(config.NOMINAL_VOLTAGE, -config.NOMINAL_VOLTAGE)
            configPeakOutputVoltage(config.PEAK_VOLTAGE, -config.PEAK_VOLTAGE)
            ConfigRevLimitSwitchNormallyOpen(config.LIMIT_SWITCH_NORMALLY_OPEN)
            enableBrakeMode(config.ENABLE_BRAKE)
            EnableCurrentLimit(config.ENABLE_CURRENT_LIMIT)
            enableForwardSoftLimit(config.ENABLE_SOFT_LIMIT)
            enableLimitSwitch(config.ENABLE_LIMIT_SWITCH, config.ENABLE_LIMIT_SWITCH)
            enableReverseSoftLimit(config.ENABLE_SOFT_LIMIT)
            enableZeroSensorPositionOnForwardLimit(false)
            enableZeroSensorPositionOnIndex(false, false)
            enableZeroSensorPositionOnReverseLimit(false)
            reverseOutput(false)
            reverseSensor(false)
            setAnalogPosition(0)
            setCurrentLimit(config.CURRENT_LIMIT)
            expiration = config.EXPIRATION_TIMEOUT_SECONDS
            setForwardSoftLimit(config.FORWARD_SOFT_LIMIT)
            inverted = config.INVERTED
            setNominalClosedLoopVoltage(config.NOMINAL_CLOSED_LOOP_VOLTAGE)
            position = 0.0
            setProfile(0)
            pulseWidthPosition = 0
            isSafetyEnabled = config.SAFETY_ENABLED
            SetVelocityMeasurementPeriod(config.VELOCITY_MEASUREMENT_PERIOD)
            SetVelocityMeasurementWindow(config.VELOCITY_MEASUREMENT_ROLLING_AVERAGE_WINDOW)
            setVoltageCompensationRampRate(config.VOLTAGE_COMPENSATION_RAMP_RATE)
            setVoltageRampRate(config.VOLTAGE_RAMP_RATE)

            setStatusFrameRateMs(CANTalon.StatusFrameRate.General, config.GENERAL_STATUS_FRAME_RATE_MS)
            setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, config.FEEDBACK_STATUS_FRAME_RATE_MS)
            setStatusFrameRateMs(CANTalon.StatusFrameRate.QuadEncoder, config.QUAD_ENCODER_STATUS_FRAME_RATE_MS)
            setStatusFrameRateMs(CANTalon.StatusFrameRate.QuadEncoder, config.QUAD_ENCODER_STATUS_FRAME_RATE_MS)
            setStatusFrameRateMs(CANTalon.StatusFrameRate.AnalogTempVbat, config.ANALOG_TEMP_VBAT_STATUS_FRAME_RATE_MS)
            setStatusFrameRateMs(CANTalon.StatusFrameRate.PulseWidth, config.PULSE_WIDTH_STATUS_FRAME_RATE_MS)
        }
    }

    /**
     * Run this on a fresh talon to produce good values for the defaults.
     */
    fun getFullTalonInfo(talon: CANTalon): String {
        val sb = StringBuilder().append("isRevLimitSwitchClosed = ")
                .append(talon.isRevLimitSwitchClosed).append("\n").append("getBusVoltage = ")
                .append(talon.busVoltage).append("\n").append("isForwardSoftLimitEnabled = ")
                .append(talon.isForwardSoftLimitEnabled).append("\n").append("getFaultRevSoftLim = ")
                .append(talon.faultRevSoftLim).append("\n").append("getStickyFaultOverTemp = ")
                .append(talon.stickyFaultOverTemp).append("\n").append("isZeroSensorPosOnFwdLimitEnabled = ")
                .append(talon.isZeroSensorPosOnFwdLimitEnabled).append("\n")
                .append("getMotionProfileTopLevelBufferCount = ").append(talon.motionProfileTopLevelBufferCount)
                .append("\n").append("getNumberOfQuadIdxRises = ").append(talon.numberOfQuadIdxRises).append("\n")
                .append("getInverted = ").append(talon.inverted).append("\n")
                .append("getPulseWidthRiseToRiseUs = ").append(talon.pulseWidthRiseToRiseUs).append("\n")
                .append("getError = ").append(talon.error).append("\n").append("isSensorPresent = ")
                .append(talon.isSensorPresent(CANTalon.FeedbackDevice.CtreMagEncoder_Relative)).append("\n")
                .append("isControlEnabled = ").append(talon.isControlEnabled).append("\n").append("getTable = ")
                .append(talon.table).append("\n").append("isEnabled = ").append(talon.isEnabled).append("\n")
                .append("isZeroSensorPosOnRevLimitEnabled = ").append(talon.isZeroSensorPosOnRevLimitEnabled)
                .append("\n").append("isSafetyEnabled = ").append(talon.isSafetyEnabled).append("\n")
                .append("getOutputVoltage = ").append(talon.outputVoltage).append("\n").append("getTemperature = ")
                .append(talon.temperature).append("\n").append("getSmartDashboardType = ")
                .append(talon.smartDashboardType).append("\n").append("getPulseWidthPosition = ")
                .append(talon.pulseWidthPosition).append("\n").append("getOutputCurrent = ")
                .append(talon.outputCurrent).append("\n").append("get = ").append(talon.get()).append("\n")
                .append("isZeroSensorPosOnIndexEnabled = ").append(talon.isZeroSensorPosOnIndexEnabled).append("\n")
                .append("getMotionMagicCruiseVelocity = ").append(talon.motionMagicCruiseVelocity).append("\n")
                .append("getStickyFaultRevSoftLim = ").append(talon.stickyFaultRevSoftLim).append("\n")
                .append("getFaultRevLim = ").append(talon.faultRevLim).append("\n").append("getEncPosition = ")
                .append(talon.encPosition).append("\n").append("getIZone = ").append(talon.iZone).append("\n")
                .append("getAnalogInPosition = ").append(talon.analogInPosition).append("\n")
                .append("getFaultUnderVoltage = ").append(talon.faultUnderVoltage).append("\n")
                .append("getCloseLoopRampRate = ").append(talon.closeLoopRampRate).append("\n")
                .append("toString = ").append(talon.toString()).append("\n")
                // .append("getMotionMagicActTrajPosition =
                // ").append(talon.getMotionMagicActTrajPosition()).append("\n")
                .append("getF = ").append(talon.f).append("\n").append("getClass = ").append(talon.javaClass)
                .append("\n").append("getAnalogInVelocity = ").append(talon.analogInVelocity).append("\n")
                .append("getI = ").append(talon.i).append("\n").append("isReverseSoftLimitEnabled = ")
                .append(talon.isReverseSoftLimitEnabled).append("\n").append("getPIDSourceType = ")
                .append(talon.pidSourceType).append("\n").append("getEncVelocity = ")
                .append(talon.encVelocity).append("\n").append("GetVelocityMeasurementPeriod = ")
                .append(talon.GetVelocityMeasurementPeriod()).append("\n").append("getP = ").append(talon.p)
                .append("\n").append("GetVelocityMeasurementWindow = ").append(talon.GetVelocityMeasurementWindow())
                .append("\n").append("getDeviceID = ").append(talon.deviceID).append("\n")
                .append("getStickyFaultRevLim = ").append(talon.stickyFaultRevLim).append("\n")
                // .append("getMotionMagicActTrajVelocity =
                // ").append(talon.getMotionMagicActTrajVelocity()).append("\n")
                .append("getReverseSoftLimit = ").append(talon.reverseSoftLimit).append("\n").append("getD = ")
                .append(talon.d).append("\n").append("getFaultOverTemp = ").append(talon.faultOverTemp)
                .append("\n").append("getForwardSoftLimit = ").append(talon.forwardSoftLimit).append("\n")
                .append("GetFirmwareVersion = ").append(talon.GetFirmwareVersion()).append("\n")
                .append("getLastError = ").append(talon.lastError).append("\n").append("isAlive = ")
                .append(talon.isAlive).append("\n").append("getPinStateQuadIdx = ").append(talon.pinStateQuadIdx)
                .append("\n").append("getAnalogInRaw = ").append(talon.analogInRaw).append("\n")
                .append("getFaultForLim = ").append(talon.faultForLim).append("\n").append("getSpeed = ")
                .append(talon.speed).append("\n").append("getStickyFaultForLim = ")
                .append(talon.stickyFaultForLim).append("\n").append("getFaultForSoftLim = ")
                .append(talon.faultForSoftLim).append("\n").append("getStickyFaultForSoftLim = ")
                .append(talon.stickyFaultForSoftLim).append("\n").append("getClosedLoopError = ")
                .append(talon.closedLoopError).append("\n").append("getSetpoint = ").append(talon.setpoint)
                .append("\n").append("isMotionProfileTopLevelBufferFull = ")
                .append(talon.isMotionProfileTopLevelBufferFull).append("\n").append("getDescription = ")
                .append(talon.description).append("\n").append("hashCode = ").append(talon.hashCode()).append("\n")
                .append("isFwdLimitSwitchClosed = ").append(talon.isFwdLimitSwitchClosed).append("\n")
                .append("getPinStateQuadA = ").append(talon.pinStateQuadA).append("\n")
                .append("getPinStateQuadB = ").append(talon.pinStateQuadB).append("\n").append("GetIaccum = ")
                .append(talon.GetIaccum()).append("\n").append("getFaultHardwareFailure = ")
                .append(talon.faultHardwareFailure).append("\n").append("pidGet = ").append(talon.pidGet())
                .append("\n").append("getBrakeEnableDuringNeutral = ").append(talon.brakeEnableDuringNeutral)
                .append("\n").append("getStickyFaultUnderVoltage = ").append(talon.stickyFaultUnderVoltage)
                .append("\n").append("getPulseWidthVelocity = ").append(talon.pulseWidthVelocity).append("\n")
                .append("GetNominalClosedLoopVoltage = ").append(talon.GetNominalClosedLoopVoltage()).append("\n")
                .append("getPosition = ").append(talon.position).append("\n").append("getExpiration = ")
                .append(talon.expiration).append("\n").append("getPulseWidthRiseToFallUs = ")
                .append(talon.pulseWidthRiseToFallUs).append("\n")
                // .append("createTableListener = ").append(talon.createTableListener()).append("\n")
                .append("getControlMode = ").append(talon.controlMode).append("\n")
                .append("getMotionMagicAcceleration = ").append(talon.motionMagicAcceleration).append("\n")
                .append("getControlMode = ").append(talon.controlMode)

        return sb.toString()
    }
}
