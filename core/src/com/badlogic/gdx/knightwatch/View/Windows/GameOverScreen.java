package com.badlogic.gdx.knightwatch.View.Windows;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.knightwatch.Model.Helper.CONSTANTS;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverScreen implements Screen {

    private SpriteBatch batch;
    private BitmapFont font;
    private Viewport viewport;
    private Game game;
    
    public GameOverScreen(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
        viewport = new FitViewport(CONSTANTS.Game_WIDTH, CONSTANTS.Game_HEIGHT);
    }

    @Override
    public void show() {
        // Setup any initialization logic here
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        // Render game over message
        font.draw(batch, "Game Over", 100, 200);
        font.draw(batch, "Click To Play Again", 100, 400);
      
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        // Handle pausing logic
    }

    @Override
    public void resume() {
        // Handle resuming logic
    }

    @Override
    public void hide() {
        // Handle hiding logic
    }
    


    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
