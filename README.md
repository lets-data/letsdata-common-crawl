# letsdata-common-crawl
This package has implementation of the different record types for the different common-crawl filetypes. It also has #Let's Data interface implementations that can be used to process the common crawl datasets using the #Let's Data infrastructure.

Common Crawl is an open repository of web crawl data and serves as a fantastic resource for any project that needs to leverage the www web crawl data. Personally, I've found it a great data resource that can be used to build and test big data infrastructures that can be reused across other big data usecases. You can learn more about common crawl at: https://commoncrawl.org/
## Problem Definition
We'd like to process the Common Crawl Web Archives files using the #Let's Data compute to reduce the web archives to JSON documents that could be used to create a database index. Each archive has ~ 240K files, ~477 TB S3 data.

## Solution & Architecture
There are two different types of development efforts needed for such a Big Data use-case:
1. The Functional Data Model: Understanding the data formats for the big data functional domain and developing how to parse the data and extract output documents.
2. The Data Pipeline Infrastructure: This is the infrastructure code that is required to orchestrate the data pipeline, reading from the source, writing to the destination, scheduling computation tasks and data jobs, tracking errors and building in fault tolerance and the necessary diagnostics.

In traditional data pipeline development, one would spend a disproportionately large development effort in developing the data pipeline infrastructure. With #Let’s Data, the focus is mostly on developing the functional data model, with only an integration effort to orchestrate and run the data pipeline.
Let’s look at each of these development efforts in detail.
### Common Crawl Data Model
The Common Crawl Dataset has the following characteristics:
* it has three filetypes the Archive (WARC), Metadata (WAT) and Conversion (WET) files
* each data record (crawled link) has data that is spread across these three files:
    * the archive file (WARC) has the http request and response with some high level metadata
    * the metadata (WAT) file has the metadata about the records in the archive file such as record types and their record offsets etc.
    * the conversion template (WET) has the converted Html document
* each of these files follows a record state machine for each data record (crawled link) – for example,
    * the archive file state machine is REQUEST -> RESPONSE -> METADATA for each crawled link
    * the metadata file state machine is METADATA (Request) -> METADATA(Response) -> METADATA(Metadata) for each crawled link (remember that this is metadata about the archive file records)
    * the conversion file state machine is simple – a single CONVERSION record for each crawled link

With this high-level information, we do the following development tasks:
* `The POJOS:` create Java POJOs that map to each record type – this is the majority of the work, where you define how to create an object from a byte array and validating the integrity of the object.
* `The Parsers:` define a parser state machine for each of the file using the #Let’s Data interfaces – this is relatively simpler, you encode the record types as a state machine and specify the start and end delimiters for each records
* `The Reader:` define a reader that constructs an output document from these file parser state machines using the #Let’s Data interface – this is the simplest of the three, encode the record retrieval logic from the parsers and then construct an output record by combining the these.

###	 #Let’s Data Data-Pipeline
With the above common crawl data model, we can now simply orchestrate the data pipeline by specifying the dataset configuration. We’d be creating a pipeline that reads the common crawl dataset files from AWS S3, writes them to AWS Kinesis and uses AWS Lambda to run the parser and extraction code. We also do some access setup so that #Let’s Data can automatically manage the read and write resources.

Here are the dataset configuration details:
* Read Connector configuration:
    * the S3 Bucket to read from
    * the JAR file that has the #Let’s Data interface implementations
    * the mapping of #Let’s Data interfaces to file types (archive file type -> archive file parser class name etc.)
* Write Connector Configuration
    * the Kinesis stream that we need to write to
    * the number of shards for the Kinesis stream
* Error Connector Configuration
    * the S3 Bucket to write the error records to
* Compute Engine Configuration
    * AWS Lambda compute details – these are the function concurrency, timeout, memory and log level
* Manifest file
    * the manifest file that defines the list of all the files that should be processed and their mapping – example:
```
Archive : file1.archive | Metadata : file1.metadata | Conversion : file1.conversion
Archive : file2.archive | Metadata : file2.metadata | Conversion : file2.conversion
…
```
* Each line in the manifest file becomes a #Let’s Data task that can be tracked from creation to completion and has its own progress, errors and diagnostics tracing.

We use the #Let’s Data CLI to create this dataset and monitor its execution via the CLI and Console.
```
# create the dataset
$ > ./letsdata datasets create --configFile dataset.json --prettyPrint

# view the dataset, monitor its creation
$ > ./letsdata datasets view --datasetName <datasetName> --prettyPrint

# list the datset's tasks
$ > ./letsdata tasks list --datasetName <datasetName> --prettyPrint
```

## Data Model Implementation Design Notes
This package defines enums to create a structure for the WARC, WAT and WET ("warc" file) files while parsing them into java objects

Here is a definition that we are going to work with:  a warc file record is composed of 2 types of sub-records - a WARC record and a DOCUMENT record (you can think of these as similar to header record and a payload record)

WARC Records are the different types of WARC records / header records / annotations that are present in the warc files.

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
* `com.letsdata.commoncrawl.interfaces.implementations.reader.CommonCrawlReader:` This implements the reader interface (MultipleFileStateMachineReader) which is responsible for extracting different individual records from the parsers (multiple files) and combining them into a composite output doc. Here are some high level callouts for the reader implementation:
  * `Accessing Parser Implementations:` The reader interface (parseDocument) has access to the file reader for each type which it uses to get the next record and state machine information from each file.
  * `Multiple File State Machine:` The reader maintains a state machine across the different file readers. For example, it validates that for a next record, the warc document needs to be request record type, wat document needs to be request metadata and wet document needs to be conversion record type. As it advances each file reader to the next record, it validates the expected record type in the state machine and handles errors as needed.
  * `Creating CompositeDoc:` The reader creates the composite doc from the extracted records and returns these with the result. 
  * `Offset Management:` Since the reader is reading from each file reader, it is responsible for creating an offset record that has the byte offsets into the file for each filetype.
* `com.letsdata.commoncrawl.interfaces.implementations.reader.CommonCrawlWARCSingleFileStateMachineReader:` This implements the reader interface (SingleFileStateMachineReader) which is responsible for extracting different individual records from a single file state machine parser and combining them into a composite output doc. Here are some high level callouts for the reader implementation:
  * `Accessing Parser Implementation:` The reader interface (parseDocument) has access to the file reader the single file it is processing. It uses this parser to get the next record and state machine information from the file.
  * `Multiple File State Machine:` The reader validates the state machine from the parsers. For example, it validates that the records follow the state machine - request -> response - metadata -> request .... 
  * `Creating CompositeDoc:` The reader creates the composite doc from the extracted records and returns these with the result.
  * `Offset Management:` It receives the offset from the system file reader (parser) and passes that to #Let's Data in the result. 
### The bin directory
The bin directory is contains the following scripts:
* `build.sh:` This builds this module using mvn. Assumes that the maven is installed and added in the PATH. The script downloads the letsdata-data-interface jars, adds them to the local maven repo and then builds this module. It generates 3 JAR artifacts - the jar-with-dependencies has the dependent jars packaged into a single assembly. The SNAPSHOT jar  has the jar for this module only. The sources jar has the source code and debug information. We've used the jar-with-dependencies and the sources jar for development and #Let's Data runs. 
* `generate_commoncrawl_manifest.py:` This is a quick script to generate the #Let's Data manifest file from the common crawl data for the CommonCrawlReader (MultipleFileStateMachineReader) usecase. The script has instructions on how to run.
* `generate_commoncrawl_warc_singlefilestatemachine_manifest.py:` This is a quick script to generate the #Let's Data manifest file from the common crawl data for the CommonCrawlWARCSingleFileStateMachineReader (SingleFileStateMachineReader) usecase. The script has instructions on how to run.
### The config and iam_policy directories
* The `config` and `iam_policy` directories have example dataset configuration and the access grant iam role policy documents which can be used to run the `CommonCrawlReader` and `CommonCrawlWARCSingleFileStateMachineReader` usecases.
* Learn about access grants and the required IAM policies at [access grant docs](www.letsdata.io/docs#accessgrants)
### Running the datasets
* The following CLI commands can be used to create and view datasets (after replacing placeholders and creating IAM roles)
```
# create the dataset
$ > ./letsdata datasets create --configFile dataset.json --prettyPrint

# view the dataset, monitor its creation 
$ > ./letsdata datasets view --datasetName <datasetName> --prettyPrint

# list the datset's tasks
$ > ./letsdata tasks list --datasetName <datasetName> --prettyPrint 
```
* The records that are extracted and written to Kinesis stream can be accessed using the IAM role that is created by #Let's Data. See [Granting Customer Access to #Let's Data Resources](www.letsdata.io/docs#accessgrants) section in the docs. There is a sample implementation code and CLI program that can be used to retrieve the records [letsdata-writeconnector-reader](https://github.com/lets-data/letsdata-writeconnector-reader)
* Details about tasks, execution logs, errors and dataset metrics can be learned at: [www.letsdata.io/docs](www.letsdata.io/docs)

### The Unit Tests
TBA

## Creating Common Crawl Datasets on #Let's Data
TBA