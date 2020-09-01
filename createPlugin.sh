echo "Remove Old ElasticSearch folder and elasticsearch.zip if present..."
rm -rf elasticsearch
rm elasticsearch.zip
mkdir elasticsearch
echo "copying plugin files into ElasticSearch Folder...."
cp target/payload-similarity-plugin-1.jar elasticsearch/payload-similarity-plugin-1.jar
cp target/classes/plugin-descriptor.properties elasticsearch/plugin-descriptor.properties 
echo "Zip elasticSearch"
zip -r elasticsearch.zip elasticsearch
echo "Complete! Now install elasticsearch using bin/elasticsearch-plugin install ..."
