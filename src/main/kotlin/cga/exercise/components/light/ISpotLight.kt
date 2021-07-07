package cga.exercise.components.light

import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f

interface ISpotLight {
    val name : String
    fun bind(shaderProgram: ShaderProgram, name: String, viewMatrix: Matrix4f)
}