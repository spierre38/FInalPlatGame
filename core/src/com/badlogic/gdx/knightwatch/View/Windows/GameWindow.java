
package com.badlogic.gdx.knightwatch.View.Windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.knightwatch.View.Helper.LevelHelper;
import com.badlogic.gdx.knightwatch.Model.Helper.ListenerHelper;
import com.badlogic.gdx.knightwatch.MainGame;
import com.badlogic.gdx.knightwatch.Model.Objects.Enemy;
import com.badlogic.gdx.knightwatch.Controller.Objects.Player;
import com.badlogic.gdx.knightwatch.Model.Helper.CONSTANTS;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.knightwatch.Model.Objects.Minion;

/**
 *
 * @author ecloaner
 */
public class GameWindow implements Screen  {
    
    private MainGame game;
    private TextureAtlas playerAtlas;
    private TextureAtlas enemyAtlas;
    private TextureAtlas itemAtlas;
    
    Texture texture;
    private OrthographicCamera gameCamera;
    private Viewport gameViewPort;
    
    private TmxMapLoader mapHelper;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapLoader;
    
    private World world;
    private Box2DDebugRenderer hitBoxes;
    private LevelHelper b2dhelper;
    
    private Player player;
    

    private Music music;
    
    
    public GameWindow(MainGame game){
        this.game = game;
        
        playerAtlas = new TextureAtlas("DifferentPlayerSprites.pack");
        enemyAtlas = new TextureAtlas("EnemySprites.pack");   
            
        //CAMMMMMMMMMMMMMMMMMMMMMMMMM///================
        gameCamera = new OrthographicCamera();
        gameViewPort = new FitViewport(CONSTANTS.Game_WIDTH / CONSTANTS.PPM,CONSTANTS.Game_HEIGHT / CONSTANTS.PPM,gameCamera);
        
        // map helper loading in map
        mapHelper = new TmxMapLoader();
        map = mapHelper.load("maps/level1.tmx");
        mapLoader = new OrthogonalTiledMapRenderer(map , 1 / CONSTANTS.PPM);
        gameCamera.position.set(gameViewPort.getWorldWidth() / 2 , gameViewPort.getWorldHeight() / 2, 0);
        
        //box 2d chaning the things in the world 
        world = new World(new Vector2(0,-10 ), true);
        hitBoxes = new Box2DDebugRenderer();
       

        b2dhelper = new LevelHelper(this);
        player = Player.getInstance(this);
        

       
        world.setContactListener(new ListenerHelper());
        
        player.getSwordHItBox();

       music = MainGame.assetManager.get("sounds/filler.mp3",Music.class);
       music.setLooping(true);
       music.play();
        
    }
    
    public TextureAtlas getplayerAtlas(){
        return playerAtlas;
    }
    
    public TextureAtlas getenemyAtlas(){
        return enemyAtlas;
    }
    


    
    public void update(float dt){
      //  handleInput(dt);
        world.step(1/60f, 6, 2);
        player.update(dt);
        gameCamera.position.x = player.playerBody.getPosition().x;
        gameCamera.update();
        mapLoader.setView(gameCamera);
        
        for (Enemy enemy : b2dhelper.getMinion())
            enemy.update(dt);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapLoader.render();
        hitBoxes.render(world,gameCamera.combined);
        
        
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : b2dhelper.getMinion())
            enemy.draw(game.batch);        
        game.batch.end();
        
        
    
        }
    
    

    @Override
    public void resize(int width, int height) {
        gameViewPort.update(width,height);
    }
    
    public TiledMap getMap(){
        return map;
    }
    
    public World getWorld(){
        return world;
    }
    
    public MainGame getGane(){
        return game;
    }
    
    public Player getPlayer(){
        return player;
    }
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        hitBoxes.dispose();
        world.dispose();
        map.dispose();
        mapLoader.dispose();
    }
    
}
