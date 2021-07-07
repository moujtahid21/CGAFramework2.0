package cga.exercise.components.geometry

import org.joml.Matrix4f
import org.joml.Vector3f
import java.lang.Math.atan2


open class Transformable(var modelMatrix: Matrix4f = Matrix4f(), var parent: Transformable? = null) {

    /**
     * Rotates object around its own origin.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     */
    fun rotateLocal(pitch: Float, yaw: Float, roll: Float)
    {
        val rotateMatrix = Matrix4f().rotateXYZ(pitch,yaw,roll)

        // M * R
        modelMatrix.mul(rotateMatrix)
    }

    /**
     * Rotates object around given rotation center.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     * @param altMidpoint rotation center
     */
    fun rotateAroundPoint(pitch: Float, yaw: Float, roll: Float, altMidpoint: Vector3f)
    {
        val translateToPoint = Matrix4f().translate(altMidpoint)
        val translateMatrix = Matrix4f(translateToPoint).invert()
        val rotateMatrix = Matrix4f().rotateXYZ(pitch,yaw,roll)

        // M * TP * R * T
        modelMatrix.mul(translateToPoint).mul(rotateMatrix).mul(translateMatrix)
    }

    /**
     * Translates object based on its own coordinate system.
     * @param deltaPos delta positions
     */
    fun translateLocal(deltaPos: Vector3f)
    {
        val translateMatrix = Matrix4f().translate(deltaPos)

        // M * T
        modelMatrix.mul(translateMatrix)
    }

    /**
     * Translates object based on its parent coordinate system.
     * Hint: global operations will be left-multiplied
     * @param deltaPos delta positions (x, y, z)
     */
    fun translateGlobal(deltaPos: Vector3f)
    {
        val translateMatrix = Matrix4f().translate(deltaPos)

        // T * M
        translateMatrix.mul(modelMatrix,modelMatrix)
    }

    /**
     * Scales object related to its own origin
     * @param scale scale factor (x, y, z)
     */
    fun scaleLocal(scale: Vector3f)
    {
        val scaleMatrix = Matrix4f().scale(scale)

        // M * S
        modelMatrix.mul(scaleMatrix)
    }

    /**
     * Returns position based on aggregated translations.
     * Hint: last column of model matrix
     * @return position
     */
    fun getPosition(): Vector3f
    {
        return Vector3f(modelMatrix.m30(),modelMatrix.m31(),modelMatrix.m32())
    }

    /**
     * Returns position based on aggregated translations incl. parents.
     * Hint: last column of world model matrix
     * @return position
     */
    fun getWorldPosition(): Vector3f
    {
        return if (parent == null){
            getPosition()
        } else{
            Vector3f(getWorldModelMatrix().m30(), getWorldModelMatrix().m31(), getWorldModelMatrix().m32())
        }
    }


    /**
     * Returns x-axis of object coordinate system
     * Hint: first normalized column of model matrix
     * @return x-axis
     */
    fun getXAxis(): Vector3f
    {
        return Vector3f(modelMatrix.m00(),modelMatrix.m01(),modelMatrix.m02())
    }

    /**
     * Returns y-axis of object coordinate system
     * Hint: second normalized column of model matrix
     * @return y-axis
     */
    fun getYAxis(): Vector3f
    {
        return Vector3f(modelMatrix.m10(),modelMatrix.m11(),modelMatrix.m12())
    }

    /**
     * Returns z-axis of object coordinate system
     * Hint: third normalized column of model matrix
     * @return z-axis
     */
    fun getZAxis(): Vector3f
    {
        return Vector3f(modelMatrix.m20(),modelMatrix.m21(),modelMatrix.m22())
    }

    /**
     * Returns x-axis of world coordinate system
     * Hint: first normalized column of world model matrix
     * @return x-axis
     */
    fun getWorldXAxis(): Vector3f
    {
        val matrix = parent?.modelMatrix ?: modelMatrix
        val vector = Vector3f(matrix.m00(),matrix.m01(),matrix.m02())
        return vector.normalize()
    }

    /**
     * Returns y-axis of world coordinate system
     * Hint: second normalized column of world model matrix
     * @return y-axis
     */
    fun getWorldYAxis(): Vector3f
    {
        val matrix = parent?.modelMatrix ?: modelMatrix
        val vector = Vector3f(matrix.m10(), matrix.m11(), matrix.m12())
        return vector.normalize()
    }

    /**
     * Returns z-axis of world coordinate system
     * Hint: third normalized column of world model matrix
     * @return z-axis
     */
    fun getWorldZAxis(): Vector3f
    {
        val matrix = parent?.modelMatrix ?: modelMatrix
        val vector = Vector3f(matrix.m20(), matrix.m21(), matrix.m22())
        return vector.normalize()
    }

    /**
     * Returns multiplication of world and object model matrices.
     * Multiplication has to be recursive for all parents.
     * Hint: scene graph
     * @return world modelMatrix
     */
    fun getWorldModelMatrix(): Matrix4f
    {
        if(parent == null) return getLocalModelMatrix()
        val parentMatrix = parent!!.getWorldModelMatrix()

        return Matrix4f(parentMatrix.mul(modelMatrix))
    }

    /**
     * Returns object model matrix
     * @return modelMatrix
     */
    fun getLocalModelMatrix(): Matrix4f
    {
        return Matrix4f(modelMatrix)
    }

    fun get_x_direction() = atan2(modelMatrix.m11().toDouble(), modelMatrix.m12().toDouble())

    fun get_y_direction() = atan2(modelMatrix.m00().toDouble(), modelMatrix.m02().toDouble())

    fun get_z_direction() = Math.asin(modelMatrix.m01().toDouble())
}