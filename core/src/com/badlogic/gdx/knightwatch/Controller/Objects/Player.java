package com.badlogic.gdx.knightwatch.Controller.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.knightwatch.MainGame;
import com.badlogic.gdx.knightwatch.Model.Helper.CONSTANTS;
import com.badlogic.gdx.knightwatch.Model.Objects.Minion;
import com.badlogic.gdx.knightwatch.View.Screens.Hud;
import com.badlogic.gdx.knightwatch.View.Windows.GameWindow;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author ecloaner
 */
public class Player extends Sprite {
    
 
    //critical
    //speed
    
    private static Player instance = null;


   
    public enum State {
        JUMPING,
        ATTACKING,
        STANDING,
        RUNNING,
        HEALING,
        HANGING,
        CLIMBING,
        FALLING,
        DYING,
        ROLLING
    };
    
    
    /////main fields 
    public State previousState;
    public State currentState;        
    public World world;
    public Body  playerBody;
    private Hud hud;
    
    ////weapon fields
    
    private PolygonShape swordHitboxShape;
    private Fixture swordHitboxFixture;
    private boolean swordHitboxActive;
    
    
    ////////Animation textures
    
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> hangingAnimation; 
    private Animation<TextureRegion> rollAnimation;
    private Animation<TextureRegion> healingAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> fallingAttackAnimation;
    private Animation<TextureRegion> climbingAnimation;
    private Animation<TextureRegion> deathAnimation;
   
    /////STATSSSSSSSSSSSSSS
//    private float maxHealth;
//    private float currentHealth;
//    private float maxStamina;
//    private float currentStamina;
//    private float levelProgress;
//    private float strength;
//    private float knockback;
    
    //Animation Fields  ............
    
    private float healingTime;

    private static final float HEALING_DURATION = 1.0f; 

    private boolean runningRight;
    
    private float stateTime;

    private boolean jumpingRight; 
    
    private int jumpCounter;

    
    private float rollTimer;
    private final float maxRollDuration = 1.0f; 
    private boolean isDead;
    
    private float x , y , velX , velY , speed;

    
    
    private Player(GameWindow screen ){
        
        super(screen.getplayerAtlas().findRegion("standing"));
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        runningRight = true;
        jumpingRight = true; 
        GameWindow win = screen;
        stateTime = 0;
        this.speed = 4f;
        this.jumpCounter = 0;
         isDead = false;
        this.hud = hud;
        initPlayer();
        
        swordHitboxShape = new PolygonShape();
        swordHitboxShape.setAsBox(10 / CONSTANTS.PPM, 20 / CONSTANTS.PPM); // Adjust dimensions as needed
        swordHitboxActive = false;

        
        idleAnimation = new Animation<>(0.1f, screen.getplayerAtlas().findRegions("standing"));
        runAnimation = new Animation<>(0.1f, screen.getplayerAtlas().findRegions("running"));
        jumpAnimation = new Animation<>(0.1f, screen.getplayerAtlas().findRegions("jumping"));
        hangingAnimation = new Animation<>(0.1f, screen.getplayerAtlas().findRegions("hanging"));
        rollAnimation = new Animation<>(0.1f, screen.getplayerAtlas().findRegions("roll"));
        healingAnimation = new Animation<>(0.1f, screen.getplayerAtlas().findRegions("healing"));
        attackAnimation = new Animation<>(0.15f, screen.getplayerAtlas().findRegions("attack"));
        fallingAttackAnimation = new Animation<>(.68f, screen.getplayerAtlas().findRegions("fallingAttack"));
        climbingAnimation = new Animation<>(0.15f, screen.getplayerAtlas().findRegions("attack"));
        deathAnimation = new Animation<>(0.15f, screen.getplayerAtlas().findRegions("death"));

        
        setBounds(0, 0, 48 / CONSTANTS.PPM, 48 / CONSTANTS.PPM);
        
         
    }
    

    
        public static Player getInstance(GameWindow screen) {
        if (instance == null) {
            instance = new Player(screen);
        }
        return instance;
    }


    
    public void update (float dt){
        updateSwordHitbox();
        checkUserInput();

        setPosition(playerBody.getPosition().x - getWidth() / 2 , playerBody.getPosition().y - getHeight() / 2.5f);
        stateTime += dt; 
        setRegion(getFrame(dt));
        //Gdx.app.log("Player", "Current State: " + currentState + ", State Time: " + stateTime);

        
            if (currentState == State.ROLLING) {
            rollTimer += dt;
            if (rollTimer >= maxRollDuration) {
                rollTimer = 0;
                adjustHitboxForRolling(false);

                if (Math.abs(playerBody.getLinearVelocity().x) > 0) {
                    setState(State.RUNNING);
                } else {
                    setState(State.STANDING);
                }
            }
        }
            

        if (currentState == State.FALLING) {
            if (stateTime >= fallingAttackAnimation.getAnimationDuration()) {
                //.app.log("Player", "Falling Attack Animation Finished");
                setState(State.STANDING);
            }
        }


        if (currentState == State.ATTACKING) {
            if (stateTime >= attackAnimation.getAnimationDuration()) {
               // Gdx.app.log("Player", "Attack Animation Finished");
                setState(State.STANDING);
            }
        }

        
        if (currentState == State.HEALING) {
            healingTime += dt;
            if (stateTime >= HEALING_DURATION) {
                setState(State.STANDING);
            }
        }

 

   }
    
    ///////////Animation State Hnadinling
    
    private TextureRegion getFrame(float dt) {
        currentState= getState();
        
        TextureRegion region;
        
        switch (currentState) {
            
            case JUMPING:
                region =  jumpAnimation.getKeyFrame(stateTime,false);
                break;
            case RUNNING:
                region = runAnimation.getKeyFrame(stateTime, true);
                break;
            case CLIMBING:
                region = climbingAnimation.getKeyFrame(stateTime, true);
                break;
            case DYING:
                region = deathAnimation.getKeyFrame(stateTime, false);
                break;

            case ATTACKING:
                region = attackAnimation.getKeyFrame(stateTime, false);
                break;
            case FALLING:
                // Check if the player is falling and attacking to play the fallingAttack animation
                if (previousState == State.ATTACKING) {
                    region = fallingAttackAnimation.getKeyFrame(stateTime, false);
                } else {
                    region = fallingAttackAnimation.getKeyFrame(stateTime, false); 
                }
                break;
            case HANGING:
                region = hangingAnimation.getKeyFrame(stateTime, true);
                break;
            case HEALING:
                region = healingAnimation.getKeyFrame(stateTime, false);
                break;
                
            case ROLLING:
                region = rollAnimation.getKeyFrame(stateTime, true);
                break;                    
            case STANDING:
            default:
                region = idleAnimation.getKeyFrame(stateTime, true);
                break;
                
        }
       if((playerBody.getLinearVelocity().x < 0 || ! runningRight ) && !region.isFlipX()) {
           region.flip(true, false);
           runningRight = false;
            jumpingRight = false;

           
       } 
       else if((playerBody.getLinearVelocity().x > 0 || runningRight ) && region.isFlipX() ){
           region.flip(true, false);
           runningRight = true;
           jumpingRight = true;

           
       }
       stateTime = currentState == previousState ? stateTime + dt : 0;
       previousState = currentState;
       
       return region;
    }
    
        public void setState(State newState) {
        previousState = currentState;
        currentState = newState;
    }
        
        /////////////// Player States

    public State getState() {
        if (currentState == State.ROLLING) {
            return State.ROLLING;
        }
        if (currentState == State.DYING) {
            return State.DYING;
        }
        
        if (currentState == State.HANGING) {
            return State.HANGING;
        }

        if (currentState == State.HEALING) {
            return State.HEALING;
        }
        if (playerBody.getLinearVelocity().y < 0 && previousState == State.ATTACKING) {
            return State.FALLING;
        } else if (currentState == State.ATTACKING) {
            return State.ATTACKING;
        }
        if (playerBody.getLinearVelocity().y > 0 || (playerBody.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (playerBody.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }
    
    ////////////////////INPUT ANIMATION CHANGING
    
    
    /// jump 
    
    public void jump() {
    float impulseX = jumpingRight ? 1 : -1;
    playerBody.applyLinearImpulse(new Vector2(impulseX, 3.5f), playerBody.getWorldCenter(), true);
    swordHitboxActive = false;

}
    
    
    
    
    //roll 

    public void roll() {
        if (currentState != State.ROLLING) {
            setState(State.ROLLING);
            adjustHitboxForRolling(true);            
            float rollSpeed = 2.0f;
            Vector2 currentVelocity = playerBody.getLinearVelocity();
            float newVelocityX = runningRight ? rollSpeed : -rollSpeed;
            playerBody.setLinearVelocity(new Vector2(newVelocityX, currentVelocity.y ));
            swordHitboxActive = false;
       
        }
    }
                        
    ///H E A L
        public void heal() {
            if (currentState != State.HEALING) {
                setState(State.HEALING);
                stateTime = 0; 
            swordHitboxActive = false;
               

            }
    }
    
        ///ATCK
    public void attack() {
        if (currentState != State.ATTACKING) {

            setState(State.ATTACKING);
            stateTime = 0; 
            swordHitboxActive = true;
            
        }
    }   
        /////////////////INNIT PLAYER
    
    public void initPlayer(){
        
        BodyDef playDef = new BodyDef();

        playDef.position.set(32 / CONSTANTS.PPM,32 / CONSTANTS.PPM);
        playDef.type = BodyDef.BodyType.DynamicBody;
        
        playerBody = world.createBody(playDef);  
        FixtureDef playFixture = new FixtureDef();
        
        PolygonShape shape = new PolygonShape();
        float halfWidth = 6 / CONSTANTS.PPM; 
        float halfHeight = 20 / CONSTANTS.PPM; 
        shape.setAsBox(halfWidth, halfHeight);
        
       https://www.youtube.com/watch?v=MsDENED-sZw&ab_channel=SoftwareCentral
       
        playFixture.filter.categoryBits = CONSTANTS.PLAYER_ID;
        playFixture.filter.maskBits = CONSTANTS.GROUND_ID |
                                      CONSTANTS.CRATE_ID | 
                                      CONSTANTS.POLE_ID|
                                      CONSTANTS.ENEMY_ID|
                                      CONSTANTS.INTERACTABLE_ID;
        

        
        
        playFixture.shape = shape;
        playerBody.createFixture(playFixture);
        
        Fixture playerFixture = playerBody.createFixture(playFixture);
        playerFixture.setUserData(this); 

        
        PolygonShape handSensorShape = new PolygonShape();
        handSensorShape.setAsBox(4 / CONSTANTS.PPM, 4 / CONSTANTS.PPM, new Vector2(8 / CONSTANTS.PPM, 6 / CONSTANTS.PPM), 0);
        FixtureDef handSensorFixture = new FixtureDef();
        handSensorFixture.shape = handSensorShape;
        handSensorFixture.isSensor = true;
        playerBody.createFixture(handSensorFixture).setUserData("handSensor");
 

        
    }
    
    //////////////////adjusting roll logic
    
    private void adjustHitboxForRolling(boolean isRolling) {
        Fixture fixture = playerBody.getFixtureList().first();
        PolygonShape shape = (PolygonShape) fixture.getShape();
        float halfWidth = 6 / CONSTANTS.PPM; 
        float standingHalfHeight = 20 / CONSTANTS.PPM; 
        float rollingHalfHeight = standingHalfHeight / 1.5f;

        float offsetY = 0;

        if (isRolling) {
            shape.setAsBox(halfWidth / 2, rollingHalfHeight);
            offsetY = (standingHalfHeight - rollingHalfHeight) / 2;
        } else {
            shape.setAsBox(halfWidth, standingHalfHeight);
            offsetY = (rollingHalfHeight - standingHalfHeight) / 2;
        }

        playerBody.setTransform(playerBody.getPosition().x, playerBody.getPosition().y + offsetY, playerBody.getAngle());
    }
    
    
    
    ///////////////sword hitboxes
    
    private void createSwordHitbox() {
        if (swordHitboxFixture != null) {
            playerBody.destroyFixture(swordHitboxFixture);
        }

        FixtureDef swordFixtureDef = new FixtureDef();
        swordFixtureDef.shape = swordHitboxShape;
        swordFixtureDef.isSensor = true; 
        swordHitboxFixture = playerBody.createFixture(swordFixtureDef);
        swordHitboxFixture.setUserData("swordHitbox"); 
        
    }    
    private void updateSwordHitbox() {

        if (currentState == State.ATTACKING) {
            createSwordHitbox();
            swordHitboxActive = true;
        } else {
            if (swordHitboxFixture != null) {
                playerBody.destroyFixture(swordHitboxFixture);
                swordHitboxFixture = null;
            }
            swordHitboxActive = false;
        }
    }

    public boolean getSwordHItBox(){
        return swordHitboxActive ;
    }
    
     public void hitByMinion(Minion minion) {
         isDead = true;
         playerBody.applyLinearImpulse(new Vector2(0f,2f), playerBody.getWorldCenter(), true);
        setState(Player.State.DYING);
        

     }
   
       public boolean isDead(){
        return isDead;
    }
    
    ///////////////////////////////////////////
    ///////////////// INPUT
    ////////////////////////////
    
    private void checkUserInput(){
     if (this.currentState != this.getState().DYING){
        velX = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            velX = .5f;
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            velX = -.5f;
        if(Gdx.input.isKeyJustPressed(Input.Keys.S))
            this.roll(); 
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
            this.attack(); 
        if(Gdx.input.isKeyJustPressed(Input.Keys.Q))
          this.heal();        
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) && jumpCounter < 2){
            float force = playerBody.getMass() *3;
            playerBody.setLinearVelocity(playerBody.getLinearVelocity().x,0);
            playerBody.applyLinearImpulse(new Vector2(0,force), playerBody.getPosition(), true);
            MainGame.assetManager.get("sounds/jump.wav", Music.class).play();
            
        //https://www.youtube.com/watch?v=43DrvCp9-is&ab_channel=SmallPixelGames
            jumpCounter++;
        }   
        //reset counter
        if( playerBody.getLinearVelocity().y == 0){
          jumpCounter = 0;  
        }
        
        playerBody.setLinearVelocity(velX*speed, playerBody.getLinearVelocity().y <25 ?
        playerBody.getLinearVelocity().y : 25);

      }
    }
}
