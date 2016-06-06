package com.tangledworlds.jgl2d.shader;

import static org.lwjgl.opengl.GL11.GL_FALSE;

import org.lwjgl.opengl.GL20;

import com.tangledworlds.jgl2d.GLObject;
import com.tangledworlds.jgl2d.exception.ShaderCompileException;

public class Shader extends GLObject{
	private String source;
	private int type;
	
	public Shader(String source, int shaderType){
		super();
		this.source = source;
		this.type = shaderType;
	}
	
	public void compile(){
		if(this.handle != -1) return;
		int shaderNumber = GL20.glCreateShader(this.type);
		GL20.glShaderSource(shaderNumber, this.source);
		GL20.glCompileShader(shaderNumber);
		int status[] = new int[1];
		GL20.glGetShaderiv(shaderNumber, GL20.GL_COMPILE_STATUS, status);
		if(status[0] == GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderNumber));
			GL20.glDeleteShader(shaderNumber);
			throw new ShaderCompileException("Unable to compile shader!");
		}
		
		this.handle = shaderNumber;
	}
	
	public void delete(){
		if(this.handle == -1) return;
		GL20.glDeleteShader(this.handle);
		this.handle = -1;
	}
	
	public boolean isVertexShader(){
		if(this.type == GL20.GL_VERTEX_SHADER) return true;
		else return false;
	}
	
	public boolean isFragmentShader(){
		if(this.type == GL20.GL_FRAGMENT_SHADER) return true;
		else return false;
	}
	
	public static Shader createVertexShader(String source){
		return new Shader(source, GL20.GL_VERTEX_SHADER);
	}
	
	public static Shader createFragmentShader(String source){
		return new Shader(source, GL20.GL_FRAGMENT_SHADER);
	}
}
