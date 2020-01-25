package org.ghrobotics.falcondashboard.generator.fragments

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Translation2d
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil
import edu.wpi.first.wpilibj.util.Units
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import kfoenix.jfxtextarea
import org.ghrobotics.falcondashboard.generator.GeneratorView
import tornadofx.*
import java.awt.Desktop
import java.lang.StringBuilder
import java.net.URI

class CodeFragment : Fragment() {
    override val root = vbox {

        title = "Generated Code"

        style {
            padding = box(1.em)
        }

        prefWidth = 800.0
        prefHeight = 500.0

        jfxtextarea {
            font = Font.font("Monospaced")
            isEditable = false

            vgrow = Priority.ALWAYS

//            text = TrajectoryUtil.serializeTrajectory(GeneratorView.trajectory.value)

            val waypoints = GeneratorView.waypoints
            text = ""

            val builder = StringBuilder(text)

            for (pose in waypoints){
                val translation = pose.translation;
                val rotation2d = pose.rotation;
                if(pose.equals(waypoints[0]))
                    text +=
                        "new Pose2d(Units.feetToMeters(" + Units.metersToFeet(translation.x) +
                                "), Units.feetToMeters(" + Units.metersToFeet(translation.y) + "), new Rotation2d().fromDegrees(" + rotation2d.degrees+")), \n \n"
                else if(pose.equals(waypoints[waypoints.size-1]))
                    text +=
                        "new Pose2d(Units.feetToMeters(" + Units.metersToFeet(translation.x) +
                                "), Units.feetToMeters(" + Units.metersToFeet(translation.y) + "), new Rotation2d().fromDegrees(" + rotation2d.degrees+")) \n" +
                                " \n"
                else{
                    text += "List.of("
                    text += "new Translation2d(Units.feetToMeters(" + Units.metersToFeet(translation.x)+"), Units.feetToMeters(" + Units.metersToFeet(translation.y)+")"
                    text += "), \n" +
                            " \n"
                }
            }

        }
        vbox {
            style {
                padding = box(0.5.em, 0.em, 0.em, 0.em)
            }
            add(text(" This code is generated to be used with FalconLibrary"))
            add(hyperlink("https://github.com/5190GreenHopeRobotics/FalconLibrary") {
                setOnAction {
                    Desktop.getDesktop()
                        .browse(URI("https://github.com/5190GreenHopeRobotics/FalconLibrary"))
                }
            })
        }
    }
}