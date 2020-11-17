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
        testParalleloDivisioneIn2Righe(inputImage,outputImage);
        testParalleloDivisioneInRettangoli(inputImage,outputImage);
        testParalleloDivisioneIn4Righe(inputImage,outputImage);

        long startTime = System.currentTimeMillis();
        try {
            ImageIO.write(outputImage, "png", new File("outputImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Scrittura immagine: "+(System.currentTimeMillis() - startTime)+" millisecondi.");
    }

    public static void testSequenziale(BufferedImage inputImage, BufferedImage outputImage){
        long startTime = System.currentTimeMillis();
        ImageBorderThread t1 = new ImageBorderThread(inputImage,outputImage,0,inputImage.getWidth(),0,inputImage.getHeight());
        t1.run();
        System.out.println("Sequenziale: "+(System.currentTimeMillis() - startTime)+" millisecondi.");
    }

    public static void testParalleloDivisioneIn2Righe(BufferedImage inputImage, BufferedImage outputImage){
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
        System.out.println("Parallelo divisione in 2 righe: "+(System.currentTimeMillis() - startTime)+" millisecondi.");
    }

    public static void testParalleloDivisioneInRettangoli(BufferedImage inputImage, BufferedImage outputImage){
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
        System.out.println("Parallelo divisione in 4 rettangoli: "+(System.currentTimeMillis() - startTime)+" millisecondi.");
    }

    public static void testParalleloDivisioneIn4Righe(BufferedImage inputImage, BufferedImage outputImage){
        long startTime = System.currentTimeMillis();
        ImageBorderThread t1 = new ImageBorderThread(inputImage,outputImage,0,inputImage.getWidth(),0,inputImage.getHeight()/4);
        ImageBorderThread t2 = new ImageBorderThread(inputImage,outputImage,0,inputImage.getWidth(), inputImage.getHeight()/4,(inputImage.getHeight()/4)*2);
        ImageBorderThread t3 = new ImageBorderThread(inputImage,outputImage,0,inputImage.getWidth(),(inputImage.getHeight()/4)*2,(inputImage.getHeight()/4)*3);
        ImageBorderThread t4 = new ImageBorderThread(inputImage,outputImage,0,inputImage.getWidth(), (inputImage.getHeight()/4)*3,inputImage.getHeight());

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
        System.out.println("Parallelo divisione in 4 righe: "+(System.currentTimeMillis() - startTime)+" millisecondi.");
    }
}
