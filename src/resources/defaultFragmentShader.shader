#version 330

out vec4 colorOut;

in vec2 uvCoord;
in vec4 overrideColor;
uniform sampler2D tex1;
 
void main()
{
    colorOut = texture2D(tex1, uvCoord);
    if(overrideColor.w > 0.0)
    	colorOut = mix(colorOut, overrideColor, vec4(1.0, 1.0, 1.0, 0.0));
    //colorOut = overrideColor;
    //colorOut = vec4(0.0, overrideColor.w, 0.0, 1.0);
}