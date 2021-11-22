package com.pewpewman.screen.gameScreen;

import com.pewpewman.map.*;
import com.pewpewman.model.attack.*;
import com.pewpewman.model.character.*;
import com.pewpewman.movement.*;
import com.pewpewman.audio.*;
import com.pewpewman.dialogue.*;
import com.pewpewman.screen.*;
import com.pewpewman.screen.transition.*;
import com.pewpewman.util.*;
import com.pewpewman.util.Action;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pewpewman.Pewpewman;
import com.pewpewman.Settings;
import java.util.ArrayList;

public class Level2 extends AbstractScreen {

    private final PlayerController controller;
    private final MusicManager musicManager = Pewpewman.musicManager;
    private final SoundEffectManager soundEffectManager = Pewpewman.soundEffectManager;
    private int playVictoryMusic = 0;   // 0 - Default, 1 - Play song

    public static Player player;
    private final ArrayList<Enemy> enemy = new ArrayList<>();
    private final ArrayList<EnemyMovement> enemyMovement = new ArrayList<>();
    private final TileMap map;
    private final SpriteBatch batch;      //Small tool to render sprites really fast to the screen compared to manually.
    private final TextureAtlas.AtlasRegion floorTile1;
    private final TextureAtlas.AtlasRegion floorTile2;
    private final TextureAtlas.AtlasRegion wall;
    private final TextureAtlas.AtlasRegion obstacle;
    private final TextureAtlas.AtlasRegion gate;
    private final TextureAtlas.AtlasRegion enemyBullet;
    private final ArrayList<TextureAtlas.AtlasRegion> playerBullet = new ArrayList<>();

    // Bullet
    float shootTimer;       // Keep track of the shooting time
    float enemyShootTimer;
    private TextureAtlas.AtlasRegion pewBulletTexture;
    private final ArrayList<Bullet> playerBullets = new ArrayList<>();
    private final ArrayList<Bullet> enemyBullets = new ArrayList<>();
    private final ArrayList<Enemy> deadEnemy = new ArrayList<>();

    // Animation for collision
    boolean playerCollision = false;
    int playerCollisionTimer = 0;
    int hiddenTime = 12;

    // StoryBox UI
    private final StoryBoxUI openingBox = new StoryBoxUI();
    private final StoryBoxUI endingBox  = new StoryBoxUI();
    boolean storyEnd = false;
    boolean levelEnd = false;
    boolean controllerPaused = false;
    boolean gatePassed = false;

    public Level2(Pewpewman game) {
        super(game);

        TextureAtlas elementAtlas = game.getAssetManager().get("packed/element/elementpack.atlas", TextureAtlas.class);
        TextureAtlas playerAtlas  = game.getAssetManager().get("packed/player/textures.atlas", TextureAtlas.class);
        TextureAtlas enemyAtlas   = game.getAssetManager().get("packed/enemy/textures.atlas", TextureAtlas.class);

        floorTile1   = elementAtlas.findRegion("level2_tile1");
        floorTile2   = elementAtlas.findRegion("level2_tile2");
        wall         = elementAtlas.findRegion("level2_wall");
        obstacle     = elementAtlas.findRegion("level2_obstacle");
        gate         = elementAtlas.findRegion("gate");
        enemyBullet  = elementAtlas.findRegion("enemy_bullet");
        playerBullet.add(elementAtlas.findRegion("player_bullet_north"));
        playerBullet.add(elementAtlas.findRegion("player_bullet_south"));
        playerBullet.add(elementAtlas.findRegion("player_bullet_east"));
        playerBullet.add(elementAtlas.findRegion("player_bullet_west"));

        batch = new SpriteBatch();

        AnimationSet playerAnimations = new AnimationSet(
                new Animation(0.1f, playerAtlas.findRegions("player_walk_north"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(0.1f, playerAtlas.findRegions("player_walk_south"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(0.1f, playerAtlas.findRegions("player_walk_east"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(0.1f, playerAtlas.findRegions("player_walk_west"), Animation.PlayMode.LOOP_PINGPONG),
                playerAtlas.findRegion("player_stand_north"),
                playerAtlas.findRegion("player_stand_south"),
                playerAtlas.findRegion("player_stand_east"),
                playerAtlas.findRegion("player_stand_west")
        );

        AnimationSet enemyAnimations = new AnimationSet(
                new Animation(0.1f, enemyAtlas.findRegions("enemy_walk_north"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(0.1f, enemyAtlas.findRegions("enemy_walk_south"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(0.1f, enemyAtlas.findRegions("enemy_walk_east"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(0.1f, enemyAtlas.findRegions("enemy_walk_west"), Animation.PlayMode.LOOP_PINGPONG),
                enemyAtlas.findRegion("enemy_stand_north"),
                enemyAtlas.findRegion("enemy_stand_south"),
                enemyAtlas.findRegion("enemy_stand_east"),
                enemyAtlas.findRegion("enemy_stand_west")
        );

        map         = new TileMap(Settings.MAP_SIZE_X, Settings.MAP_SIZE_Y);
        player      = new Player(map, Settings.PLAYER_DEFAULT_X, Settings.PLAYER_DEFAULT_Y, playerAnimations);
        controller  = new PlayerController(player);

        int enemyStartX = Settings.MAP_CENTRE_X - 7;
        int enemyStartY;
        for (int i = 0; i < Settings.ENEMY_LEVEL2; i++) {
            if (i < 4) {
                enemyStartX += 3;
                enemyStartY = Settings.MAP_CENTRE_Y + 2;
            } else {
                enemyStartX -= 3;
                enemyStartY = Settings.MAP_CENTRE_Y - 3;
            }
            enemy.add(new Enemy(map, enemyStartX, enemyStartY, enemyAnimations, ENEMY_TYPE.NORMAL));
            enemyMovement.add(new EnemyMovement(enemy.get(i)));
        }

        // Enter text here
        openingBox.createStoryBoxUI("Pathetic human being\n\n\n" +
                "Let's see what have you got!!!\n\n\n" +
                "If you are ready...\n\n\n" +
                "press X and you shall DIEEEE!!!!!");
        endingBox.createStoryBoxUI("Wow...\n\n\n" +
                "It seems that you are ready for the final round...\n\n\n" +
                "Then I shall let you see my true power!!!\n\n\n" +
                "HAHAHAHAHAHAHAHA");
    }

    @Override
    public void render(float delta) {

        // For debug
//        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
//            getApp().startTransition(
//                    this,
//                    getApp().getLevel3(),
//                    new FadeOutTransition(0.5f, Color.BLACK, getApp().getAssetManager()),
//                    new FadeInTransition(0.5f, Color.BLACK, getApp().getAssetManager()),
//                    new Action() {
//                        @Override
//                        public void action() {
//                            musicManager.playMusic(TRACK.LEVEL3);
//                        }
//                    });
//        }

        // Check whether story box ongoing and level ends
        if (storyEnd && !levelEnd) {
            for (Enemy enemies: enemy){enemies.update(delta);}
            for (EnemyMovement movement: enemyMovement){movement.update(delta);}
            shootTimer      += delta;
            enemyShootTimer += delta;
            player.update(delta);
        }

        // Check whether player can control player
        if (!controllerPaused) { controller.update(delta); }

        int getHit = 0;     //player get hitted count every update

        // Player's shooting code
        if(storyEnd && Gdx.input.isKeyPressed(Input.Keys.SPACE) && shootTimer >= Settings.PLAYER_SHOOT_WAIT_TIME)
        {
            shootTimer = 0;
            if(player.getFacing() == DIRECTION.NORTH){pewBulletTexture  = playerBullet.get(0);}
            if(player.getFacing() == DIRECTION.SOUTH){pewBulletTexture  = playerBullet.get(1);}
            if(player.getFacing() == DIRECTION.EAST){pewBulletTexture  = playerBullet.get(2);}
            if(player.getFacing() == DIRECTION.WEST){pewBulletTexture  = playerBullet.get(3);}

            playerBullets.add(new Bullet(player.getX(), player.getY(), Settings.PLAYER_BULLET_SPEED, player.getFacing(),pewBulletTexture));
            soundEffectManager.playSoundEffect(SOUNDEFFECT.SHOOTING);
        }

        // Enemies' shooting code
        if(storyEnd && enemyShootTimer >= Settings.ENEMY_SHOOT_WAIT_TIME)
        {
            enemyShootTimer = 0;
            for(Enemy enemies: enemy)
            {
                enemyBullets.add(new Bullet(enemies.getX(), enemies.getY(), Settings.ENEMY_BULLET_SPEED,enemies.getFacing(), enemyBullet));
            }
        }

        // Update player bullets
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for(Bullet bullet: playerBullets)
        {
            bullet.update(delta);
            if(bullet.remove)
            {
                bulletsToRemove.add(bullet);
            }
            // To block player's bullets from passing through wall & obstacles
            if ((map.getTile((int)bullet.getX(),(int)bullet.getY()).getTerrain() != TERRAIN.FLOOR_TILE_1 &&
                    map.getTile((int)bullet.getX(),(int)bullet.getY()).getTerrain() != TERRAIN.FLOOR_TILE_2 &&
                    map.getTile((int)bullet.getX(),(int)bullet.getY()).getTerrain() != TERRAIN.GATE) ||
                    (map.getTile((int)bullet.getX(),(int)bullet.getY()+1).getTerrain() != TERRAIN.FLOOR_TILE_1 &&
                            map.getTile((int)bullet.getX(),(int)bullet.getY()+1).getTerrain() != TERRAIN.FLOOR_TILE_2 &&
                            map.getTile((int)bullet.getX(),(int)bullet.getY()+1).getTerrain() != TERRAIN.GATE &&
                            bullet.getBulletDir() == DIRECTION.NORTH) ||
                    (map.getTile((int)bullet.getX()+1,(int)bullet.getY()).getTerrain() != TERRAIN.FLOOR_TILE_1 &&
                            map.getTile((int)bullet.getX()+1,(int)bullet.getY()).getTerrain() != TERRAIN.FLOOR_TILE_2 &&
                            map.getTile((int)bullet.getX()+1,(int)bullet.getY()).getTerrain() != TERRAIN.GATE &&
                            bullet.getBulletDir() == DIRECTION.EAST)) {
                bulletsToRemove.add(bullet);
            }
        }

        // Update enemy bullets
        ArrayList<Bullet> enemyBulletsToRemove = new ArrayList<>();
        for(Bullet enemyBullet: enemyBullets)
        {
            enemyBullet.update(delta);
            if(enemyBullet.remove)
            {
                enemyBulletsToRemove.add(enemyBullet);
            }

            // To block enemies' bullets from passing through wall & obstacles
            if ((map.getTile((int)enemyBullet.getX(),(int)enemyBullet.getY()).getTerrain() != TERRAIN.FLOOR_TILE_1 &&
                    map.getTile((int)enemyBullet.getX(),(int)enemyBullet.getY()).getTerrain() != TERRAIN.FLOOR_TILE_2 &&
                    map.getTile((int)enemyBullet.getX(),(int)enemyBullet.getY()).getTerrain() != TERRAIN.GATE) ||
                    (map.getTile((int)enemyBullet.getX(),(int)enemyBullet.getY()+1).getTerrain() != TERRAIN.FLOOR_TILE_1 &&
                            map.getTile((int)enemyBullet.getX(),(int)enemyBullet.getY()+1).getTerrain() != TERRAIN.FLOOR_TILE_2 &&
                            map.getTile((int)enemyBullet.getX(),(int)enemyBullet.getY()+1).getTerrain() != TERRAIN.GATE &&
                            enemyBullet.getBulletDir() == DIRECTION.NORTH) ||
                    (map.getTile((int)enemyBullet.getX()+1,(int)enemyBullet.getY()).getTerrain() != TERRAIN.FLOOR_TILE_1 &&
                            map.getTile((int)enemyBullet.getX()+1,(int)enemyBullet.getY()).getTerrain() != TERRAIN.FLOOR_TILE_2 &&
                            map.getTile((int)enemyBullet.getX()+1,(int)enemyBullet.getY()).getTerrain() != TERRAIN.GATE &&
                            enemyBullet.getBulletDir() == DIRECTION.EAST)){
                enemyBulletsToRemove.add(enemyBullet);
            }
        }

        //After all updates, check player's bullet and monster collision
        ArrayList<EnemyMovement> enemyMovementToRemove = new ArrayList<>();
        for(Bullet bullet : playerBullets)
        {
            for (int i = 0; i<enemy.size(); i++)
            {
                if (bullet.getHitbox().hit(enemy.get(i).getHitbox()))
                {
                    bulletsToRemove.add(bullet);
                    enemy.get(i).healthBar --;
                    if(enemy.get(i).isDead())
                    {
                        deadEnemy.add(enemy.get(i));
                        enemyMovementToRemove.add(enemyMovement.get(i));
                    }
                    soundEffectManager.playSoundEffect(SOUNDEFFECT.ENEMYSHOT);
                }
            }
        }

        //After all updates, check enemies' bullet and player collision
        for(Bullet enemyBullet : enemyBullets)
        {
            if (enemyBullet.getHitbox().hit(player.getHitbox()))
            {
                playerCollision = true;
                getHit++;
                enemyBulletsToRemove.add(enemyBullet);
                player.isHit(getHit);
            }
        }

        // Hidden effect when player shot by enemies' bullets
        if (playerCollision) {
            playerCollisionTimer++;
            if (playerCollisionTimer == hiddenTime) {
                playerCollisionTimer = 0;
                playerCollision = false;
            }
        }

        enemyBullets.removeAll(enemyBulletsToRemove);
        playerBullets.removeAll(bulletsToRemove);
        enemy.removeAll(deadEnemy);
        enemyMovement.removeAll(enemyMovementToRemove);

        if(player.isDead()){
            getApp().startTransition(
                    this,
                    getApp().getGameOverScreen(),
                    new FadeOutTransition(0.5f, Color.BLACK, getApp().getAssetManager()),
                    new FadeInTransition(0.5f, Color.BLACK, getApp().getAssetManager()),
                    new Action()
                    {
                        @Override
                        public void action(){
                            musicManager.playMusic(TRACK.GAMEOVER);
                        }
                    });
        }

        batch.begin();

        //To put tiles
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                TextureAtlas.AtlasRegion render;
                if (map.getTile(x, y).getTerrain() == TERRAIN.FLOOR_TILE_1) {
                    render = floorTile1;
                } else if (map.getTile(x, y).getTerrain() == TERRAIN.FLOOR_TILE_2) {
                    render = floorTile2;
                } else if (map.getTile(x, y).getTerrain() == TERRAIN.OBSTACLE)  {
                    render = obstacle;
                } else if (map.getTile(x, y).getTerrain() == TERRAIN.WALL){
                    render = wall;
                } else {
                    render = floorTile1;
                }

                batch.draw(render,
                        x*Settings.SCALED_TILE_SIZE,
                        y*Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE);
            }
        }

        //render player bullets
        for(Bullet bullet: playerBullets)
        {
            bullet.render(batch);
        }

        // Initialize enemy only after story ends
        if (storyEnd) {
            player.renderHP(batch);

            for (Enemy enemies: enemy) {
                if (enemies.getWorldY() > player.getWorldY()) {
                    renderEnemy(enemies);
                    if (!playerCollision || playerCollisionTimer == hiddenTime/2) {
                        renderPlayer(player);
                    }
                } else {
                    if (!playerCollision || playerCollisionTimer == hiddenTime/2) {
                        renderPlayer(player);
                    }
                    renderEnemy(enemies);
                }
            }

            if (enemy.size() == 0) {
                // Runs only once
                if (playVictoryMusic == 0) {
                    levelEnd = true;
                    musicManager.playMusic(TRACK.VICTORY);
                    playVictoryMusic++;
                    controllerPaused = true;
                }

                if (map.getTile(Settings.MAP_CENTRE_X, Settings.MAP_CENTRE_Y).getTerrain() == TERRAIN.GATE) {
                    batch.draw(gate,
                            (Settings.MAP_CENTRE_X-1) * Settings.SCALED_TILE_SIZE,
                            Settings.MAP_CENTRE_Y * Settings.SCALED_TILE_SIZE,
                            Settings.SCALED_TILE_SIZE*2,
                            Settings.SCALED_TILE_SIZE*2);

                    if ((map.getTile(player.getX(),player.getY()).getTerrain() == TERRAIN.GATE || map.getTile(player.getX()+1,player.getY()).getTerrain() == TERRAIN.GATE) && player.getFacing() == DIRECTION.NORTH) {

                        gatePassed = true;
                        getApp().startTransition(
                                this,
                                getApp().getLevel3(),
                                new FadeOutTransition(0.5f, Color.BLACK, getApp().getAssetManager()),
                                new FadeInTransition(0.5f, Color.BLACK, getApp().getAssetManager()),
                                new Action() {
                                    @Override
                                    public void action() {
                                        musicManager.playMusic(TRACK.LEVEL3);
                                    }
                                });
                    }
                }
                if (!gatePassed) {
                    renderPlayer(player);
                }
            }
            for(Bullet bullet: enemyBullets){bullet.render(batch);}
        }

        batch.end();

        // Conditions for story box
        if (Gdx.input.isKeyPressed(Input.Keys.X) && openingBox.getStoryBox().isFinished()) {
            openingBox.getStoryBox().setVisible(false);
            storyEnd = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X) && endingBox.getStoryBox().isVisible()) {
            endingBox.getStoryBox().setVisible(false);
            levelEnd = false;
            controllerPaused = false;
        }
        endingBox.getStoryBox().setVisible(levelEnd);

        // To update and draw story box
        endingBox.getRoot().act(delta);
        openingBox.getRoot().act(delta);
        endingBox.getStage().draw();
        openingBox.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    private void renderPlayer(Player player) {
        batch.draw(player.getSprite(),
                player.getWorldX()*Settings.SCALED_TILE_SIZE,
                player.getWorldY()*Settings.SCALED_TILE_SIZE,
                Settings.SCALED_TILE_SIZE,
                Settings.SCALED_TILE_SIZE*1.2f);
    }

    private void renderEnemy(Enemy enemies) {
        batch.draw(enemies.getSprite(),
                enemies.getWorldX() * Settings.SCALED_TILE_SIZE,
                enemies.getWorldY() * Settings.SCALED_TILE_SIZE,
                Settings.SCALED_TILE_SIZE,
                Settings.SCALED_TILE_SIZE * 1.2f);
    }
}
