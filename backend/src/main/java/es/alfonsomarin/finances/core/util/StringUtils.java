/*
 * Copyright 2016-2007 Alfonso Marin Lopez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  
 * 	  http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.alfonsomarin.finances.core.util;

/**
 * The type String utils.
 *
 * @author alfonso.marin.lopez
 */
public class StringUtils {
    /**
     * Sub string string.
     *
     * @param source the source
     * @param start  the start
     * @param end    the end
     * @return the string
     */
    public static String subString(String source, int start, int end){
        int realStart = source.length()>start?start:source.length();
        int realEnd = source.length()>end?end:source.length();
        return source.substring(realStart,realEnd);
    }
}
