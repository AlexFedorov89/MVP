package geekbrains.ru.lesson1mvc.Lesson2;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;

public class EventBus {
    PublishSubject<Long> subject;

    public EventBus() {
        this.subject = PublishSubject.create();
    }

    public void subscribeObserver(Observer observer){
        subject.subscribe(observer);
    }

    public void subscribeObservable(Observable observable){
        observable.subscribe(subject);
    }

    public void sendData(Long data){
        subject.onNext(data);
    }
}
