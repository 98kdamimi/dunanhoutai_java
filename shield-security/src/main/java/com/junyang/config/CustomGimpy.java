package com.junyang.config;

import com.google.code.kaptcha.GimpyEngine;
import com.google.code.kaptcha.util.Configurable;

import java.awt.image.BufferedImage;

/**
 * 验证码图片样式配置-KaptchaConfig配置中通过反射在使用
 *
 */
public class CustomGimpy extends Configurable implements GimpyEngine {

    @Override
    public BufferedImage getDistortedImage(BufferedImage baseImage) {
        int imageHeight = baseImage.getHeight();
        int imageWidth = baseImage.getWidth();
        int[] pix = new int[imageHeight * imageWidth];
        int j = 0;
        for (int j1 = 0; j1 < imageWidth; j1++) {
            for (int k1 = 0; k1 < imageHeight; k1++) {
                pix[j] = baseImage.getRGB(j1, k1);
                j++;
            }
        }
        double distance = ranInt(imageWidth / 4, imageWidth / 3);
        int widthMiddle = baseImage.getWidth() / 2;
        int heightMiddle = baseImage.getHeight() / 2;
        for (int x = 0; x < baseImage.getWidth(); x++) {
            for (int y = 0; y < baseImage.getHeight(); y++) {
                int relX = x - widthMiddle;
                int relY = y - heightMiddle;
                double d1 = Math.sqrt(relX * relX + relY * relY);
                if (d1 >= distance) {
                    continue;
                }
                int j2 = widthMiddle + (int) (fishEyeFormula(d1 / distance) * distance / d1 * (x - widthMiddle));

                int k2 = heightMiddle + (int) (fishEyeFormula(d1 / distance) * distance / d1 * (y - heightMiddle));
                baseImage.setRGB(x, y, pix[(j2 * imageHeight + k2)]);
            }
        }
        return baseImage;
    }

    private int ranInt(int i, int j) {
        double d = Math.random();
        return (int) (i + (j - i + 1) * d);
    }

    private double fishEyeFormula(double s) {
        if (s < 0.0D) {
            return 0.0D;
        }
        if (s > 1.0D) {
            return s;
        }
        return -0.75D * s * s * s + 1.5D * s * s + 0.25D * s;
    }
}
