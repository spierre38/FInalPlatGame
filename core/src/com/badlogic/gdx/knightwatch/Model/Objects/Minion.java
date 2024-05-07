package com.badlogic.gdx.knightwatch.Model.Objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.knightwatch.Controller.Objects.Player;
import com.badlogic.gdx.knightwatch.Model.Helper.CONSTANTS;
import com.badlogic.gdx.knightwatch.View.Windows.GameWindow;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import static java.awt.geom.Point2D.distance;

/**
 *
 * @author ecloaner
 */
public class Minion extends Enemy implements Entity {
    
    
    private float animTimer;
    private Animation<TextureRegion> idleAni;
    private Array<TextureRegion>animations;
    private Animation<TextureRegion> deathAni;
    
    
    private boolean setToDestroy;
    private boolean destroyed;
    private boolean isDead;
    
    private float chasingRange;
    private float chasingSpeed;
    
    private Player player = Player.getInstance(win);
            
    public Minion(GameWindow win, float x, float y) {
        super(win, x, y);
        animations = new Array<TextureRegion>();
        setToDestroy = false;
        destroyed = false;
        isDead = false;
        TextureAtlas.AtlasRegion region = win.getenemyAtlas().findRegion("minion");
        Array<TextureAtlas.AtlasRegion> regions = win.getenemyAtlas().findRegions("minion");
        Array<TextureAtlas.AtlasRegion> deathRegions = win.getenemyAtlas().findRegions("minionDeath");
        Array<TextureRegion> deathFrames = new Array<>();

        for (TextureAtlas.AtlasRegion deathRegion : deathRegions) {
            deathFrames.add(new TextureRegion(deathRegion));
        }
        deathAni = new Animation<>(.51f, deathFrames);
        
        
        float frameWidth = region.getRegionWidth() / 3;
        float frameHeight = region.getRegionHeight(); 
      
        frameWidth *= 2f;
        frameHeight *= .8f;
        
        chasingRange = 1;
        chasingSpeed = 1 ;
                
     
        for (TextureAtlas.AtlasRegion atlasRegion : regions) {
            animations.add(new TextureRegion(atlasRegion));
        }
        idleAni = new Animation<TextureRegion>(.33f, animations);
        animTimer = 0;     
        setBounds(getX(), getY(), frameWidth / CONSTANTS.PPM, frameHeight / CONSTANTS.PPM);
        
    }
    
    public void update(float dt) {
        animTimer += dt;
        
        float distanceToPlayer = (float) distance(getX(), getY(), player.getX(), player.getY());
        
        if(setToDestroy && !destroyed){
            world.destroyBody(enemyBody);
            destroyed = true;
            animTimer = 0;
            
        }
        else if(!destroyed){
        if (animTimer >= idleAni.getAnimationDuration()) {
            animTimer = 0; 
        }
        enemyBody.setLinearVelocity(velocity);
        setPosition(enemyBody.getPosition().x - getWidth() / 2 , enemyBody.getPosition().y - getHeight() / 2);
        setRegion(idleAni.getKeyFrame(animTimer, true));
              if (distanceToPlayer < chasingRange) {

                  float deltaX = player.getX() - getX();
            float deltaY = player.getY() - getY();
            
            //ffrom final project in LUA
            float angleToPlayer = MathUtils.atan2(deltaY, deltaX);
            
            //From final LUA project 
            float velX = chasingSpeed * MathUtils.cos(angleToPlayer);
            enemyBody.setLinearVelocity(velX, 0);
        } else {
            enemyBody.setLinearVelocity(0, 0);
        }
        }
    }

    @Override
    protected void initEnemy() {
        BodyDef enemyDef = new BodyDef();
        enemyDef.position.set(this.getX(), this.getY());
        enemyDef.type = BodyDef.BodyType.DynamicBody;

        enemyBody = world.createBody(enemyDef);

        FixtureDef enemyFixture = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        float halfWidth = 3 / CONSTANTS.PPM;
        float halfHeight = 10 / CONSTANTS.PPM;
        shape.setAsBox(halfWidth, halfHeight);

        enemyFixture.shape = shape;
        enemyFixture.filter.categoryBits = CONSTANTS.ENEMY_ID;
        enemyFixture.filter.maskBits = CONSTANTS.GROUND_ID | CONSTANTS.POLE_ID | CONSTANTS.CRATE_ID | CONSTANTS.ENEMY_ID | CONSTANTS.INTERACTABLE_ID|CONSTANTS.PLAYER_ID ;

        enemyBody.createFixture(enemyFixture).setUserData(this);

        shape.dispose();
    }
    
    public void draw(Batch batch) {
        if (!destroyed || animTimer < deathAni.getAnimationDuration()) {
            if (isDead) {
                TextureRegion currentFrame = deathAni.getKeyFrame(animTimer, false);
                batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            } else {
                super.draw(batch);
            }
        }
    }

    public void onhitWithSword(){
        setToDestroy = true;
    }
    

}