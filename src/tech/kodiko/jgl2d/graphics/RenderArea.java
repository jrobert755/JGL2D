package tech.kodiko.jgl2d.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import tech.kodiko.jgl2d.program.GLSLProgram;

public class RenderArea extends Sprite {
	private int width, height;
	private Texture fboTexture = null;
	private int fboHandle;
	private Batch2DRenderer renderer;
	private GLSLProgram program;
	private boolean initialized;
	
	public RenderArea(GLSLProgram program, int width, int height){
		super(new Texture(null, width, height));
		this.width = width;
		this.height = height;
		this.renderer = new Batch2DRenderer(program);
		this.program = program;
		this.initialized = false;
		this.setCameraIndependent(true);
	}
	
	public void initialize(){
		this.renderer.init(this.width, this.height);
		
		/*int data[] = new int[32*32];
		for(int i = 0; i < (32*32); i++){
			data[i] = 0x0000ffff;
		}
		Texture testingTexture = new Texture(data, 32, 32);
		this.renderer.addRenderable(new Sprite(testingTexture));*/
		
		this.fboHandle = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.fboHandle);
		
		this.fboTexture = new Texture(null, this.width, this.height);
		this.fboTexture.update();
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, this.fboTexture.getHandle(), 0);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
		System.out.println("Status: " + status + "\nOK Status: " + GL30.GL_FRAMEBUFFER_COMPLETE);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		
		this.renderer.setClearColor(0f, 0f, 0f, 0f);
		
		this.initialized = true;
	}
	
	public void renderToTexture(){	
		if(!this.initialized) this.initialize();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.fboHandle);
		int viewport[] = new int[4];
		GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
		GL11.glViewport(0, 0, this.width, this.height);
		this.program.setOrthoCamera(this.width, this.height);
		
		this.renderer.render();
		
		GL11.glViewport(viewport[0], viewport[1], viewport[2], viewport[3]);
		this.program.setOrthoCamera(viewport[2], viewport[3]);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	@Override
	public void render(int offset) {
		fboTexture.bind();
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, offset, 4);
		fboTexture.unbind();
	}
	
	public void addRenderable(Renderable r){
		this.renderer.addRenderable(r);
	}
	
	public void removeRenderable(Renderable r){
		this.renderer.removeRenderable(r);
	}
}
