/*
 * Copyright (c) 2010 Julien Nicoulaud <julien.nicoulaud@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.nicoulaj.bonita.connectors;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link org.ow2.bonita.connector.core.Connector} that encodes a {@link #text} to a QR code.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 0.1
 */
public class EncodeQRCode extends ProcessConnector {

    /**
     * The default generated QR code image width.
     */
    private static final int QR_CODE_DEFAULT_WIDTH = 256;

    /**
     * The default generated QR code image height.
     */
    private static final int QR_CODE_DEFAULT_HEIGHT = 256;

    /**
     * The text to encode.
     */
    private String text;

    /**
     * The resulting bit matrix.
     */
    private BitMatrix matrix;

    /**
     * Validate the input values.
     * </p>
     * Only checks the {@link #text} is not blank.
     *
     * @return the list of validation errors.
     */
    @Override
    protected List<ConnectorError> validateValues() {
        List<ConnectorError> errors = new ArrayList<ConnectorError>();
        if (text == null || text.length() == 0) {
            errors.add(new ConnectorError("text", new Exception("text must not be empty")));
        }
        return errors;
    }

    /**
     * Encode the value of {@link #text} as a QR code.
     *
     * @throws Exception if the QR code could not be encoded for some reason.
     */
    @Override
    protected void executeConnector() throws Exception {
        matrix = null;
        try {
            matrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, QR_CODE_DEFAULT_WIDTH, QR_CODE_DEFAULT_HEIGHT); // TODO add width and height as parameters of the connector
        } catch (WriterException e) {
            throw new Exception("Failed generating QR code", e);
        }
    }

    /**
     * The value of the text to be encoded as a QR code.
     *
     * @return the value of {@link #text}.
     */
    public String getText() {
        return text;
    }

    /**
     * Set the text to be encoded as a QR code.
     *
     * @param text the value to use.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the resulting QR code encoded {@link BitMatrix}.
     * <p/>
     * The matrix can then be used this way:
     * <pre>
     * MatrixToImageWriter.writeToFile(matrix,"PNG",new File("some/path.png")); // Write to a file
     * MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream); // Write to a stream
     * BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix); // Get a BufferedImage
     * </pre>
     *
     * @return
     */
    public BitMatrix getMatrix() {
        return matrix;
    }
}
