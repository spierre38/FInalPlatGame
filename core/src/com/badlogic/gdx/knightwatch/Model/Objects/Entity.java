package com.badlogic.gdx.knightwatch.Model.Objects;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.knightwatch.View.Windows.GameWindow;

public interface Entity {
    
    World getWorld();
    GameWindow getGameWindow();
    void setPosition(float x, float y);
    
}
