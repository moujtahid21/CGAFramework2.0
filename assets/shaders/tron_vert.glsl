#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoords;
layout(location = 2) in vec3 normal;


//uniforms
// translation object to world
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

uniform vec2 tcMultiplier;


// light
uniform vec3 pointLightPosition;
uniform vec3 spotLightPosition;
uniform vec3 spotLightDirection;

out struct VertexData
{
    vec3 position;
    vec3 normal;
    vec2 texCoords;

} vertexData;

out vec3 toCamera;
out vec3 toLight;
out vec3 viewSpotLightDirection;
out vec3 toSpotLight;
out mat4 inverseTransposeViewMatrixToFragment;

void main()
{
    vec4 pos = vec4(position,1.0f);
    vec4 worldPosition = model_matrix * pos;
    vec4 positionInCamera = view_matrix * worldPosition;
    mat4 inverseTransposeView= inverse(transpose(view_matrix));
    inverseTransposeViewMatrixToFragment = inverseTransposeView;
    gl_Position = projection_matrix * positionInCamera;
    vertexData.position = worldPosition.xyz;
    vertexData.texCoords = tcMultiplier * texCoords;
    vertexData.normal = (inverseTransposeView * model_matrix * vec4(normal,0.0f)).xyz;


   // toLight = (inverseTransposeView * vec4(pointLightPosition,1.0f)).xyz - (inverseTransposeView *vec4(vertexData.position,1.0f)).xyz;
    toCamera = -positionInCamera.xyz;
   // toSpotLight = (inverseTransposeView *vec4(spotLightPosition,1.0f)).xyz - (inverseTransposeView *vec4(vertexData.position,1.0f)).xyz;
    viewSpotLightDirection = (inverseTransposeView * vec4(spotLightDirection,0.0f)).xyz;
  }
