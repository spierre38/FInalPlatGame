package com.badlogic.gdx.knightwatch.View.Windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.knightwatch.MainGame;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 *
 * @author ecloaner
 */
public class MenuWindow implements Screen {
    
    private MainGame game;
    private Texture backgroundTexture;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    
    public MenuWindow(MainGame game){
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Load the background texture
        backgroundTexture = new Texture("Designer.png");

        batch = new SpriteBatch();
    }
    
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Draw the background texture
        batch.draw(backgroundTexture, 0, 0, 800, 480);
        
        // Draw the text on top of the background
        game.font.draw(batch, "Welcome to Drop!!! ", 100, 150);
        game.font.draw(batch, "Tap anywhere to begin!", 100, 100);
        batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameWindow(game));
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        batch.dispose();
    }
}
