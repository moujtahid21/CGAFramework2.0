package cga.exercise.components.camera

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Vector3f
import java.lang.Math.PI


class TronCamera() : ICamera, Transformable()
{


    override fun bind(shader: ShaderProgram)
    {
        shader.setUniform("view_matrix",getCalculateViewMatrix())
        shader.setUniform("projection_matrix",getCalculateProjectionMatrix())
    }

    override fun getCalculateProjectionMatrix(): Matrix4f
    {
        val projectionMatrix = Matrix4f()

        projectionMatrix.perspective(Math.toRadians(90.0).toFloat(),16.0f/9.0f,0.1f,100.0f)

       return projectionMatrix
    }

    override fun getCalculateViewMatrix(): Matrix4f
    {
        val viewMatrix = Matrix4f()

        viewMatrix.lookAt(getWorldPosition(), getWorldPosition().sub(getWorldZAxis()), getWorldYAxis())

        return viewMatrix
    }
}