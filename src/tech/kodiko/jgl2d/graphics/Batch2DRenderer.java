package tech.kodiko.jgl2d.graphics;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import tech.kodiko.jgl2d.exception.UninitializedException;
import tech.kodiko.jgl2d.math.Vector2;
import tech.kodiko.jgl2d.math.Vector4;
import tech.kodiko.jgl2d.program.GLSLProgram;

public class Batch2DRenderer extends Renderer{
	private int vaoHandle;
	private int vboHandle;
	private GLSLProgram program;
	private Vector2 camera;
	private Vector4 clearColor;
	private float[] data;
	
	public Batch2DRenderer(GLSLProgram program){
		super();
		this.vaoHandle = -1;
		this.vboHandle = -1;
		this.program = program;
		this.data = null;
	}
	
	@Override
	public void init(int width, int height){
		if(this.isInitialized()) return;
		program.init();
		if(this.vaoHandle != -1 || this.vboHandle != -1) return;
		int vao_handles[] = new int[1];
		GL30.glGenVertexArrays(vao_handles);
		this.vaoHandle = vao_handles[0];
		GL30.glBindVertexArray(this.vaoHandle);
		
		this.vboHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboHandle);
		
		//2 for vertex, 2 for uv, 4 for override color
		//int numberOfItems = 2 + 2 + 4;
		//2 for vertex, 2 for uv, 4 for override color, 1 for camera independence
		int numberOfItems = 2 + 2 + 4 + 1;
		int stride = numberOfItems * 4;
		
		/*
		//Vertex Coordinate
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, stride, 0l);
		//UV Coordinate
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, stride, 2 * 4);
		//Override color
		GL20.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, false, stride, 4 * 4);
		*/
		//Vertex Coordinate
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, stride, 0l);
		//UV Coordinate
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, stride, 2 * 4);
		//Override color
		GL20.glVertexAttribPointer(2, 4, GL11.GL_FLOAT, false, stride, 4 * 4);
		//Camera Independent
		GL20.glVertexAttribPointer(3, 1, GL11.GL_FLOAT, false, stride, 8 * 4);
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL30.glBindVertexArray(0);
		
		//Alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL20.glBlendEquationSeparate(GL14.GL_FUNC_ADD, GL14.GL_FUNC_ADD);
		GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		
		//Multisampling
		GL11.glEnable( GL13.GL_MULTISAMPLE );
		
		this.camera = new Vector2();
		this.program.setOrthoCamera(width, height);
		this.program.setCameraPosition(this.camera);
		this.setClearColor(1f, 0f, 0f, 1f);
		super.init(width, height);
	}
	
	@Override
	public void render(){
		synchronized(this){
			if(this.vaoHandle == -1 || this.vboHandle == -1) throw new UninitializedException("Batch renderer uninitialized!");
			GL11.glClearColor(this.clearColor.getX(), this.clearColor.getY(), this.clearColor.getZ(), this.clearColor.getW());
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			
			if(renderables.size() == 0) return;
			
			ArrayList<Float> values = new ArrayList<Float>();
			
			Object[] ren = this.renderables.toArray();
			for(int i = 0; i < ren.length; i++){
				Renderable r = (Renderable) ren[i];
				if(r instanceof RenderArea){
					((RenderArea)r).renderToTexture();
				}
				float data[] = r.getData();
				for(int j = 0; j < data.length; j++) values.add(data[j]);
			}
			
			boolean needsUpdate = false;
			if(this.data == null || values.size() != this.data.length){
				this.data = new float[values.size()];
				needsUpdate = true;
			}
			
			for(int i = 0; i < values.size(); i++){
				if(!needsUpdate && this.data[i] != values.get(i)){
					needsUpdate = true;
				}
				this.data[i] = values.get(i);
			}
			
			program.begin();
			this.program.setCameraPosition(this.camera);
			program.setSampler(0);
			
			GL30.glBindVertexArray(this.vaoHandle);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboHandle);
			
			if(needsUpdate) GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_DYNAMIC_DRAW);
			
			int location = 0;
			for(int i = 0; i < this.renderables.size(); i++){
				//Renderable r = (Renderable) ren[i];
				Renderable r = this.renderables.get(i);
				r.render(location);
				location += r.vertexCount();
			}
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
			
			program.end();
		}
	}
	
	@Override
	public void destroy(){
		GL15.glDeleteBuffers(this.vboHandle);
		GL30.glDeleteVertexArrays(this.vaoHandle);
		this.vaoHandle = -1;
		this.vboHandle = -1;
		super.destroy();
	}
	
	public GLSLProgram getProgram(){
		return this.program;
	}
	
	public void setClearColor(float red, float green, float blue, float alpha){
		//GL11.glClearColor(red, green, blue, alpha);
		this.clearColor = new Vector4(red, green, blue, alpha);
	}
	
	public void setCamera(Vector2 camera){
		this.camera = camera;
		/*synchronized(this.program){
			this.program.setCameraPosition(this.camera);
		}*/
	}

	public int getVaoHandle() {
		return vaoHandle;
	}

	public int getVboHandle() {
		return vboHandle;
	}
}
