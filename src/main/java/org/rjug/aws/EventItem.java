package org.rjug.aws;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedTimestamp;

import lombok.Builder;
import lombok.Data;

import org.joda.time.DateTime;

@Data
@Builder
@DynamoDBTable(tableName = "S3Events")
public class EventItem {

    @DynamoDBHashKey
    private String id;

    @DynamoDBTypeConvertedTimestamp
    private DateTime ts;
    private String fineName;
    private String eventName;
}
