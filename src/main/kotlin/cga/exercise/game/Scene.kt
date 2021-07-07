package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import cga.framework.OBJLoader
import org.lwjgl.opengl.GL11.*
import org.joml.*
import org.lwjgl.glfw.GLFW
import kotlin.random.Random


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram
    private val groundMesh : Mesh
    private val ground : Renderable
    private val camera : TronCamera
    private val motorrad : Renderable?
    private val pointLight : PointLight
    private val spotLight : SpotLight


    //scene setup
    init {


        staticShader = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")

        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        glEnable(GL_CULL_FACE); GLError.checkThrow()
        glFrontFace(GL_CCW); GLError.checkThrow()
        glCullFace(GL_BACK); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()


        // Textures

        val diffTex = Texture2D("assets/textures/ground_diff.png",true)
        diffTex.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_NEAREST)
        val emitTex = Texture2D("assets/textures/ground_emit.png",true)
        emitTex.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_NEAREST, GL_NEAREST)
        val specTex = Texture2D("assets/textures/ground_spec.png",true)
        specTex.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_NEAREST)

        val groundMaterial = Material(diffTex,emitTex,specTex,100.0f, Vector2f(64.0f,64.0f))


        // ground

        val groundOBJ = OBJLoader.loadOBJ("assets/models/ground.obj")
        val groundMeshOBJ  = groundOBJ.objects[0].meshes[0]
        val groundAttrib = arrayOf(
            VertexAttribute(3, GL_FLOAT,8*4,0),
            VertexAttribute(2, GL_FLOAT,8*4,3*4),
            VertexAttribute(3, GL_FLOAT,8*4,5*4))
        groundMesh = Mesh(groundMeshOBJ.vertexData,groundMeshOBJ.indexData,groundAttrib,groundMaterial)
        ground = Renderable(mutableListOf(groundMesh))

        // motorrad

        motorrad = ModelLoader.loadModel(
            "assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj",
            Math.toRadians(-90.0).toFloat(),
            Math.toRadians(90.0).toFloat(),0f)

        motorrad?.scaleLocal(Vector3f(0.8f,0.8f,0.8f))


        // Cam
        camera = TronCamera()
        camera.rotateLocal(Math.toRadians(-35.0).toFloat(),0f,0f)
        camera.translateLocal(Vector3f(0f,0f,4.0f))
        camera.parent = motorrad

        // light

        pointLight = PointLight(Vector3f(0f,1f,-4f),Vector3i(255,255,255))

        spotLight = SpotLight(Vector3f(1f,1f,1f),Vector3i(255,255,255),0.91f,0.82f)

        pointLight.parent = motorrad
        spotLight.parent = camera





    }

    fun render(dt: Float, t: Float)
    {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        camera.bind(staticShader)
        ground.render(staticShader)
        motorrad?.render(staticShader)
        spotLight.bind(staticShader,"spotLight")
        pointLight.bind(staticShader,"pointLight")

    }

    fun update(dt: Float, t: Float)
    {

        // Key States

        val w = window.getKeyState(GLFW.GLFW_KEY_W)
        val s = window.getKeyState(GLFW.GLFW_KEY_S)
        val a = window.getKeyState(GLFW.GLFW_KEY_A)
        val d = window.getKeyState(GLFW.GLFW_KEY_D)


        if(w)
        {
            motorrad?.translateLocal(Vector3f(0f,0f,-10.0f*dt))
        }
        if(s)
        {
            motorrad?.translateLocal(Vector3f(0f,0f,10.0f*dt))
        }
        if(w || s)
        {
            if(a)
            {
                motorrad?.rotateLocal(0f,2.0f*dt,0f)
            }
            if(d)
            {
                motorrad?.rotateLocal(0f,-2.0f*dt,0f)
            }
        }

    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {

        var lastX = 0.0
        var lastY = 0.0
        var sensitivity = 0.025f

        motorrad?.rotateLocal(0f, (lastX - xpos).toFloat() * sensitivity, 0f)
        camera.rotateLocal((lastY - ypos).toFloat() * sensitivity, 0f, 0f)

        if(camera.get_x_direction() > 180f)
        {
            camera.rotateLocal(-(lastY - ypos).toFloat() * sensitivity, 0f, 0f)
        }
        else if(camera.get_x_direction() < 0f)
        {
            camera.rotateLocal(-(lastY - ypos).toFloat() * sensitivity, 0f, 0f)
        }

        lastX = xpos
        lastY = ypos
    }


    fun cleanup() {}
}
