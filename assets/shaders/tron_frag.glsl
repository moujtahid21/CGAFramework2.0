#version 330 core

uniform sampler2D emitTex;
uniform sampler2D diffTex;
uniform sampler2D specTex;
uniform float shininess;
uniform vec3 emitColor;

//light
uniform vec3 pointLightColor;
uniform vec3 pointLightPosition;
uniform float pointLightConstAtt;
uniform float pointLightLinAtt;
uniform float pointLightQuadAtt;

uniform vec3 spotLightColor;
uniform vec3 spotLightPosition;
uniform float spotLightConstAtt;
uniform float spotLightLinAtt;
uniform float spotLightQuadAtt;
uniform float spotLightKegelInnen;
uniform float spotLightKegelAussen;


//input from vertex shader
in struct VertexData
{
    vec3 position;
    vec3 normal;
    vec2 texCoords;

} vertexData;


//fragment shader output
out vec4 color;
in vec3 toLight;
in vec3 toCamera;
in vec3 viewSpotLightDirection;
in mat4 inverseTransposeViewMatrixToFragment;
in vec3 toSpotLight;

void main()
{

    vec3 normalizedToCamera = normalize (toCamera);
    vec3 normalizedNormal = normalize (vertexData.normal);

    vec3 finalDiff = vec3(0.0f);
    vec3 finalSpec = vec3(0.0f);
    vec3 finalPointAmbient = vec3(0.0f);
    vec3 finalSpotDiff = vec3(0.0f);
    vec3 finalSpotSpec = vec3(0.0f);
    vec3 finalSpotAmbient = vec3(0.0f);

    vec3 toLight = (inverseTransposeViewMatrixToFragment * vec4(pointLightPosition, 1.0f)).xyz - (inverseTransposeViewMatrixToFragment * vec4(vertexData.position, 1.0f)).xyz;
    vec3 normalizedToLight = normalize (toLight);
    float distanceToLight = length(toLight);
    float pointAttenuation = 1.0f / (pointLightConstAtt + pointLightLinAtt * distanceToLight + pointLightQuadAtt * (distanceToLight*distanceToLight));

    finalPointAmbient += 0.1f * pointLightColor * pointAttenuation;


    float brightnessDiff = max(0.0f, dot(normalizedNormal, normalizedToLight));
    finalDiff +=  (pointAttenuation * pointLightColor * brightnessDiff * texture(diffTex, vertexData.texCoords).rgb);

    vec3 reflectedToLight = reflect(-normalizedToLight, normalizedNormal);
    float brightnessSpecular = max(0.0f, dot(reflectedToLight, normalizedToCamera));

    finalSpec += pointAttenuation * pow(brightnessSpecular, shininess) * texture(specTex, vertexData.texCoords).rgb * pointLightColor;


    vec3 toSpotLight = (inverseTransposeViewMatrixToFragment * vec4(spotLightPosition, 1.0f)).xyz - (inverseTransposeViewMatrixToFragment * vec4(vertexData.position, 1.0f)).xyz;

    //Spotlight
    vec3 normalizedToSpotLight = normalize (toSpotLight);
    vec3 normalizedSpotLightDirection = normalize(viewSpotLightDirection);
    float distanceToSpot = length(toSpotLight);
    float spotAttenuation = 1.0f / (spotLightConstAtt + spotLightLinAtt * distanceToSpot + spotLightQuadAtt * (distanceToSpot*distanceToSpot));
    finalSpotAmbient += 0.1f * spotLightColor * spotAttenuation;


    float theta = dot(normalizedToSpotLight, normalize(-normalizedSpotLightDirection));

    float epsilon = spotLightKegelInnen - spotLightKegelAussen;

    float intensity = clamp((theta-spotLightKegelAussen) / epsilon, 0.0f, 1.0f);

    float brightnessSpotDiff = max(dot(normalizedNormal, normalizedToSpotLight), 0.0f);

    finalSpotDiff += spotAttenuation * brightnessSpotDiff * spotLightColor * intensity * texture(diffTex, vertexData.texCoords).rgb;

    vec3 reflectedToSpotLight = reflect(-normalizedToSpotLight, normalizedNormal);
    float brightnessSpotSpecular = max(0.0f, dot(reflectedToSpotLight, normalizedToCamera));

    finalSpotSpec += spotAttenuation * pow(brightnessSpotSpecular, shininess) * texture(specTex, vertexData.texCoords).rgb * spotLightColor * intensity;

    //Output
    //vec3 result = emitColor * texture(emitTex, vertexData.textureCoord).rgb + (finalDiff + finalSpec + finalSpotDiff );
    vec3 result = texture(emitTex, vertexData.texCoords).rgb + finalPointAmbient + finalDiff + finalSpec + finalSpotDiff + finalSpotAmbient + finalSpotSpec;

    //vec3 result = finalDiff * texture(emitTex, vertexData.textureCoord).rgb + finalSpec;
    //vec3 result = (ambient + finalDiff + finalSpec) + texture(emitTex, vertexData.textureCoord).rgb;

    color = vec4(result.rgb, 1.0);
    //color = vec4(vertexData.testPos.xy / vertexData.testPos.w, 1.0, 1.0);
    //color = vec4(0,1,0,1.0);


}