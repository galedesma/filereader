# FileReader

Source application designed to read .csv files and send a message for each line of a given file,
with that line as message payload. Currently only reads .csv files from an specific AWS S3 bucket.

## Options

The **filereader** has the following options:

***filereader.filename***

  The name of the file to download (String, default: `none`)