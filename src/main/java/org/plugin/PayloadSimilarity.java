package org.plugin;

import org.apache.lucene.analysis.payloads.PayloadHelper;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.util.BytesRef;

public class PayloadSimilarity extends ClassicSimilarity {

    public float scorePayload(int doc, int start, int end, BytesRef payload) {
        if (payload != null) {
            return PayloadHelper.decodeFloat(payload.bytes, payload.offset);
        } else {
            return 1;
        }
    }
    @Override
    public float coord(int overlap, int maxOverlap) {
        return 1;
    }

    @Override
    public float queryNorm(float sumOfSquaredWeights) {
        return 1.0f;
    }

    @Override
    public float tf(float freq) {
        return 1.0f;
    }


    @Override
    public float idf(long docFreq, long docCount) {
        return 1.0f;
    }

    @Override
    public float lengthNorm(FieldInvertState state) {
        return 1.0f;
    }
    @Override
    public String toString() {
        return "PayloadSimilarity";
    }
}
