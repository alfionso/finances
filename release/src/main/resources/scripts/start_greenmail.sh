#!/bin/bash
# Copyright 2016-2007 Alfonso Marin Lopez.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at  
# 	  http://www.apache.org/licenses/LICENSE-2.0
#    
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
clear
source setenv.sh

echo -e "[INFO] starting greenmail"
cd $GREENMAIL_HOME
nohup java -Dgreenmail.setup.test.all -Dgreenmail.hostname=0.0.0.0 -Dgreenmail.auth.disabled -Dgreenmail.users=admin:1234@finances.com,user:1234@finances.com -Dgreenmail.verbose -jar $GREENMAIL_HOME/greenmail-standalone-1.5.0.jar &