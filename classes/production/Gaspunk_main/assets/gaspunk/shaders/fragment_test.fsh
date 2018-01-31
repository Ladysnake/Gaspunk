#version 120
//#extension GL_EXT_gpu_shader4 : require TODO make some varying flat

#define totalSteps 4
#define totalLayers 4
#define noiseTextureResolution 512

const float noiseTextureResolutionInverse = 1 / noiseTextureResolution;

varying vec2 texcoord;        // the texture coordinate of the current pixel
varying vec4 vPosition;       // the screen position of the current pixel
varying mat4 gbufferModelViewInverse;
varying mat4 gbufferProjectionInverse;

// uniform variables are given in the java code
uniform sampler2D texture;    // represents what's currently displayed on the screen
uniform int iTime;
uniform int radius;   // the radius of the sphere
uniform vec3 playerPosition; // the position of the camera
uniform vec3 center; // the center position of this entity

float rand(vec2 co) {
   return fract(sin(dot(co.xy, vec2(12.9898,78.233))) * 43758.5453);
}

vec3 clipToView(in vec2 uvCoord, in float depth) {
    vec4 viewPos = gbufferProjectionInverse * vec4(vec3(uvCoord, depth) * 2.0 - 1.0, 1.0);
    viewPos /= viewPos.w;

    return viewPos.xyz;
}

vec3 viewToWorld(in vec3 view) {
    vec4 worldPos = gbufferModelViewInverse * vec4(view, 1.0);
    worldPos /= worldPos.w;

    return worldPos.xyz;
}

float noise3D(in vec3 uvCoord) {
    float p = floor(uvCoord.z);
    float f = uvCoord.z - p;

    vec2 noise = texture2D(texture, fract(uvCoord.xy * noiseTextureResolutionInverse + (p * 17.0 / noiseTextureResolution))).xy;

    return mix(noise.x, noise.y, f);
}

float cloudFunction(in vec3 world) {
  float layeredNoise = 0.0;

    float weight = 1.0;

    for(int i = 0; i < totalLayers; i++) {
        layeredNoise += noise3D(world) * weight;

        world *= 2.0;
        weight *= 0.5;
    }

    return layeredNoise;
}

float bayer2(vec2 a){
    a = floor(a);
    return fract( dot(a, vec2(.5, a.y * .75)) );
}
  #define bayer4(a)   (bayer2( .5*(a))*.25+bayer2(a))
  #define bayer8(a)   (bayer4( .5*(a))*.25+bayer2(a))
  #define bayer16(a)  (bayer8( .5*(a))*.25+bayer2(a))
  #define bayer32(a)  (bayer16(.5*(a))*.25+bayer2(a))
  #define bayer64(a)  (bayer32(.5*(a))*.25+bayer2(a))
  #define bayer128(a) (bayer64(.5*(a))*.25+bayer2(a))

bool intersectSphere(in vec3 rayOrigin, in vec3 rayDirection, in vec3 sphereOrigin, in float sphereRadius, inout vec3 intersection0, inout vec3 intersection1) {
  vec3 L = sphereOrigin - rayOrigin;

  //if(sdfSphere(sphereOrigin, rayOrigin) < 0.0) return false;

  float tca = dot(L, rayDirection);

  if(tca < 0.0) return false;

  float d2 = dot(L, L) - tca * tca;
  float r2 = sphereRadius * sphereRadius;

  if(d2 > r2) return false;

  float tch = sqrt(r2 - d2);

  intersection0 = rayDirection * (tca - tch) + rayOrigin;
  intersection1 = rayDirection * (tca + tch) + rayOrigin;

  return true;
}

void main() {
    float depth = gl_FragCoord.z;
    vec3 dither = vec3(1);
    vec3 start = vec3(texcoord, 0.0);
    vec3 end = vec3(texcoord, 1.0);

    vec3 increment = (end - start) / totalSteps;
    vec3 position = increment * bayer16(gl_FragCoord.xy) + start;

    float incrementSize = length(increment);

    vec3 intersection0, intersection1 = vec3(0.0);

    bool isIntersecting = intersectSphere(start, normalize(start), vec3(0.5), 0.1, intersection0, intersection1);

    gl_FragColor = vec4(vec3(distance(intersection0, intersection1)), 1.0);
}
