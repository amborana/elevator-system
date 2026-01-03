package org.aashish.valueobject;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Request {
    private int floor;
    private Direction direction;
}