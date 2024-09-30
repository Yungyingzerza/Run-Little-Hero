package com.yungying.game.textureLoader;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

public class playerTextureList {

    public static final Texture teeRunLeft = new Texture("characters/Tee/TeeRunLeft.png");
    public static final Texture teeAgainLeft = new Texture("characters/Tee/TeeAgainLeft.png");
    public static final Texture teeRunRight = new Texture("characters/Tee/TeeRunRight.png");
    public static final Texture teeAgainRight = new Texture("characters/Tee/TeeAgainRight.png");
    public static final Texture teeJump = new Texture("characters/Tee/TeeJump.png");
    public static final Texture teeSlide = new Texture("characters/Tee/TeeSlide.png");


    public static final Animation<Texture> runAnimationTee = new Animation<>(0.1f, teeRunLeft, teeAgainLeft, teeRunRight, teeAgainRight, teeAgainLeft);
    public static final Animation<Texture> jumpAnimationTee = new Animation<>(0.1f, teeJump);
    public static final Animation<Texture> slideAnimationTee = new Animation<>(0.1f, teeSlide);

    public static final Texture sakuraRunLeft = new Texture("characters/Sakura/SakuraRunLeft.png");
    public static final Texture sakuraAgainLeft = new Texture("characters/Sakura/SakuraAgainLeft.png");
    public static final Texture sakuraRunRight = new Texture("characters/Sakura/SakuraRunRight.png");
    public static final Texture sakuraAgainRight = new Texture("characters/Sakura/SakuraAgainRight.png");
    public static final Texture sakuraJump = new Texture("characters/Sakura/SakuraJump.png");
    public static final Texture sakuraSlide = new Texture("characters/Sakura/SakuraSlide.png");

    public static final Animation<Texture> runAnimationSakura = new Animation<>(0.1f, sakuraRunLeft, sakuraAgainLeft, sakuraRunRight, sakuraAgainRight, sakuraAgainLeft);
    public static final Animation<Texture> jumpAnimationSakura = new Animation<>(0.1f, sakuraJump);
    public static final Animation<Texture> slideAnimationSakura = new Animation<>(0.1f, sakuraSlide);

    public static Texture getRunTexture(PlayerType player, float stateTime){
        if(player == PlayerType.TEE){
            return runAnimationTee.getKeyFrame(stateTime, true);
        }else if(player == PlayerType.SAKURA){
            return runAnimationSakura.getKeyFrame(stateTime, true);
        }
        return null;
    }

    public static Texture getJumpTexture(PlayerType player, float stateTime){
        if(player == PlayerType.TEE){
            return jumpAnimationTee.getKeyFrame(stateTime, true);
        }else if(player == PlayerType.SAKURA){
            return jumpAnimationSakura.getKeyFrame(stateTime, true);
        }
        return null;
    }

    public static Texture getSlideTexture(PlayerType player, float stateTime){
        if(player == PlayerType.TEE){
            return slideAnimationTee.getKeyFrame(stateTime, true);
        }else if(player == PlayerType.SAKURA){
            return slideAnimationSakura.getKeyFrame(stateTime, true);
        }
        return null;
    }

    public static Texture getTextureByString(PlayerType player, String skinPath){
        if(player == PlayerType.TEE){

            if(skinPath == null){
                return teeRunLeft;
            }

            switch (skinPath) {
                case "characters/Tee/TeeRunLeft.png":
                    return teeRunLeft;
                case "characters/Tee/TeeAgainLeft.png":
                    return teeAgainLeft;
                case "characters/Tee/TeeRunRight.png":
                    return teeRunRight;
                case "characters/Tee/TeeAgainRight.png":
                    return teeAgainRight;
                case "characters/Tee/TeeJump.png":
                    return teeJump;
                case "characters/Tee/TeeSlide.png":
                    return teeSlide;
                default:
                    return null;
            }


        }else if(player == PlayerType.SAKURA){

            if(skinPath == null){
                return sakuraRunLeft;
            }

            switch (skinPath) {
                case "characters/Sakura/SakuraRunLeft.png":
                    return sakuraRunLeft;
                case "characters/Sakura/SakuraAgainLeft.png":
                    return sakuraAgainLeft;
                case "characters/Sakura/SakuraRunRight.png":
                    return sakuraRunRight;
                case "characters/Sakura/SakuraAgainRight.png":
                    return sakuraAgainRight;
                case "characters/Sakura/SakuraJump.png":
                    return sakuraJump;
                case "characters/Sakura/SakuraSlide.png":
                    return sakuraSlide;
                default:
                    return null;
            }

        }

        return null;
    }
}
