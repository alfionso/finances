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

package es.alfonsomarin.finances;

/**
 * The type Test constants.
 *
 * @author alfonso.marin.lopez
 */
public final class TestConstants {


    private TestConstants() {
    }

    /**
     * The constant ASSERT_NULL.
     */
    public static final String ASSERT_NULL="Expected return NULL";
    /**
     * The constant ASSERT_NOT_NULL.
     */
    public static final String ASSERT_NOT_NULL = "Expected return object";
    /**
     * The constant ASSERT_EQUALS.
     */
    public static final String ASSERT_EQUALS = "The objects has to be equals";
    /**
     * The bacth file.
     */
    public static final String BATCH_FILE = "file1.csv";
    /**
     * The Job instance id.
     */
    public static final Long JOB_INSTANCE_ID = 1L;
    /**
     * The Job instance test.
     */
    public static final String JOB_INSTANCE_NAME = "test_instance";
    /**
     * The test Value.
     */
    public static final String TEST_VALUE = "test_value";
    /**
     * The Notification Test value.
     */
    public static final String NOTIFICATION_MESSAGE = "Test notificaion";
    /**
     * The Notification user name.
     */
    public static final String NOTIFICATION_USER = "User Notification Test";
    /**
     * The Notification mail user.
     */
    public static final String NOTIFICATION_MAIL_USER = "test@localhost.com";
    /**
     * The Notification mail user.
     */
    public static final String NOTIFICATION_MAIL_SENDER = "admin@localhost.com";
    /**
     * The Notification mail user.
     */
    public static final String SYSTEM_LIST_MAIL = "test1@localhost.com,test2@localhost.com";
    /**
     * The Notification mail port.
     */
    public static final int NOTIFICATION_MAIL_PORT = 3025;
    /**
     * The metadata path file.
     */
    public static final String META_PATH_FILE = "./";
    
    /**
     * The create Step Execution metadata.
     */
    public static final String STEP_EXECUTION_NAME = "Step_test_1";
    /**
     * The constant META_TEST_RESOURCE.
     */
    public static final String META_TEST_RESOURCE = "src/test/resources/upload/sample-metadata.csv";
    /**
     * The constant META_TEST_RESOURCE_NAME.
     */
    public static final String META_TEST_RESOURCE_NAME = "sample-metadata.csv";
    /**
     * The constant TEST_CONF_PATH.
     */
    public static final String TEST_CONF_PATH = "src/test/resources/";
    /**
     * The constant REEMARKER_SUCCESS_TEMPLATE_FILENAME.
     */
    public static final String REEMARKER_SUCCESS_TEMPLATE_FILENAME = "insert_finish_template.html";
    /**
     * The constant FREEMARKER_TEMPLATE_FOLDER.
     */
    public static final String FREEMARKER_TEMPLATE_FOLDER = TEST_CONF_PATH + "templates";


}
