/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.gdx.knightwatch.Model.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.knightwatch.MainGame;
import com.badlogic.gdx.knightwatch.Model.Helper.CONSTANTS;
import com.badlogic.gdx.knightwatch.View.Windows.GameWindow;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 *
 * @author ecloaner
 */
public class Crate extends TileChangeable {
    private boolean isVisible;

    public Crate(GameWindow window, Rectangle tile) {
        
        super(window, tile);
        fixture.setUserData(this);
        setCategoryFilteR(CONSTANTS.CRATE_ID);
        setFixtureBodyType(BodyDef.BodyType.StaticBody); 
        isVisible = true;
    }


    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void destroy() {
        world.destroyBody(body);
        setVisible(false);
    }



    @Override
    public void handHit() {
        setVisible(false);

    }

    @Override
    public void onHitWithSword(){
        setCategoryFilteR(CONSTANTS.DESTROYEDITEM_ID);
        removeCell();
        
    }
    
    private void removeCell() {
    TiledMapTileLayer lr = (TiledMapTileLayer) map.getLayers().get(4);
    float tileSize = lr.getTileWidth();
    
    int cellX = (int) ((body.getPosition().x * CONSTANTS.PPM) / tileSize);
    int cellY = (int) ((body.getPosition().y * CONSTANTS.PPM) / tileSize);

    cellX = MathUtils.clamp(cellX, 0, lr.getWidth() - 1);
    cellY = MathUtils.clamp(cellY, 0, lr.getHeight() - 1);

    lr.setCell(cellX, cellY, null);
    
    ///https://gamedev.stackexchange.com/questions/74821/libgdx-how-do-you-remove-a-cell-from-tiledmap
}
    
}
