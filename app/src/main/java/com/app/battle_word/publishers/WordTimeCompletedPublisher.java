package com.app.battle_word.publishers;

import com.app.battle_word.subscribers.WordTimeCompletedSubscriber;

public interface WordTimeCompletedPublisher {
    public void subscribe(WordTimeCompletedSubscriber subscriber);
    public void notifyTimeCompleted();
}
