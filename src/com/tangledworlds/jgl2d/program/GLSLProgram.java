package com.tangledworlds.jgl2d.program;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.tangledworlds.jgl2d.GLObject;
import com.tangledworlds.jgl2d.exception.LocationNotBoundException;
import com.tangledworlds.jgl2d.exception.ProgramLinkException;
import com.tangledworlds.jgl2d.math.Matrix44;
import com.tangledworlds.jgl2d.math.Vector2;
import com.tangledworlds.jgl2d.math.Vector3;
import com.tangledworlds.jgl2d.math.Vector4;
import com.tangledworlds.jgl2d.shader.Shader;

public class GLSLProgram extends GLObject{
	private Shader vertexShader;
	private Shader fragmentShader;
	//private int programNumber;
	private HashMap<String, Integer> attributeLocations;
	private String cameraName, cameraPositionName, samplerName;
	
	public GLSLProgram(String vertexSource, String fragmentSource){
		super();
		this.vertexShader = Shader.createVertexShader(vertexSource);
		this.fragmentShader = Shader.createFragmentShader(fragmentSource);
		//this.programNumber = -1;
		this.attributeLocations = new HashMap<String, Integer>();
		this.cameraName = "camera";
		this.cameraPositionName = "cameraPosition";
		this.samplerName = "tex1";
	}
	
	public void init(){
		//if(this.programNumber != -1) return;
		if(this.handle != -1) return;
		this.vertexShader.compile();
		this.fragmentShader.compile();
		
		
		this.compileProgram();
		
		/*this.setSamplerName("tex1");
		this.setCameraName("orthoCamera");
		this.setCameraPositionName("cameraLocation");*/
	}
	
	private void compileProgram(){
		/*this.programNumber = GL20.glCreateProgram();
        GL20.glAttachShader(programNumber, vertexShader.getHandle());
        GL20.glAttachShader(programNumber, fragmentShader.getHandle());
        
        GL20.glLinkProgram(programNumber);
        
        int status[] = new int[1];
		GL20.glGetProgramiv(programNumber, GL20.GL_LINK_STATUS, status);
		if(status[0] == GL11.GL_FALSE){
			System.out.println(GL20.glGetProgramInfoLog(programNumber));
			
			GL20.glDeleteProgram(programNumber);
			throw new ProgramLinkException("Unable to link program!");
		}
		
		GL20.glDetachShader(programNumber, vertexShader.getHandle());
		GL20.glDetachShader(programNumber, fragmentShader.getHandle());*/
		this.handle = GL20.glCreateProgram();
        GL20.glAttachShader(this.handle, vertexShader.getHandle());
        GL20.glAttachShader(this.handle, fragmentShader.getHandle());
        
        GL20.glLinkProgram(this.handle);
        
        int status[] = new int[1];
		GL20.glGetProgramiv(this.handle, GL20.GL_LINK_STATUS, status);
		if(status[0] == GL11.GL_FALSE){
			System.out.println(GL20.glGetProgramInfoLog(this.handle));
			
			GL20.glDeleteProgram(this.handle);
			throw new ProgramLinkException("Unable to link program!");
		}
		
		GL20.glDetachShader(this.handle, vertexShader.getHandle());
		GL20.glDetachShader(this.handle, fragmentShader.getHandle());
	}
	
	public void deleteProgram(){
		/*GL20.glDeleteProgram(this.programNumber);
		this.vertexShader.delete();
		this.fragmentShader.delete();
		this.programNumber = -1;*/
		GL20.glDeleteProgram(this.handle);
		this.vertexShader.delete();
		this.fragmentShader.delete();
		super.destroy();
	}
	
	public void begin(){
		//GL20.glUseProgram(this.programNumber);
		GL20.glUseProgram(this.handle);
	}
	
	public void end(){
		GL20.glUseProgram(0);
	}

	public int getProgramNumber() {
		return this.getHandle();
	}
	
	public void checkLocation(int location){
		if(location == -1){
			throw new LocationNotBoundException("Location not bound!");
		}
	}
	
	public int getUniformLocation(String name){
		if(this.attributeLocations.containsKey(name)) return this.attributeLocations.get(name);
		
		//int location = GL20.glGetUniformLocation(this.programNumber, name);
		int location = GL20.glGetUniformLocation(this.handle, name);
		this.attributeLocations.put(name, location);
		return location;
	}
	
	public void setUniform1(String name, int value){
		int location = this.getUniformLocation(name);
		this.checkLocation(location);
		
		GL20.glUniform1i(location, value);
	}
	
	public void setUniformVector2(String name, Vector2 vector){
		int location = this.getUniformLocation(name);
		this.checkLocation(location);
		
		float value[] = vector.getXY();
		GL20.glUniform2fv(location, value);
	}
	
	public void setUniformVector3(String name, Vector3 vector){
		int location = this.getUniformLocation(name);
		this.checkLocation(location);
		
		GL20.glUniform3fv(location, vector.getXYZ());
	}
	
	public void setUniformVector4(String name, Vector4 vector){
		int location = this.getUniformLocation(name);
		this.checkLocation(location);
		
		GL20.glUniform3fv(location, vector.getXYZW());
	}
	
	public void setUniformMatrix4x4(String name, Matrix44 matrix){
		int location = this.getUniformLocation(name);
		this.checkLocation(location);
		
		GL20.glUniformMatrix4fv(location, false, matrix.getData());
	}
	
	public void setUniformObject(String name, Object uniform, boolean end){
		this.begin();
		if(uniform instanceof Vector4) this.setUniformVector4(name, (Vector4)uniform);
		else if(uniform instanceof Vector3) this.setUniformVector3(name, (Vector3)uniform);
		else if(uniform instanceof Vector2) this.setUniformVector2(name, (Vector2)uniform);
		else if(uniform instanceof Matrix44) this.setUniformMatrix4x4(name, (Matrix44)uniform);
		else if(uniform instanceof Integer) this.setUniform1(name, (Integer)uniform);
		if(end) this.end();
	}
	
	public void setUniformObject(String name, Object uniform){
		this.setUniformObject(name, uniform, true);
	}
	
	public void setOrthoCamera(int width, int height){
		this.setUniformObject(this.cameraName, Matrix44.getOrthogonal(width, height));
	}
	
	public void setCameraPosition(Vector2 position){
		this.setUniformObject(this.cameraPositionName, position);
	}
	
	public void setSampler(int sampler){
		//this.setUniform1(this.samplerName, sampler);
		this.setUniformObject(this.samplerName, new Integer(sampler), false);
	}
}
