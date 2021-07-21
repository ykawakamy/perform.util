package ccs.perform.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopicNameSupplier implements Supplier<String> {

    AtomicInteger seq = new AtomicInteger();
    List<String> topics = new ArrayList<>();

    public TopicNameSupplier(String prefix, int startInclude, int endInclude) {
        for (int i = startInclude; i <= endInclude; i++) {
            this.topics.add(prefix + i);
        }
    }

    public TopicNameSupplier(String... cacheTopic) {
        this.topics.addAll(List.of(cacheTopic));
    }

    public TopicNameSupplier(Collection<String> cacheTopic) {
        this.topics.addAll(cacheTopic);
    }

    @Override
    public String get() {
        int i = seq.getAndIncrement();
        return topics.get(i % topics.size());
    }

    public List<String> getAll() {
        return Collections.unmodifiableList(topics);
    }

    /**
     * @param topic トピック名のプレフィクス
     * @param topicrange "xx-yy"形式で範囲を指定します。xx<=i<=yyの範囲でトピック名を提供するSupplierを生成します。
     * @return
     */
    public static TopicNameSupplier create(String topic, String topicrange) {
        if( topicrange == null ) {
            return new TopicNameSupplier(topic);
        }
        Matcher matcher = Pattern.compile("([0-9]+)-([0-9]+)").matcher(topicrange);
        if( matcher.matches() ) {
            int start = Integer.parseInt(matcher.group(1));
            int end = Integer.parseInt(matcher.group(2));
            return new TopicNameSupplier(topic, start, end);
        }

        return new TopicNameSupplier(topic);
    }

}
