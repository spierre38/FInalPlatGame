
package com.badlogic.gdx.knightwatch.Model.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.knightwatch.Model.Helper.CONSTANTS;
import com.badlogic.gdx.knightwatch.View.Windows.GameWindow;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author ecloaner
 */
public class Poles extends TileChangeable {
    
    public Poles(GameWindow window, Rectangle tile) {
        
        super(window, tile);
        fixture.setUserData(this);
        setCategoryFilteR(CONSTANTS.POLE_ID);
        
        
    }

    @Override
    public void handHit() {
        Gdx.app.log("hand:", "TOUCHING POLE");
    }

    @Override
    public void onHitWithSword() {
        Gdx.app.log("sword:", "HITTING WALL");
    }
    
}
