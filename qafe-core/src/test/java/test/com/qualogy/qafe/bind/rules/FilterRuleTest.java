/**
 * Copyright 2008-2017 Qualogy Solutions B.V.
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
package test.com.qualogy.qafe.bind.rules;

/**
 * Test functions of filtering rules
 */
import java.util.Calendar;
import java.util.logging.Logger;

import junit.framework.TestCase;

import com.qualogy.qafe.bind.io.Reader;
import com.qualogy.qafe.bind.io.Writer;
import com.qualogy.qafe.bind.rules.FilterRules;

public class FilterRuleTest extends TestCase {
    public final static String SAMPLES_DIR_PATH = "samples/";

    public final static String OUTPUT_DIR_PATH = "../output/";

    private Reader reader = new Reader(FilterRules.class, false);

    private final static Logger log = Logger.getLogger(FilterRuleTest.class.getName());


    public void testFilterRulesLoad() {
        String fileNameIn = "SampleFilterRules.xml";
        String fileNameOut = "marshalled-"+fileNameIn;
        marshallAndUnmarshall(SAMPLES_DIR_PATH + fileNameIn, fileNameOut);
    }

    /**
     * It does the following:
     *  1- Read a xml file and marshall it to the specified object.
     *  2- Write the generated object into the specified file. 
     * @param fileNamesIn file to read from
     * @param fileNameOut file to put the unmarshalled xml
     */
    private void marshallAndUnmarshall(String fileNamesIn, String fileNameOut){

        long millis = Calendar.getInstance().getTimeInMillis();

        FilterRules filterRules = (FilterRules) reader.read(fileNamesIn);
        long readtime = Calendar.getInstance().getTimeInMillis()-millis;
        new Writer().write(filterRules, OUTPUT_DIR_PATH, fileNameOut);
        long ttltime = Calendar.getInstance().getTimeInMillis()-millis;
        log.info(filterRules.toString());
        log.info("processed in:["+fileNamesIn+"], out["+fileNameOut+"], reading took ["+readtime+"]ms writting ["+(ttltime-readtime)+"]ms ttl.p.time ["+ttltime+"]ms");
    }


}
