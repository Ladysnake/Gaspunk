#version 120
//#extension GL_EXT_gpu_shader4 : require

#define totalSteps 4
#define totalLayers 4
#define noiseTextureResolution 512

const float noiseTextureResolutionInverse = 1 / noiseTextureResolution;

varying vec2 texcoord;        // the texture coordinate of the current pixel
varying vec4 vPosition;       // the screen position of the current pixel

// uniform variables are given in the java code
uniform sampler2D texture;    // represents what's currently displayed on the screen
uniform int iTime;      // current system time in milliseconds
uniform vec4 gasColor; // the color of the gas


vec3 hash33(in vec3 pos) {
    pos  = fract(pos * vec3(0.1031, 0.1030, 0.0973));
    pos += dot(pos, pos.yzx + 19.19);
    return fract((pos.xxy + pos.yxx) * pos.zyx);
}

float simplex3D(vec3 p) {
    vec3 s = floor(p + dot(p, vec3(0.3333333)));
    vec3 x = p - s + dot(s, vec3(0.1666666));

    vec3 e = step(x.yzx, x.xyz);
    e -= e.zxy;

    vec3 i1 = clamp(e   ,0.,1.);
    vec3 i2 = clamp(e+1.,0.,1.);

    vec3 x1 = x - i1 + .1666666;
    vec3 x2 = x - i2 + .3333333;
    vec3 x3 = x - .5;

    vec4 w = vec4(
      dot(x , x ),
      dot(x1, x1),
      dot(x2, x2),
      dot(x3, x3));

    w = clamp(.6 - w,0.,1.);
    w *= w;
    w *= w;

    vec4 d=vec4(
      dot(hash33(s     )-.5, x ),
      dot(hash33(s + i1)-.5, x1),
      dot(hash33(s + i2)-.5, x2),
      dot(hash33(s + 1.)-.5, x3));

    return dot(d, w*52.);
  }

float cloudFunction(in vec3 world) {
  world *= 4;
  float layeredNoise = 0.0;

  float weight = 1.0;

  const vec3 movementDirection = vec3(1.0) * 0.05;
  vec3 movement = movementDirection * iTime * 0.01;

  const float rotationAmount = 0.7;
  const mat2 rotation = mat2(cos(rotationAmount), -sin(rotationAmount), sin(rotationAmount), cos(rotationAmount));

  for(int i = 0; i < totalLayers; i++) {
      layeredNoise += (simplex3D(world + movement) * 0.5 + 0.5) * weight;

      world *= 2.0;
      weight *= 0.5;
      movement *= 2.0;

      world.xy *= rotation;
      world.yz *= rotation;
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

void main() {
    float depth = gl_FragCoord.z;
    vec3 dither = vec3(1);
    vec3 start = vec3(texcoord, 0.0);
    vec3 end = vec3(texcoord, 1.0);

    vec3 intersection0, intersection1 = vec3(0.0);

    // bool isIntersecting = intersectSphere(start, normalize(vec3(0,0,1)), vec3(0.5, 0.5, 0.5), 0.5, intersection0, intersection1);


    // if(intersection0.z < intersection1.z)
    //   start = intersection0, end = intersection1;
    // else
    //   start = intersection1, end = intersection0;

    // start = viewToWorld(clipToView(start.xy, start.z));
    // end = viewToWorld(clipToView(end.xy, end.z));

    vec3 increment = (end - start) / totalSteps;
    vec3 position = increment * bayer16(gl_FragCoord.xy) + start;

    float incrementSize = length(increment);

    float density = 0.0;
    for(int i = 0; i < totalSteps; i++, position += increment) {
      density += cloudFunction(position) * incrementSize * 0.5;
    }
    gl_FragColor = vec4(gasColor.rgb, gasColor.a * density);
    // gl_FragColor = texture2D(texture, texcoord);
    // gl_FragColor = vec4(vec3(distance(intersection0, intersection1)), 1.0);
}
