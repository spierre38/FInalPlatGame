package com.badlogic.gdx.knightwatch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.knightwatch.View.Windows.MenuWindow;

// https://www.youtube.com/watch?v=MsDENED-sZw&ab_channel=SoftwareCentral

public class MainGame extends Game {
        
    public static AssetManager assetManager;

    
        public SpriteBatch batch;
        
	public BitmapFont font;
        
        
	
	@Override
	public void create () {
            
        assetManager = new AssetManager();
        
        assetManager.load("sounds/footsteps.wav", Music.class);
//        assetManager.load("sounds/birdbg.mp3", Music.class);
//        assetManager.load("sounds/click.mp3", Music.class);
        assetManager.load("sounds/filler.mp3", Music.class);
//        assetManager.load("sounds/hurt.wav", Sound.class);
        assetManager.load("sounds/jump.wav", Music.class);
//       assetManager.load("sounds/attack.wav", Sound.class);
       assetManager.load("sounds/swordslide.wav", Music.class);
      
        
        assetManager.finishLoading();             
        batch = new SpriteBatch();
        font = new BitmapFont(); 
        setScreen(new MenuWindow(this));
	}

	@Override 
	public void render () {
            super.render();
        }
	
        @Override
        public void dispose() {
            batch.dispose();
            font.dispose();
         }

}
