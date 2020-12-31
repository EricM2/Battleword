package com.app.battleword.publishers;

import com.app.battleword.subscribers.WordTimeCompletedSubscriber;

public interface WordTimeCompletedPublisher {
    public void subscribe(WordTimeCompletedSubscriber subscriber);
    public void notifyTimeCompleted();
}
