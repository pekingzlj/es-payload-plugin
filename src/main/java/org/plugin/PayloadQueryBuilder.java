package org.plugin;

import org.apache.lucene.index.Term;
import org.apache.lucene.queries.payloads.AveragePayloadFunction;
import org.apache.lucene.queries.payloads.PayloadScoreQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.ParsingException;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.query.BaseTermQueryBuilder;
import java.io.IOException;
import java.util.Optional;

import org.elasticsearch.index.query.QueryShardContext;
import org.elasticsearch.index.query.*;
/**
 * A Span Query that matches documents containing a term.
 * @see SpanTermQuery
 */
public class PayloadQueryBuilder extends BaseTermQueryBuilder<PayloadQueryBuilder> implements SpanQueryBuilder {
    public static final String NAME = "payload_term";

    private static final ParseField TERM_FIELD = new ParseField("term");

    /** @see BaseTermQueryBuilder#BaseTermQueryBuilder(String, String) */
    public PayloadQueryBuilder(String name, String value) {
        super(name, (Object) value);
    }

    /** @see BaseTermQueryBuilder#BaseTermQueryBuilder(String, int) */
    public PayloadQueryBuilder(String name, int value) {
        super(name, (Object) value);
    }

    /** @see BaseTermQueryBuilder#BaseTermQueryBuilder(String, long) */
    public PayloadQueryBuilder(String name, long value) {
        super(name, (Object) value);
    }

    /** @see BaseTermQueryBuilder#BaseTermQueryBuilder(String, float) */
    public PayloadQueryBuilder(String name, float value) {
        super(name, (Object) value);
    }

    /** @see BaseTermQueryBuilder#BaseTermQueryBuilder(String, double) */
    public PayloadQueryBuilder(String name, double value) {
        super(name, (Object) value);
    }

    /** @see BaseTermQueryBuilder#BaseTermQueryBuilder(String, Object) */
    public PayloadQueryBuilder(String name, Object value) {
        super(name, value);
    }

    /**
     * Read from a stream.
     */
    public PayloadQueryBuilder(StreamInput in) throws IOException {
        super(in);
    }

    @Override
    protected SpanQuery doToQuery(QueryShardContext context) throws IOException {
        return new PayloadScoreQuery(new SpanTermQuery(new Term(fieldName, (BytesRef) value)), new AveragePayloadFunction());

    }

    public static Optional<PayloadQueryBuilder> fromXContent(QueryParseContext parseContext) throws IOException, ParsingException {
        XContentParser parser = parseContext.parser();
        XContentParser.Token token = parser.currentToken();
        if (token == XContentParser.Token.START_OBJECT) {
            token = parser.nextToken();
        }
        assert token == XContentParser.Token.FIELD_NAME;
        String fieldName = parser.currentName();
        token = parser.nextToken();
        Object value;
        if (token == XContentParser.Token.START_OBJECT) {
            throw new RuntimeException();
        } else {
            value = parser.objectBytes();
            parser.nextToken();
        }
        PayloadQueryBuilder result = new PayloadQueryBuilder(fieldName, value);
        return Optional.of(result);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }
}
