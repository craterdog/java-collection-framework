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
        logger.info("Running Various Map Class Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    public static void tearDownClass() {
        logger.info("Completed Various Map Class Unit Tests.\n");
    }


    /**
     * This unit test method does simple functional and performance tests on the java.util and
     * craterdog.collections.primitives classes that support the java.util.Map interface.
     *
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     */
    @Test
    public void testMethods() throws InstantiationException, IllegalAccessException,
            NoSuchMethodException, java.lang.reflect.InvocationTargetException {
        logger.info("Beginning testMethods()...");

        Class<?>[] classes = {
            java.util.HashMap.class,
            java.util.LinkedHashMap.class,
            HashTable.class
        };

        for (Class<?> clazz : classes) {
            String classname = clazz.getName();
            logger.info("  Testing map class: {}", classname);
            long startTime = System.currentTimeMillis();

            int count = 1000;
            while (count-- > 0) {
                @SuppressWarnings("unchecked")
                Map<Integer, Integer> map = (Map<Integer, Integer>) clazz.getDeclaredConstructor().newInstance();

                // confirm that the map starts out empty
                assertTrue(map.isEmpty());

                // add in enough elements to get the map to resize its capacity
                for (int i = 0; i < 50; i++) {
                    map.put(i, i);
                    assertTrue(map.containsKey(i));
                    assertTrue(map.containsValue(i));
                    assertEquals(i, (int) map.get(i));
                    assertEquals(i, map.size() - 1);
                }

                // clone and compare
                if (map instanceof HashTable) {
                    HashTable<Integer, Integer> hashtable = (HashTable<Integer, Integer>) map;
                    @SuppressWarnings("unchecked")
                    HashTable<Integer, Integer> copy = (HashTable<Integer, Integer>) hashtable.clone();
                    assertEquals(map, copy);
                }

                // remove the elements one by one so that the map resizes its capacity back to the minimum
                for (int i = 49; i > 4; i--) {
                    map.remove(i);
                    assertEquals(i, map.size());
                }

                // add a few more associations
                Map<Integer, Integer> more = new HashTable<>();
                more.put(5, 5);
                more.put(6, 6);
                map.putAll(more);
                assertEquals(7, map.size());

                // clear out the rest of the elements
                map.clear();

                // confirm that the map is now empty
                assertTrue(map.isEmpty());
            }

            long duration = System.currentTimeMillis() - startTime;
            logger.info("  Testing of map class: {} completed in {} milliseconds.\n", classname, duration);
        }

        logger.info("Completed testMethods().\n");
    }

}
