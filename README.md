# letsdata-common-crawl
This package has implementation of the different record types for the different common-crawl filetypes. It also has #Let's Data interface implementations that can be used to process the common crawl datasets using the #Let's Data infrastructure.

Common Crawl is an open repository of web crawl data and serves as a fantastic resource for any project that needs to leverage the www web crawl data. Personally, I've found it a great data resource that can be used to build and test big data infrastructures that can be reused across other big data usecases. You can learn more about common crawl at: https://commoncrawl.org/

## Data Model Design Notes
This package defines enums to create a structure for the WARC, WAT and WET ("warc" file) files while parsing them into java objects

We are defining that a warc files has 2 types of records - a WARC record and a DOCUMENT record.

WARC Records are the different types of WARC records / annotations that are present in the warc files.

The different types of WARC Records are defined in WARCRecordTypes enum and have been taken from a webarchive-commons implementation.
(https://github.com/iipc/webarchive-commons/blob/master/src/main/java/org/archive/format/warc/WARCConstants.java#L106)

The different WARC Record Types are:
* warcinfo
* responseDocument,
* resource
* request
* metadata
* revisit
* conversion
* continuation

In the warc files, we have a WARC Records (of the above mentioned types) followed by a DOCUMENT record that the value document for the warc record type.

We've defined the following types for (DocumentRecordTypes.java) the DOCUMENT records

* `WARC_WARCINFO_PAYLOAD / WAT_WARCINFO_PAYLOAD / WET_WARCINFO_PAYLOAD:` These are the warcinfo document values for the warcinfo record - normally found at the start of each of the warc files defining the metadata of the file

### WARC files
* `WARC_HTTP_REQUEST_PAYLOAD:` This is the document for the Http Request in WARC file - essentially the http request headers
* `WARC_HTTP_RESPONSE_PAYLOAD:` This is the document for the Http Response in WARC file - essentially the http request headers and the HTML document
* `WARC_METADATA_PAYLOAD:` This is the document that contains the metadata values for the HTML document - values such as the detected charsets, languages and the confidence scores for the prediction
### WAT files
* `WAT_WARC_WARCINFO_PAYLOAD:` Document that has the different metadata attributes and values for the 'warcinfo' WARCRecordType in the WARC file
* `WAT_METADATA_WARC_REQUEST_PAYLOAD:` Document that has the different metadata attributes and values for the 'request' WARCRecordType in the WARC file
* `WAT_METADATA_WARC_RESPONSE_PAYLOAD:` Document that has the different metadata attributes and values for the 'responseDocument' WARCRecordType in the WARC file
* `WAT_METADATA_WARC_METADATA_PAYLOAD:` Document that has the different metadata attributes and values for the 'metadata' WARCRecordType in the WARC file
### WET files
* `WET_CONVERSION_PAYLOAD:` Document that has the converted document (such as plain text from a web page) for each WARC_HTTP_RESPONSE_PAYLOAD in the WARC file


<br/>
With the above definitions, we can define a structure of the warc files as it is parsed into POJOs as follows (this is essentially the state machine):

### WARC file structure
```
'warcinfo' WARC Record
WARC_WARCINFO_PAYLOAD Document Record

'request' WARC Record
WARC_HTTP_REQUEST_PAYLOAD Document Record
'responseDocument' WARC Record
WARC_HTTP_RESPONSE_PAYLOAD Document Record
'metadata' WARC Record
WARC_METADATA_PAYLOAD Document Record

'request' WARC Record
WARC_HTTP_REQUEST_PAYLOAD Document Record
'responseDocument' WARC Record
WARC_HTTP_RESPONSE_PAYLOAD Document Record
'metadata' WARC Record
WARC_METADATA_PAYLOAD Document Record

'request' WARC Record
WARC_HTTP_REQUEST_PAYLOAD Document Record
'responseDocument' WARC Record
WARC_HTTP_RESPONSE_PAYLOAD Document Record
'metadata' WARC Record
WARC_METADATA_PAYLOAD Document Record

...
```

### WAT file structure
```
'warcinfo' WARC Record
WAT_WARCINFO_PAYLOAD Document Record

'metadata' WARC Record
WAT_METADATA_WARC_WARCINFO_PAYLOAD Document Record

'metadata' WARC Record
WAT_METADATA_WARC_REQUEST_PAYLOAD Document Record
'metadata' WARC Record
WAT_METADATA_WARC_RESPONSE_PAYLOAD Document Record
'metadata' WARC Record
WAT_METADATA_WARC_METADATA_PAYLOAD Document Record

'metadata' WARC Record
WAT_METADATA_WARC_REQUEST_PAYLOAD Document Record
'metadata' WARC Record
WAT_METADATA_WARC_RESPONSE_PAYLOAD Document Record
'metadata' WARC Record
WAT_METADATA_WARC_METADATA_PAYLOAD Document Record

'metadata' WARC Record
WAT_METADATA_WARC_REQUEST_PAYLOAD Document Record
'metadata' WARC Record
WAT_METADATA_WARC_RESPONSE_PAYLOAD Document Record
'metadata' WARC Record
WAT_METADATA_WARC_METADATA_PAYLOAD Document Record

...
```

### WET file structure
```
'warcinfo' WARC Record
WET_WARCINFO_PAYLOAD Document Record

'conversion' WARC Record
WET_CONVERSION_PAYLOAD Document Record

'conversion' WARC Record
WET_CONVERSION_PAYLOAD Document Record

'conversion' WARC Record
WET_CONVERSION_PAYLOAD Document Record

...
```

## Implementation Notes
Overall the code in the package is divided into two distinct code sub-components and 1 test component:
* `The Common-Crawl Java POJOs:` These are java container object implementations of the different constructs that are in the common-crawl files. These do not have any #Let's Data artifacts. These POJOs can be reused by projects that may not be using #Let's Data
* `The #Let's Data Interface Implementations:` The #Let's Data interface implementations for DocumentInterfaces, Parsers and Readers etc. These use the POJOs defined in #1 to create an end to end record transformation implementation that is used by the #Let's Data infrastructure to process the files.
* `The bin directory:` The bin directory has a couple of interesting scripts - a build script to build the jar and a script to generate manifest. 
* `The Unit Tests:` The unit tests for the #Let's Data interface implementations. We've added tests for the happy cases only and at the topmost interface level with the hope that they will cover most of the POJOs and parsing logics. Negative cases and failure cases have not been added now due to resource / schedule crunch.

### The Common-Crawl Java POJOs
Here is how the code for the common crawl Java POJOs is structured in different packages:
* `com.letsdata.commoncrawl.model:` The files at the root of this package again are enums that are used in parsing - enums for different filetypes, headers, languages and language stats.
* `com.letsdata.commoncrawl.model.filerecords.docs:` The way we've defined the common crawl file parsing, a record has WARC record and DOCUMENT record. This package has the DOCUMENT container implementations for different WARC record documents. The WarcDoc serves as the abstract base class for all document implementations. The simpler documents (WarcHttpRequest, WarcHttpResponse, WarcMetadataDoc and WetConversionDoc) are at the root of this package. Complex docs such as WATMetadata records are in the sub packages along with reusable container objects.
* `com.letsdata.commoncrawl.model.filerecords.docs.http:` An Html doc in from the common crawl files has the normal html elements, the head element and the body element. The Head element has important metadata for search engines and indexers. We've coded up a few object classes (metas element, link element, script element, http hearders etc) to store these Html elements as objects in the doc and to allow for easy lookups.
* `com.letsdata.commoncrawl.model.filerecords.docs.warcinfo:` Each file starts with a warcinfo record and files that reference the WARC file (WAT and WET files) have metadata about the warc info record as well. This is the 'DOCUMENT' part of the WARCINFO records. (Probably does not need its own package)
* `com.letsdata.commoncrawl.model.filerecords.docs.watmetadata:` The DOCUMENT records for the different WAT metadata records. The WAT metadata records have a lot of detailed metadata about the request and responses - we've created container records for  Envelope and Payload metadata DOCUMENTS
* `com.letsdata.commoncrawl.model.filerecords.types:` The filerecords.types package has enums for the list of record and document types
### The #Let's Data Interface Implementations
The # Let's Data interface implementations and a utility class or two are in this package. This package defines the documents, parsers and readers that are used by the #Let's Data infrastructure.
* `com.letsdata.commoncrawl.interfaces.implementations.documents:` The #Let's Data DocumentInterface implementations. IndexRecord is the implementation of the #Let's Data document interface and has the common fields from the Html that we thought we'd need / use to build a simple indexer / domain ranker and other such developer projects. CompositeIndexRecord is the implementation of the CompositeDocInterface - since an index record for the html is created after looking at multiple WARC Records/Documents, WAT Records/Documents and WET Records/Documents, this composite doc contains these individual records and errors for each of these so that the caller may access the individual records etc to construct a custom doc if needed (For example they'd like to do some custom analysis on the script elements that are not output to the index record etc). We've also defined a WarcErrorDoc as a document for any errors encountered in parsing.
* `com.letsdata.commoncrawl.interfaces.implementations.parser:` The #Let's Data file parser interface (SingleFileStateMachineParser) implementations for the WARC, WAT and WET filetypes. Here are some high level callouts:
  * `File State Machine:` Each implementation manages a state machine for the file that is encoded in the getNextExpectedRecordType function. (See the file structure docs above to understand what the state machine looks like.)
  * `Record Start/End Delimiters:` The record start and end file delimiters are returned to the caller in getNextRecordStartPattern / getNextRecordEndPattern functions. The WARCParserUtils.WARCFileRecordTypes enum defines each record's start / end phrases.
  * `parseDocument:` The parseDocument function has the logic to construct the DocumentInterface for the expected record type in the State Machine. The function is given a byte array with start and end offsets into the record start and end byte indexes. It uses the WARCParserUtils.parseWarcHeaderFields function to read the header bytes and parse them into key value pairs, it finds the record type from the header, validates that it is the expected record type in the state machine and then calls the Java POJO (model) class to construct the RECORD and DOCUMENT and then construct the output index record from these.
* `com.letsdata.commoncrawl.interfaces.implementations.parser.WARCParserUtils`: WARCParserUtils has a few different utilities - an enum for file type start and end phrases, a Header container java object and a parser function that parses the header from the records. One important detail here is the use of the `com.resonance.letsdata.data.util.Matcher` utility. This function is defined in the lets data interface utilities and is used to get the line delimiter and then the key value delimiter to parse each line into key value pairs. This uses an implementation of Boyer Moore algo that is optimized for byte arrays and does preprocessing such as regex pattern compiles etc. Developers would be interested in seeing how this is implemented and evaluating if such implementations would give their code performance benefits. 
* `com.letsdata.commoncrawl.interfaces.implementations.reader:` This implements the reader interface (MultipleFileStateMachineReader) which is responsible for extracting different individual records from the parsers and combining them into a composite output doc. Here are some high level callouts for the reader implementation:
  * `Accessing Parser Implementations:` The reader interface (parseDocument) has access to the file reader for each type which it uses to get the record and state machine information from each file.
  * `Multiple File State Machine:` The reader maintains a state machine across the different file readers. For example, it validates that for a next record, the warc document needs to be request record type, wat document needs to be request metadata and wet document needs to be conversion record type. As it advances each file reader to the next record, it validates the expected record type in the state machine and handles errors as needed.
  * `Creating CompositeDoc:` The reader creates the composite doc from the extracted records and returns these with the result. 
  * `Offset Management:` Since the reader is reading from each file reader, it is responsible for creating an offset record that has the byte offsets into the file for each filetype.
### The bin directory
The bin directory is contains the following scripts:
* `build.sh:` This builds this module using mvn. Assumes that the maven is installed and added in the PATH. The script downloads the letsdata-data-interface jars, adds them to the local maven repo and then builds this module. It generates 3 JAR artifacts - the jar-with-dependencies has the dependent jars packaged into a single assembly. The SNAPSHOT jar  has the jar for this module only. The sources jar has the source code and debug information. We've used the jar-with-dependencies and the sources jar for development and #Let's Data runs. 
* `generate_commoncrawl_manifest.py:` This is a quick script to generate the #Let's Data manifest file from the common crawl data. The script has instructions on how to run. 
### The Unit Tests
TBA

## Creating Common Crawl Datasets on #Let's Data
TBA