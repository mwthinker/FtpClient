# FtpClient - A cordova plugin for android
A ftp client implemented as a cordova plugin for android.

Can be used to download/upload a file from/to a server. 
Installation of this plugin gives the appliction permission to save/load files to/from disc.

## License
Is under the MIT license, see LICENSE.TXT.

## Installation

    $ cd MyCordovaProject
    $ cordova plugin add https://github.com/mwthinker/FtpClient.git
    
## Uninstallation
 
    $ cd MyCordovaProject
    $ cordova plugin remove se.mwthinker.ftpclient
    
## Javascript API

```js
/**
* Upload a file to a FTP server.
*
* @param file      The file to be uploaded to the ftp server.
* @param url       The url for the ftp server.
* @param success   The success callback.
* @param failure   The failure callback.
*/
function upload(file, url, success, failure);

/**
* Download an ascii file from a FTP server.
*
* @param file      The ascii file to be downloaded from the ftp server.
* @param url       The url for the ftp server.
* @param success   The success callback.
* @param failure   The failure callback. 
*/
function downloadAsciiFile(file, url, success, failure);
     
/**
* Download a binary file from a FTP server.
*
* @param file      The binary file to be downloaded from the ftp server.
* @param url       The url for the ftp server.
* @param success   The success callback.
* @param failure   The failure callback.
*/
function downloadBinaryFile(file, url, success, failure);

/**
* Download an ascii file as an ascii string from a FTP server.
*
* @param url       The url for the ftp server.
* @param success   The success callback.
* @param failure   The failure callback.
*/   
function downloadAsciiString(url, success, failure);
```

## Usage example
Edit `www/js/index.js` and add the following code inside `onDeviceReady`

```js
var success = function(asciiText) {
    alert(asciiText); // Whole file is written in the alert message.
}

var failure = function() {
    alert("Error calling ftpClient");
}

// Variable ftpClient is defined by the plugin at appliction start up.
ftpClient.downloadAsciiString("ftp://user:password@host:port/path ", success, failure);
```

## Acknowledgment
This plugin was created with help from an older ftp plugin found at http://simonmacdonald.blogspot.se/2011/06/ftp-plugin-for-phonegap-android.html.
Special thanks to its author Simon Mcdonald for providing the guide.

## More Info
For more information on setting up Cordova see [the documentation](http://cordova.apache.org/docs/en/4.0.0/guide_cli_index.md.html#The%20Command-Line%20Interface).
For more info on plugins see the [Plugin Development Guide](http://cordova.apache.org/docs/en/4.0.0/guide_hybrid_plugins_index.md.html#Plugin%20Development%20Guide)
