package org.trams.sicbang.common.utils;

import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by voncount on 4/14/2016.
 */
public class FileUtils {

    private static final int IMG_WIDTH = 150;
    private static final int IMG_HEIGHT = 150;

    private static final Logger logger = Logger.getLogger(FileUtils.class);

    public static String uploadFile(InputStream is, String basepath, String directory, String fileName) {
        try {
            String _fileName = UUID.randomUUID().toString() + fileName;
            FileCopyUtils.copy(is, new FileOutputStream(new File(basepath + directory, _fileName)));
            return directory + _fileName;
        } catch (Exception e) {
            logger.error(e);
            throw new ApplicationException(MessageResponse.EXCEPTION_FILEUPLOAD_FAILED);
        }
    }

    public static String[] uploadImage(InputStream is, String basepath, String directory) {
        try {
            logger.info("basepath : " + basepath);
            logger.info("directory : " + directory);


            String filename = UUID.randomUUID().toString() + ".jpg";
            String thumbname = "thumb_" + filename;

            BufferedImage originalImage = ImageIO.read(is);
            BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, originalImage.getType());

            Graphics2D graphics2D = resizedImage.createGraphics();
            graphics2D.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
            graphics2D.dispose();
            logger.info("basepath + directory, filename : " + basepath + directory + filename);
            ImageIO.write(originalImage, "jpg", new FileOutputStream(new File(basepath + directory, filename)));
            ImageIO.write(resizedImage, "jpg", new FileOutputStream(new File(basepath + directory, thumbname)));

            // return path
            return new String[]{directory + filename, directory + thumbname};
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e);
            logger.info("file error : " + e.getMessage());
            throw new ApplicationException(MessageResponse.EXCEPTION_FILEUPLOAD_FAILED);
        }
    }

}
