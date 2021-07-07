package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.*


/**
 * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
 *
 * @param vertexdata plain float array of vertex data
 * @param indexdata  index data
 * @param attributes vertex attributes contained in vertex data
 * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
 *
 * Created by Fabian on 16.09.2017.
 */
class Mesh(vertexdata: FloatArray, indexdata: IntArray, attributes: Array<VertexAttribute>, var material: Material) {
    //private data
    private var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = 0

    init {

        indexcount = indexdata.size

        // generate ID
        vao = glGenVertexArrays()
        vbo = glGenBuffers()
        ibo = glGenBuffers()

        // binding & upload
        glBindVertexArray(vao)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,ibo)

        val indexBuffer = BufferUtils.createIntBuffer(indexcount)
        indexBuffer.put(indexdata)
        indexBuffer.flip()
        GL15.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW)

        glBindBuffer(GL_ARRAY_BUFFER,vbo)
        val vertexBuffer = BufferUtils.createFloatBuffer(vertexdata.size)
        vertexBuffer.put(vertexdata)
        vertexBuffer.flip()
        GL15.glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)

        for(i: Int in attributes.indices)
        {
            GL20.glVertexAttribPointer(i,attributes[i].n,attributes[i].type,false,attributes[i].stride,attributes[i].offset.toLong())
            glEnableVertexAttribArray(i)
        }

        glBindVertexArray(0)
    }

    /**
     * renders the mesh
     */
    fun render()
    {

        glBindVertexArray(vao)
        glDrawElements(GL15.GL_TRIANGLES,indexcount,GL15.GL_UNSIGNED_INT,0)
        glBindVertexArray(0)

    }

    fun render(shaderProgram : ShaderProgram)
    {
        material.bind(shaderProgram)
        render()
        material.unbind()
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        if (ibo != 0) glDeleteBuffers(ibo)
        if (vbo != 0) glDeleteBuffers(vbo)
        if (vao != 0) glDeleteVertexArrays(vao)
    }
}

