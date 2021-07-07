package cga.exercise.components.light

import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector3i
import org.joml.Math

class SpotLight(position : Vector3f, color : Vector3i, var kegelInnen : Float, var kegelAussen : Float)
    :ISpotLight,PointLight(position,color) {

    override val name: String = "spotLight"

    init{
        constAttenuation = 0.5f
        linearAttenuation = 0.05f
        quadraticAttenuation = 0.01f
    }

    override fun bind(shaderProgram: ShaderProgram, name: String, viewMatrix: Matrix4f) {
        super.bind(shaderProgram,name)
        shaderProgram.setUniform("${name}Direction", getWorldZAxis().negate())
        shaderProgram.setUniform("${name}Position", getWorldPosition())
        shaderProgram.setUniform("${name}KegelInnen", kegelInnen)
        shaderProgram.setUniform("${name}KegelAussen", kegelAussen)
        shaderProgram.setUniform("${name}ConstAtt", constAttenuation)
        shaderProgram.setUniform("${name}LinAtt", linearAttenuation)
        shaderProgram.setUniform("${name}QuadAtt", quadraticAttenuation)
        shaderProgram.setUniform("${name}Color",Vector3f(color).mul(1f/255f))
    }
}