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

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import org.ow2.bonita.connector.core.ConnectorError;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Hashtable;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Tests for {@link EncodeQRCode}.
 *
 * @author <a href="mailto:julien.nicoulaud@gmail.com">Julien Nicoulaud</a>
 * @since 0.1
 */
public class EncodeQRCodeTest {

    /**
     * Assert {@link net.nicoulaj.bonita.connectors.EncodeQRCode#validateValues()} throws an error if
     * the text is not set.
     */
    @Test
    public void testValidateValuesTextNotSet() {
        List<ConnectorError> errors = new EncodeQRCode().validateValues();
        assertFalse(errors.isEmpty());
    }

    /**
     * Assert {@link net.nicoulaj.bonita.connectors.EncodeQRCode#validateValues()} throws an error if
     * the text is blank.
     */
    @Test
    public void testValidateValuesTextBlank() {
        EncodeQRCode connector = new EncodeQRCode();
        connector.setText("");
        List<ConnectorError> errors = connector.validateValues();
        assertFalse(errors.isEmpty());
    }

    /**
     * Assert {@link net.nicoulaj.bonita.connectors.EncodeQRCode#validateValues()} does not throw any error
     * if parameters are valid.
     */
    @Test
    public void testValidateValuesValidParameters() {
        EncodeQRCode connector = new EncodeQRCode();
        connector.setText("text");
        List<ConnectorError> errors = connector.validateValues();
        assertTrue(errors.isEmpty());
    }

    /**
     * Data provider for {@link #testEncodeQRCodeText(String)}.
     *
     * @return
     */
    @DataProvider(name = "testEncodeQRCodeText")
    public Object[][] testAttributeExistsProvider() {
        return new Object[][]{{"testQREncoderConnector"},
                              {"test QR Encoder Connector with spaces"},
                              {"test QR Encoder Connector \n with new lines"},
                              {"test QR Encoder Connector with special characters: ù$*µ!§é"},
                              {"example@test.com"}};
    }

    /**
     * Encode a text using the connector, decode it, and assert values match.
     *
     * @param text the text to encode.
     * @throws Exception should not happen.
     */
    @Test(dataProvider = "testEncodeQRCodeText")
    public void testEncodeQRCodeText(String text) throws Exception {
        EncodeQRCode connector = new EncodeQRCode();
        connector.setText(text);
        connector.executeConnector();

        final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(MatrixToImageWriter.toBufferedImage(connector.getMatrix()))));
        final Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        final String decodedQRCode = new QRCodeReader().decode(bitmap, hints).getText();

        assertEquals(text, decodedQRCode);
    }
}
