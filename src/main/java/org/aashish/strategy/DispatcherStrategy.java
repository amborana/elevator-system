package org.aashish.strategy;

import org.aashish.entity.Car;
import org.aashish.valueobject.Request;

import java.util.List;

public interface DispatcherStrategy{
    int selectCar(Request request, List<Car> cars);
}
