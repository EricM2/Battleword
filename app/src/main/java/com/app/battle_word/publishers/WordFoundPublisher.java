package com.app.battle_word.publishers;

import com.app.battle_word.subscribers.WordFoundSubscriber;

public interface WordFoundPublisher {
    public void publishWordFound();
    public void subscribe(WordFoundSubscriber subscriber);

}
