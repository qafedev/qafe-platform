/**
 * Copyright 2008-2014 Qualogy Solutions B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qualogy.qafe.core.xml;

/*
 * $Id: Ux2Dos.java,v 1.1 2002/08/19 18:28:51 jjbarton Exp $
 * 
 * Copyright 1997 Hewlett-Packard Company
 * 
 * This file may be copied, modified and distributed only in
 * accordance with the terms of the limited licence contained
 * in the accompanying file LICENSE.TXT.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ux2Dos {

    private static final Logger LOG = Logger.getLogger(Ux2Dos.class.getName());
    
    /**
     * dos2ux <file> ...
     * 
     * <p>
     * Converts Unix ASCII files to Windows format.
     * 
     * <p>
     * Reads each specified file in sequence and writes it to System.out (standard output) converting all
     * occurrences of the pattern [\r]*[\n] (any number of carriage returns followed by a line feed) to a CRLF
     * sequence.
     * 
     * <p>
     * If no input file is given or if the argument - is encountered, ux2dos reads from System.in (standard
     * input). Standard input can be combined with other files.
     * 
     * @author Anders Kristensen
     */

    // CR (carriage-return) is US-ASCII 13 and LF (line-feed) is US-ASCII 10
    public static final int CR = '\r';

    public static final int LF = '\n';

    /*
     * public static void main(String[] args) throws IOException { int errs = 0; Reader in = null; Writer out
     * = new BufferedWriter(new OutputStreamWriter(System.out));
     * 
     * if (args.length == 0) { ux2dos(new BufferedReader(new InputStreamReader(System.in)), out); }
     * 
     * for (int i = 0; i < args.length; i++) { try { if ("-".equals(args[i])) { in = new BufferedReader(new
     * InputStreamReader(System.in)); } else { in = new BufferedReader(new FileReader(args[i])); } ux2dos(in,
     * out); in.close(); } catch (IOException e) { System.err.println("Couldn't open file " + args[i]);
     * errs++; } }
     * 
     * try { out.close(); } catch (IOException e) { }
     * 
     * if (errs > 0) { System.exit(2); } else { System.exit(0); } }
     */

    public static void ux2dos(InputStream in, OutputStream out) {
        Reader reader = new BufferedReader(new InputStreamReader(in));
        Writer writer = new BufferedWriter(new OutputStreamWriter(out));

        try {
            // FIXME reader/writers might not close in case of IOE
            ux2dos(reader, writer);
            reader.close();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Problem performing ux2dos", e);
        }
    }

    public static void ux2dos(Reader in, Writer out) throws IOException {
        boolean was_cr = false; // true iff previous char was carriage
        // return: '\r'
        int ch = 0;

        while ((ch = in.read()) != -1) {
            if (ch == '\n' && !was_cr) {
                out.write('\r');
                out.write('\n');
            } else if (ch == '\r') {
                if (!was_cr)
                    out.write('\r');
                was_cr = true;
            } else {
                out.write(ch);
                was_cr = false;
            }
        }
    }
}
