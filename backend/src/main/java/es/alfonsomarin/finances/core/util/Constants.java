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

import java.math.RoundingMode;

/**
 * The type Constants.
 *
 * @author alfonso.marin.lopez
 */
public final class Constants {


    private Constants() {
    }

    /**
     * The constant SCALE.
     */
    //Scale and rounding properties
    public static final int SCALE = 2;
    
    /**
     * The constant ROUNDING.
     */
    public static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    /**
     * The constant MAX_STRING.
     */
    public static final int MAX_STRING = 255;
    /**
     * The constant MIN_PERCENT.
     */
    public static final int MIN_PERCENT = 0;
    /**
     * The constant MAX_PERCENT.
     */
    public static final int MAX_PERCENT = 100;
    /**
     * The constant MAX_USERNAME.
     */
    public static final int MAX_USERNAME = 32;
    /**
     * The constant MAX_DESCRIPTION.
     */
    public static final int MAX_DESCRIPTION = 4096;
    
    /**
     * The constant ERROR.
     */
    public static final String ERROR = "error";

    /**
     * The constant RESPONSE_CONFLICT.
     */
    public static final String RESPONSE_CONFLICT = "Conflict";
    /**
     * The constant RESPONSE_OK.
     */
    public static final String RESPONSE_OK = "Ok";
    /**
     * The constant RESPONSE_CREATED.
     */
    public static final String RESPONSE_CREATED = "Created";
    /**
     * The constant RESPONSE_NO_CONTENT.
     */
    public static final String RESPONSE_NO_CONTENT = "No content";
    /**
     * The constant RESPONSE_BAD_REQUEST.
     */
    public static final String RESPONSE_BAD_REQUEST = "Bad request";
    /**
     * The constant RESPONSE_NOT_FOUND.
     */
    public static final String RESPONSE_NOT_FOUND = "Not found";
    /**
     * The constant DOT.
     */
    public static final String DOT = ".";
    /**
     * The constant HASH.
     */
    public static final String HASH = "#";
    /**
     * The constant COMMA.
     */
    public static final String COMMA = ",";
    /**
     * The constant SLASH.
     */
    public static final String SLASH = "/";
    /**
     * The constant HYPHEN.
     */
    public static final String HYPHEN = " - ";
    /**
     * The constant UNDERSCORE.
     */
    public static final String UNDERSCORE = "_";
    /**
     * The constant PERCENT.
     */
    public static final String PERCENT = "%";
    /**
     * The constant OPEN_BRACKET.
     */
    public static final String OPEN_BRACKET = "[";
    /**
     * The constant CLOSE_BRACKET.
     */
    public static final String CLOSE_BRACKET = "]";
    /**
     * The constant ANONYMOUS.
     */
    public static final String ANONYMOUS = "anonymous";
    /**
     * The constant UNKNOWN.
     */
    public static final String UNKNOWN = "unknown";

    /**
     * The constant MESSAGE.
     */
    public static final String MESSAGE = "message";

    /**
     * The constant EMPTY.
     */
    public static final String EMPTY = "^$|";
    private static final String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
    private static final String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)+";
    private static final String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";
    /**
     * The constant PATTERN.
     */
    public static final String PATTERN =
            "^" + ATOM + "+(\\." + ATOM + "+)*@"
                    + DOMAIN
                    + "|"
                    + IP_DOMAIN
                    + ")$";

    /**
     * The constant NON_PARSEABLE_ARGUMENT.
     */
    public static final String NON_PARSEABLE_ARGUMENT = "non parseable argument";

   

    /**
     * The constant SKD_GROUP_INSERT.
     */
    public static final String SKD_GROUP_INSERT = "sdkGroupInsert";
    /**
     * The constant SKD_TRIGGER_GROUP_INSERT.
     */
    public static final String SKD_TRIGGER_GROUP_INSERT = "sdkTriggerGroupInsert";
    /**
     * The constant SKD_PARAM_ID_INSERT.
     */
    public static final String SKD_PARAM_ID_INSERT = "skdParamIdInsert";

    /**
     * The constant JOB_PARAM_PATH_FILE.
     */
    // batch constants
    public static final String JOB_PARAM_PATH_FILE = "jobParamPathMetadata";
    /**
     * The constant JOB_PARAM_ID_INSERT_REQUEST.
     */
    public static final String JOB_PARAM_ID_INSERT_REQUEST = "jobParamIDInsertRequest";
   
}
