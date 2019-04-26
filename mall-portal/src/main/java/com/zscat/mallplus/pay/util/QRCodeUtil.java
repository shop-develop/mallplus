//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zscat.mallplus.pay.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class QRCodeUtil {
    private static Logger log = LoggerFactory.getLogger(QRCodeUtil.class);

    public QRCodeUtil() {
    }

    public static void buildQRCode(String content, OutputStream out) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map<EncodeHintType, String> hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "jpg", out);
        } catch (Exception var5) {
            log.error("build qrCode error. content=" + content, var5);
        }

    }

    public static void buildQRCode(String content, HttpServletResponse response) {
        try {
            buildQRCode(content, (OutputStream)response.getOutputStream());
        } catch (Exception var3) {
            log.error("build qrCode error. content=" + content, var3);
        }

    }

    public static void buildQrCodeWithLogo(String content, String logoPath, OutputStream out) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map<EncodeHintType, String> hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);
            BufferedImage qrcode = MatrixToImageWriter.toBufferedImage(bitMatrix);
            addLogo(qrcode, logoPath);
            ImageIO.write(qrcode, "jpg", out);
        } catch (Exception var7) {
            log.error("build qrCode error. content=" + content, var7);
        }

    }

    private static void addLogo(BufferedImage qrcode, String logoPath) {
        try {
            BufferedImage logo = ImageIO.read(new File(logoPath));
            int widthLogo = logo.getWidth((ImageObserver)null) > qrcode.getWidth() * 13 / 100 ? qrcode.getWidth() * 13 / 100 : logo.getWidth((ImageObserver)null);
            int heightLogo = logo.getHeight((ImageObserver)null) > qrcode.getHeight() * 13 / 100 ? qrcode.getHeight() * 13 / 100 : logo.getHeight((ImageObserver)null);
            int x = (qrcode.getWidth() - widthLogo) / 2;
            int y = (qrcode.getHeight() - heightLogo) / 2;
            Graphics2D g = qrcode.createGraphics();
            g.drawImage(logo, x, y, widthLogo, heightLogo, (ImageObserver)null);
            g.dispose();
            logo.flush();
            qrcode.flush();
        } catch (IOException var8) {
            log.error("add logo to qrcode error. logoPath=" + logoPath);
        }

    }
}
