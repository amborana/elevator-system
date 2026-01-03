package org.aashish.service;

import org.aashish.entity.Car;
import org.aashish.strategy.DispatcherStrategy;
import org.aashish.valueobject.Request;

import java.util.List;

public class NearestCarDispatcher implements DispatcherStrategy {
    @Override
    public int selectCar(Request request, List<Car> cars) {
        int chosenCar = -1;
        int minDistance = Integer.MAX_VALUE;

        for (Car car : cars) {
            int distance = Math.abs(car.getFloor() - request.getFloor());
            if (distance < minDistance) {
                minDistance = distance;
                chosenCar = car.getCarId();
            }
        }
        return chosenCar;
    }
}
