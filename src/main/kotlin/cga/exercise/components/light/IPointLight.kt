package cga.exercise.components.light

import cga.exercise.components.shader.ShaderProgram

interface IPointLight {
    val name : String
    fun bind(shaderProgram: ShaderProgram, name: String)
}