import org.jfree.chart.title.TextTitle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TestHistogram {
//http://stackoverflow.com/a/12520104/714968

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new TestHistogram();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TestHistogram() throws IOException {


        //Path path = Paths.get("C:\\Users\\Jonathan\\Downloads\\Day2\\ShoppingCartEx1\\Design1\\wh\\src\\hid_lsb_4_LionInGrass_868x592_24.bmp");
        //Path path = Paths.get("C:\\Users\\Jonathan\\Downloads\\Day2\\ShoppingCartEx1\\Design1\\wh\\src\\LionInGrass_868x592_24.bmp");

        //Path path1 = Paths.get("C:\\Users\\Jonathan\\Downloads\\Day2\\ShoppingCartEx1\\Design1\\wh\\src\\abcdefg.txt");
        String msg = new String(Files.readAllBytes(Paths.get("C:\\Users\\Jonathan\\Desktop\\a\\wh\\src\\input.txt")));
        //byte[] msg = Files.readAllBytes(path1);
        //String msg = "";
        Stegno1 steg = new Stegno1();
        steg.encode("C:\\Users\\Jonathan\\Desktop","11","bmp","standard",msg);
        Steganography st = new Steganography();
        st.encode("C:\\Users\\Jonathan\\Desktop","11","bmp","enhanced",msg);
        //st.encode_with_byte("C:\\Users\\Jonathan\\Downloads\\Day2\\ShoppingCartEx1\\Design1\\wh\\src","LionInGrass_868x592_24","bmp","haha",msg);

        //String decodedmsg = st.decode("C:\\Users\\Jonathan\\Downloads\\Day2\\ShoppingCartEx1\\Design1\\wh\\src","haha");
        //System.out.println(decodedmsg);
        //Path path = Paths.get("C:\\Users\\Jonathan\\Downloads\\Day2\\ShoppingCartEx1\\Design1\\wh\\src\\haha.bmp");
        //Steganography st2 = new Steganography();


        try {
            Map<Byte,Integer> mapHistory11 = getTreeMap("C:\\Users\\Jonathan\\Desktop\\11.bmp");

            /*
            for ( Byte treekey : mapHistory.keySet()){
                System.out.println("key: " + treekey);
                System.out.println("Value: " + mapHistory.get(treekey));
            }

            */
            JFrame frame = new JFrame("original");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(new JScrollPane(new Graph(mapHistory11)));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);



        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Map<Byte,Integer> mapHistory = getTreeMap("C:\\Users\\Jonathan\\Desktop\\standard.bmp");

            /*
            for ( Byte treekey : mapHistory.keySet()){
                System.out.println("key: " + treekey);
                System.out.println("Value: " + mapHistory.get(treekey));
            }

            */
            JFrame frame = new JFrame("standard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(new JScrollPane(new Graph(mapHistory)));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);



        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Map<Byte,Integer> mapHistory1 = getTreeMap("C:\\Users\\Jonathan\\Desktop\\enhanced.bmp");

            /*
            for ( Byte treekey : mapHistory.keySet()){
                System.out.println("key: " + treekey);
                System.out.println("Value: " + mapHistory.get(treekey));
            }

            */
            JFrame frame = new JFrame("enchanced");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(new JScrollPane(new Graph(mapHistory1)));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected class Graph extends JPanel {

        protected static final int MIN_BAR_WIDTH = 4;
        private Map<Byte, Integer> mapHistory;

        public Graph(Map<Byte, Integer> mapHistory) {
            this.mapHistory = mapHistory;
            int width = (mapHistory.size() * MIN_BAR_WIDTH) + 11;
            Dimension minSize = new Dimension(width, 128);
            Dimension prefSize = new Dimension(width, 256);
            setMinimumSize(minSize);
            setPreferredSize(prefSize);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (mapHistory != null) {
                int xOffset = 5;
                int yOffset = 5;
                int width = getWidth() - 1 - (xOffset * 2);
                int height = getHeight() - 1 - (yOffset * 2);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawRect(xOffset, yOffset, width, height);
                int barWidth = Math.max(MIN_BAR_WIDTH,
                        (int) Math.floor((float) width
                                / (float) mapHistory.size()));
                System.out.println("width = " + width + "; size = "
                        + mapHistory.size() + "; barWidth = " + barWidth);
                int maxValue = 0;
                for (Byte key : mapHistory.keySet()) {
                    int value = mapHistory.get(key);
                    maxValue = Math.max(maxValue, value);
                }
                int xPos = xOffset;
                for (Byte key : mapHistory.keySet()) {
                    int value = mapHistory.get(key);
                    int barHeight = Math.round(((float) value
                            / (float) maxValue) * height);
                    //g2d.setColor(new Color(key, key, key));
                    int yPos = height + yOffset - barHeight;
//Rectangle bar = new Rectangle(xPos, yPos, barWidth, barHeight);
                    Rectangle2D bar = new Rectangle2D.Float(
                            xPos, yPos, barWidth, barHeight);
                    g2d.fill(bar);
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.draw(bar);
                    xPos += barWidth;
                }
                g2d.dispose();
            }
        }


    }
    public static Map<Byte,Integer> getTreeMap ( String filename) throws IOException {
        BufferedImage readImage = ImageIO.read(new File(filename));
        //byte imageData[] = get_byte_data(readImage); // Then I need to convert this to original image
        WritableRaster raster = readImage.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        byte [] temp1 = buffer.getData();
        //byte[] temp1 = Files.readAllBytes(path);
        Map<Byte,Integer> mapHistory = new TreeMap<Byte,Integer>();
        for ( byte b : temp1){
            if (mapHistory.get(b)!=null){
                int count = mapHistory.get(b);
                //int bb = (int)b;
                mapHistory.put(b,++count);
            } else {
                //int bb = (int)b;
                mapHistory.put(b,1);
            }
        }

        return mapHistory;
    }
    public static HashMap<Byte,Integer> getTreeMapWithByte ( byte[] image) throws IOException {
        //BufferedImage readImage = ImageIO.read(new File(filename));
        //byte imageData[] = get_byte_data(readImage); // Then I need to convert this to original image
        //WritableRaster raster = readImage.getRaster();
        //DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        //byte [] temp1 = buffer.getData();
        //byte[] temp1 = Files.readAllBytes(path);
        HashMap<Byte,Integer> mapHistory = new HashMap<>(255);
        for ( byte b : image){
            if (mapHistory.containsKey(b)){
                int count = mapHistory.get(b);
                //int bb = (int)b;
                mapHistory.put(b,++count);
            } else {
                //int bb = (int)b;
                mapHistory.put(b,1);
            }
        }

        return mapHistory;
    }
}