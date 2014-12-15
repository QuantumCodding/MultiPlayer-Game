package com.GameName.Render.Window;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class IconLoader {
   
    public static ByteBuffer[] load(BufferedImage image) {
        ByteBuffer[] buffers = null;        
        String OS = System.getProperty("os.name").toUpperCase();
        
        if(OS.contains("WIN")) {
            buffers = new ByteBuffer[2];
            buffers[0] = loadInstance(image, 16);
            buffers[1] = loadInstance(image, 32);
            
        } else if(OS.contains("MAC")) {
            buffers = new ByteBuffer[1];
            buffers[0] = loadInstance(image, 128);
            
        } else {
            buffers = new ByteBuffer[1];
            buffers[0] = loadInstance(image, 32);
        }
        
        return buffers;
    }
    
    private static ByteBuffer loadInstance(BufferedImage image, int dimension) {
        BufferedImage icon = new BufferedImage(
        		dimension, dimension, BufferedImage.TYPE_INT_ARGB_PRE
        	);
        
        Graphics g = icon.createGraphics();
        
        double ratio = getIconRatio(image, icon);
        double width = image.getWidth() * ratio;
        double height = image.getHeight() * ratio;
        
        g.drawImage(image, 
        		(int) ((icon.getWidth() - width) / 2),
                (int) ((icon.getHeight() - height) / 2),
                (int) width, (int) height, null);
        
        g.dispose(); 
        return convertToByteBuffer(icon);
    }
 
    private static double getIconRatio(BufferedImage src, BufferedImage icon) {
        double ratio = 1;
        if (src.getWidth() > icon.getWidth()) {
            ratio = (double) (icon.getWidth()) / src.getWidth();
        } else {
            ratio = (int) (icon.getWidth() / src.getWidth());
        }
        
        double ratio2 = 0;
        if (src.getHeight() > icon.getHeight()) {
            ratio2 = (double) icon.getHeight() / src.getHeight();            
        } else {
            ratio2 = (int) (icon.getHeight() / src.getHeight());            
        }
        
        if (ratio2 < ratio) { ratio = ratio2; }
        
        return ratio;
    }
 
    public static ByteBuffer convertToByteBuffer(BufferedImage image) {
        byte[] buffer = new byte[image.getWidth() * image.getHeight() * 4];
       
        int count = 0;
        for (int y = 0; y < image.getHeight(); y ++) {
            for (int x = 0; x < image.getWidth(); x ++) {
                int colorSpace = image.getRGB(x, y);
                
                buffer[count + 0] = (byte) ((colorSpace << 8) >> 24);
                buffer[count + 1] = (byte) ((colorSpace << 16) >> 24);
                buffer[count + 2] = (byte) ((colorSpace << 24) >> 24);
                buffer[count + 3] = (byte) (colorSpace >> 24);
                
                count += 4;
            }
        }
        
        return ByteBuffer.wrap(buffer);
    }
}
