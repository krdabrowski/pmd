/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package javasymbols.testdata;


public class StaticNameCollision {

    public static final int Ola = 0;


    public static int Ola() {
        return 0;
    }


    public static class Ola {

    }
}
