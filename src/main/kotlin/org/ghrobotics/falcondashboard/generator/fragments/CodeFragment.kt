package org.ghrobotics.falcondashboard.generator.fragments

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Translation2d
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil
import edu.wpi.first.wpilibj.util.Units
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import kfoenix.jfxtextarea
import org.ghrobotics.falcondashboard.Settings
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

        prefWidth = 1300.0
        prefHeight = 500.0

        jfxtextarea {
            font = Font.font("Monospaced")
            isEditable = false

            vgrow = Priority.ALWAYS

            val waypoints = GeneratorView.waypoints
            text = ""

            text += "RamseteGenerator.getRamseteCommand(\n"
            text += "\tdrivetrain,\n"

            if(Settings.clampedCubic.value) {
                for (pose in waypoints) {
                    val translation = pose.translation;
                    val rotation2d = pose.rotation;
                    if (pose == waypoints[0]) {
                        text += String.format(
                            "\tnew Pose2d(Units.feetToMeters(%.2f), Units.feetToMeters(%.2f), new Rotation2d().fromDegrees(%.2f)), \n \n",
                            Units.metersToFeet(translation.x), Units.metersToFeet(translation.y), rotation2d.degrees
                        )

                        text += "List.of( \n"

                    } else if (pose == waypoints[waypoints.size - 1]) {
                        text += "), \n \n"

                        text += String.format(
                            "\tnew Pose2d(Units.feetToMeters(%.2f), Units.feetToMeters(%.2f), new Rotation2d().fromDegrees(%.2f)),",
                            Units.metersToFeet(translation.x), Units.metersToFeet(translation.y), rotation2d.degrees
                        )

                    } else {
                        text += String.format(
                            "\t\tnew Translation2d(Units.feetToMeters(%.2f), Units.feetToMeters(%.2f)), \n",
                            Units.metersToFeet(translation.x), Units.metersToFeet(translation.y)
                        )
                    }
                }

            } else {
                text+="\tList.of(\n"
                val iterator = waypoints.toList().listIterator()
                while(iterator.hasNext()){
                    val pose = iterator.next()
                    val translation = pose.translation
                    val rotation2d = pose.rotation
                    text += String.format(
                        "\t\tnew Pose2d(Units.feetToMeters(%.2f), Units.feetToMeters(%.2f), new Rotation2d().fromDegrees(%.2f))",
                        Units.metersToFeet(translation.x), Units.metersToFeet(translation.y), rotation2d.degrees
                    )

                    if(iterator.hasNext())
                        text += ","
                    text += "\n"

                }

                text += "\t),\n"
            }
            text+= "\tUnits.feetToMeters(velocity), Units.feetToMeters(accel), "
            if(Settings.reversed.value)
                text += "true"
            else
                text+="false"
            text += "\n"
            text += ");"

        }
        vbox {
            style {
                padding = box(0.5.em, 0.em, 0.em, 0.em)
            }
            add(text(" This code is generated to be used with WPILib"))
            add(hyperlink("https://github.com/allwpilib/") {
                setOnAction {
                    Desktop.getDesktop()
                        .browse(URI("https://github.com/allwpilib"))
                }
            })
        }
    }
}