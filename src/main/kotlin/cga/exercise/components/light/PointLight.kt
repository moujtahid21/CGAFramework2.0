package cga.exercise.components.light

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f
import org.joml.Vector3i

open class PointLight(position : Vector3f, var color : Vector3i):Transformable(), IPointLight {

    override val name: String = "pointLight"

    var constAttenuation = 1.0f
    var linearAttenuation = 0.5f
    var quadraticAttenuation = 0.1f

    init {
        translateLocal(position)
    }
    override fun bind(shaderProgram: ShaderProgram, name: String) {
        shaderProgram.use()

        shaderProgram.setUniform("${name}Color",Vector3f(color).mul(1f/255f))
        shaderProgram.setUniform("${name}Position",getWorldPosition())
        shaderProgram.setUniform("${name}ConstAtt", constAttenuation)
        shaderProgram.setUniform("${name}LinAtt", linearAttenuation)
        shaderProgram.setUniform("${name}QuadAtt", quadraticAttenuation)

    }
}