package tech.kodiko.jgl2d.graphics;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class Renderer {
	protected ArrayList<Renderable> renderables;
	protected boolean initialized;
	protected Comparator<Renderable> compare = new Comparator<Renderable>(){
		@Override
		public int compare(Renderable arg0, Renderable arg1) {
			int oneZ = arg0.getZ(), twoZ = arg1.getZ();
			if(oneZ < twoZ) return -1;
			else if(oneZ == twoZ) return 0;
			else return 1;
		}
	};
	
	public Renderer(){
		this.renderables = new ArrayList<Renderable>();
		this.initialized = false;
	}
	
	public void addRenderable(Renderable r){
		synchronized(this.renderables){
			if(this.renderables.size() == 0){
				this.renderables.add(r);
				return;
			}
			
			
			for(int i = 0; i < this.renderables.size(); i++){
				Renderable toCompare = this.renderables.get(i);
				if(compare.compare(toCompare, r) == 1){
					this.renderables.add(i, r);
					return;
				}
			}
			
			this.renderables.add(r);
			//if(this.renderables.size() >= 2) this.sortRenderables();
		}
	}
	
	public void removeRenderable(Renderable r){
		synchronized(this.renderables){
			this.renderables.remove(r);
		}
	}
	
	public void init(int width, int height){
		this.initialized = true;
	}
	
	public boolean isInitialized(){
		return this.initialized;
	}
	
	public abstract void render();
	
	public void destroy(){
		this.initialized = false;
	}
	
	public void sortRenderables(){
		synchronized(this.renderables){
			this.renderables.sort(new Comparator<Renderable>(){
				@Override
				public int compare(Renderable arg0, Renderable arg1) {
					int oneZ = arg0.getZ(), twoZ = arg1.getZ();
					if(oneZ < twoZ) return -1;
					else if(oneZ == twoZ) return 0;
					else return 1;
				}
			});
		}
	}
}
