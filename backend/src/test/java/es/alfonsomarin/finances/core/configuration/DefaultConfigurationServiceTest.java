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

package es.alfonsomarin.finances.core.configuration;

import es.alfonsomarin.finances.TestConstants;
import es.alfonsomarin.finances.core.domain.notification.Recipient;
import es.alfonsomarin.finances.core.domain.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultConfigurationServiceTest {

    @InjectMocks
    private DefaultConfigurationService defaultConfigurationService;

    @Mock
    private User mockCurrentUser;

    /**
     * Set up.
     */
    @Before
    public void setUp(){
        ReflectionTestUtils.setField(defaultConfigurationService, "currentUser", mockCurrentUser);
    }

    @Test
    public void getListMailsRecipientsOK() {
        ReflectionTestUtils.setField(defaultConfigurationService, "defaultUserMail", TestConstants.SYSTEM_LIST_MAIL);

        List<Recipient> result = defaultConfigurationService.getListMailsRecipients();
        assertTrue(result.size()>0);
    }

    @Test
    public void getListMailsRecipientsException() {
        ReflectionTestUtils.setField(defaultConfigurationService, "defaultUserMail", null);

        List<Recipient> result = defaultConfigurationService.getListMailsRecipients();
        assertFalse(result.size()>0);
    }
}
