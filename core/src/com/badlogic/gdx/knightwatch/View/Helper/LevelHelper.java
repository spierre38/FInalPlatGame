/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badlogic.gdx.knightwatch.View.Helper;

import com.badlogic.gdx.knightwatch.MainGame;
import com.badlogic.gdx.knightwatch.Model.Helper.CONSTANTS;
import com.badlogic.gdx.knightwatch.Model.Objects.Crate;
import com.badlogic.gdx.knightwatch.Model.Objects.Ladder;
import com.badlogic.gdx.knightwatch.Model.Objects.Minion;
import com.badlogic.gdx.knightwatch.Model.Objects.Poles;
import com.badlogic.gdx.knightwatch.View.Windows.GameWindow;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

//level parser tutuorial 
//https://www.youtube.com/watch?v=43DrvCp9-is&ab_channel=SmallPixelGames

public class LevelHelper {
    private Array<Minion>minions;
    public LevelHelper(GameWindow window){
        
        World world = window.getWorld();
        TiledMap map = window.getMap();
        
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        
        //GROUND
        
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / CONSTANTS.PPM , (rect.getY() + rect.getHeight() / 2) / CONSTANTS.PPM); 
            body = world.createBody(bdef);
            
            shape.setAsBox(rect.getWidth() / 2 / CONSTANTS.PPM , rect.getHeight() / 2 / CONSTANTS.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = CONSTANTS.INTERACTABLE_ID;
            body.createFixture(fdef);
            
            
        }
        
        //CRATES
        
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            
            new Crate(window , rect);
            
            
        }
        
        //POLES

        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            
            new Poles(window, rect);
            
            
            
        }       
        
        //Ladder
        
        for(MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.isSensor = true; 

            fixtureDef.shape = shape;

            new Ladder(window , rect, fixtureDef);
            
            
        }        
        ///create enemies
        
        minions = new Array<Minion>();        
            for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject)object).getRectangle();
                minions.add(new Minion(window,rect.getX() / CONSTANTS.PPM,rect.getY() / CONSTANTS.PPM ));
        }
            
      
    }
    
    public Array<Minion> getMinion(){
        return minions;
    }
}