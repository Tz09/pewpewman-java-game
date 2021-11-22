package com.pewpewman.util;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTools {
    public static void main(String[] args) {

//        TexturePacker.process(
//                "core/assets/unpacked/player/",
//                "core/assets/packed/player/",
//                "textures");

//        TexturePacker.process(
//                "core/assets/unpacked/enemy/",
//                "core/assets/packed/enemy/",
//                "textures");

//        TexturePacker.process(
//                "core/assets/unpacked/ui/",
//                "core/assets/packed/ui/",
//                "uipack");

//        TexturePacker.process(
//                "core/assets/unpacked/element/",
//                "core/assets/packed/element/",
//                "elementpack");

        TexturePacker.process(
                "core/assets/unpacked/boss/",
                "core/assets/packed/boss/",
                "textures");
    }
}
