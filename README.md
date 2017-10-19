[![Build Status](https://travis-ci.org/alfionso/finances.svg?branch=master)](https://travis-ci.org/alfionso/finances)
# README #



### The project ###

_**Finances**_ is an application to upload a bank statement with this features:
* Validation the data
* Transform the data
* Schedule the process
* Generate reports
* Sending emails with the process status

### How do I get set up in a develop environment? ###

* Clone project in local folder
* Import maven project in Intellij
* Go to release folder
* Run `mvn clean install` 
* Create a spring boot application and configure the **working directory** with the path to _source/release/target/finances-release_
* Run or debug the application
* Go to frontend folder
* Install the frontend with `npm install`
* Execute the frontend module with `npm run dev`
* Go to _localhost:8070_

### How do I get set up in production environment? 
* In the root folder run `mvn clean package -P release`
* Copy the file in _source/release/target/finances-release.zip_ to production folder ($INSTALL) and unzip
* Edit the file $INSTALL/finances-release/scripts/finances.sh and change:
    * INSTALL_DIR: $INSTALL/finances-release
* Execute in  $INSTALL/finances-release/scripts `chmod +x *.sh`
* run `./finances.sh start`


For more information go to [Wiki](https://github.com/alfionso/finances/wiki)


