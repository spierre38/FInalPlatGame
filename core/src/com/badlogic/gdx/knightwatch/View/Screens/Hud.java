
package com.badlogic.gdx.knightwatch.View.Screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 *
 * @author ecloaner
 */
public class Hud {
    public Stage stage;
    private Viewport gameViewPort;
    
    Label levelLabel;
    Label worldLabel;
    Label livesLabel;
    
    public Hud(SpriteBatch sb){
        
        gameViewPort =  new ScreenViewport(new OrthographicCamera());
        stage = new Stage(gameViewPort, sb);
        
        Table table = new Table();
        
        table.top();
     }
    
    
    
}
