package org.aashish.aggregator;

import org.aashish.entity.Car;
import org.aashish.strategy.DispatcherStrategy;
import org.aashish.valueobject.Request;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

class ElevatorController {

    private final List<Car> cars;
    private final Map<Integer, BlockingQueue<Request>> queues;
    private final DispatcherStrategy dispatcher;

    ElevatorController(List<Car> cars,
                       Map<Integer, BlockingQueue<Request>> queues,
                       DispatcherStrategy dispatcher) {
        this.cars = cars;
        this.queues = queues;
        this.dispatcher = dispatcher;
    }

    public void onRequest(Request request) {
        int carId;

        // Serialize scheduling decision
        synchronized (this) {
            carId = dispatcher.selectCar(request, cars);
        }

        // Async handoff to car
        queues.get(carId).offer(request);
    }
}
