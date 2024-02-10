package com.letsdata.commoncrawl.interfaces.implementations.spark;

import com.amazonaws.auth.AWSSessionCredentials;
import com.google.gson.Gson;
import com.resonance.letsdata.data.readers.interfaces.spark.SparkReducerInterface;

import java.util.List;
import java.util.Map;

import com.resonance.letsdata.data.util.SparkUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.expr;
import static org.apache.spark.sql.functions.count;
import static org.apache.spark.sql.functions.desc;

public class CommonCrawlSparkReducer implements SparkReducerInterface {
    private static final Gson gson = new Gson();

    /**
     * This an example implementation of the spark's mapper interface. LetsData calls this interface with a list of read destination uris (s3 file links) for the files that'd be the inputs of the reducer task. User's spark transformation code is run and an output dataframe is created.
     *
     *    The implementation uses the 'default implementations' for the following which wrap the user's spark transformation code:
     *      * the code has default credentials get - separate credentials are used for the read bucket and write bucket
     *      * spark session setup - spark is setup in standalone mode
     *      * read the file into the dataframe method calls for each file in the readUris - the dataframes are appended (union) to create a combined dataframe from all the input files
     *      * user's spark transformation code (wide transformations that need to be applied to across partitions (files))
     *           * group the dataframe by the 'language' column
     *           * calculate aggregates for the 'contentLength' column
     *                * calculate the 90th percentile
     *                * calculate the sample count
     *           * order by sample count, desc
     *      * write output dataframe to the the write destination S3 bucket.
     *
     *    For example, the following read connector and write connector parameters would be sent via different function parameters (json annotated below)
     *      "readConnector": {
     *           "connectorDestination": "S3",                     # passed as the readDestination, if runSparkInterfaces is set to REDUCER_ONLY, otherwise system generated for the intermediate files output by the mapper
     *           "artifactImplementationLanguage": "python",
     *           "interfaceECRImageResourceLocation": "Customer",
     *           "interfaceECRImagePath": "151166716410.dkr.ecr.us-east-1.amazonaws.com/letsdata_python_functions:latest",
     *           "readerType": "SPARKREADER",
     *           "bucketName": "commoncrawl",
     *           "bucketResourceLocation": "Customer",
     *           "sparkFileFormat": "text",                        # passed as the readFormat, if runSparkInterfaces is set to REDUCER_ONLY, otherwise system specifies 'parquet', which is the format for the intermediate files output by the mapper
     *           "sparkReadOptions": {                             # passed as the readOptions, if runSparkInterfaces is set to REDUCER_ONLY, otherwise system specifies options for the intermediate files output by the mapper
     *                "lineSep": "\n\r\n\r\n"
     *           }
     *      }
     *
     *      "manifestFile": {
     *           "readerType": "SPARKREADER",
     *           "manifestType": "S3ReaderTextManifestFile",
     *           "region": "us-east-1",
     *           "fileContents": "crawl-data/CC-MAIN-2023-50/segments/1700679099281.67/wet/CC-MAIN-20231128083443-20231128113443-00000.warc.wet.gz\n
     *                          crawl-data/CC-MAIN-2023-50/segments/1700679099281.67/wet/CC-MAIN-20231128083443-20231128113443-00001.warc.wet.gz\n
     *                          crawl-data/CC-MAIN-2023-50/segments/1700679099281.67/wet/CC-MAIN-20231128083443-20231128113443-00002.warc.wet.gz\n
     *                          crawl-data/CC-MAIN-2023-50/segments/1700679099281.67/wet/CC-MAIN-20231128083443-20231128113443-00003.warc.wet.gz"
     *                                                             #   ['s3a://<bucketName>/<fileName1>',
     *                                                             #     's3a://<bucketName>/<fileName2>',
     *                                                             #     's3a://<bucketName>/<fileName3>',
     *                                                             #     's3a://<bucketName>/<fileName4>'
     *                                                             #    ]
     *                                                             # is passed as the readUris if runSparkInterfaces is set to REDUCER_ONLY, otherwise, similar uris for intermediate files output by the mapper are generated.
     *      }
     *
     *
     *      "writeConnector": {
     *           "connectorDestination": "S3",                     # passed as the writeDestination
     *           "resourceLocation": "LetsData",
     *           "writerType": "Spark",
     *           "sparkFileFormat": "json",                        # passed as the writeFormat
     *           "sparkWriteOptions": {"compression":"gzip"}       # passed as the writeOptions
     *      },
     *
     * @param appName - The appName is a system generated spark app name
     * @param readDestination - The readDestination for spark reducer - currently only S3 is supported. If run configuration is 'MAPPER_AND_REDUCER', intermediate s3 bucket is the read destination. If run configuration is 'REDUCER_ONLY', the dataset's readConnector s3 bucket is used as the readDestination.
     * @param readUris - The list of readUris (the s3 file links) for that files that the reducer will read. If run configuration is 'MAPPER_AND_REDUCER', intermediate files outputted by the mapper are specified. If run configuration is 'REDUCER_ONLY', the dataset's readConnector s3 bucket and manifest's files are specified.
     * @param readFormat - The format of the file being read by the reducer. If run configuration is 'MAPPER_AND_REDUCER', system specifies these as 'parquet' for the intermediate files. If run configuration is 'REDUCER_ONLY', the dataset's readConnector sparkFileFormat is used.
     * @param readOptions - The options for the spark mapper's read. System specifies for 'MAPPER_AND_REDUCER' run config, dataset's readConnector's sparkReadOptions are used for run config 'REDUCER_ONLY'.
     * @param writeDestination - The writeDestination for spark reducer - currently only S3 is supported and is specified in the dataset's writeConnector.
     * @param writeUri - The writeUri for the writeDestination (the s3 file link) that the reducer will write the output file to, specified in the dataset's write destination bucket
     * @param writeFormat - The format of the file being written by the reducer, specified in dataset's writeConnector.sparkFileFormat attribute.
     * @param writeMode - The writeMode for spark reducer - currently defaults to 'overwrite'.
     * @param writeOptions - The options for the spark mapper's write, specified in the writeConnector.sparkWriteOptions, for example, '{"compression":"gzip"}'. Different formats can have different options that can be specified here.
     * @param sparkCredentialsSecretArn - The secret manager arn for credentials for reading and writing to the read / write buckets
     * @returns - The function writes the data to write destination and does not return anything
     */
    @Override
    public void reducer(String appName, String readDestination, List<String> readUris, String readFormat, Map<String, String> readOptions, String writeDestination, String writeUri, String writeFormat, String writeMode, Map<String, String> writeOptions, String sparkCredentialsSecretArn) {
        AWSSessionCredentials readCredentials = getReadDestinationCredentials(gson, System.getenv("AWS_REGION"), sparkCredentialsSecretArn);
        AWSSessionCredentials writeCredentials = getWriteDestinationCredentials(gson, System.getenv("AWS_REGION"), sparkCredentialsSecretArn);

        SparkSession spark = SparkUtils.createSparkSession(appName, readDestination, writeDestination, readUris.get(0), writeUri, readCredentials, writeCredentials);
        Dataset<Row> dataframe = null;

        for (String readUri : readUris) {
            Dataset<Row> currDataFrame = SparkUtils.readSparkDataframe(spark, readDestination, readUri, readFormat, readOptions);
            if (dataframe == null) {
                dataframe = currDataFrame;
            } else {
                dataframe = dataframe.union(currDataFrame);
            }
        }

        // Group by language and calculate the 90th percentile for the contentLength along with the number of samples
        // Order by the number of samples
        dataframe = dataframe.groupBy("language")
                .agg(
                        expr("percentile(contentLength, 0.90)").alias("90th Percentile ContentLength"),
                        count("contentLength").alias("# of Samples")
                )
                .orderBy(desc("# of Samples"));

        SparkUtils.writeSparkDataframe(spark, writeDestination, writeUri, writeFormat, writeMode, writeOptions, dataframe);
    }
}
