package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f

class Material(var diff: Texture2D,
               var emit: Texture2D,
               var specular: Texture2D,
               var shininess: Float,
               var tcMultiplier : Vector2f){

    fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.use()
        shaderProgram.setUniform("tcMultiplier",tcMultiplier)

        emit.bind(0)
        shaderProgram.setUniform("emitTex",0)

        specular.bind(1)
        shaderProgram.setUniform("specTex",1)

        diff.bind(2)
        shaderProgram.setUniform("diffTex",2)

        shaderProgram.setUniform("shininess",shininess)
    }
    fun unbind()
    {
        emit.unbind()
    }
}