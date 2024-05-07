
package com.badlogic.gdx.knightwatch.Model.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.knightwatch.View.Windows.GameWindow;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.FixtureDef;


public class Ladder extends TileChangeable {
    
    public Ladder(GameWindow window, Rectangle tile, FixtureDef fixtureDef) {
        
        super(window, tile);
        fixture.setUserData(this);
        //setCategoryFilteR(MainGame.INTERACTABLE_ID);
        
        
    }

    @Override
    public void handHit() {
        Gdx.app.log("hand:", "TOUCHING Ladder");
    }

    @Override
    public void onHitWithSword() {
        Gdx.app.log("sword:", "HITTING WALL");
    }
    
}
