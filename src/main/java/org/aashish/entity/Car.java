package org.aashish.entity;

import lombok.Getter;
import org.aashish.valueobject.CarState;
import org.aashish.valueobject.Direction;
import org.aashish.valueobject.Request;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;


public class Car implements Runnable {
    @Getter
    private final int carId;
    @Getter
    private CarState state;
    @Getter
    private volatile int currentFloor;
    @Getter
    private Direction direction = Direction.IDLE;
    // LOOK queues
    private final PriorityQueue<Integer> upPQ = new PriorityQueue<>(); // min-heap
    private final PriorityQueue<Integer> downPQ = new PriorityQueue<>(Comparator.reverseOrder()); // max-heap

    private final BlockingQueue<Request> incomingQueue;

    Car(int carId, BlockingQueue<Request> incomingQueue) {
        this.carId = carId;
        this.incomingQueue = incomingQueue;
    }
    @Override
    public void run() {
        try {
            while (true) {
                Request request = incomingQueue.take();
                acceptRequest(request);
                processRequests();
            }
        }
        catch (Exception ignored) {}
    }
    private void acceptRequest(Request request) {
        if (request.getFloor() >= currentFloor) {
            upPQ.offer(request.getFloor());
        } else {
            downPQ.offer(request.getFloor());
        }
        if (direction == Direction.IDLE) {
            direction = request.getFloor() >= currentFloor
                    ? Direction.UP
                    : Direction.DOWN;
        }
    }

    private void moveTo(int targetFloor) {
        state = CarState.MOVING;
        //simulate movement
        currentFloor = targetFloor;
    }

    private void processRequests() {
        if (direction == Direction.UP) {
            processUp();
        } else {
            processDown();
        }
    }

    private void processUp(){
        while (!upPQ.isEmpty()) {
            moveTo(upPQ.poll());
        }
        if (!downPQ.isEmpty()) {
            direction = Direction.DOWN;
        } else {
            direction = Direction.IDLE;
        }
    }
    private void processDown() {
        while (!downPQ.isEmpty()) {
            moveTo(downPQ.poll());
        }
        if (!upPQ.isEmpty()) {
            direction = Direction.UP;
        } else {
            direction = Direction.IDLE;
        }
    }
}
