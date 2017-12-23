package frc.team1018.steamworksKotlin.subsystems

import com.ctre.CANTalon
import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.RobotDrive
import frc.team1018.lib.util.CANTalonFactory
import frc.team1018.steamworksKotlin.Constants
import frc.team1018.steamworksKotlin.loops.Looper


object Drivetrain: Subsystem() {
    private val frontLeftMotor = CANTalonFactory.createDefaultTalon(Constants.kFrontLeftDriveId).apply {
        changeControlMode(CANTalon.TalonControlMode.PercentVbus)
    }
    private val frontRightMotor = CANTalonFactory.createDefaultTalon(Constants.kFrontRightDriveId).apply {
        changeControlMode(CANTalon.TalonControlMode.PercentVbus)
        inverted = true
    }
    private val rearLeftMotor = CANTalonFactory.createDefaultTalon(Constants.kRearLeftDriveId).apply {
        changeControlMode(CANTalon.TalonControlMode.PercentVbus)
    }
    private val rearRightMotor = CANTalonFactory.createDefaultTalon(Constants.kRearRightDriveId).apply {
        changeControlMode(CANTalon.TalonControlMode.PercentVbus)
        inverted = true
    }

    private val rightEncoder = Encoder(Constants.kRightEncoderChA, Constants.kRightEncoderChB, true).apply {
        reset()
    }
    private val leftEncoder = Encoder(Constants.kLeftEncoderChA, Constants.kLeftEncoderChB, false).apply {
        reset()
    }

    private val driveHelper = RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor).apply {
        isSafetyEnabled = false
    }

    override fun outputToSmartDashboard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun zeroSensors() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerEnabledLoops(enabledLooper: Looper) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}