
package imagesegmentation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javafx.scene.layout.Border;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class segmentClass extends JPanel{

    static Image image;
    static ImageIcon image2;
    static JLabel  outputImage;
    public Mat source ;
    static int SEG_MIN = 0;
    static int SEG_MAX = 255;
    static int SEG_INIT = 127;
    static JPanel panel1 = new JPanel();
    static JPanel panel2 = new JPanel();
    static JLabel label2 = new JLabel( );
    static JLabel label3 = new JLabel( );
    static JLabel label4 = new JLabel( );
    static JLabel label5 = new JLabel( );
    static JLabel label6 = new JLabel( );



    public static void main(String[] args)  {

        try{

            //frame    	
            JFrame frame = new JFrame("Image Segmantation");

            frame.setSize(1500,650);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new GridBagLayout());
            GridBagConstraints co = new GridBagConstraints();  
            co.weightx = 0.5;
            co.fill = GridBagConstraints.HORIZONTAL;
            
            co.gridx = 0;
            co.gridy = 0;
            frame.add(panel1,co);
            co.gridx = 0;
            co.gridy = 1;
            frame.add(panel2,co);
            
            javax.swing.border.Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder title = BorderFactory.createTitledBorder( blackline, "Original Image");
            title.setTitleJustification(TitledBorder.CENTER);
            panel1.setBorder(title);
            
            TitledBorder title2 = BorderFactory.createTitledBorder( blackline, "Segmented Images");
            title.setTitleJustification(TitledBorder.CENTER);
            panel2.setBorder(title2);
            
            //postavljanje slika u panele; "images.jpg" je hardcodirana input slika na kojoj se vrši segmentacija
            ImageIcon image1 = new ImageIcon("images.jpg");
            JLabel label1 = new JLabel(image1);

            
            panel1.add(label1);
            //postavljanje layout-a, naslova i slika u panel2 - tu ce se prikazivati sehmentirane slike
            panel2.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();  
            c.weightx = 0.5;
            
            c.fill = GridBagConstraints.HORIZONTAL;
            
            JLabel labela = new JLabel("Binary Thresh");
            c.gridx = 0;
            c.gridy = 0;
            panel2.add(labela,c);
            JLabel labelb = new JLabel("Binary Inverted");
            c.gridx = 1;
            c.gridy = 0;
            panel2.add(labelb,c);
            JLabel labelc = new JLabel("Thresh to Zero");
            c.gridx = 2;
            c.gridy = 0;
            panel2.add(labelc,c);
            JLabel labeld = new JLabel("Thresh to Zero Inv");
            c.gridx = 3;
            c.gridy = 0;
            panel2.add(labeld,c);
            JLabel labele = new JLabel("Truncate Thresh");
            c.gridx = 4;
            c.gridy = 0;
            panel2.add(labele,c);
            
            c.gridx = 0;
            c.gridy = 1;
            panel2.add(label2,c);
            c.gridx = 1;
            c.gridy = 1;
            panel2.add(label3,c);
            c.gridx = 2;
            c.gridy = 1;
            panel2.add(label4,c);
            c.gridx = 3;
            c.gridy = 1;
            panel2.add(label5,c);
            c.gridx = 4;
            c.gridy = 1;
            panel2.add(label6,c);


            //pozivanje metode za segmentaciju slike - ovjde je pozivamo sa inicijalnom vrijednosti segmentacije (127,255)
            Mat source = SetSource();
            SegmentImageBinary(SEG_INIT,source);
            Mat sourceB = SetSource();
            SegmentImageBinaryInv(SEG_INIT,sourceB);
            Mat source0 = SetSource();
            SegmentImageZero(SEG_INIT,source0);
            Mat source1 = SetSource();
            SegmentImageZeroInverted(SEG_INIT,source1);
            Mat source2 = SetSource();
            SegmentImageTrunc(SEG_INIT,source2);
            
            //implementacija slidera
            final JSlider Segmentation = new JSlider(JSlider.HORIZONTAL,
               SEG_MIN, SEG_MAX, SEG_INIT);

            Segmentation.setMajorTickSpacing(20);
            Segmentation.setMinorTickSpacing(5);
            Segmentation.setPaintTicks(true);
            Segmentation.setPaintLabels(true);
            c.ipady = 20;
            c.gridwidth = 5;
            c.gridx = 0;
            c.gridy = 2;
            panel2.add(Segmentation,c);
            
            TitledBorder title3 = BorderFactory.createTitledBorder( blackline, "Move the slider to change thresholding value");
            title.setTitleJustification(TitledBorder.CENTER);
            Segmentation.setBorder(title3);
            
            frame.setVisible(true);
            Segmentation.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                   //ovdje pozivamo metodu za segmentiranje slike sa vrijednosti koja je trenutno oznacena na slideru
                   Mat source = SetSource();
                   SegmentImageBinary(Segmentation.getValue(),source);
                   Mat sourceB = SetSource();
                   SegmentImageBinaryInv(Segmentation.getValue(),sourceB);
                   Mat source0 = SetSource();
                   SegmentImageZero(Segmentation.getValue(),source0);
                   Mat source1 = SetSource();
                   SegmentImageZeroInverted(Segmentation.getValue(),source1);
                   Mat source2 = SetSource();
                   SegmentImageTrunc(Segmentation.getValue(),source2);

                }
            });

        }catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }

    public static Mat SetSource() { 
        try {                
                image = ImageIO.read(new File("images.jpg"));
        } 
        catch (IOException e) {
                    System.out.println("error: " + e.getMessage());
        }

        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        Mat source = Highgui.imread("images.jpg", 
        Highgui.CV_LOAD_IMAGE_COLOR);
        return source;
      }

    //metoda za segmentaciju slike     
    public static void SegmentImageBinary(int seg, Mat source) { 

        //thresh binary funkcija
        Mat destination = source;
        Imgproc.threshold(source,destination,seg,255,Imgproc.THRESH_BINARY);

        //pretvaranje buffered image u sliku za prikaz
        BufferedImage image5=matToBufferedImage(destination);
        ImageIcon imageIcon1 = new ImageIcon(image5);
        label2.setIcon(imageIcon1);

    }
    public static void SegmentImageBinaryInv(int seg, Mat source) { 
        
        //thresh binary inverted funkcija
       Mat destination = source;
       Imgproc.threshold(source,destination,seg,255,Imgproc.THRESH_BINARY_INV);

       //pretvaranje buffered image u sliku za prikaz
       BufferedImage image6=matToBufferedImage(destination);
       ImageIcon imageIcon2 = new ImageIcon(image6);
       label3.setIcon(imageIcon2);

    }
    public static void SegmentImageZero(int seg, Mat source) { 

        //thresh to zefo funkcija
        Mat destination = source;
        Imgproc.threshold(source,destination,seg,255,Imgproc.THRESH_TOZERO);

        //pretvaranje buffered image u sliku za prikaz
        BufferedImage image4=matToBufferedImage(destination);
        ImageIcon imageIcon = new ImageIcon(image4);
        label4.setIcon(imageIcon);   

    }
    public static void SegmentImageZeroInverted(int seg, Mat source) { 

        //thresh to zero inverted funkcija
        Mat destination = source;
        Imgproc.threshold(source,destination,seg,255,Imgproc.THRESH_TOZERO_INV);

        //pretvaranje buffered image u sliku za prikaz
        BufferedImage image7=matToBufferedImage(destination);
        ImageIcon imageIcon3 = new ImageIcon(image7);
        label5.setIcon(imageIcon3);
    }
    public static void SegmentImageTrunc(int seg, Mat source) { 

        //thresh truncate funkcija
        Mat destination = source;
        Imgproc.threshold(source,destination,seg,255,Imgproc.THRESH_TRUNC);

        //pretvaranje buffered image u sliku za prikaz
        BufferedImage image8=matToBufferedImage(destination);
        ImageIcon imageIcon4 = new ImageIcon(image8);
        label6.setIcon(imageIcon4);
    }
    //pretvaranje slike iz buffered image formata kojeg vraca algoritam za segmentaciju u icon image koji se prikazuje na sucelju
    public static BufferedImage matToBufferedImage(Mat bgr) {
        
        int width = bgr.width();
        int height = bgr.height();
        BufferedImage image;
        WritableRaster raster;

        if (bgr.channels()==1) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            raster = image.getRaster();

            byte[] px = new byte[1];

            for (int y=0; y<height; y++) {
                for (int x=0; x<width; x++) {
                    bgr.get(y,x,px);
                    raster.setSample(x, y, 0, px[0]);
                }
            }
        } 
        else {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            raster = image.getRaster();

            byte[] px = new byte[3];
            int[] rgb = new int[3];

            for (int y=0; y<height; y++) {
                for (int x=0; x<width; x++) {
                    bgr.get(y,x,px);
                    rgb[0] = px[2];
                    rgb[1] = px[1];
                    rgb[2] = px[0];
                    raster.setPixel(x,y,rgb);
                }
            }
        }

        return image;
    }
}



