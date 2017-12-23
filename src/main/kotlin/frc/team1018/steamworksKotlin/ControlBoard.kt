package frc.team1018.steamworksKotlin

import edu.wpi.first.wpilibj.Joystick

object ControlBoard {
    private val leftStick = Joystick(Constants.kLeftStickPort)
    private val rightStick = Joystick(Constants.kRightStickPort)
    private val buttonPanel = Joystick(Constants.kButtonPanelPort)

    val x: Double
        get() = leftStick.x
    val y: Double
        get() = leftStick.y
    val z: Double
        get() = rightStick.z

    val climbUpButton: Boolean
        get() = buttonPanel.getRawButton(2) && buttonPanel.getRawButton(16)
    val climbDownButton: Boolean
        get() = buttonPanel.getRawButton(1) && buttonPanel.getRawButton(11)
    val paddlesInButton: Boolean
        get() = buttonPanel.getRawButton(2) && buttonPanel.getRawButton(11)
    val brakesButton: Boolean
        get() = buttonPanel.getRawButton(10)
    val gearRotatorButton: Boolean
        get() = buttonPanel.getRawButton(15)
}