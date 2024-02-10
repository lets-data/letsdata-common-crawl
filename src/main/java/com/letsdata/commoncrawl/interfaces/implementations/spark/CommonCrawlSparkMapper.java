package com.letsdata.commoncrawl.interfaces.implementations.spark;

import com.amazonaws.auth.AWSSessionCredentials;
import com.google.gson.Gson;
import com.resonance.letsdata.data.readers.interfaces.spark.SparkMapperInterface;
import com.resonance.letsdata.data.util.SparkUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.types.DataTypes;
import scala.collection.mutable.WrappedArray;
import static org.apache.spark.sql.functions.regexp_replace;
import static org.apache.spark.sql.functions.split;
import static org.apache.spark.sql.functions.expr;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.udf;
import static org.apache.spark.sql.functions.lit;

public class CommonCrawlSparkMapper implements SparkMapperInterface, Serializable {
    private static final Gson gson = new Gson();

    /**
     * This an example implementation of the spark's mapper interface.
     *
     *    The implementation uses the 'default implementations' for the following which wrap the user's spark transformation code:
     *      * the code has default credentials get - separate credentials are used for the read bucket and write bucket
     *      * spark session setup - spark is setup in standalone mode
     *      * read the file into the dataframe method calls - read the data frame using the format, options and the uri specified in the method args
     *      * user's spark transformation code (narrow transformations that need to be applied to each partition)
     *           * read the file as a text file using \n\r\n\r\n as the line separator (record separator)
     *           * trim the whitespaces from the record
     *           * split the line read into a header and payload components using \r\n\r\n as the delimiter
     *           * header further split as each line key value pair into a string array ['warcFormatKey', 'k1', 'v1', 'k2', 'v2', ...]
     *           * a custom udf is used to convert the above key value pair list to a map  {'k1': 'v1', 'k2': 'v2', ... }
     *           * each key is converted to a column and value as the value of that colum for each row
     *           * the output dataframe with headerKey columns and a payload column is created
     *      * write the output dataframe as files to the intermediate S3 bucket for processing by reduce tasks
     *
     *    For example, the following read connector and write connector parameters would be sent via different function parameters (json annotated below)
     *      "readConnector": {
     *           "connectorDestination": "S3",                     # passed as the readDestination
     *           "artifactImplementationLanguage": "python",
     *           "interfaceECRImageResourceLocation": "Customer",
     *           "interfaceECRImagePath": "151166716410.dkr.ecr.us-east-1.amazonaws.com/letsdata_python_functions:latest",
     *           "readerType": "SPARKREADER",
     *           "bucketName": "commoncrawl",
     *           "bucketResourceLocation": "Customer",
     *           "sparkFileFormat": "text",                        # passed as the readFormat
     *           "sparkReadOptions": {                             # passed as readOptions
     *                "lineSep": "\n\r\n\r\n"
     *           }
     *      }
     *
     *      "manifestFile": {
     *           "manifestType": "S3ReaderTextManifestFile",
     *           "region": "us-east-1",
     *           "fileContents": "crawl-data/CC-MAIN-2023-50/segments/1700679099281.67/wet/CC-MAIN-20231128083443-20231128113443-00000.warc.wet.gz\n     # s3a://<bucketName>/<fileName> is passed as the readUri
     *                          crawl-data/CC-MAIN-2023-50/segments/1700679099281.67/wet/CC-MAIN-20231128083443-20231128113443-00001.warc.wet.gz\n
     *                          crawl-data/CC-MAIN-2023-50/segments/1700679099281.67/wet/CC-MAIN-20231128083443-20231128113443-00002.warc.wet.gz\n
     *                          crawl-data/CC-MAIN-2023-50/segments/1700679099281.67/wet/CC-MAIN-20231128083443-20231128113443-00003.warc.wet.gz",
     *           "readerType": "SPARKREADER"
     *      }
     *
     *      Since this is MAPPER_AND_REDUCER run configuration, system generates intermediate file locations for write as follows:
     *           * writeDestination : S3
     *           * writeUri : s3a://<intermediateFilesBucketName>/<intermediateFilename>
     *           * writeFormat : Store in parquet format to allow for efficient reduce processing
     *           * writeMode : Currently defaults to 'overwrite'.
     *           * writeOptions : Write as a gzip compressed file. '{"compression":"gzip"}'
     *
     * @param appName - The appName is a system generated spark app name
     * @param readDestination - The readDestination for spark mapper - currently only S3 is supported
     * @param readUri - The readUri for the readDestination (the s3 file link) that the mapper will read
     * @param readFormat - The format of the file being read by the mapper. We currently support csv, text, json and parquet. You can add additional formats, just make sure to add code in spark utils to handle these.
     * @param readOptions - The options for the spark mapper's read. For text files, we can specify the lineSep as an option. Different formats can have different options that can be specified here.
     * @param writeDestination - The readDestination for spark mapper - currently only S3 is supported.
     * @param writeUri - The writeUri for the writeDestination (the s3 file link) that the mapper will write the output file to. In case of "MAPPER_AND_REDUCER" run config, this is a system generated uri that saves the file in the intermediate bucket. In case of "MAPPER_ONLY" run configuration, an output file is written to write destination bucket
     * @param writeFormat - The format of the file being written by the mapper. In case of "MAPPER_AND_REDUCER" run config, 'parquet' is passed, otherwise this is passed as the format. We currently support csv, text, json and parquet. You can add additional formats, just make sure to add code in spark utils to handle these.
     * @param writeMode - The writeMode for spark mapper - currently defaults to 'overwrite'.
     * @param writeOptions - The options for the spark mapper's write. In case of "MAPPER_AND_REDUCER" run config, '{"compression":"gzip"}' is passed, otherwise the dataset's sparkWriteOptions are passed as the writeOptions. Different formats can have different options that can be specified here.
     * @param sparkCredentialsSecretArn - The secret manager arn for credentials for reading and writing to the read / write buckets
     * @returns - The function writes the data to write destination and does not return anything
     */
    @Override
    public void mapper(String appName, String readDestination, String readUri, String readFormat, Map<String, String> readOptions, String writeDestination, String writeUri, String writeFormat, String writeMode, Map<String, String> writeOptions, String sparkCredentialsSecretArn) {
        AWSSessionCredentials readCredentials = getReadDestinationCredentials(gson, System.getenv("AWS_REGION"), sparkCredentialsSecretArn);
        AWSSessionCredentials writeCredentials = getWriteDestinationCredentials(gson, System.getenv("AWS_REGION"), sparkCredentialsSecretArn);

        SparkSession spark = SparkUtils.createSparkSession(appName, readDestination, writeDestination, readUri, writeUri, readCredentials, writeCredentials);
        Dataset<Row> df = SparkUtils.readSparkDataframe(spark, readDestination, readUri, readFormat, readOptions);

        // Take a smaller set to keep it manageable in build and tests
        // df = df.randomSplit(new double[]{0.3, 0.7}, 5)[0];

        // Trim whitespaces, not just spaces
        df = df.select(regexp_replace(df.col("value"), "^\\s+|\\s+$", "").alias("value"));
        df.show(5, false);

        // Split string into header and payload columns
        df = df.select(split(df.col("value"), "\r\n\r\n").alias("s"))
                .withColumn("header", expr("s[0]"))
                .withColumn("payload", expr("s[1]"))
                .select(col("header"), col("payload"));
        df.show(5, false);

        // Split the header into key-value pair list
        df = df.select(split(df.col("header"), ": |\r\n").alias("h"), col("payload"));
        df.show(5, false);

        // Create a custom UDF used to convert the key value pair list ['warcFormatKey', 'k1', 'v1', 'k2', 'v2', ...] to a map  {'k1': 'v1', 'k2': 'v2', ... }
        UserDefinedFunction arrayToMap = udf((WrappedArray<String> list) -> {
            Map<String, String> map_value = new HashMap<>();
            try {
                for (int i = 1; i < list.size(); i+=2) {
                    String key = list.apply(i);
                    String value = list.apply(i + 1);
                    map_value.put(key, value);
                }
            } catch (Exception ex) {
                System.out.println("error: " + ex + ", arr: " + list);
            }
            return map_value;
        }, DataTypes.createMapType(DataTypes.StringType, DataTypes.StringType));

        // Convert array to map, and then map to columns using the known schema
        df = df.withColumn("headerMap", arrayToMap.apply(df.col("h")))
                .select(
                        expr("headerMap['WARC-Filename']").alias("warcFileName"),
                        expr("cast(headerMap['Content-Length'] as int)").alias("contentLength"),
                        expr("to_timestamp(headerMap['WARC-Date'])").alias("warcDate"),
                        expr("headerMap['WARC-Record-ID']").alias("warcRecordId"),
                        expr("headerMap['Content-Type']").alias("contentType"),
                        expr("headerMap['WARC-Type']").alias("warcType"),
                        expr("headerMap['WARC-Refers-To']").alias("warcRefersTo"),
                        expr("headerMap['WARC-Identified-Content-Language']").alias("language"),
                        expr("headerMap['WARC-Block-Digest']").alias("blockDigest"),
                        expr("headerMap['WARC-Target-URI']").alias("url"),
                        expr("headerMap['WARC-Target-URI']").alias("docId"),
                        expr("headerMap['WARC-Target-URI']").alias("partitionKey"),
                        col("payload").alias("docText")
                )
                .where(col("warcType").equalTo(lit("conversion")))
                .drop("warcFileName");

        SparkUtils.writeSparkDataframe(spark, writeDestination, writeUri, writeFormat, writeMode, writeOptions, df);
    }
}
