package com.pewpewman.map;

import com.pewpewman.*;
import com.pewpewman.screen.*;

public class TileMap {

    private final int width;
    private final int height;
    private final Tile[][] tiles;

    public TileMap(int width, int height) {

        this.width = width;
        this.height = height;

        tiles = new Tile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (Math.random() > 0.5d) {   //d for double
                    tiles[x][y] = new Tile(TERRAIN.FLOOR_TILE_1);
                } else {
                    tiles[x][y] = new Tile(TERRAIN.FLOOR_TILE_2);
                }
                if (x == Settings.MAP_CENTRE_X && y == Settings.MAP_CENTRE_Y){
                    tiles[x][y] = new Tile(TERRAIN.GATE);
                }
                if (x == 0 || x == Settings.MAP_SIZE_X-1 || y == 0 || y == Settings.MAP_SIZE_Y-1) {
                    tiles[x][y] = new Tile(TERRAIN.WALL);
                }
                if (Pewpewman.currentScreen == SCREEN.LEVEL1) {
                    if ((x == 8 && y >= 5 && y <= 12) || (x == 23 && y >= 5 && y <= 12)) {
                        tiles[x][y] = new Tile(TERRAIN.OBSTACLE);
                    }
                } else if (Pewpewman.currentScreen == SCREEN.LEVEL2) {
                    if ((y == 12 && x >= 8 && x <= 23) || (y == 5 && x >= 8 && x <= 23)) {
                        tiles[x][y] = new Tile(TERRAIN.OBSTACLE);
                    }
                } else if (Pewpewman.currentScreen == SCREEN.LEVEL3) {
                    if ((x == 6 && y >= 4 && y <= 13) || (x == 25 && y >= 4 && y <= 13) || (y == 13 && x >= 12 && x <= 19) || (y == 4 && x >= 12 && x <= 19)) {
                        tiles[x][y] = new Tile(TERRAIN.OBSTACLE);
                    }
                }
            }
        }
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public int getWidth()  {return width;}
    public int getHeight() {return height;}
}