package tech.kodiko.jgl2d;

import tech.kodiko.jgl2d.event.GLFWEventHandler;
import tech.kodiko.jgl2d.exception.InitializationException;
import tech.kodiko.jgl2d.graphics.window.WindowManager;

/**
 * A base class for applications to inherit from.
 * <p>
 * Implements a standard {@link #run(boolean)} function that will repeatedly call the {@link #loopFunction()};
 * 
 * @author Jean-Luc Roberts
 *
 */
public abstract class LWJGLApplication {
	/**
	 * Called to initialize the application.
	 * 
	 * @return Whether or not the application was able
	 * to be initialized.
	 */
	public abstract boolean init();
	/**
	 * Called when the program is to end.
	 */
	public abstract void destroy();
	/**
	 * The function that is repeatedly called. This means
	 * that it does not need to loop inside of the function!
	 */
	public abstract void loopFunction();
	/**
	 * A default run implementation, which may be overwritten. This
	 * will perform the following actions:
	 * <ul>
	 * <li>initialize the {@link tech.kodiko.jgl2d.graphics.window.WindowManager}</li>
	 * <li>initialize this application</li>
	 * <li>Create a separate event thread if requested</li>
	 * <li>enter a loop which calls the {@link #loopFunction()}</li>
	 * <li>terminate the threads created, if any</li>
	 * <li>clean up the {@link tech.kodiko.jgl2d.graphics.window.WindowManager}</li>
	 * </ul>
	 * 
	 * @param multithreadedEvents Whether or not to put event processing it is own thread.
	 */
	public void run(boolean multithreadedEvents){
		WindowManager.initialize();
		try {
			if(!init()) throw new InitializationException("Unable to initialize app!");
			GLFWEventHandler handler = null;
			Thread eventThread = null;
			if(multithreadedEvents){
				handler = new GLFWEventHandler();
				eventThread = new Thread(handler);
				eventThread.start();
			}
			while(WindowManager.getWindowCount() > 0){
				loopFunction();
			}
			if(multithreadedEvents){
				handler.end();
				try {
					eventThread.join();
				} catch (InterruptedException e) {
					
				}
			}
		} finally {
			WindowManager.terminate();
		}
	}
	
	/**
	 * A basic run method which calls the main run implementation with events being
	 * processed on the same thread.
	 */
	public void run(){
		this.run(false);
	}
}
