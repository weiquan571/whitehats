
/*
 *@author  William_Wilson
 *@version 1.6
 *Created: May 8, 2007
 */

/*
 *import list
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;


/*
 *Class Steganography
 */
public class Steganography
{

    /*
     *Steganography Empty Constructor
     */
    public Steganography()
    {
    }

    /*
     *Encrypt an image with text, the output file will be of type .bmp
     *@param path		 The path (folder) containing the image to modify
     *@param original	The name of the image to modify
     *@param ext1		  The extension type of the image to modify (jpg, bmp)
     *@param stegan	  The output name of the file
     *@param message  The text to hide in the image
     *@param type	  integer representing either basic or advanced encoding
     */
    public boolean encode(String path, String original, String ext1, String stegan, String message)
    {
        String			file_name 	= image_path(path,original,ext1);
        BufferedImage 	image_orig	= getImage(file_name);

        //user space is not necessary for Encrypting
        BufferedImage image = user_space(image_orig);
        image = add_text(image,message);

        return(setImage(image,new File(image_path(path,stegan,"bmp")),"bmp"));
    }
    public boolean encode_with_byte(String path, String original, String ext1, String stegan, byte[] message)
    {
        String			file_name 	= image_path(path,original,ext1);
        BufferedImage 	image_orig	= getImage(file_name);

        //user space is not necessary for Encrypting
        BufferedImage image = user_space(image_orig);
        image = add_text_without_string(image,message);

        return(setImage(image,new File(image_path(path,stegan,"bmp")),"bmp"));
    }

    /*
     *Decrypt assumes the image being used is of type .bmp, extracts the hidden text from an image
     *@param path   The path (folder) containing the image to extract the message from
     *@param name The name of the image to extract the message from
     *@param type integer representing either basic or advanced encoding
     */
    public String decode(String path, String name)
    {
        byte[] decode;
        try
        {
            //user space is necessary for decrypting
            BufferedImage image  = user_space(getImage(image_path(path,name,"bmp")));
            decode = decode_text(get_byte_data(image));
            return(new String(decode));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "There is no hidden message in this image!","Error",
                    JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    /*
     *Returns the complete path of a file, in the form: path\name.ext
     *@param path   The path (folder) of the file
     *@param name The name of the file
     *@param ext	  The extension of the file
     *@return A String representing the complete path of a file
     */
    private String image_path(String path, String name, String ext)
    {
        return path + "/" + name + "." + ext;
    }

    /*
     *Get method to return an image file
     *@param f The complete path name of the image.
     *@return A BufferedImage of the supplied file path
     *@see	Steganography.image_path
     */
    private BufferedImage getImage(String f)
    {
        BufferedImage 	image	= null;
        File 		file 	= new File(f);

        try
        {
            image = ImageIO.read(file);
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,
                    "Image could not be read!","Error",JOptionPane.ERROR_MESSAGE);
        }
        return image;
    }

    /*
     *Set method to save an image file
     *@param image The image file to save
     *@param file	  File  to save the image to
     *@param ext	  The extension and thus format of the file to be saved
     *@return Returns true if the save is succesful
     */
    private boolean setImage(BufferedImage image, File file, String ext)
    {
        try
        {
            file.delete(); //delete resources used by the File
            ImageIO.write(image,ext,file);
            return true;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "File could not be saved!","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /*
     *Handles the addition of text into an image
     *@param image The image to add hidden text to
     *@param text	 The text to hide in the image
     *@return Returns the image with the text embedded in it
     */
    private BufferedImage add_text(BufferedImage image, String text)
    {
        //convert all items to byte arrays: image, message, message length
        byte img[]  = get_byte_data(image);
        byte msg[] = text.getBytes();
        //byte len[]   = bit_conversion(msg.length);
        try
        {
            //encode_text(img, len,  0); //0 first positiong
            encode_text(img, msg, 0); //4 bytes of space for length: 4bytes*8bit = 32 bits
        }
        catch(Exception e)
        {
            /*
            JOptionPane.showMessageDialog(null,
                    "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
                    */
            e.printStackTrace();
        }
        return image;
    }
    private BufferedImage add_text_without_string(BufferedImage image, byte[] text)
    {
        //convert all items to byte arrays: image, message, message length
        byte img[]  = get_byte_data(image);
        //byte msg[] = text.getBytes();
        //byte len[]   = bit_conversion(text.length);
        try
        {
            //encode_text(img, len,  0); //0 first positiong
            encode_text(img, text, 0); //4 bytes of space for length: 4bytes*8bit = 32 bits
        }
        catch(Exception e)
        {
            //JOptionPane.showMessageDialog(null,
                    //"Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return image;
    }

    /*
     *Creates a user space version of a Buffered Image, for editing and saving bytes
     *@param image The image to put into user space, removes compression interferences
     *@return The user space version of the supplied image
     */
    private BufferedImage user_space(BufferedImage image)
    {
        //create new_img with the attributes of image
        BufferedImage new_img  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D	graphics = new_img.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose(); //release all allocated memory for this image
        return new_img;
    }

    /*
     *Gets the byte array of an image
     *@param image The image to get byte data from
     *@return Returns the byte array of the image supplied
     *@see Raster
     *@see WritableRaster
     *@see DataBufferByte
     */
    private byte[] get_byte_data(BufferedImage image)
    {
        WritableRaster raster   = image.getRaster();
        DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
        return buffer.getData();
    }

    /*
     *Gernerates proper byte format of an integer
     *@param i The integer to convert
     *@return Returns a byte[4] array converting the supplied integer into bytes
     */
    private byte[] bit_conversion(int i)
    {
        //originally integers (ints) cast into bytes
        //byte byte7 = (byte)((i & 0xFF00000000000000L) >>> 56);
        //byte byte6 = (byte)((i & 0x00FF000000000000L) >>> 48);
        //byte byte5 = (byte)((i & 0x0000FF0000000000L) >>> 40);
        //byte byte4 = (byte)((i & 0x000000FF00000000L) >>> 32);

        //only using 4 bytes
        byte byte3 = (byte)((i & 0xFF000000) >>> 24); //0
        byte byte2 = (byte)((i & 0x00FF0000) >>> 16); //0
        byte byte1 = (byte)((i & 0x0000FF00) >>> 8 ); //0
        byte byte0 = (byte)((i & 0x000000FF)	   );
        //{0,0,0,byte0} is equivalent, since all shifts >=8 will be 0
        return(new byte[]{byte3,byte2,byte1,byte0});
    }

    /*
     *Encode an array of bytes into another array of bytes at a supplied offset
     *@param image	 Array of data representing an image
     *@param addition Array of data to add to the supplied image data array
     *@param offset	  The offset into the image array to add the addition data
     *@return Returns data Array of merged image and addition data
     */
    private byte[] encode_text(byte[] image, byte[] addition, int offset)
    {
        //check that the data + offset will fit in the image
        //if(addition.length + offset > image.length)
        //{
         //   throw new IllegalArgumentException("File not long enough!");
        //}
        //loop through each addition byte
        int imagelength = image.length;
        ArrayList<String> output = new ArrayList<>();
        HashMap<Byte,Integer> mapHistory = null;
        try {
            mapHistory = TestHistogram.getTreeMapWithByte(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0; i<addition.length; ++i)
        {
            //loop through the 8 bits of each byte
            int add = addition[i];
            for(int bit=7; bit>=0; --bit, ++offset) //ensure the new offset value carries on through both loops
            {

                if ( offset > imagelength){
                    System.out.println("END OF LINE");
                }


                //assign an integer to b, shifted by bit spaces AND 1
                //a single bit of the current byte
                int b = (add >>> bit) & 1;
                //assign the bit by taking: [(previous byte value) AND 0xfe] OR bit to add
                //changes the last bit of the byte in the image to be the bit of addition
                byte oldbyte = (image[offset]);
                int oldbytevalue = mapHistory.get(oldbyte);
                byte a = (byte)((image[offset] & 0xFE) | b );
                boolean isMiddleSpike= false;
                double valueLeft=0;
                double valueRight=0;
                double valueOldByteLeft=0;
                double valueOldByteRight=0;
                int value = mapHistory.get(a);
                try {
                    Byte getByte = a;
                    int leftvalue = getByte.intValue() - 1;
                    int rightvalue = getByte.intValue() + 1;
                    Byte getByte1 = oldbyte;
                    int oldByteLeft = getByte1.intValue() -1;
                    int oldByteRight = getByte1.intValue() + 1;

                    if ( (leftvalue>= -128 && leftvalue <=127 ) && (rightvalue>= -128 && rightvalue <=127)) { //if there are both left and right bits
                        byte a1 = (byte) (leftvalue); // convert new int value to byte value
                        byte a2 = (byte) (rightvalue);
                        //leftchecker =true;
                        valueLeft = mapHistory.get(a1);
                        valueRight = mapHistory.get(a2);
                        if (((value + 1) >= (valueLeft)) && ((value + 1) <= (valueRight))) {
                            if ((oldByteLeft >= -128 && oldByteLeft <= 127) && (oldByteRight >= -128 && oldByteRight <= 127)) {
                                byte a3 = (byte) (oldByteLeft);
                                byte a4 = (byte) (oldByteRight);
                                valueOldByteLeft = mapHistory.get(a3);
                                valueOldByteRight = mapHistory.get(a4);
                                if (((oldbytevalue - 1) >= (valueOldByteLeft)) && ((oldbytevalue - 1) <= (valueOldByteRight))) {
                                    image[offset] = a;
                                    mapHistory.put(a, ++value);
                                    mapHistory.put(oldbyte, --oldbytevalue);
                                    output.add(Integer.toString(offset));

                                } else if (((oldbytevalue - 1) <= (valueOldByteLeft)) && ((oldbytevalue - 1) >= (valueOldByteRight))) {
                                    image[offset] = a;
                                    mapHistory.put(a, ++value);
                                    mapHistory.put(oldbyte, --oldbytevalue);
                                    output.add(Integer.toString(offset));

                                } else {
                                    ++bit;
                                }
                            }else {
                                ++bit;
                            }

                        }else if (((value+1) <= ((valueLeft)))&& (( value+1) >= ((valueRight))) ) {
                            if ((oldByteLeft >= -128 && oldByteLeft <= 127) && (oldByteRight >= -128 && oldByteRight <= 127)) {
                                byte a3 = (byte) (oldByteLeft);
                                byte a4 = (byte) (oldByteRight);
                                valueOldByteLeft = mapHistory.get(a3);
                                valueOldByteRight = mapHistory.get(a4);
                                if (((oldbytevalue - 1) >= (valueOldByteLeft)) && ((oldbytevalue - 1) <= (valueOldByteRight))) {
                                    image[offset] = a;
                                    mapHistory.put(a, ++value);
                                    mapHistory.put(oldbyte, --oldbytevalue);
                                    output.add(Integer.toString(offset));

                                } else if (((oldbytevalue - 1) <= (valueOldByteLeft)) && ((oldbytevalue - 1) >= (valueOldByteRight))) {
                                    image[offset] = a;
                                    mapHistory.put(a, ++value);
                                    mapHistory.put(oldbyte, --oldbytevalue);
                                    output.add(Integer.toString(offset));

                                } else {
                                    ++bit;
                                }



                            }else {
                                ++bit;
                            }
                        } else {
                            ++bit;
                        }
                    }else {
                        ++bit;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }







                }



            }



        try (FileWriter f = new FileWriter("C:\\Users\\Jonathan\\Desktop\\stegno-index.txt", true);
             BufferedWriter bb = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(bb);)
        {
            for (String s: output){
                p.println(s);
            }


        }
        catch (IOException s)
        {
            s.printStackTrace();
        }

        return image;
    }


    /*
     *Retrieves hidden text from an image
     *@param image Array of data, representing an image
     *@return Array of data which contains the hidden text
     */
    private byte[] decode_text(byte[] image)
    {
        /*
        int length = 0;
        int offset = 32;
        //loop through 32 bytes of data to determine text length
        for(int i=0; i<32; ++i) //i=24 will also work, as only the 4th byte contains real data
        {
            length = (length << 1) | (image[i] & 1);
        }



        */

        ArrayList<Integer> list = new ArrayList<>();

        String fileName = "C:\\Users\\Jonathan\\Desktop\\stegno-index.txt";

        try (Scanner scanner = new Scanner(new File(fileName))) {

            while (scanner.hasNextLine()){

                list.add(Integer.parseInt(scanner.nextLine()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        int sizeoftext = list.size()/8;
        byte[] result = new byte[sizeoftext];
        //loop through each byte of text
        int counter = 0;
        for(int b=0; b<result.length; ++b )
        {
            //loop through each bit within a byte of text
            for(int i=0; i<8; ++i)
            {
                //assign bit: [(new byte value) << 1] OR [(text byte) AND 1]
                result[b] = (byte)((result[b] << 1) | (image[list.get(counter)] & 1));
                counter++;
            }
        }



        return result;
    }
    /*
                            try (FileWriter f = new FileWriter("C:\\Users\\Jonathan\\Desktop\\yourdad.txt", true);
                                 BufferedWriter bb = new BufferedWriter(f);
                                 PrintWriter p = new PrintWriter(bb);)
                            {

                                p.println("Byte:"+ a + "Left Value:" + valueLeft +" Value:" + value + " Right Value: " + valueRight);



                            }
                            catch (IOException s)
                            {
                                s.printStackTrace();
                            }
                            */
     /*
                if ( (leftchecker && rightchecker && leftcheck && rightcheck) || (leftchecker && rightchecker && leftcheck1 && rightcheck1 )
                        || (!leftchecker && rightchecker && rightcheck) ||
                        (!leftchecker && rightchecker && rightcheck1) || (!rightchecker && leftchecker && leftcheck ) ||
                        (!rightchecker && leftchecker && leftcheck1) ){
                    //System.out.println("Value:" + value +" left:" +valueLeft + " right: " + valueRight);
                    image[offset] = a;
                    //int valuea = mapHistory.get(a);
                    mapHistory.put(a,++value);
                    mapHistory.put(oldbyte,--oldbytevalue);
                    output.add(Integer.toString(offset));
                    //System.out.println("NOT IN RANGE");
                }else {
                    ++bit;
                    */
}
