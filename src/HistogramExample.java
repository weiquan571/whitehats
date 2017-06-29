import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import org.jfree.chart.*;
import org.jfree.data.statistics.*;
import org.jfree.chart.plot.PlotOrientation;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class HistogramExample {
    public static void main(String[] args) {
        /*
        File imgPath = new File("C:\\Users\\Jonathan\\Downloads\\Day2\\ShoppingCartEx1\\Design1\\wh\\src\\LionInGrass_868x592_24.bmp");
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage .getRaster();
        DataBufferByte temp   = (DataBufferByte) raster.getDataBuffer();
        byte [] temp1 = temp.getData();
        */

        Path path = Paths.get("C:\\\\Users\\\\Jonathan\\\\Downloads\\\\Day2\\\\ShoppingCartEx1\\\\Design1\\\\wh\\\\src\\\\LionInGrass_868x592_24.bmp");
        try {
            byte[] temp1 = Files.readAllBytes(path);
            double [] data = toDoubleArray(temp1);
            for (int i=1; i < data.length; i++) {

                int number = 10;
                HistogramDataset dataset = new HistogramDataset();
                dataset.setType(HistogramType.RELATIVE_FREQUENCY);
                dataset.addSeries("Histogram",data,number);
                String plotTitle = "Histogram";
                String xaxis = "number";
                String yaxis = "value";
                PlotOrientation orientation = PlotOrientation.VERTICAL;
                boolean show = false;
                boolean toolTips = false;
                boolean urls = false;
                JFreeChart chart = ChartFactory.createHistogram( plotTitle, xaxis, yaxis,
                        dataset, orientation, show, toolTips, urls);
                int width = 500;
                int height = 300;
                try {
                    ChartUtilities.saveChartAsPNG(new File("histogram.PNG"), chart, width, height);
                } catch (IOException e) {}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        //double[] value = new double[100];
        //Random generator = new Random();

    }
    public static double[] toDoubleArray(byte[] byteArray){
        int times = Double.SIZE / Byte.SIZE;
        double[] doubles = new double[byteArray.length / times];
        for(int i=0;i<doubles.length;i++){
            doubles[i] = ByteBuffer.wrap(byteArray, i*times, times).getDouble();
        }
        return doubles;
    }
}