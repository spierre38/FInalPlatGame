
package com.badlogic.gdx.knightwatch.Model.Objects;

import com.badlogic.gdx.knightwatch.MainGame;
import com.badlogic.gdx.knightwatch.Model.Helper.CONSTANTS;
import com.badlogic.gdx.knightwatch.View.Windows.GameWindow;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author ecloaner
 */

//https://www.youtube.com/watch?v=43DrvCp9-is&ab_channel=SmallPixelGames

public abstract class TileChangeable {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile mapTile;
    protected Rectangle tile;
    protected Body body;
    protected Fixture fixture;
            
    public TileChangeable(GameWindow window, Rectangle tile){
        
        this.world = window.getWorld();
        this.map = window.getMap();
        this.tile = tile;
        
        BodyDef tileBody = new BodyDef();
        FixtureDef tileFixture = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        
        tileBody.type = BodyDef.BodyType.StaticBody;
        tileBody.position.set((tile.getX() + tile.getWidth() / 2) / CONSTANTS.PPM , (tile.getY() + tile.getHeight() / 2) / CONSTANTS.PPM); 
        body = world.createBody(tileBody);

        shape.setAsBox(tile.getWidth() / 2 / CONSTANTS.PPM , tile.getHeight() / 2 / CONSTANTS.PPM);
        tileFixture.shape = shape;
        fixture = body.createFixture(tileFixture);  
        
        
        
        
        
        
    }
    
    protected void setBodyType(BodyDef.BodyType bodyType) {
        body.setType(bodyType);
    }
    protected void setFixtureBodyType(BodyDef.BodyType bodyType) {
        fixture.getBody().setType(bodyType);
    }
    
    public abstract void handHit();
    
    public void setCategoryFilteR(short filterBit){
       Filter f = new Filter() ;
       f.categoryBits = filterBit;
       fixture.setFilterData(f);
       
    }
    
    public abstract void onHitWithSword();

    ////////////https://gamedev.stackexchange.com/questions/74821/libgdx-how-do-you-remove-a-cell-from-tiledmap
    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer lr = (TiledMapTileLayer) map.getLayers().get(4);
        float tileSize = lr.getTileWidth(); 
        int cellX = (int) (body.getPosition().x * CONSTANTS.PPM / tileSize);
        int cellY = (int) (body.getPosition().y * CONSTANTS.PPM / tileSize);
        return lr.getCell(cellX, cellY);
    }
  
}
