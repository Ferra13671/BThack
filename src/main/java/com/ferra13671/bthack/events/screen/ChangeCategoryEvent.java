package com.ferra13671.bthack.events.screen;

import com.ferra13671.MegaEvents.event.Event;
import com.ferra13671.bthack.features.category.ICategory;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChangeCategoryEvent extends Event<ChangeCategoryEvent> {
    public final ICategory category;
}
