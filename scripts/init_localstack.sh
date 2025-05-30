#!/bin/bash
set -e

# Create SQS queue
awslocal sqs create-queue --queue-name payment-events

# Create DynamoDB table
awslocal dynamodb create-table \
  --table-name notifications \
  --attribute-definitions AttributeName=id,AttributeType=S \
  --key-schema AttributeName=id,KeyType=HASH \
  --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5