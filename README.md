## es-payload-plugin
It is elastic search 5.2.2 payload plugin

## 1.Compiling the code and Installing the plugin

   1.1 Clone the repo, in es-payload-plugin:

   1.2 mvn clean install

   1.3 ./createPlugin.sh

   1.4  ~/Documents/programe/elasticsearch-5.2.2/bin/elasticsearch-plugin install file:/Users/zlj/git/es-payload-plugin/elasticsearch.zip 


   1.5 Restart elasticsearch ( if it was open ).

## Using the plugin
2.1 curl -XDELETE 'localhost:9200/test'
2.2 
curl -XPUT 'localhost:9200/test' -d '
{
  "mappings": {
 
    "mytype":{

    "properties": {

      "content1": {

        "type": "text",

        "term_vector": "with_positions_payloads",

        "analyzer": "payload_delimiter",

        "similarity" : "my_payload_similarity",

        "norms": false

      },

      "content2": {

        "type": "text",

        "term_vector": "with_positions_payloads",

        "analyzer": "payload_delimiter",

        "similarity" : "my_payload_similarity",

        "norms": false

      },

      "content3": {

        "type": "text",

        "term_vector": "with_positions_payloads",

        "analyzer": "payload_delimiter",

        "similarity" : "my_payload_similarity",

        "norms": false

      }
    }
  }
  },
  "settings": {

     "number_of_shards" : "1",

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
    "content1" : "1|0.3 2|0.2 3|0.1",

    "content2" : "2|0.2 3|0.1",

    "content3" : "1|0.3 2|0.2 3|0.1"

}
'

curl -XPUT 'localhost:9200/test/my_type/2' -d '
{
    "content1" : "1|0.2 2|0.3 3|0.2",

    "content2" : "2|0.3 3|0.2",

    "content3" : "1|0.2 2|0.3 3|0.2"

}
'

curl -XPUT 'localhost:9200/test/my_type/3' -d '
{
    "content1" : "1|0.1 2|0.1 3|0.3",

    "content2" : "2|0.1 3|0.3",

    "content3" : "1|0.1 2|0.1 3|0.3"

}
'

curl -XPUT 'localhost:9200/test/my_type/4' -d '
{

    "content1" : "2|0.1 3|0.3",

    "content2" : "1|0.3 3|0.3",

    "content3" : "1|0.1 2|0.1 3|0.3"

}
'

curl -XPUT 'localhost:9200/test/my_type/5' -d '
{

    "content1" : "2|0.1 3|0.3",

    "content2" : "1|0.2 3|0.3",

    "content3" : "1|0.1 2|0.1 3|0.3"

}
'

curl -XPUT 'localhost:9200/test/my_type/6' -d '
{

    "content1" : "2|0.1 3|0.3",

    "content2" : "1|0.1 3|0.3",

    "content3" : "1|0.1 2|0.1 3|0.3"

}
'
2.4
curl -XGET 'localhost:9200/test/my_type/_search?pretty' -d '
{

  "explain" : false,

  "query":

  {
  "function_score": {

    "query": {

       "bool": {

          "should":[

            {

              "payload_term":{ "content1" : "1"}

            },

            {

              "payload_term":{ "content2" : "1"}

            }

          ]
       }
     },
     "functions":[
        {

          "filter": { "term": { "content1": "1" }},
          "weight": 4
        },
        {
          "filter": { "term": { "content2": "1" }},
          "weight": 2
        }
      ],
      "score_mode": "max",
      "boost_mode": "sum"
   }
  }
}
'

result:
{
  "took" : 2,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "failed" : 0
  },
  "hits" : {
    "total" : 6,
    "max_score" : 4.3,
    "hits" : [
      {
        "_index" : "test",
        "_type" : "my_type",
        "_id" : "1",
        "_score" : 4.3,
        "_source" : {
          "content1" : "1|0.3 2|0.2 3|0.1",
          "content2" : "2|0.2 3|0.1",
          "content3" : "1|0.3 2|0.2 3|0.1"
        }
      },
      {
        "_index" : "test",
        "_type" : "my_type",
        "_id" : "2",
        "_score" : 4.2,
        "_source" : {
          "content1" : "1|0.2 2|0.3 3|0.2",
          "content2" : "2|0.3 3|0.2",
          "content3" : "1|0.2 2|0.3 3|0.2"
        }
      },
      {
        "_index" : "test",
        "_type" : "my_type",
        "_id" : "3",
        "_score" : 4.1,
        "_source" : {
          "content1" : "1|0.1 2|0.1 3|0.3",
          "content2" : "2|0.1 3|0.3",
          "content3" : "1|0.1 2|0.1 3|0.3"
        }
      },
      {
        "_index" : "test",
        "_type" : "my_type",
        "_id" : "4",
        "_score" : 2.3,
        "_source" : {
          "content1" : "2|0.1 3|0.3",
          "content2" : "1|0.3 3|0.3",
          "content3" : "1|0.1 2|0.1 3|0.3"
        }
      },
      {
        "_index" : "test",
        "_type" : "my_type",
        "_id" : "5",
        "_score" : 2.2,
        "_source" : {
          "content1" : "2|0.1 3|0.3",
          "content2" : "1|0.2 3|0.3",
          "content3" : "1|0.1 2|0.1 3|0.3"
        }
      },
      {
        "_index" : "test",
        "_type" : "my_type",
        "_id" : "6",
        "_score" : 2.1,
        "_source" : {
          "content1" : "2|0.1 3|0.3",
          "content2" : "1|0.1 3|0.3",
          "content3" : "1|0.1 2|0.1 3|0.3"
        }
      }
    ]
  }
}


见查询结果是按照bar 的payload进行排序的
