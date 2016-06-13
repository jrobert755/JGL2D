package tech.kodiko.jgl2d.examples;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import java.io.IOException;

import org.lwjgl.glfw.GLFW;

import tech.kodiko.jgl2d.LWJGLApplication;
import tech.kodiko.jgl2d.event.GLFWEventHandler;
import tech.kodiko.jgl2d.graphics.Batch2DRenderer;
import tech.kodiko.jgl2d.graphics.RenderArea;
import tech.kodiko.jgl2d.graphics.Sprite;
import tech.kodiko.jgl2d.graphics.Texture;
import tech.kodiko.jgl2d.graphics.tile.TileMap;
import tech.kodiko.jgl2d.graphics.window.Window;
import tech.kodiko.jgl2d.graphics.window.WindowManager;
import tech.kodiko.jgl2d.math.Vector2;
import tech.kodiko.jgl2d.program.GLSLProgram;
import tech.kodiko.jgl2d.util.ImageLoader;
import tech.kodiko.jgl2d.util.ResourceLoader;

public class ExampleApp extends LWJGLApplication {
	private Texture testingTexture;
	private Batch2DRenderer renderer;
	private GLSLProgram program;
	
	@Override
	public boolean init() {
		//Window window = WindowManager.createWindow("Hello!", 600, 600);
		Window window = WindowManager.createWindow("Hello!", 600, 600, 0, false, 4);
		
		String vertexSource;
		String fragmentSource;
		try {
			vertexSource = ResourceLoader.getResourceFileContentsAsString("defaultVertexShader.shader");
			fragmentSource = ResourceLoader.getResourceFileContentsAsString("defaultFragmentShader.shader");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		program = new GLSLProgram(vertexSource, fragmentSource);
		
		renderer = new Batch2DRenderer(program);
		
		window.setRenderer(renderer);
		
		testingTexture = ImageLoader.loadTexture(ResourceLoader.getResourceInputStream("textures/uvgrid.png"));
		
		Sprite testSprite1 = new Sprite(testingTexture, 200, 200, 300, 300, 512, 512);
		Sprite testSprite2 = new Sprite(testingTexture, 64, 64, 64, 64, 50, 50, 64, 64);
		
		renderer.addRenderable(testSprite1);
		renderer.addRenderable(testSprite2);
		
		GLFWEventHandler.addEventHandler(new ExampleKeyHandler());
		//GLFWEventHandler.addEventHandler(new ExampleMouseButtonHandler());
		GLFWEventHandler.addEventHandler(new ExampleMouseHandler(renderer, GLFW.GLFW_MOUSE_BUTTON_1));
		
		int data[] = new int[4096];
		for(int i = 0; i < 4096; i++){
			data[i] = 0x00FF0080;
		}
		
		Texture alphaTexture = new Texture(data, 64, 64);
		Sprite testAlphaSprite = new Sprite(alphaTexture, 220, 220);
		testAlphaSprite.setRotationDirection(true);
		testAlphaSprite.setRotationDegrees(30f);
		renderer.addRenderable(testAlphaSprite);
		
		TileMap tmn = TileMap.loadTileMap("mono");
		RenderArea renderArea = new RenderArea(program, 100, 100);
		renderArea.addRenderable(tmn);
		renderer.addRenderable(renderArea);
		
		return true;
	}
	
	Vector2 cam = new Vector2();

	@Override
	public void loopFunction() {
		cam.add(0.05f, 0.05f);
		glfwPollEvents();
		WindowManager.closeWindows();
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		new ExampleApp().run(true);
	}

	@Override
	public void destroy() {
		testingTexture.destroy();
		renderer.destroy();
		program.deleteProgram();
	}
}
