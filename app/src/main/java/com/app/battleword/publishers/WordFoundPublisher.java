package com.app.battleword.publishers;

import com.app.battleword.subscribers.WordFoundSubscriber;

public interface WordFoundPublisher {
    public void publishWordFound();
    public void subscribe(WordFoundSubscriber subscriber);

}
