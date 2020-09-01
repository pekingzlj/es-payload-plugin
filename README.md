# es-payload-plugin
It is elastic search 5.2.2 payload plugin

1.Compiling the code and Installing the plugin

1.1 Clone the repo, in es-payload-plugin:

1.2 mvn clean install

1.3 ./createPlugin.sh

1.4  ~/Documents/programe/elasticsearch-5.2.2/bin/elasticsearch-plugin install file:/Users/zlj/git/es-payload-plugin/elasticsearch.zip 


1.5 Restart elasticsearch ( if it was open ).

2.Using the plugin
2.1 curl -XDELETE 'localhost:9200/test'
2.2 curl -XPUT 'localhost:9200/test' -d '
{
  "mappings": {
    "mytype":{
    "properties": {
      "content": {
        "type": "text",
        "term_vector": "with_positions_payloads",
        "analyzer": "payload_delimiter",
        "similarity" : "my_payload_similarity"
      }
    }
  }
  },
  "settings": {
     "similarity" : {
        "my_payload_similarity" : {
             "type" : "customer-similarity"
         }
     },
    "analysis": {
      "analyzer": {
        "payload_delimiter": {
          "tokenizer": "whitespace",
          "filter": [ "delimited_payload_filter" ]
        }
      }
    }
 }
}
'
2.3
curl -XPUT 'localhost:9200/test/my_type/1' -d '
{
    "content" : "foo bar baz"
}
'

curl -XPUT 'localhost:9200/test/my_type/2' -d '
{
    "content" : "foo bar|2.0 baz"
}
'

curl -XPUT 'localhost:9200/test/my_type/3' -d '
{
    "content" : "foo bar|3.0 baz"
}

2.4 curl -XGET 'localhost:9200/test/my_type/_search?pretty' -d '
{
    "explain" : false,
    "query": {
        "payload_term": {
            "content" : "bar"
        }
    }
}
'

result:



{
  "took" : 132,
  "timed_out" : false,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "hits" : {
    "total" : 3,
    "max_score" : 1.5,
    "hits" : [
      {
        "_index" : "test",
        "_type" : "my_type",
        "_id" : "3",
        "_score" : 1.5,
        "_source" : {
          "content" : "foo bar|3.0 baz"
        }
      },
      {
        "_index" : "test",
        "_type" : "my_type",
        "_id" : "2",
        "_score" : 1.0,
        "_source" : {
          "content" : "foo bar|2.0 baz"
        }
      },
      {
        "_index" : "test",
        "_type" : "my_type",
        "_id" : "1",
        "_score" : 0.5,
        "_source" : {
          "content" : "foo bar baz"
        }
      }
    ]
  }
}

可见查询结果是按照bar 的payload进行排序的
