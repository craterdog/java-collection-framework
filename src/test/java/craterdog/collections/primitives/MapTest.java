/*
 * The MIT License
 *
 * Copyright 2014 Crater Dog Technologies(TM).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package craterdog.collections.primitives;

import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This unit test class tests each of the methods defined in table classes.
 *
 * @author Derk
 */
public class MapTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(MapTest.class);


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    public static void setUpClass() {
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    public static void tearDownClass() {
    }


    @Test
    public void testMethods() throws InstantiationException, IllegalAccessException {
        Class<?>[] classes = {
            java.util.Hashtable.class,
            java.util.HashMap.class,
            java.util.LinkedHashMap.class,
            HashTable.class
        };

        for (Class<?> clazz : classes) {
            String classname = clazz.getName();
            logger.info("Testing map class: {}", classname);
            long startTime = System.currentTimeMillis();

            int count = 1000;
            while (count-- > 0) {
                @SuppressWarnings("unchecked")
                Map<Integer, Integer> map = (Map<Integer, Integer>) clazz.newInstance();

                // confirm that the map starts out empty
                assertTrue(map.isEmpty());

                // add in enough elements to get the map to resize its capacity
                for (int i = 0; i < 50; i++) {
                    map.put(i, i);
                    assertTrue(map.containsKey(i));
                    assertTrue(map.containsValue(i));
                    assertEquals(i, (int) map.get(i));
                    assertEquals(i, map.size() - 1);
                    if (map instanceof HashTable) {
                        HashTable<Integer, Integer> table = (HashTable<Integer, Integer>) map;
                        logger.info("Number of collisions: {}", table.collisions());
                    }
                }

                // remove the elements one by one so that the map resizes its capacity back to the minimum
                for (int i = 49; i > 4; i--) {
                    map.remove(i);
                    assertEquals(i, map.size());
                }

                // clear out the rest of the elements
                map.clear();

                // confirm that the map is now empty
                assertTrue(map.isEmpty());
            }

            long duration = System.currentTimeMillis() - startTime;
            logger.info("Testing of map class: {} completed in {} milliseconds.\n", classname, duration);
        }
    }

}
