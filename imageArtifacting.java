import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ImageArtifact {

    public static void main(String[] args) {
        try {
           //load it
            BufferedImage originalImage = ImageIO.read(new File("input.jpg"));

            // make it pixalized
            BufferedImage pixelatedImage = pixelate(originalImage, 10);

            // noisy image code
            BufferedImage noisyImage = addNoise(originalImage, 50);

            // save the images
            ImageIO.write(pixelatedImage, "jpg", new File("pixelated_image.jpg"));
            ImageIO.write(noisyImage, "jpg", new File("noisy_image.jpg"));

            System.out.println("Images artifacted and saved.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Pixelation effect
    public static BufferedImage pixelate(BufferedImage image, int blockSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage pixelatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {
                int avgRed = 0, avgGreen = 0, avgBlue = 0;
                int count = 0;

                for (int yy = y; yy < Math.min(y + blockSize, height); yy++) {
                    for (int xx = x; xx < Math.min(x + blockSize, width); xx++) {
                        int color = image.getRGB(xx, yy);
                        avgRed += (color >> 16) & 0xFF;
                        avgGreen += (color >> 8) & 0xFF;
                        avgBlue += color & 0xFF;
                        count++;
                    }
                }

                if (count > 0) {
                    avgRed /= count;
                    avgGreen /= count;
                    avgBlue /= count;

                    int avgColor = (avgRed << 16) | (avgGreen << 8) | avgBlue;

                    for (int yy = y; yy < Math.min(y + blockSize, height); yy++) {
                        for (int xx = x; xx < Math.min(x + blockSize, width); xx++) {
                            pixelatedImage.setRGB(xx, yy, avgColor);
                        }
                    }
                }
            }
        }
        return pixelatedImage;
    }

    // Adding noise effect
    public static BufferedImage addNoise(BufferedImage image, int noiseLevel) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage noisyImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Random random = new Random();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = image.getRGB(x, y);
                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = color & 0xFF;

                // add random noise to each color
                red = Math.max(0, Math.min(255, red + (random.nextInt(2 * noiseLevel) - noiseLevel)));
                green = Math.max(0, Math.min(255, green + (random.nextInt(2 * noiseLevel) - noiseLevel)));
                blue = Math.max(0, Math.min(255, blue + (random.nextInt(2 * noiseLevel) - noiseLevel)));

                noisyImage.setRGB(x, y, (red << 16) | (green << 8) | blue);
            }
        }
        return noisyImage;
    }
}
