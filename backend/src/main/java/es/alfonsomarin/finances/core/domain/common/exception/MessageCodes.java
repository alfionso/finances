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

package es.alfonsomarin.finances.core.domain.common.exception;

/**
 * Message codes
 *
 * @author alfonso.marin.lopez
 */
public final class MessageCodes {

    /* 1xx - Authentication/Authorization messages */

    /**
     * The constant CODE_AUTH_GENERIC.
     */
    public static final int CODE_AUTH_GENERIC = 100;
    /**
     * The constant CODE_AUTH_LOGIN.
     */
    public static final int CODE_AUTH_LOGIN = 101;
    /**
     * The constant CODE_AUTH_LOGOUT.
     */
    public static final int CODE_AUTH_LOGOUT = 102;
    /**
     * The constant CODE_AUTH_SESSION.
     */
    public static final int CODE_AUTH_SESSION = 103;
    /**
     * The constant CODE_ACCESS_DENIED.
     */
    public static final int CODE_ACCESS_DENIED = 105;


    /* 2xx - User slice messages */
    
    /**
     * The constant CODE_NOT_FOUND_USER.
     */
    public static final int CODE_NOT_FOUND_USER = 200;
    /**
     * The constant CODE_NOT_FOUND_ROLE.
     */
    public static final int CODE_NOT_FOUND_ROLE = 201;
    /**
     * The constant CODE_DUPLICATE_USER.
     */
    public static final int CODE_DUPLICATE_USER = 202;

    /* 3xx - Transaction message */
    /**
     * The constant CODE_NOT_FOUND_INSERT.
     */
    public static final int CODE_NOT_FOUND_TRANSACTION = 300;
    
    /* 4xx - Insert message */

    /**
     * The constant CODE_NOT_FOUND_INSERT.
     */
    public static final int CODE_NOT_FOUND_INSERT = 400;
    /**
     * The constant CODE_NOT_ALLOWED_UPDATE_INSERT.
     */
    public static final int CODE_NOT_ALLOWED_UPDATE_INSERT = 401;
    /**
     * The constant CODE_NOT_ALLOWED_START_INSERTION.
     */
    public static final int CODE_NOT_ALLOWED_START_INSERTION = 402;

    /**
     * The constant CODE_NOT_ALLOWED_DELETE_INSERT.
     */
    public static final int CODE_NOT_ALLOWED_DELETE_INSERT = 403;

    /* 5xx - Schedule message */

    /**
     * The constant CODE_BAD_DATETIME_SCHEDULE.
     */
    public static final int CODE_BAD_DATETIME_SCHEDULE = 500;
    /**
     * The constant CODE_SCHEDULE_UNSCHEDULE.
     */
    public static final int CODE_SCHEDULE_UNSCHEDULE=501;
    /**
     * The constant CODE_SCHEDULE_SCHEDULING.
     */
    public static final int CODE_SCHEDULE_SCHEDULING=502;
    /**
     * The constant CODE_SCHEDULE_MESSAGE_CONFIRMATION.
     */
    public static final int CODE_SCHEDULE_MESSAGE_CONFIRMATION=503;
    
    /* 6xx metadata message */

    /**
     * The constant CODE_METADATA_HEADER.
     */
    public static final int CODE_METADATA_HEADER = 600;
    
    
    /* 7xx batch message */
    /**
     * The constant CODE_BATCH_NOT_FOUND.
     */
    public static final int CODE_BATCH_NOT_FOUND = 700;
    /**
     * The constant CODE_BATCH_ALREADY_RUNNING_JOB.
     */
    public static final int CODE_BATCH_ALREADY_RUNNING_JOB = 701;
    /**
     * The constant CODE_BATCH_INTERNAL_ERROR.
     */
    public static final int CODE_BATCH_INTERNAL_ERROR = 702;
    
    /* 8xx validation message */
    
    /**
     * The constant CODE_VALIDATION_REGEX_INCORRECT.
     */
    public static final int CODE_VALIDATION_REGEX_INCORRECT = 800;
    /**
     * The constant CODE_VALIDATION_METADATA_FIELD_NOT_EXISTS.
     */
    public static final int CODE_VALIDATION_METADATA_FIELD_NOT_EXISTS = 801;
    /**
     * The constant CODE_VALIDATION_FIELD_EMPTY.
     */
    public static final int CODE_VALIDATION_FIELD_EMPTY = 802;
    /**
     * The constant CODE_VALIDATION_LIST_EMPTY.
     */
    public static final int CODE_VALIDATION_LIST_EMPTY = 803;
    /**
     * The constant CODE_VALIDATION_NOT_IN_LIST.
     */
    public static final int CODE_VALIDATION_NOT_IN_LIST = 804;
    /**
     * The constant CODE_VALIDATION_STRING_SIZE_ERROR.
     */
    public static final int CODE_VALIDATION_STRING_SIZE_ERROR = 805;
    
    /* 9xx - Technical back-end messages */
    
    /**
     * The constant CODE_UNEXPECTED_ERROR.
     */
    public static final int CODE_UNEXPECTED_ERROR = 900;
    /**
     * The constant CODE_NOT_ALLOWED.
     */
    public static final int CODE_NOT_ALLOWED = 901;
    /**
     * The constant CODE_VALIDATION_ERRORS.
     */
    public static final int CODE_VALIDATION_ERRORS = 902;
    /**
     * The constant CODE_DATA_INTEGRITY.
     */
    public static final int CODE_DATA_INTEGRITY = 903;
    /**
     * The constant CODE_BAD_REQUEST_CREATE.
     */
    public static final int CODE_BAD_REQUEST_CREATE = 904;
    /**
     * The constant CODE_BAD_REQUEST_UPDATE.
     */
    public static final int CODE_BAD_REQUEST_UPDATE = 905;
    /**
     * The constant CODE_BAD_REQUEST_LOCATION_MISMATCH.
     */
    public static final int CODE_BAD_REQUEST_LOCATION_MISMATCH = 906;

    // 907 reserved for FE - "There is no data to display"

    /**
     * The constant CODE_BAD_REQUEST_EMPTY_FILE.
     */
    public static final int CODE_BAD_REQUEST_EMPTY_FILE = 908;
    /**
     * The constant CODE_SECURITY_ERRORS.
     */
    public static final int CODE_SECURITY_ERRORS = 909;
    /**
     * The constant CODE_BAD_REQUEST_NOT_ALLOWED_CONTENT_TYPE_FILE.
     */
    public static final int CODE_BAD_REQUEST_NOT_ALLOWED_CONTENT_TYPE_FILE =910;
    /**
     * The constant CODE_BAD_REQUEST_NOT_ALLOWED_EXTENSION_FILE.
     */
    public static final int CODE_BAD_REQUEST_NOT_ALLOWED_EXTENSION_FILE =911;
    /**
     * The constant CODE_BAD_REQUEST_NOT_ALLOWED_NAME_FILE.
     */
    public static final int CODE_BAD_REQUEST_NOT_ALLOWED_NAME_FILE =912;


    // Error message keys
    
    /**
     * The constant VALIDATION_PATH_MANDATORY.
     */
    public static final String VALIDATION_PATH_MANDATORY = "{validation.path.mandatory}";
    /**
     * The constant VALIDATION_MANDATORY.
     */
    public static final String VALIDATION_MANDATORY = "{validation.mandatory}";
    /**
     * The constant VALIDATION_MANDATORY_LESSER_THAN.
     */
    public static final String VALIDATION_MANDATORY_LESSER_THAN = "{validation.mandatoryLesserThan}";
    /**
     * The constant VALIDATION_WRONG_EMAIL.
     */
    public static final String VALIDATION_WRONG_EMAIL = "{validation.wrongEmail}";

    
    
    private MessageCodes() {
    }
}
