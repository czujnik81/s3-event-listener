package org.rjug.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;

import java.util.UUID;

public class S3EventListener implements RequestHandler<S3Event, Void> {
    AWSCredentialsProvider credentialsProvider;
    DynamoDBMapper mapper;

    public Void handleRequest(S3Event s3Event, Context context) {
        if (s3Event != null && s3Event.getRecords() != null) {
            for (S3EventNotification.S3EventNotificationRecord event : s3Event.getRecords()) {
                mapper.save(EventItem.builder()
                .ts(event.getEventTime())
                .id(UUID.randomUUID().toString())
                .eventName(event.getEventName())
                .fineName(event.getS3().getObject().getKey())
                .build());
            }
        }

        return null;
    }

    public S3EventListener() {
        credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                    System.getenv("ACC"),
                    System.getenv("SEC")
                )
        );

        mapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard()
        .withCredentials(credentialsProvider)
        .withRegion(Regions.US_EAST_1)
        .build());
    }
}
