package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f

class Renderable(val meshes : MutableList<Mesh>): IRenderable, Transformable()
{
    override fun render(shaderProgram: ShaderProgram)
    {
        meshes.forEach{mesh -> shaderProgram.setUniform("model_matrix",getWorldModelMatrix())
            mesh.render(shaderProgram)}
    }

    fun setMaterial(mat: Material)
    {
        meshes.forEach{mesh -> mesh.material = mat}
    }

}