package com.pewpewman.movement;

import com.pewpewman.*;
import com.pewpewman.model.character.*;
import com.pewpewman.screen.*;
import com.pewpewman.screen.gameScreen.*;

public class EnemyMovement{

    public Enemy enemy;

    public EnemyMovement(Enemy e) {
        this.enemy = e;
    }

    public void update(float delta) {

        Player player;
        if (Pewpewman.currentScreen == SCREEN.LEVEL1) {
            player = Level1.player;
        } else if (Pewpewman.currentScreen == SCREEN.LEVEL2) {
            player = Level2.player;
        } else {
            player = Level3.player;
        }

        if (enemy.isBlocked()) {
            if (enemy.getFacing() == DIRECTION.EAST ||enemy.getFacing() == DIRECTION.WEST) {
                if (enemy.getY() > player.getY()) {
                    enemy.reface(DIRECTION.SOUTH);
                    enemy.move(DIRECTION.SOUTH);
                } else if (enemy.getY() < player.getY()) {
                    enemy.reface(DIRECTION.NORTH);
                    enemy.move(DIRECTION.NORTH);
                }
            } else if (enemy.getFacing() == DIRECTION.NORTH ||enemy.getFacing() == DIRECTION.SOUTH){
                if (enemy.getX() > player.getX()) {
                    enemy.reface(DIRECTION.WEST);
                    enemy.move(DIRECTION.WEST);
                } else if (enemy.getX() < player.getX()) {
                    enemy.reface(DIRECTION.EAST);
                    enemy.move(DIRECTION.EAST);
                }
            }
        } else {
            if (enemy.getX() == player.getX()) {
                if (enemy.getY() > player.getY()) {
                    enemy.reface(DIRECTION.SOUTH);
                    enemy.move(DIRECTION.SOUTH);
                } else {
                    enemy.reface(DIRECTION.NORTH);
                    enemy.move(DIRECTION.NORTH);
                }
            } else if (enemy.getY() == player.getY()) {
                if (enemy.getX() > player.getX()) {
                    enemy.reface(DIRECTION.WEST);
                    enemy.move(DIRECTION.WEST);
                } else {
                    enemy.reface(DIRECTION.EAST);
                    enemy.move(DIRECTION.EAST);
                }
            } else if (enemy.getX() > player.getX()) {
                enemy.move(DIRECTION.WEST);
            } else if (enemy.getX() < player.getX()) {
                enemy.move(DIRECTION.EAST);
            } else if (enemy.getY() > player.getY()) {
                enemy.move(DIRECTION.SOUTH);
            } else if (enemy.getY() < player.getY()) {
                enemy.move(DIRECTION.NORTH);
            }
        }
    }
}