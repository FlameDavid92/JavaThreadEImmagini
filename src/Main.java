import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read(new File("gris.png")); //caricamento immagine
        } catch (IOException e) {
        }
        BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR); // creazione immagine output

        testSequenziale(inputImage,outputImage);
        testParallelo1(inputImage,outputImage);
        testParallelo2(inputImage,outputImage);
        testParallelo3(inputImage,outputImage); /*Non ha senso utilizzare questo approccio in questo caso!*/
    }

    public static void testSequenziale(BufferedImage inputImage, BufferedImage outputImage){
        long startTime = System.currentTimeMillis();
        ImageBorderThread t1 = new ImageBorderThread(inputImage,outputImage,0,inputImage.getWidth(),0,inputImage.getHeight());
        t1.run();
        try {
            ImageIO.write(outputImage, "png", new File("outputImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sequenziale: "+(System.currentTimeMillis() - startTime)+" millisecondi.");
    }

    public static void testParallelo1(BufferedImage inputImage, BufferedImage outputImage){
        long startTime = System.currentTimeMillis();
        ImageBorderThread t1 = new ImageBorderThread(inputImage,outputImage,0,inputImage.getWidth(),0,inputImage.getHeight()/2);
        ImageBorderThread t2 = new ImageBorderThread(inputImage,outputImage,0,inputImage.getWidth(), inputImage.getHeight()/2,inputImage.getHeight());

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            ImageIO.write(outputImage, "png", new File("outputImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Parallelo1: "+(System.currentTimeMillis() - startTime)+" millisecondi.");
    }

    public static void testParallelo2(BufferedImage inputImage, BufferedImage outputImage){
        long startTime = System.currentTimeMillis();
        ImageBorderThread t1 = new ImageBorderThread(inputImage,outputImage,0,inputImage.getWidth()/2,0,inputImage.getHeight()/2);
        ImageBorderThread t2 = new ImageBorderThread(inputImage,outputImage,inputImage.getWidth()/2, inputImage.getWidth(), 0,inputImage.getHeight()/2);
        ImageBorderThread t3 = new ImageBorderThread(inputImage,outputImage,0,inputImage.getWidth()/2,inputImage.getHeight()/2,inputImage.getHeight());
        ImageBorderThread t4 = new ImageBorderThread(inputImage,outputImage,inputImage.getWidth()/2, inputImage.getWidth(), inputImage.getHeight()/2,inputImage.getHeight());

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            ImageIO.write(outputImage, "png", new File("outputImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Parallelo2: "+(System.currentTimeMillis() - startTime)+" millisecondi.");
    }

    public static void testParallelo3(BufferedImage inputImage, BufferedImage outputImage){
        long startTime = System.currentTimeMillis();

        Thread t1 = new Thread(() -> {
            testParallelo3Util(inputImage,outputImage, 0, inputImage.getWidth()/2);
        });
        Thread t2 = new Thread(() -> {
            testParallelo3Util(inputImage,outputImage, inputImage.getWidth()/2, inputImage.getWidth());
        });
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            ImageIO.write(outputImage, "png", new File("outputImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Parallelo3: "+(System.currentTimeMillis() - startTime)+" millisecondi.");
    }

    private static void testParallelo3Util(BufferedImage inputImage, BufferedImage outputImage, int startW, int endW){
        ImageBorderThread t1 = new ImageBorderThread(inputImage,outputImage,startW,endW,inputImage.getHeight()/2,inputImage.getHeight());
        ImageBorderThread t2 = new ImageBorderThread(inputImage,outputImage,startW, endW, inputImage.getHeight()/2,inputImage.getHeight());

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
