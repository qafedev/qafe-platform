/**
 * Copyright 2008-2016 Qualogy Solutions B.V.
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
package com.qualogy.qafe.bind.presentation.style;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.jibx.runtime.IUnmarshallingContext;

import com.googlecode.lesscss4j.LessCompiler;
import com.googlecode.lesscss4j.LessException;
import com.qualogy.qafe.bind.PostProcessing;
import com.qualogy.qafe.bind.core.messages.HasMessage;
import com.qualogy.qafe.bind.domain.BindBase;
import com.qualogy.qafe.bind.io.FileLocation;
import com.qualogy.qafe.bind.orm.jibx.BindException;

public class Style extends BindBase implements PostProcessing, StyleInjector, HasMessage {

    private static final Logger LOG = Logger.getLogger(Style.class.getName());

    private static final long serialVersionUID = 6890926230102215957L;

    protected String windowId;

    protected FileLocation styleFileLocation;

    private byte[] cssData = null;

    private ArrayList<String> messages = new ArrayList<String>();

    public byte[] getCssData() {
        return cssData;
    }

    public String getWindowId() {
        return windowId;
    }

    public Style() {
        super();
    }

    public Style(FileLocation styleFileLocation) {
        super();
        this.styleFileLocation = styleFileLocation;
    }

    public void setWindowId(String windowId) {
        this.windowId = windowId;
    }

    public FileLocation getStyleFileLocation() {
        return styleFileLocation;
    }

    public void performPostProcessing() {
        if (styleFileLocation == null || styleFileLocation.toURI() == null) {
            String message = "Style file location [" + styleFileLocation + "] points to a non-existing file";
            LOG.severe(message);
            messages.add(message);
            return;
        }

        InputStream inputStream = null;
        try {
            if (FileLocation.SCHEME_HTTP.equals(styleFileLocation.toURI().getScheme())) {
                URL url = new URL(styleFileLocation.toURI().toString());
                inputStream = url.openStream();
            } else {
                inputStream = new FileInputStream(new File(styleFileLocation.toURI()));

            }
            LessCompiler lessCompiler = new LessCompiler();

            String less = IOUtils.toString(inputStream);
            // Compile LESS input string to CSS output string
            String css = lessCompiler.compile(less);
            cssData = css != null ? css.getBytes() : null;
            LOG.info("Less CSS data read " + cssData);
        } catch (IOException e) {
            throw new BindException("Error occured on file [" + styleFileLocation + "]", e);
        } catch (LessException e) {
            throw new BindException("Error occured in less file [" + styleFileLocation + "]", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Problem closing input stream", e);
                }
            }
        }
    }

    public void setStyleFileLocation(FileLocation styleFileLocation) {
        this.styleFileLocation = styleFileLocation;
    }

    public void postset(IUnmarshallingContext context) {
        performPostProcessing();
    }

    public List<String> getMessages() {
        return messages;
    }

}
