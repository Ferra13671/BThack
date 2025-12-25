package com.ferra13671.bthack.managers.bind;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Bind {
    private int key;
    private BindType type;
    private final BindController controller;

    public static Bind NONE = new Bind(0, BindType.Toggle, BindController.Keyboard);
}
