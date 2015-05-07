var exec = require('cordova/exec');

module.exports = {
     /**
      * Upload a file to a FTP server.
      *
      * @param file      The file to be uploaded to the ftp server.
      * @param url       The url for the ftp server.
      * @param success   The success callback.
      * @param failure   The failure callback.
      */
     upload: function(file, url, success, failure) {
          return exec(success, failure, "FtpClient", "upload", [file, url]);
     },

     /**
      * Download a ascii file from a FTP server.
      *
      * @param file      The ascii file to be downloaded from the ftp server.
      * @param url       The url for the ftp server.
      * @param success   The success callback.
      * @param failure   The failure callback. 
      */
     downloadAsciiFile: function(file, url, success, failure) {
          return exec(success, failure, "FtpClient", "downloadAsciiFile", [file, url]);
     },
     
     /**
      * Download a binary file from a FTP server.
      *
      * @param file      The binary file to be downloaded from the ftp server.
      * @param url       The url for the ftp server.
      * @param success   The success callback.
      * @param failure   The failure callback.
      */
     downloadBinaryFile: function(file, url, success, failure) {
          return exec(success, failure, "FtpClient", "downloadBinaryFile", [file, url]);
     },
     
     /**
      * Download a ascii file as an ascii string from a FTP server.
      *
      * @param url       The url for the ftp server.
      * @param success   The success callback.
      * @param failure   The failure callback.
      */
     downloadAsciiString: function(url, success, failure) {
          return exec(success, failure, "FtpClient", "downloadAsciiString", ["", url]);
     }
};
