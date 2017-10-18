#! /bin/bash
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
# description: Finances startup script



INSTALL_DIR=${INSTALL_DIR}

case "$1" in
  start)
        proc_find=`ps -ef | grep 'backend.jar'| grep -v grep`
        if [ "${proc_find}" = '' ]
        then
            cd ${INSTALL_DIR}
            nohup java -jar backend.jar > /dev/null 2>&1 &
            echo Finances started
        else
            echo Finances already started:
            echo ${proc_find}
            exit 1
        fi
        ;;
  stop)
        process=`ps -ef | grep 'backend.jar' | grep -v grep | awk '{print $2}'`
        if [ "${process}" != '' ]
        then
            kill -9 ${process}
            echo Finances stopped
        else
            echo Finances not running
            exit 1
        fi
        ;;
  status)
        proc_find=`ps -ef | grep 'backend.jar'| grep -v grep`
        if [ "${proc_find}" = '' ]
        then
              echo Finances stopped
        else
              echo Finances started:
              echo ${proc_find}
        fi
        ;;
  restart)
        $0 stop
        sleep 2
        $0 start
        ;;
  *)
        echo $"Usage: $0 {start|stop|restart|status}"
        exit 1
esac

exit 0