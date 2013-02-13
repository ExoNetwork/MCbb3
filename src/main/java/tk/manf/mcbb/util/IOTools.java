/**************************************************************************************************
 * Copyright (c) 2013, Björn Heinrichs <manf@derpymail.org>                                       *
 * Permission to use, copy, modify, and/or distribute this software                               *
 * for any purpose with or without fee is hereby granted, provided                                *
 * that the above copyright notice and this permission notice appear in all copies.               *
 *                                                                                                *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD           *
 * TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS.              *
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL     *
 * DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,                 *
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING                 *
 * OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.                          *
 **************************************************************************************************/

package tk.manf.mcbb.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOTools {
    /**
     * Copies an InputStream to a File. File will be created, if the File
     * already exists, then nothing happens at all and false will be returned
     *
     * Returns true for a successful copy Throws IOExeption if parent could not
     * be found Throws IOException if writing fails
     *
     * @param in
     * @param file
     * @return string
     * @throws IOException
     */
    public static boolean copyInputStreamToFile(InputStream in, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
            OutputStream out = new FileOutputStream(file);
            byte[] tmp = new byte[1024];
            int length;
            while ((length = in.read(tmp)) > 0) {
                out.write(tmp, 0, length);
            }
            out.close();
            in.close();
            return true;
        }
        return false;
    }

    /**
     * Reads a InputStream saves it in a String
     * <a href="http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string">Credits for thig go to TacB0sS</a>
     *
     * @param inputStream
     * @return string
     * @throws IOException
     */
    public static String toString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return new String(baos.toByteArray());
    }
}
