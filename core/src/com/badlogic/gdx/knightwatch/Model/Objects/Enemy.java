package com.badlogic.gdx.knightwatch.Model.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.knightwatch.Model.Objects.Entity;
import com.badlogic.gdx.knightwatch.View.Windows.GameWindow;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Sprite implements Entity {
    
    protected World world;
    protected GameWindow win;
    public Body enemyBody;
    public Vector2 velocity;
    
    
    public Enemy(GameWindow win, float x, float y) {
        this.win = win;
        this.world = win.getWorld();
        setPosition(x, y);
        initEnemy(); 
        velocity = new Vector2(1,0);
        }
    
    protected abstract void initEnemy();
    public abstract void update(float dt);

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public GameWindow getGameWindow() {
        return win;
    }
    
}