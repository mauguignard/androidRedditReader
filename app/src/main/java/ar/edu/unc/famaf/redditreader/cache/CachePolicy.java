package ar.edu.unc.famaf.redditreader.cache;

import java.io.File;
import java.io.IOException;

/**
 * Created by mauguignard on 10/14/16.
 * Code based on: http://gabesechansoftware.com/disk-cacheing-on-android/
 */

interface CachePolicy<ValueType> {
    boolean write(File outputFile, ValueType value) throws IOException;
    ValueType read(File inputFile) throws IOException;
    long size(ValueType value);
}
